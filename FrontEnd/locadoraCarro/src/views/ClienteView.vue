<script setup>
import Card from '@/components/Card.vue';
import Header from '@/components/Header.vue';
import Table from '@/components/Table.vue';
import Modal from '@/components/Modal.vue';
import Input from '@/components/Input.vue';
import Pagination from '@/components/Pagination.vue';
import { onMounted, ref } from 'vue';
import Alert from '@/components/Alert.vue';
const url = 'http://localhost:8080/';

let dadosClientes = ref([]);
let dadoCliente = ref([]);
let statusCliente = ref();
let visivelModalAdicionar = ref(false);
let visivelModalRemover = ref(false);
let visivelModalEditar = ref(false);
let visivelReintegrar = ref(false);
let deletarCheck = ref(true);
let ativarClienteBoolean = ref(false);
let dadosLocacao = ref([])
let dadosCliente = {
    email: '',
    telefone: '',
    nome: '',
    cpf: ''
};
let pageable = ref([]);
let page = ref(0);
let active = ref(true);

let messages = ref({
    data: '',
    status: ''
})

// On mounted
onMounted(() => {
    getClientes();
    getLocacoes();
})



// function para buscar os clientes 
function getClientes() {

    let token = localStorage.getItem('authToken');
    axios.get(url + 'cliente/index', {
        params: {
            size: 5,
            page: page.value,
            active: active.value
        },
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        console.log(response.data);
        dadosClientes.value = response.data.content;
        pageable.value = response.data;

    }).catch(erro => {
        console.log(erro);

    })
}

//function para buscar as locacoes

function getLocacoes() {
    let token = localStorage.getItem('authToken');
    axios.get(url + 'locacao', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        console.log(response.data.content);
        dadosLocacao.value = response.data.content;
    }).catch(erro => {
        console.log(erro.response.data);

    })
}

// function para add cliente

function saveCliente() {
    let token = localStorage.getItem('authToken');

    axios.post(url + 'cliente', {
        name: dadosCliente.nome,
        cpf: dadosCliente.cpf.replace(/\D/g, ''),
        email: dadosCliente.email,
        telefone: dadosCliente.telefone.replace(/\D/g, '')
    }, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {

        messages.value.data = response.data;
        messages.value.status = 'Sucesso';
        getClientes();

    }).catch(erro => {


        messages.value.data = erro.response.data;
        messages.value.status = 'Erro';
    })

}

//Modals

function modalAdicionar() {

    visivelModalAdicionar.value = true;
}

function modalEdit(params) {
    visivelModalEditar.value = true;
    dadoCliente.value = params
}

function modalExcluir(obj) {
    visivelModalRemover.value = true;
    dadoCliente.value = obj;
    statusCliente.value = false;
}

function reintegrarCliente(params) {
    visivelReintegrar.value = true;
    dadoCliente.value = params;
    statusCliente.value = true;
    console.log(params);

}

function closeModal() {
    visivelModalAdicionar.value = false;
    visivelModalRemover.value = false;
    visivelReintegrar.value = false;
    visivelModalEditar.value = false;
    messages.value.data = '';
    messages.value.status = ''
}

//pagination

function changePage(params) {
    page.value = params;
    getClientes();
}

function checkFunck(params) {
    active.value = params;
    deletarCheck.value = params;
    if (params) {
        ativarClienteBoolean.value = false;
    } else {
        ativarClienteBoolean.value = true;
    }
    getClientes();

}

//function para editar o cliente
function editCliente(message, id) {

    let token = localStorage.getItem('authToken');

    // emailIdCliente  nameClienteEdit telefoneClienteEdit cpfClienteEdit
    let name = document.getElementById('nameClienteEdit').value;
    let email =  document.getElementById('emailIdCliente').value;
    let cpf =  document.getElementById('cpfClienteEdit').value;
    let telefone = document.getElementById('telefoneClienteEdit').value;
    let messageStatus = message;
    axios.put(url + 'cliente/' + id, {
        name: name,
        cpf: cpf.replace(/\D/g, ''),
        email: email,
        telefone: telefone.replace(/\D/g, ''),
        messageStatus: messageStatus
    },{
        headers: {
            'Authorization' : 'Bearer ' + token
        }
    }).then(response => {
        messages.value.data = response.data;
        messages.value.status = 'Sucesso';
        getClientes();
        
    }).catch(erro => {
        messages.value.data = erro.response.data;
        messages.value.status = 'Sucesso';
        getClientes();
        
    })
}

//functio para excluir o cliente

function statusClienteFunc(params) {

    let pendente = document.getElementById('simPendente').value;
    let menssagem = document.getElementById('messagemCliente').value;
    if (pendente === 'Sim') {
        console.log('esta aqui');
        document.getElementById('excluirBtn').disabled = true;
        return alert('Resolver pendencia para remover cliente');
    }

    let token = localStorage.getItem('authToken');
    let data = {
        message: menssagem,
        status: statusCliente.value
    }
    axios.put(url + 'cliente/mudarstatus/' + params, data, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {


        messages.value.data = response.data;
        messages.value.status = 'Sucesso';
        getClientes();

    }).catch(erro => {

        messages.value.data = erro.esponse.data;
        messages.value.status = 'Erro';
    })
}

</script>

<template>
    <Header></Header>
    <Card titulo="Clientes" check="Ativo" :value-check="true" @check-func="checkFunck">
        <template v-slot:conteudo>
            <Table :titulos="['id', 'name', 'cpf', 'telefone']" :dados="dadosClientes" :editar="true"
                @editObj="modalEdit" :deletar="deletarCheck" @delete="modalExcluir" :ativar="ativarClienteBoolean"
                @ativarCliente="reintegrarCliente"> </Table>
        </template>
        <template v-slot:footer>
            <div class="row">
                <div class="col text-start">
                    <Pagination :dados-page="pageable" @change-page="changePage"></Pagination>
                </div>
                <div class="col text-end">
                    <button type="button" class="btn btn-primary btn-sm" @click="modalAdicionar">Adicionar</button>
                </div>
            </div>
        </template>
    </Card>
    <Modal titulo="Adicionar Cliente" :visivel="visivelModalAdicionar">
        <template v-slot:alert>
            <Alert v-if="messages.status == 'Sucesso'" cls-alert="alert alert-success" :message="messages.data"
                titulo="Sucesso"></Alert>
            <Alert v-if="messages.status == 'Erro'" cls-alert="alert alert-success" :message="messages.data"
                titulo="Erro"></Alert>
        </template>
        <template v-slot:conteudo>
            <Input for-label="emailCliente" id-att="clientEmail" id-att-ajuda="idClienteAjuda" titulo="Email"
                titulo-ajuda="Informe o Email">
            <input type="email" class="form-control" v-model="dadosCliente.email">
            </Input>
            <div class="row">
                <div class="col">
                    <Input for-label="nameCliente" id-att="idNameCliente" id-att-ajuda="idNameCliente" titulo="Nome"
                        titulo-ajuda="Informe o nome do Cliente">
                    <input type="text" name="nameCliente" id="nameCliente" class="form-control"
                        v-model="dadosCliente.nome">
                    </Input>
                </div>
                <div class="col">
                    <Input for-label="telefoneCliente" id-att="telefoneid" id-att-ajuda="ajudatelefoneId"
                        titulo="Telefone" titulo-ajuda="Informe o telefone do cliente">
                    <input type="text" v-mask="'(##) #####-####'" class="form-control"
                        v-model="dadosCliente.telefone" />
                    </Input>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <Input for-label="cpfCliente" id-att="cpfCliente" id-att-ajuda="ajudaCpfCliente" titulo="CPF"
                        titulo-ajuda="Informe o cpf do cliente">
                    <input type="text" v-mask="'###.###.###-##'" class="form-control" id="cpfCliente"
                        v-model="dadosCliente.cpf" />
                    </Input>
                </div>
            </div>
        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" class="btn btn-primary" @click="saveCliente">Save changes</button>
        </template>
    </Modal>
    <!-- Modal para editar o cliente -->
    <Modal titulo="Editar Cliente" :visivel="visivelModalEditar">
        <template v-slot:alert>
            <Alert v-if="messages.status == 'Sucesso'" cls-alert="alert alert-success" :message="messages.data"
                titulo="Sucesso"></Alert>
            <Alert v-if="messages.status == 'Erro'" cls-alert="alert alert-success" :message="messages.data"
                titulo="Erro"></Alert>
        </template>
        <template v-slot:conteudo>
            <Input for-label="emailClienteEdit" id-att="clientEmailEdit" id-att-ajuda="idClienteAjuda" titulo="Email"
                titulo-ajuda="Informe o Email">
            <input type="email" class="form-control" id="emailIdCliente" :value="dadoCliente.email">
            </Input>
            <div class="row">
                <div class="col">
                    <Input for-label="nameCliente" id-att="idNameClienteEdit" id-att-ajuda="idNameCliente" titulo="Nome"
                        titulo-ajuda="Informe o nome do Cliente">
                    <input type="text" name="nameCliente" id="nameClienteEdit" class="form-control"
                       :value="dadoCliente.name">
                    </Input>
                </div>
                <div class="col">
                    <Input for-label="telefoneCliente" id-att="telefoneidEdit" id-att-ajuda="ajudatelefoneId"
                        titulo="Telefone" titulo-ajuda="Informe o telefone do cliente">
                    <input type="text" v-mask="'(##) #####-####'" class="form-control"
                        :value="dadoCliente.telefone"  id="telefoneClienteEdit" />
                    </Input>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <Input for-label="cpfCliente" id-att="cpfClienteEdit" id-att-ajuda="ajudaCpfCliente" titulo="CPF"
                        titulo-ajuda="Informe o cpf do cliente">
                    <input type="text" v-mask="'###.###.###-##'" class="form-control" id="cpfClienteEdit"
                        :value="dadoCliente.cpf" />
                    </Input>
                </div>
            </div>
        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" class="btn btn-primary" @click="editCliente(dadoCliente.messageStatus, dadoCliente.id)">Save changes</button>
        </template>
    </Modal>
    <!-- Modal para remover o cliente (deixar inativo)-->
    <Modal titulo="Remover Cliente" :visivel="visivelModalRemover">
        <template v-slot:alert>
            <Alert v-if="messages.status == 'Sucesso'" cls-alert="alert alert-success" :message="messages.data"
                titulo="Sucesso"></Alert>
            <Alert v-if="messages.status == 'Erro'" cls-alert="alert alert-success" :message="messages.data"
                titulo="Erro"></Alert>
        </template>
        <template v-slot:conteudo>
            <div class="row">
                <div class="col">
                    <label for="nameCliente" class="form-label"> Nome </label>
                    <input class="form-control" :value="dadoCliente.name" disabled />
                </div>
                <div class="col">
                    <label for="cpfCliente" class="form-label"> CPF </label>
                    <input class="form-control" v-mask="'###.###.###-##'" :value="dadoCliente.cpf" disabled />
                </div>
                <div class="row">
                    <div class="col">
                        <label for="emailCliente" class="form-label">Email</label>
                        <input class="form-control" :value="dadoCliente.email" disabled />
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <label for="telefoneCliente" class="form-label">Telefone</label>
                        <input class="form-control" :value="dadoCliente.telefone" v-mask="'(##) #####-####'" disabled />
                    </div>
                    <div class="col">
                        <label for="estaPendente" class="form-label">Tem carro alugado</label>
                        <input id="simPendente"
                            v-if="dadosLocacao.filter(i => i.idCliente === dadoCliente.id && i.finalizada === false).length > 0"
                            class="form-control" :value="'Sim'" disabled />
                        <input id="simPendente" v-else class="form-control" :value="'Não'" disabled>
                    </div>
                    <div class="row">
                        <div class="col">
                            <Input for-label="textStatus" id-att="textoStatus" id-att-ajuda="idAjudaStatus"
                                :titulo="'Menssagem sobre o Cliente: ' + dadoCliente.name"
                                titulo-ajuda="Informe o motivo desta ação">
                            <textarea name="statusMessagem" id="messagemCliente" class="form-control"
                                :value="dadoCliente.messageStatus"></textarea>
                            </Input>
                        </div>
                    </div>
                </div>
            </div>
        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" class="btn btn-primary" id="excluirBtn"
                @click="statusClienteFunc(dadoCliente.id)">Save
                changes</button>
        </template>
    </Modal>
    <!-- Reintegrar cliente  -->
    <Modal titulo="Reintegrar Cliente" :visivel="visivelReintegrar">
        <template v-slot:alert>
            <Alert v-if="messages.status == 'Sucesso'" cls-alert="alert alert-success" :message="messages.data"
                titulo="Sucesso"></Alert>
            <Alert v-if="messages.status == 'Erro'" cls-alert="alert alert-success" :message="messages.data"
                titulo="Erro"></Alert>
        </template>
        <template v-slot:conteudo>
            <div class="row">
                <div class="col">
                    <label for="nameCliente" class="form-label"> Nome </label>
                    <input class="form-control" :value="dadoCliente.name" disabled />
                </div>
                <div class="col">
                    <label for="cpfCliente" class="form-label"> CPF </label>
                    <input class="form-control" v-mask="'###.###.###-##'" :value="dadoCliente.cpf" disabled />
                </div>
                <div class="row">
                    <div class="col">
                        <label for="emailCliente" class="form-label">Email</label>
                        <input class="form-control" :value="dadoCliente.email" disabled />
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <label for="telefoneCliente" class="form-label">Telefone</label>
                        <input class="form-control" :value="dadoCliente.telefone" v-mask="'(##) #####-####'" disabled />
                    </div>
                    <div class="col">
                        <label for="estaPendente" class="form-label">Tem carro alugado</label>
                        <input id="simPendente"
                            v-if="dadosLocacao.filter(i => i.idCliente === dadoCliente.id && i.finalizada === false).length > 0"
                            class="form-control" :value="'Sim'" disabled />
                        <input id="simPendente" v-else class="form-control" :value="'Não'" disabled>
                    </div>
                    <div class="row">
                        <div class="col">
                            <Input for-label="textStatus" id-att="textoStatus" id-att-ajuda="idAjudaStatus"
                                :titulo="'Menssagem sobre o Cliente: ' + dadoCliente.name"
                                titulo-ajuda="Informe o motivo desta ação">
                            <textarea name="statusMessagem" id="messagemCliente" class="form-control"
                                :value="dadoCliente.messageStatus"></textarea>
                            </Input>
                        </div>
                    </div>
                </div>
            </div>
        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" class="btn btn-primary" id="excluirBtn"
                @click="statusClienteFunc(dadoCliente.id)">Save
                changes</button>
        </template>
    </Modal>
</template>