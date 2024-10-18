<script setup>
import Card from '@/components/Card.vue';
import Header from '@/components/Header.vue';
import Input from '@/components/Input.vue';
import Modal from '@/components/Modal.vue';
import Table from '@/components/Table.vue';

import { onMounted, ref } from 'vue';

const urlLocacao = 'http://localhost:8080/locacao';
let array = ref([]);
let titulos = ['codigo', 'nameCliente', 'valor', 'nameCarro'];
let details = true;
let create = true;

let dadosLocacao = {
  nameClinte : '',
  dataPrevista: ''
}
// Estado de controle da visibilidade do modal
let visivelModal = ref(false);

function modal() {
  console.log(dadosLocacao.nameClinte,dadosLocacao.dataPrevista);
  

}

function getLocacoes() {
  let token = localStorage.getItem('authToken');

  axios.get(urlLocacao, {
    headers: {
      'Authorization': 'Bearer ' + token
    }
  }).then(response => {
    array.value = response.data;
    console.log(response.data);
  }).catch(error => {
    console.log(error);
  });
}

// Função para abrir o modal
function abrirModal() {
  visivelModal.value = true;
}

// Função para fechar o modal
function fecharModal() {
  visivelModal.value = false;
}

onMounted(() => {
  getLocacoes();
});
</script>

<template>
  <Header />
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-12">
        <Card titulo="Locações">
          <template v-slot:conteudo>
            <Table :dados="array" :titulos="titulos" :details="details" :create="create" :urlApi="urlLocacao"></Table>
          </template>
          <template v-slot:footer>
            <div class="row">
              <div class="col text-end">
                <button type="button" class="btn btn-primary btn-sm" @click="abrirModal">Adicionar</button>
              </div>
            </div>
          </template>
        </Card>
      </div>
    </div>
  </div>

  <!-- Modal -->
  <Modal :titulo="'Alugar Carro'" :visivel="visivelModal">
    <template v-slot:conteudo>
      <div class=" form row">
        <div class="col">
          <Input idAtt="nameClienteLocacao" forLabel="nameCliente" idAttAjuda="idAjudaTexto" titulo="Nome Cliente"
            tituloAjuda="Informe o nome do cliente">
          <input type="text" class="form-control" v-model="dadosLocacao.nameClinte">
          </Input>
        </div>
        <div class="col">
          <Input idAtt="dataPrevistaLocacao" forLabel="dataPrevista" idAttAjuda="idAjudaTexto"
            titulo="Data de Entrega Prevista" tituloAjuda="Informe a data de entrega prevista">
          <input type="datetime-local" class="form-control" v-model="dadosLocacao.dataPrevista">
          </Input>
        </div>
      </div>
    </template>
    <template v-slot:rodape>
      <button type="button" class="btn btn-secondary" @click="fecharModal">Close</button>
      <button type="button" class="btn btn-primary" @click="modal">Save changes</button>
    </template>
  </Modal>
</template>
