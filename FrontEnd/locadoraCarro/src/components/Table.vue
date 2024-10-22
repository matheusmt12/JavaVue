<script setup>
defineProps(['dados', 'titulos', 'details', 'create', 'urlApi'])
const aux = 0;
const emit = defineEmits(['delete','detalhes']);

function deleteObj(id) {
    emit('delete', id)
}

function detalhes(obj) {
    emit('detalhes',obj)
}
// Função para dividir strings por letras maiúsculas
function splitByUppercase(str) {
    if (typeof str === 'string') {
        console.log(str);

        return str.split(/(?=[A-Z])/).join(' '); // Dividindo e juntando com espaço
        
    }
    console.log(str);

    return str; // Se não for string, retorna o valor como está
}

</script>

<template>
    <table class="table table-sm table-striped">
        <thead class="thead-light">
            <tr>
                <th v-for="i in titulos" class="text-capitalize">
                    {{ splitByUppercase(i) }}
                </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="obj in dados" :key="obj.codigo">
                <td v-for="valor, chave in obj" :key="chave">
                    <span v-if="titulos.includes(chave)">{{ valor }}</span>
                </td>
                <td v-if="details">
                    <button type="button" class="btn btn-info btn-sm text-light btn-sm rounded-pill" @click="detalhes(obj)">Detalhes</button>
                </td>
                <td v-if="create">
                    <button type="button" class="btn btn-danger btn-sm text-light btn-sm rounded-pill" @click="deleteObj(obj.codigo)">Excluir</button>
                </td>
            </tr>
        </tbody>
    </table>
</template>