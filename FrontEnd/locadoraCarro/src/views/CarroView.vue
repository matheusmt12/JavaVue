<script setup>
import Card from '@/components/Card.vue';
import Header from '@/components/Header.vue';
import Modal from '@/components/Modal.vue';
import Pagination from '@/components/Pagination.vue';
import Table from '@/components/Table.vue';
import Input from '@/components/Input.vue';
import Alert from '@/components/Alert.vue';
import { onMounted, ref } from 'vue';

const url = 'http://localhost:8080/';
let dadosCarro = ref([]);
let disponivel = ref(true);
let dadosAluguelCarro = ref([]);
let pageable = ref([]);
let dadosClinteServer = ref([]);
let modelosCarros = ref([]);
let page = ref(0);
let alugarCheck = ref(true);
let visivelModalAdicionar = ref(false);
let visivelModalAluguel = ref(false);
let visivelStatusModel = ref(false);
let dataPrevistaEntrega = ref()
let valorAluguel = ref();
let dadoCarro = {
    placa: '',
    km: '',
}

let messages = ref({
    data: '',
    status: ''
})
onMounted(() => {
    getCarros();
    getModelosCarro();
    getClients();
})
//function para pegar os modelos 
function getModelosCarro() {
    let token = localStorage.getItem('authToken');
    axios.get(url + 'modelo', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        modelosCarros.value = response.data;

    }).catch(erro => {
        console.log(erro);

    })
}

//function para pegar os clientes disponiveis 

function getClients() {

    let token = localStorage.getItem('authToken');

    axios.get(url + 'cliente', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        dadosClinteServer.value = response.data;
        console.log(dadosClinteServer.value);

    }).catch(erro => {
        console.log(erro.response.data);

    })
}

//function para pegar os carros

function getCarros() {
    let token = localStorage.getItem('authToken');

    axios.get(url + 'carro/index', {
        headers: {
            'Authorization': 'Bearer ' + token
        },
        params: {
            page: page.value,
            size: 4,
            disponivel: disponivel.value
        }

    }).then(response => {
        dadosCarro.value = response.data.content;
        pageable.value = response.data;

    }).catch(erro => {
        console.log(erro);

    })
}

//functio para alugar um carro

function alugarCarro(params) {
    visivelModalAluguel.value = true;
    dadosAluguelCarro.value = params;

}

//function para buscar carro pelo status de disponivel

function disponivelCarro(params) {
    disponivel.value = params;
    if (!params) {
        alugarCheck.value = false
    } else alugarCheck.value = true
    getCarros();
}


// function para mudar a page do pageable

function changePage(params) {
    page.value = params;
    getCarros();
}



//function para adicionar um novo carro

function modalAdicionar() {
    visivelModalAdicionar.value = true;
}



// finction para adicionar carro
function saveCarro() {
    let token = localStorage.getItem('authToken');
    let idModelo = document.getElementById('idModeloSelect').value
    let data = {
        km: dadoCarro.km,
        placa: dadoCarro.placa,
        modelo: {
            id: idModelo
        }
    }
    axios.post(url + 'carro', data, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        messages.value.data = response.data;
        messages.value.status = 'Sucesso';
        getCarros();
    }).catch(erro => {
        messages.value.data = erro.response.data;
        messages.value.status = 'Erro';
    })
}


//function para modal de alugar
function alugarCar(idCarro) {

    let token = localStorage.getItem('authToken');
    let idCliente = document.getElementById('selectCliente').value;
    console.log(idCarro, idCliente, valorAluguel.value, dataPrevistaEntrega.value);

    axios.post(url + 'locacao', {
        data_fim_locacao_previsto: dataPrevistaEntrega.value,
        valor: valorAluguel.value,
        cliente: {
            id: idCliente
        },
        carro: {
            id: idCarro
        }
    }, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        messages.value.data = response.data
        messages.value.status = 'Sucesso'
        getCarros();
    }).catch(erro => {
        messages.value.data = erro.response.data;
        messages.value.status = 'Erro'
    })
}

// functios modal close

function closeModal() {
    visivelModalAdicionar.value = false;
    visivelModalAluguel.value = false;
    visivelStatusModel.value = false;
}

//function para relatar status do carro

function statusCarroModal(obj) {
    visivelStatusModel.value = true;
    dadosAluguelCarro.value = obj;
    console.log(dadosAluguelCarro.value);

}

function relatarStatus(idCarro) {

    let status = document.getElementById('statusCarro').value;
    let disponivel = document.getElementById('carroDisponivelSelect').value


    let token = localStorage.getItem('authToken');
    console.log(idCarro, status, disponivel, token);
    axios.put(url + 'carro/relatarstatus/' + idCarro, {
        status: disponivel,
        menssagemStatus: status
    }, {
        headers: { 'Authorization': 'Bearer ' + token }
    }).then(response => {
        console.log(response.data);
        getCarros();
    }).catch(erro => {
        console.log(erro.response.data);

    })

}
</script>

<template>
    <Header></Header>
    <div class="container">
        <div class="row">
            <Card titulo="Carro" check="Disponível" @checkFunc="disponivelCarro" :valueCheck="true">
                <template v-slot:conteudo>
                    <Table :dados="dadosCarro" :titulos="['id', 'namecarro', 'placa', 'km']" :alugar="alugarCheck"
                        @alugarCarro="alugarCarro" :edtInfo="true" @statusCarro="statusCarroModal"></Table>
                </template>
                <template v-slot:footer>
                    <div class="row">
                        <div class="col text-start">
                            <Pagination :dadosPage="pageable" @changePage="changePage"></Pagination>
                        </div>
                        <div class="col text-end">
                            <button type="button" class="btn btn-primary btn-sm"
                                @click="modalAdicionar">Adicionar</button>
                        </div>
                    </div>
                </template>
            </Card>
        </div>
    </div>

    <Modal titulo="Adicionar carro" :visivel="visivelModalAdicionar">
       <template v-slot:alert>
        <Alert cls-alert="alert alert-success" :message="messages.data" titulo="Sucesso" v-if="messages.status == 'Sucesso'"></Alert>
        <Alert cls-alert="alert alert-danger" :message="messages.data" titulo="Erro" v-if="messages.status == 'Erro'"></Alert>
       </template> 
        <template v-slot:conteudo>
            <div>
                <div class="row">
                    <div class="col">
                        <Input for-label="placaCarro" id-att="carroPlaca" id-att-ajuda="ajudaCarroPlaca" titulo="Placa"
                            titulo-ajuda="Informe a placa do carro">
                        <input type="text" class="form-control" v-model="dadoCarro.placa">
                        </Input>
                    </div>
                    <div class="col">
                        <Input for-label="carroKm" id-att="carroKm" id-att-ajuda="carroKmajuda" titulo="Km"
                            titulo-ajuda="Informe a quilometragem do carro">
                        <input type="number" class="form-control" v-model="dadoCarro.km">
                        </Input>
                    </div>
                </div>
                <label for="modeloCarro" class="form-label">Modelo</label>
                <select name="modeloCarro" id="idModeloSelect" class="form-control">
                    <option v-for="m in modelosCarros" :value="m.id">{{ m.name }}</option>
                </select>

            </div>
        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" class="btn btn-primary" @click="saveCarro">Save changes</button>
        </template>
    </Modal>
    <Modal titulo="Alugar Carro" :visivel="visivelModalAluguel">
        <template v-slot:conteudo>
            <div class="row">
                <div class="col">
                    <Input idAtt="dataPrevistaLocacao" forLabel="dataPrevista" idAttAjuda="idAjudaTexto"
                        titulo="Data de Entrega Prevista" tituloAjuda="Informe a data de entrega prevista">
                    <input type="datetime-local" class="form-control" v-model="dataPrevistaEntrega" required>
                    </Input>
                </div>
                <div class="col">
                    <label for="nameCarro" class="form-label">Carro</label>
                    <label id="nameCarro" class="form-control" :value="dadosAluguelCarro.modeloCarro.name">
                        {{ dadosAluguelCarro.modeloCarro.name }}</label>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label class="form-label" for="clienteAlugarSelect">Cliente</label>
                    <select name="clienteAlugarSelect" id="selectCliente" class="form-control">
                        <option v-if="dadosClinteServer.filter(i => i.active).length > 0"
                            v-for="i in dadosClinteServer.filter(i => i.active)" :key="i.id" :value="i.id">
                            {{ i.name }}
                        </option>
                    </select>
                </div>
                <div class="col">
                    <div class="col">
                        <label for="placaCarro" class="form-label">Placa</label>
                        <label id="placaCarro" class="form-control" :value="dadosAluguelCarro.placa">
                            {{ dadosAluguelCarro.placa }}</label>
                    </div>
                </div>
            </div>
            <div>
                <Input idAtt="valorAluguel" forLabel="valorAluguel" idAttAjuda="idAjudaTextoAluguel" titulo="Valor"
                    tituloAjuda="Informe o valor do aluguel">
                <input type="number" class="form-control" v-model="valorAluguel" required>
                </Input>
            </div>
        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" class="btn btn-primary" @click="alugarCar(dadosAluguelCarro.id)">Alugar</button>
        </template>
    </Modal>
    <Modal titulo="Relatar Status do carro" :visivel="visivelStatusModel">
        <template v-slot:conteudo>
            <div class="row">
                <div class="col">
                    <label for="placaCarro" class="form-label">Placa</label>
                    <label for="placaCarro" class="form-control">{{ dadosAluguelCarro.placa }}</label>
                </div>
                <div class="col">
                    <label for="nameCarro" class="form-label">Carro</label>
                    <label for="nameCarro" class="form-control">{{ dadosAluguelCarro.modeloCarro.name }}</label>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="kmCarro" class="form-label">Km</label>
                    <label for="kmCarro" class="form-control">{{ dadosAluguelCarro.km }}</label>
                </div>
                <div class="col">
                    <Input for-label="disponivelCarro" id-att="carrodisponivel" titulo="Status do Carro"
                        id-att-ajuda="idajudaCarrodisponivel" titulo-ajuda="Informe o status do Carro">
                    <select name="selectDisponivel" id="carroDisponivelSelect" class="form-control">
                        <option value="true" v-if="dadosAluguelCarro.disponivel">Disponível</option>
                        <option value="false" v-if="dadosAluguelCarro.disponivel">Indisponível</option>
                        <option value="false" v-if="!dadosAluguelCarro.disponivel">Indisponível</option>
                        <option value="true" v-if="!dadosAluguelCarro.disponivel">Disponível</option>
                    </select>
                    </Input>
                </div>
            </div>
            <Input titulo="Relatar Status do carro" forLabel="statusCarro" idAtt="statusCarro"
                titulo-ajuda="Informe o que aconteceu com o carro " id-att-ajuda="isAjudadaStatusCarro">
            <textarea id="statusCarro" class="form-control">{{ dadosAluguelCarro.status }}</textarea>
            </Input>

        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" class="btn btn-primary" @click="relatarStatus(dadosAluguelCarro.id)">Relatar
                Status</button>
        </template>
    </Modal>
</template>

<style>
.container {
    padding-top: 20px;
}
</style>