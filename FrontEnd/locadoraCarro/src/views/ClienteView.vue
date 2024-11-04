<script setup>
import Card from '@/components/Card.vue';
import Header from '@/components/Header.vue';
import Table from '@/components/Table.vue';
import Modal from '@/components/Modal.vue';
import Input from '@/components/Input.vue';
import Pagination from '@/components/Pagination.vue';
import { onMounted, ref } from 'vue';
const url = 'http://localhost:8080/';

let dadosClientes = ref([])
let visivelModalAdicionar = ref(false);
let visivelModalRemover = ref(false);
let dadosCliente = {
    email: '',
    telefone: '',
    nome: '',
    cpf: ''
}
let pageable = ref([]);
let page = ref(0);
let active = ref(true);
// On mounted
onMounted(() => {
    getClientes();
})



// function para buscar os clientes 
function getClientes() {

    let token = localStorage.getItem('authToken');
    axios.get(url + 'cliente/index', {
        params: {
            size: 1,
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
        console.log(response.data);

    }).catch(erro => {
        console.log(erro.response.data);

    })

}

//Modals

function modalAdicionar() {

    visivelModalAdicionar.value = true;
}

function modalExcluir() {
    visivelModalRemover.value = true;
}

function closeModal() {
    visivelModalAdicionar.value = false;
    visivelModalRemover.value = false;
}

//pagination

function changePage(params) {
    page.value = params;
    getClientes();
}

function checkFunck(params) {
    active.value = params;
    getClientes();

}

//function para editar o cliente
function editCliente(params) {
    console.log(params);

}

//functio para excluir o cliente

function excluirCliente(params) {

    let token = localStorage.getItem('authToken');
    let data = {
        message: 'Não pagou'
    }
    axios.put(url+'cliente/remove/' + params.id, data, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        console.log(response.data);
        getClientes();

    }).catch(erro => {
        console.log(erro.response);

    })

    console.log(params);

}
</script>

<template>
    <Header></Header>
    <Card titulo="Clientes" check="Ativo" :value-check="true" @check-func="checkFunck">
        <template v-slot:conteudo>
            <Table :titulos="['id', 'name', 'cpf', 'telefone']" :dados="dadosClientes" :editar="true"
                @editObj="editCliente" :deletar="true" @delete="excluirCliente"></Table>
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
                <Input for-label="cpfCliente" id-att="cpfCliente" id-att-ajuda="ajudaCpfCliente" titulo="CPF"
                    titulo-ajuda="Informe o cpf do cliente">
                <input type="text" v-mask="'###.###.###-##'" class="form-control" id="cpfCliente"
                    v-model="dadosCliente.cpf" />
                </Input>
            </div>
        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" class="btn btn-primary" @click="saveCliente">Save changes</button>
        </template>
    </Modal>
    <!-- Modal para editar o cliente -->
    <Modal></Modal>
    <!-- Modal para remover o cliente (deixar inativo)-->
    <Modal titulo="Remover Cliente" :visivel="visivelModalRemover">

    </Modal>
</template>