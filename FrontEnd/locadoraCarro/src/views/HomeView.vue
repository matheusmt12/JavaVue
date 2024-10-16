<script setup>
  import Card from '@/components/Card.vue';
  import Header from '@/components/Header.vue';
  import Table from '@/components/Table.vue';

  const array = ['maca', 'pera', 'uva', 'mamao', 'melancia','melao', 'abacaxi']

</script>

<template>
  <Header />
  <Card titulo="Locações">
    <template v-slot:conteudo>
      <Table :dados="array"></Table>
    </template>
  </Card>
  <div> Esta aqui</div>
</template>