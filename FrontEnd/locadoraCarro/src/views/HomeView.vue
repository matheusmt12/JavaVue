<script setup>
import Card from '@/components/Card.vue';
import Header from '@/components/Header.vue';
import Input from '@/components/Input.vue';
import Modal from '@/components/Modal.vue';
import Pagination from '@/components/Pagination.vue';
import Table from '@/components/Table.vue';
import Alert from '@/components/Alert.vue';

import { onMounted, ref } from 'vue';

const urlLocacao = 'http://localhost:8080/locacao';
const urlCliente = 'http://localhost:8080/cliente';
const urlCarro = 'http://localhost:8080/carro';

let dadosCliente = ref([]);
let dadosCarro = ref([]);
let pageable = ref([]);
let array = ref([]);
let dadosLocacaoModal = ref({})
let titulos = ['codigo', 'nameCliente', 'valor', 'nameCarro'];
let details = true;
let create = true;
let finalizar = true
let page = ref(0);
let statusResponse = ref('');
let messageResponse = ref('');
let dadosFinalizarAluguel = ref({
  idLocacao: 0,
  idCarro: 0,
  kmAtual: 0
})
let queryFinalizar = false;

let dadosLocacao = {
  dataPrevista: '',
  valor: 0
}
// Estado de controle da visibilidade do modal
let visivelModal = ref(false);
let visivelModalDetails = ref(false)
let visivelModelFinalizar = ref(false);


//function para criar uma nova locacao
function salvar() {
  let idCliente = document.getElementById('idClienteSelect').value;
  let idCarro = document.getElementById('idCarroSelect').value;

  let token = localStorage.getItem('authToken');

  let data = {
    data_fim_locacao_previsto: dadosLocacao.dataPrevista,
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
    getLocacoes();
    getCarros();
    messageResponse.value = "Novo Aluguel adicionado";
    statusResponse.value = 'Adicionado'
    dadosLocacao.dataPrevista = '';
    dadosLocacao.valor = '';

  }).catch(error => {
    console.log(error);

  })
}

//function para deletar uma locacao
function deleteObj(obj) {

  let token = localStorage.getItem('authToken')
  console.log(urlLocacao + '/' + obj.codigo);

  axios.delete(urlLocacao + '/' + obj.codigo, {
    headers: {
      'Authorization': 'Bearer ' + token
    }
  }).then(response => {
    console.log(response.data);
    getLocacoes();
  }).catch(erro => {
    console.log(erro.response.data);

  })

}

//function para buscar todas as locacoes sendo as finalizadas ou as nao finalizadas
function getLocacoes() {
  let token = localStorage.getItem('authToken');
  console.log(queryFinalizar, 'teste finalizar');

  axios.get(urlLocacao, {
    params: {
      page: page.value,
      size: 4,
      finalizada: queryFinalizar
    },
    headers: {
      'Authorization': 'Bearer ' + token
    }
  },).then(response => {
    array.value = response.data.content;
    pageable.value = response.data;
    console.log(array.value);
    console.log(pageable);
    
    

  }).catch(erro => {
    console.log(erro.response.data);
  });
}

// buscar todos os carros disponíveis 
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

//buscar todos os clientes disponíveis 
function getClientes() {

  let token = localStorage.getItem('authToken')

  axios.get(urlCliente, {
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
  visivelModalDetails.value = false;
  visivelModelFinalizar.value = false;
  console.log(statusResponse, messageResponse);

  statusResponse = '';
  messageResponse = '';
  
}


onMounted(() => {
  getLocacoes();
  getClientes();
  getCarros();
});

function detalhesObj(obj) {
  visivelModalDetails.value = true;
  dadosLocacaoModal = obj;

}


function splitDate(data) {

  if (data == null) return '';
  let splitData = data.split('T');
  return splitData[0];

}

//function para o pagelist
function changePage(number) {
 page.value = number;
 getLocacoes();
}


//


function finalizarModal(idLocacao, idCarro) {
  dadosFinalizarAluguel.value.idLocacao = idLocacao;
  dadosFinalizarAluguel.value.idCarro = idCarro
  visivelModelFinalizar.value = true;
}

//function para finaliazar o aluguel do veículo
function finalizarLocacao() {
  let token = localStorage.getItem('authToken');
  console.log(dadosFinalizarAluguel.value);

  axios.put(urlLocacao + '/finalizar', {
    idLocacao: dadosFinalizarAluguel.value.idLocacao,
    idCarro: dadosFinalizarAluguel.value.idCarro,
    kmAtual: dadosFinalizarAluguel.value.kmAtual
  }, {
    headers: {
      'Authorization': 'Bearer ' + token
    }
  }).then(response => {
    statusResponse.value = 'Sucesso';
    messageResponse.value = response.data;
    getLocacoes()
    getCarros()
  }).catch(erro => {
    console.log(erro.response);
    
    statusResponse.value = 'Erro';
    messageResponse.value = erro.response.data;
  }
  
);


}

function getFinalizada(params) {
  queryFinalizar = params;
  console.log(queryFinalizar);
  if (queryFinalizar) {
    finalizar = false
  }
  else { finalizar = true }
  getLocacoes();

}

</script>

<template>
  <Header />
  <div class="container"  >
    <div class="row ">
        <Card titulo="Locações" @checkFunc="getFinalizada" check="Finalizada" :value-check="false">
          <template v-slot:conteudo>
            <Table :dados="array" :titulos="titulos" :details="details" :create="create" :urlApi="urlLocacao"  :edit="false"
              @delete="deleteObj" @detalhes="detalhesObj" :finalizar="finalizar" @finalizarL="finalizarModal"></Table>
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

  <!-- Modal -->
  <Modal :titulo="'Alugar Carro'" :visivel="visivelModal">
    <template v-slot:alert>
      <Alert titulo="Erro" clsAlert="alert alert-danger" v-if="statusResponse == 'Erro'" :message="messageResponse"></Alert>
      <Alert titulo="Sucesso" clsAlert="alert alert-success" v-if="statusResponse == 'Adicionado'" :message="messageResponse"></Alert>
    </template>
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
      <button type="button" class="btn btn-primary" @click="salvar">Save changes</button>
    </template>
  </Modal>
  <Modal titulo="Detalhes do aluguel" :visivel="visivelModalDetails">
    <template v-slot:conteudo>
      <div class="form row">
        <div class="col">
          <div class=" mb-3 row">
            <label for="staticEmail" class="col-sm-3 col-form-label "><strong>Cliente:</strong></label>
            <div class="col-sm-10">
              <input type="text" readonly class="form-control-plaintext" id="staticEmail"
                :value="dadosLocacaoModal.nameCliente">
            </div>
          </div>
        </div>
        <div class="col">
          <div class=" mb-3 row ">
            <label for="staticEmail" class="col-sm-3 col-form-label"><strong>Carro:</strong></label>
            <div class="col-sm-10">
              <input type="text" readonly class="form-control-plaintext" id="staticEmail"
                :value="dadosLocacaoModal.nameCarro">
            </div>
          </div>
        </div>
      </div>
      <div class="form row">
        <div class="col">
          <div class=" mb-3 row">
            <label class="col-form-label"><strong>Dato do Aluguel:</strong></label>
            <div class="col-sm-10">
              <input type="text" readonly class="form-control-plaintext"
                :value="splitDate(dadosLocacaoModal.dataLocacao)">
            </div>
          </div>
        </div>
        <div class="col">
          <div class=" mb-3 row ">
            <label for="staticEmail" class="col-form-label"><strong>Dia devolucao prevista:</strong></label>
            <div class="col-sm-10">
              <input type="text" readonly class="form-control-plaintext"
                :value="dadosLocacaoModal.dataPrevista ? splitDate(dadosLocacaoModal.dataPrevista) : 'A data nõa foi definida'">
            </div>
          </div>
        </div>
      </div>
      <div class=" mb-3 row">
        <label class="col-sm-2 col-form-label"><strong>Valor:</strong></label>
        <div class="col-sm-10">
          <input type="text" readonly class="form-control-plaintext" :value="dadosLocacaoModal.valor">
        </div>
      </div>
      <div class="form row">
        <div class="col">
          <div class=" mb-3 row">
            <label class="col-form-label"><strong>Km Inicial:</strong></label>
            <div class="col-sm-10">
              <input type="text" readonly class="form-control-plaintext" :value="dadosLocacaoModal.kmInicial">
            </div>
          </div>
        </div>
        <div class="col">
          <div class=" mb-3 row ">
            <label for="staticEmail" class="col-form-label"><strong>Km final:</strong></label>
            <div class="col-sm-10">
              <input type="text" readonly class="form-control-plaintext" :value="dadosLocacaoModal.kmFinal">
            </div>
          </div>
        </div>
      </div>
    </template>
    <template v-slot:rodape>
      <button type="button" class="btn btn-secondary" @click="fecharModal">Close</button>
    </template>
  </Modal>

  <Modal titulo="Finalizar Aluguel" :visivel="visivelModelFinalizar">
    <template v-slot:alert>
      <Alert titulo="Erro" clsAlert="alert alert-danger" v-if="statusResponse == 'Erro'" :message="messageResponse"></Alert>
      <Alert titulo="Sucesso" clsAlert="alert alert-success" v-if="statusResponse == 'Sucesso'" :message="messageResponse"></Alert>
    </template>
    <template v-slot:conteudo>
      <Input idAtt="idKmFinal" forLabel="kmFinal" idAttAjuda="KmFinal" titulo="Km Final"
        tituloAjuda="Quilometragem do veículo">
      <input type="number" v-model="dadosFinalizarAluguel.kmAtual" class="form-control">
      </Input>
    </template>
    <template v-slot:rodape>
      <button type="button" class="btn btn-secondary" @click="fecharModal">Close</button>
      <button type="button" class="btn btn-primary" @click="finalizarLocacao">Save changes</button>
    </template>

  </Modal>
</template>


<style>
.container{
  padding-top: 20px;

}
</style>