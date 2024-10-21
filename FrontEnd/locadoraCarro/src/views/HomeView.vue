<script setup>
import Card from '@/components/Card.vue';
import Header from '@/components/Header.vue';
import Input from '@/components/Input.vue';
import Modal from '@/components/Modal.vue';
import Pagination from '@/components/Pagination.vue';
import Table from '@/components/Table.vue';
import { onMounted, ref } from 'vue';

const urlLocacao = 'http://localhost:8080/locacao';
const urlMarca = 'http://localhost:8080/cliente';
const urlCarro = 'http://localhost:8080/carro';

let dadosCliente = ref([]);
let dadosCarro = ref([]);
let pageable = ref([])
let array = ref([]);
let titulos = ['codigo', 'nameCliente', 'valor', 'nameCarro'];
let details = true;
let create = true;

let dadosLocacao = {
  nameClinte: '',
  dataPrevista: '',
  valor: 0
}
// Estado de controle da visibilidade do modal
let visivelModal = ref(false);

function modal() {
  let idCliente = document.getElementById('idClienteSelect').value;
  let idCarro = document.getElementById('idCarroSelect').value;

  let token = localStorage.getItem('authToken');

  let data = {
    fim_previsto: dadosLocacao.dataPrevista,
    valor: dadosLocacao.valor,
    cliente: {
      id: idCliente
    },
    carro: {
      id: idCarro
    }
  }


  axios.post(urlLocacao, data, {
    headers: {
      'Authorization': 'Bearer ' + token
    }
  }).then(response => {
    console.log(response.data);

  }).catch(error => {
    console.log(error);

  })
}

function deleteObj(id) {

  let token = localStorage.getItem('authToken')
  console.log(urlLocacao + '/' + id);

  axios.delete(urlLocacao + '/' + id, {
    headers: {
      'Authorization': 'Bearer ' + token
    }
  }).then(response => {
    console.log(response.data);
    getLocacoes();
  }).catch(erro => {
    console.log(erro);

  })

}

function getLocacoes() {
  let token = localStorage.getItem('authToken');

  axios.get(urlLocacao, {
    params: {
      page: 0,
      size: 1
    },
    headers: {
      'Authorization': 'Bearer ' + token
    }
  },).then(response => {
    array.value = response.data.content;
    pageable = response.data;
    console.log(response.data);
    console.log(pageable);
    
  }).catch(erro => {
    console.log(erro.response.data);
  });
}

function getCarros() {
  let token = localStorage.getItem('authToken')

  axios.get(urlCarro, {
    headers: {
      'Authorization': 'Bearer ' + token
    }
  }).then(response => {
    dadosCarro = response.data;
    console.log(dadosCarro);

  })
}

function getMarcas() {

  let token = localStorage.getItem('authToken')

  axios.get(urlMarca, {
    headers: {
      'Authorization': 'Bearer ' + token
    }
  }).then(response => {
    dadosCliente = response.data;
    console.log(response.data);

  }
  ).catch(error => {
    console.log(error);

  }
  )
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
  getMarcas();
  getCarros();
});

function changePage (page){
  let token = localStorage.getItem('authToken');

axios.get(urlLocacao, {
  params: {
    page: page,
    size: 1
  },
  headers: {
    'Authorization': 'Bearer ' + token
  }
},).then(response => {
  array.value = response.data.content;
  pageable = response.data;
  console.log(response.data);
  console.log(pageable);
  
}).catch(erro => {
  console.log(erro.response.data);
});
  
}
</script>

<template>
  <Header />
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-12">
        <Card titulo="Locações">
          <template v-slot:conteudo>
            <Table :dados="array" :titulos="titulos" :details="details" :create="create" :urlApi="urlLocacao"
              @delete="deleteObj"></Table>
          </template>
          <template v-slot:footer>
            <div class="row">
              <div class="col text-start">
                <Pagination :dadosPage="pageable" @changePage="changePage">
                  
                </Pagination>
              </div>
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
          <Input idAtt="dataPrevistaLocacao" forLabel="dataPrevista" idAttAjuda="idAjudaTexto"
            titulo="Data de Entrega Prevista" tituloAjuda="Informe a data de entrega prevista">
          <input type="datetime-local" class="form-control" v-model="dadosLocacao.dataPrevista">
          </Input>
        </div>
        <div class="form row">
          <div class="col">
            <label for="idClienteSelect" class="form-label"> Cliente </label>
            <select name="cliente" id="idClienteSelect" class="form-control">
              <option v-if="dadosCliente.filter(i => i.active).length > 0"
                v-for="i in dadosCliente.filter(i => i.active)" :key="i.id" :value="i.id">
                {{ i.name }}
              </option>
              <option v-else>
                Não tem clientes cadastrados no sistema
              </option>
            </select>
          </div>
          <div class="col">
            <label for="idCarroteSelect" class="form-label"> Carro </label>
            <select name="carro" id="idCarroSelect" class="form-control">
              <option v-if="dadosCarro.filter(i => i.disponivel).length > 0"
                v-for="i in dadosCarro.filter(i => i.disponivel)" :key="i.id" :value="i.id">
                {{ i.modeloCarro.name }}
              </option>
              <option v-else>
                Não ha carros Disponivel
              </option>
            </select>
          </div>
          <div class="form row">
            <Input idAtt="valorLoc" forLabel="valorLocacao" idAttAjuda="idAjudaTextoValor" titulo="Valor"
              tituloAjuda="Valor do aluguel do Carro">
            <input type="number" class="form-control" v-model="dadosLocacao.valor" />
            </Input>
          </div>
        </div>
      </div>
    </template>
    <template v-slot:rodape>
      <button type="button" class="btn btn-secondary" @click="fecharModal">Close</button>
      <button type="button" class="btn btn-primary" @click="modal">Save changes</button>
    </template>
  </Modal>

  <div>
    {{ pageable.pageable }}
  </div>
</template>
