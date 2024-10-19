<script setup>
    defineProps(['dados','titulos','details','create','urlApi'])
    const aux =0;
    const emit =defineEmits(['delete']);

    function deleteObj(id) {
        emit('delete', id)
    }
    
</script>

<template>
    <table class="table">
        <thead class="thead-light">
            <tr>
                <th v-for="i in titulos" class="text-capitalize">
                    {{ i }}
                </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="obj in dados" :key="obj.codigo">
                <td v-for="valor, chave in obj" :key="chave">
                    <span  v-if="titulos.includes(chave)">{{ valor }}</span>
                </td>
                <td v-if="details">
                     <button type="button" class="btn btn-info btn-sm" @click="">Detalhes</button>
                </td>
                <td v-if="create">
                    <button type="button" class="btn btn-danger btn-sm" @click="deleteObj(obj.codigo)">Excluir</button>
                </td>
            </tr>
        </tbody>
    </table>
</template>