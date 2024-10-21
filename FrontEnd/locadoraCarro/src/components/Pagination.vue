<script setup>
import { computed } from 'vue';

// Props recebidos do componente pai
const props = defineProps(['dadosPage']);
let emit =defineEmits(['changePage'])

// Computa o array de páginas baseado no total de páginas (assumindo que `dadosPage` tem a propriedade `totalPages`)
const totalPages = computed(() => {
  return Array.from({ length: props.dadosPage.totalPages }, (_, i) => i + 1);
});

// Função para tratar a navegação de página
const goToPage = (page) => {
  // Aqui você pode emitir um evento ou chamar diretamente uma função para mudar a página.
  // Por exemplo, emitir um evento:
  emit('changePage', page);

};
</script>

<template>
  <nav aria-label="...">
    <ul class="pagination">
      <!-- Botão "Anterior" -->
      <li class="page-item" :class="{ disabled: dadosPage.number  === 0 }">
        <a class="page-link" href="#" @click.prevent="goToPage(dadosPage.number - 1)">Anterior</a>
      </li>

      <!-- Renderiza as páginas -->
      <li class="page-item" v-for="page in totalPages" :key="page">
        <a class="page-link" href="#" @click.prevent="goToPage(page -1)" >{{ page }}</a>
      </li>

      <!-- Botão "Próximo" -->
      <li class="page-item" :class="{ disabled: dadosPage.number === dadosPage.totalPages -1 }">
        <a class="page-link" href="#" @click.prevent="goToPage(dadosPage.number + 1)">Próximo</a>
      </li>
    </ul>
  </nav>
</template>
