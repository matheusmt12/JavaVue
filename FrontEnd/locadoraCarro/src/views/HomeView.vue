<script setup>
  import Card from '@/components/Card.vue';
  import Header from '@/components/Header.vue';
  import Table from '@/components/Table.vue';
import { onMounted } from 'vue';

  const urlLocacao = 'http://localhost:8080/'

  function getLocacoes() {
    let token = localStorage.getItem('authToken')
    
    
    axios.get(urlLocacao+'carro',{
      headers: {
        'Authorization' : 'Bearer '+token
      }
    }).then(response => {
      console.log(response);
      
    }).catch(error => {
      console.log(error);
      
    })
    
  }

onMounted(()=> {
  getLocacoes()
})
</script>

<template>
  <Header />
  <Card titulo="Locações">
    <template v-slot:conteudo>
      <Table></Table>
    </template>
  </Card>
  <div> Esta aqui</div>
</template>