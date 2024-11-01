<script setup>
import Card from '@/components/Card.vue';
import Header from '@/components/Header.vue';
import Table from '@/components/Table.vue';
import Modal from '@/components/Modal.vue';
import Input from '@/components/Input.vue';
import { onMounted, ref } from 'vue';
const url = 'http://localhost:8080/';

let dadosClientes = ref([])
let visivelModalAdicionar = ref(false);
let dadosCliente = {
    email: '',
    telefone: '',
    nome: '',
    cpf: ''
}

// On mounted
onMounted(() => {
    getClientes();
})



// function para buscar os clientes 
function getClientes() {

    let token = localStorage.getItem('authToken');
    axios.get(url + 'cliente', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        console.log(response.data);
        dadosClientes.value = response.data

    }).catch(erro => {
        console.log(erro);

    })
}

// function para add cliente

function saveCliente() {
    console.log(dadosCliente);

}

//Modals

function modalAdicionar() {

    visivelModalAdicionar.value = true;
}

function closeModal() {
    visivelModalAdicionar.value = false;
}



</script>

<template>
    <Header></Header>
    <Card titulo="Clientes" check="Ativo" :value-check="true">
        <template v-slot:conteudo>
            <Table :titulos="['id', 'name', 'cpf', 'telefone']" :dados="dadosClientes" :editar="true"></Table>
        </template>
        <template v-slot:footer>
            <div class="col text-end">
                <button type="button" class="btn btn-primary btn-sm" @click="modalAdicionar">Adicionar</button>
            </div>
        </template>
    </Card>
    <Modal titulo="Adicionar Cliente" :visivel="visivelModalAdicionar">
        <template v-slot:conteudo>
            <Input for-label="emailCliente" id-att="clientEmail" id-att-ajuda="idClienteAjuda" titulo="Email"
                titulo-ajuda="Informe o Email">
            <input type="email" class="form-control">
            </Input>
            <div class="row">
                <div class="col">
                    <Input for-label="nameCliente" id-att="idNameCliente" id-att-ajuda="idNameCliente" titulo="Nome"
                        titulo-ajuda="Informe o nome do Cliente">
                    <input type="text" name="nameCliente" id="nameCliente" class="form-control">
                    </Input>
                </div>
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
    <div>{{ dadosClientes }}</div>
</template>