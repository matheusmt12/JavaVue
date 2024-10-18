<script setup>
    const array = defineProps(['dados','titulos','details','create','urlApi'])
    const aux =0;

    function deleteObj(id, url) {
        
        console.log(url+"/"+id);
        
    }
    function detailsObj() {
        
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
                     <button type="button" class="btn btn-info btn-sm" @click="deleteObj(obj.codigo, urlApi)">Detalhes</button>
                </td>
                <td v-if="create">
                    <button type="button" class="btn btn-danger btn-sm" >Excluir</button>
                </td>
            </tr>
        </tbody>
    </table>
</template>