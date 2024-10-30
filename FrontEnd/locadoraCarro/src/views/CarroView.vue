<script setup>
import Card from '@/components/Card.vue';
import Header from '@/components/Header.vue';
import Pagination from '@/components/Pagination.vue';
import Table from '@/components/Table.vue';
import { onMounted, ref } from 'vue';

let url = 'http://localhost:8080/';
let dadosCarro = ref([]);
let disponivel = ref(true);
let pageable = ref([]);
let page = ref(0);
//function para pegar os carros

function getCarros() {
    let token = localStorage.getItem('authToken');

    axios.get(url + 'carro/index', {
        headers: {
            'Authorization': 'Bearer ' + token
        },
        params: {
            page: page.value,
            size: 1,
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
    console.log(params);

}

//function para buscar carro pelo status de disponivel

function disponivelCarro(params) {
    disponivel.value = params;
    getCarros();
}


// function para mudar a page do pageable

function changePage(params) {
    page.value = params;
    getCarros();
}

onMounted(() => {
    getCarros();
})
</script>

<template>
    <Header></Header>
    <div class="container">
        <div class="row">
            <Card titulo="Carro" check="Disponível" @checkFunc="disponivelCarro" :valueCheck="true">
                <template v-slot:conteudo>
                    <Table :dados="dadosCarro" :titulos="['id', 'namecarro', 'placa', 'km']" :details="true"
                        :alugar="true" @alugarCarro="alugarCarro"></Table>
                </template>
                <template v-slot:footer>
                    <div class="row">
                        <div class="col text-start">
                            <Pagination :dadosPage="pageable" @changePage="changePage"></Pagination>
                        </div>
                    </div>
                </template>
            </Card>
        </div>

    </div>
</template>

<style>
.container {
    padding-top: 20px;
}
</style>