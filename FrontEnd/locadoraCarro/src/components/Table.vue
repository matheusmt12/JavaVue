<script setup>
defineProps(['dados', 'titulos', 'details', 'deletar', 'urlApi', 'finalizar', 'editar', 'alugar','edtInfo','ativar'])
const aux = 0;
const emit = defineEmits(['delete', 'detalhes', 'finalizarL', 'editObj', 'alugarCarro','statusCarro','ativarCliente']);

function deleteObj(obj) {
    emit('delete', obj)
}
function detalhes(obj) {
    emit('detalhes', obj)
}
// Função para dividir strings por letras maiúsculas
function splitByUppercase(str) {
    if (typeof str === 'string') {

        return str.split(/(?=[A-Z])|_/).join(' '); // Dividindo e juntando com espaço

    }

    return str; // Se não for string, retorna o valor como está
}

function finalizarModal(obj) {
    emit('finalizarL', obj.codigo, obj.idCarro);
}

function editObj(id) {
    emit('editObj', id);
}

//functions para carro

function alugarFunc(obj) {
    emit('alugarCarro', obj)
}

function statusCarro(obj) {
    emit('statusCarro' , obj)
}

function ativarCliente(obj) {
    emit('ativarCliente', obj);
}
</script>

<template>
    <table class="table table-sm table-striped">
        <thead class="thead-light">
            <tr>
                <th v-for="i in titulos" class="text-capitalize">
                    <span v-if="i == 'namecarro'">Nome</span>
                    <span v-else> {{ splitByUppercase(i) }}</span>
                </th>
            </tr>
        </thead>
        <tbody>

            <tr v-for="obj in dados" :key="obj.codigo">
                <td v-for="chave in titulos" :key="chave">
                    <span>{{ obj[chave] }}</span>
                    <span v-if="chave == 'namecarro'">{{ obj.modeloCarro.name }}</span>
                </td>
                <td v-if="ativar">
                    <button type="button" class="btn btn-success btn-sm text-light btn-sm rounded-pill"
                        @click="ativarCliente(obj)">Reintegrar Cliente</button>
                </td>
                <td v-if="alugar">
                    <button type="button" class="btn btn-secondary btn-sm text-light btn-sm rounded-pill"
                        @click="alugarFunc(obj)">Alugar</button>
                </td>
                <td v-if="edtInfo">
                    <button type="button" class="btn btn-info btn-sm text-light btn-sm rounded-pill"
                        @click="statusCarro(obj)">Status do Carro</button>
                </td>
                <td v-if="finalizar">
                    <button type="button" class="btn btn-secondary btn-sm text-light btn-sm rounded-pill"
                        @click="finalizarModal(obj)">Finalizar</button>
                </td>
                <td v-if="details">
                    <button type="button" class="btn btn-info btn-sm text-light btn-sm rounded-pill"
                        @click="detalhes(obj)">Detalhes</button>
                </td>
                <td v-if="deletar">
                    <button type="button" class="btn btn-danger btn-sm text-light btn-sm rounded-pill"
                        @click="deleteObj(obj)">Excluir</button>
                </td>
                <td v-if="editar">
                    <button type="button" class="btn btn-secondary btn-sm text-light btn-sm rounded-pill"
                        @click="editObj(obj)">Editar</button>
                </td>
            </tr>
        </tbody>
    </table>
</template>