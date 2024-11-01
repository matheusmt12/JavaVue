<script setup>
const props = defineProps(['titulo','check','valueCheck'])
const emit = defineEmits(['checkFunc'])
let test = props.valueCheck;
function checkFunc() {
    if (!test){
        test = true
    }else{
        test = false
    }
    emit('checkFunc', test)
}

</script>

<template>
    <div class="card text-center">
        <div class="card-header row">
            <div class=" col text-start">
                {{ titulo }}
            </div>
            <div class="col form-check form-switch text-start" v-if="titulo=='Locações' || titulo == 'Carro' || titulo == 'Clientes'">
                <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault" @click="checkFunc" v-model="test">
                <label class="form-check-label" for="flexSwitchCheckDefault">{{ check }}</label>
            </div>
        </div>
        <slot name="conteudo"></slot>
        <div class="card-footer">
            <slot name="footer"></slot>
        </div>
    </div>
</template>