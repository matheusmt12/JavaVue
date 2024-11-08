<script setup>
const props = defineProps(['titulo', 'check', 'valueCheck'])
const emit = defineEmits(['checkFunc'])
let test = props.valueCheck

function checkFunc() {
    test = !test
    emit('checkFunc', test)
}
</script>

<template>
    <div class="card text-center">
        <div class="card-header d-flex align-items-center justify-content-between">
            <div class="text-start">
                {{ titulo }}
            </div>
            <div class="flex-grow-1 d-flex justify-content-center">
                <div v-if="['Locações', 'Carro', 'Clientes'].includes(titulo)" class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault" @click="checkFunc" v-model="test">
                    <label class="form-check-label ms-2" for="flexSwitchCheckDefault">{{ check }}</label>
                </div>
            </div>
        </div>
        <slot name="conteudo"></slot>
        <div class="card-footer">
            <slot name="footer"></slot>
        </div>
    </div>
</template>

<style scoped>
.card-header {
    display: flex;
    align-items: center;
}
.flex-grow-1 {
    flex-grow: 1;
}
</style>
