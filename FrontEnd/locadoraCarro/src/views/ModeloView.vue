<script setup>
import Card from '@/components/Card.vue';
import Header from '@/components/Header.vue';
import Input from '@/components/Input.vue';
import Modal from '@/components/Modal.vue';
import Pagination from '@/components/Pagination.vue';
import Table from '@/components/Table.vue';
import { onMounted, ref } from 'vue';


const url = 'http://localhost:8080/';
let dadosMarca = ref([]);
let dadosModelos = ref([]);
let pageable = ref([]);
let page = ref(0);
let visualModalAdd = ref(false);
let dadosInput = ref({
    name : '',
    num_portas : '',
    lugares : '',
    air_bag : false,
    abs : false,
    marca : {
        id : 0
    }
})

onMounted(() => {
    getMarcas()
    getModelos();
})


//function para pegar as marcas

function getMarcas() {

    let token = localStorage.getItem('authToken');

    axios.get(url + 'marca/index', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(response => {
        dadosMarca.value = response.data

    }).catch(erro => {
        console.log(erro.response.data);

    })
}
//function para pegar os modelos do carro

function getModelos() {
    let token = localStorage.getItem('authToken');
    axios.get(url + 'modelo/index', {
        headers: {
            'Authorization': 'Bearer ' + token
        },
        params: {
            size: 5,
            page: page.value
        }
    }).then(response => {
        dadosModelos.value = response.data.content;
        pageable.value = response.data

    }).catch(erro => {
        console.log(erro.response.data);

    })
}

//function para mudar a page da pagination

function pageChange(params) {

    page.value = params;
    getModelos()
}

// functions modals
function openModalAdicionar() {
    visualModalAdd.value = true;
}

function closeModal() {
    visualModalAdd.value = false;
    dadosInput.value = {}
    console.log(dadosInput);
    
}


//function para adicionar um modelo

function saveModelo() {
    

    dadosInput.value.marca.id = document.getElementById('marcaSelect').value;
    console.log(dadosInput.value);


    let token = localStorage.getItem('authToken');

    axios.post(url + 'modelo', dadosInput.value ,{
        headers:{
            'Authorization' : 'Bearer ' + token
        }
    }).then(response =>{
        console.log(response.data);
        dadosInput.value.abs = false;
        dadosInput.value.name ='';         
        dadosInput.value.air_bag =false;         
        dadosInput.value.lugares ='';
        dadosInput.value.num_portas ='';
        dadosInput.value.marca.id ='';         


    }).catch(erro =>{
        console.log(erro.response.data);
        
    })
    
}
</script>


<template>
    <Header />
    <Card titulo="Modelos">
        <template v-slot:conteudo>
            <Table :dados="dadosModelos" :titulos="['id', 'name', 'lugares', 'nameMarca']"></Table>
        </template>
        <template v-slot:footer>
            <div class="row">
                <div class="col text-start">
                    <Pagination :dados-page="pageable" @change-page="pageChange"></Pagination>
                </div>
                <div class="col text-end">
                    <button type="button" class="btn btn-primary btn-sm" @click="openModalAdicionar">Adicionar</button>
                </div>
            </div>
        </template>
    </Card>
    <Modal titulo="Adicionar Modelo" :visivel="visualModalAdd">
        <template v-slot:conteudo>
            <div class="row">
                <div class="col">
                    <Input for-label="NomeModelo" id-att="idModelonome" titulo="Nome" id-att-ajuda="idAjudaModeloNome"
                        titulo-ajuda="Informe o nome do modelo">
                    <input type="text" class="form-control" v-model="dadosInput.name"/>
                    </Input>
                </div>
                <div class="col">
                    <Input for-label="numPortas" id-att="numPortas" id-att-ajuda="idAjudaNumPortas"
                        titulo="Número de Portas" titulo-ajuda="Informe a quantidade de portas do veículo">
                    <input type="number" class="form-control" v-model="dadosInput.num_portas"/>
                    </Input>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <Input for-label="marcas" id-att="marcasid" id-att-ajuda="idajudamarcas" titulo="Marca"
                        titulo-ajuda="Selecione a marca do veículo">
                    <select name="selectMarca" id="marcaSelect" class="form-control">
                        <option v-for="i in dadosMarca" :value="i.id">{{ i.name }}</option>
                    </select>
                    </Input>
                </div>
                <div class="col">
                    <Input for-label="lugaresLabel" id-att="lugaresid" id-att-ajuda="idajudaluhares" titulo="Lugares"
                        titulo-ajuda="Informe a quantidade de lugares do veículo">
                    <input type="number" class="form-control" v-model="dadosInput.lugares">
                    </Input>
                </div>
            </div>
            <div class="row">
                <div class="form-check col">
                    <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault" v-model="dadosInput.air_bag">
                    <label class="form-check-label" for="flexCheckDefault">
                        Airbag
                    </label>
                </div>
                <div class="form-check col">
                    <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault" v-model="dadosInput.abs">
                    <label class="form-check-label" for="flexCheckDefault">
                        Freio ABS
                    </label>
                </div>
            </div>
        </template>
        <template v-slot:rodape>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
            <button type="button" class="btn btn-primary" @click="saveModelo">Save changes</button>
        </template>
    </Modal>
</template>