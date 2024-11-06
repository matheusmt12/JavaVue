<script setup>
import Alert from '@/components/Alert.vue';
import Card from '@/components/Card.vue';
import Header from '@/components/Header.vue';
import Input from '@/components/Input.vue';
import Modal from '@/components/Modal.vue';
import Pagination from '@/components/Pagination.vue';
import Table from '@/components/Table.vue';
import { onMounted, ref } from 'vue';

const url = 'http://localhost:8080/';
let dadosMarca = ref([]);
let visivelModalAdd = ref(false);
let visivelModalEdit = ref(false);
let pageable = ref([]);
let nameMarca = '';
let idMarca = ref(0);
let page = ref(0);
let dadosAlert = ref({
    message: '',
    response: ''
})
// function para carregar todos os carros 
function getMarcas() {
    let token = localStorage.getItem('authToken');
    axios.get(url + 'marca', {
        headers: {
            'Authorization': 'Bearer ' + token
        },
        params: {
            page: page.value,
            size: 2
        }
    }).then(response => {
        dadosMarca.value = response.data.content;
        pageable.value = response.data;

    }).catch(erro => {
        console.log(erro);

    })
}

// function para salvar marca

function salvar() {
    let token = localStorage.getItem('authToken')
    axios.post(url + 'marca', { name: nameMarca }, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        dadosAlert.value.message = response.data;
        dadosAlert.value.response = "Sucesso";
        getMarcas();
        nameMarca = '';
    }).catch(erro => {
        dadosAlert.value.message = erro.response.data;
        dadosAlert.value.response = 'Erro'

    })
}

onMounted(() => {
    getMarcas();
})

// open modal do adicionar nova marca
function openModal() {
    visivelModalAdd.value = true;
}


function closeModal() {
    visivelModalAdd.value = false;
    visivelModalEdit.value = false;
    dadosAlert.value.message = '';
    dadosAlert.value.response = '';
}

//function para editar o nome da marca
function modalEditObj(obj) {
    idMarca.value = obj.id;
    visivelModalEdit.value = true;
}

function saveEdit() {

    let token = localStorage.getItem('authToken');
    axios.put(url + 'marca' + '/' + idMarca.value, { name: nameMarca }, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        console.log(response.data);
        idMarca.value = 0;
        nameMarca = '';
        dadosAlert.value.message = response.data;
        dadosAlert.value.response = "Sucesso";
        document.getElementById('btnSavechange').disabled = true;

        getMarcas();

    }).catch(erro => {

        dadosAlert.value.message = erro.response.data;
        dadosAlert.value.response = 'Erro'
    })
}

//function para mudar a page 

function changePage(number) {
    page.value = number
    getMarcas();
}

</script>

<template>
    <Header />
    <div class="container">
        <div class="row ">
            <Card titulo="Marca">
                <template v-slot:conteudo>
                    <Table :titulos="['id', 'name']" :dados="dadosMarca" :editar="true" @editObj="modalEditObj"></Table>
                </template>
                <template v-slot:footer>
                    <div class="row">
                        <div class="col text-start">
                            <Pagination :dadosPage="pageable" @changePage="changePage">

                            </Pagination>
                        </div>
                        <div class="col text-end">
                            <button type="button" class="btn btn-primary btn-sm" @click="openModal">Adicionar</button>
                        </div>
                    </div>
                </template>
            </Card>
        </div>
    </div>
    <!-- Modal para adicionar Marca -->
    <Modal titulo="Adicionar marca" :visivel="visivelModalAdd">
        <template v-slot:alert>
            <Alert clsAlert="alert alert-success" titulo="Sucesso" :message="dadosAlert.message"
                v-if="dadosAlert.response == 'Sucesso'"></Alert>
            <Alert clsAlert="alert alert-danger" titulo="Erro" :message="dadosAlert.message"
                v-if="dadosAlert.response == 'Erro'"></Alert>
        </template>
        <template v-slot:conteudo>
            <div>
                <Input id-att="nameMarca" titulo="Nome" for-label="nameMarca" id-att-ajuda="ajudaMarca"
                    titulo-ajuda="Informe a Marca">
                <input type="text" class="form-control" v-model="nameMarca">
                </Input>
            </div>
        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" class="btn btn-primary" @click="salvar">Save changes</button>
        </template>
    </Modal>

    <!-- Modal para editar a marca -->
    <Modal titulo="Editar a Marca" :visivel="visivelModalEdit">
        <template v-slot:alert>
            <Alert clsAlert="alert alert-success" titulo="Sucesso" :message="dadosAlert.message"
                v-if="dadosAlert.response == 'Sucesso'"></Alert>
            <Alert clsAlert="alert alert-danger" titulo="Erro" :message="dadosAlert.message"
                v-if="dadosAlert.response == 'Erro'"></Alert>
        </template>
        <template v-slot:conteudo>
            <div>
                <Input id-att="nameMarca" titulo="Nome" for-label="nameMarca" id-att-ajuda="ajudaMarca"
                    titulo-ajuda="Informe a Marca">
                <input type="text" class="form-control" v-model="nameMarca">
                </Input>
            </div>
        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" id="btnSavechange" class="btn btn-primary" @click="saveEdit">Save changes</button>
        </template>
    </Modal>
</template>

<style>
.container {
    padding-top: 20px;
}
</style>