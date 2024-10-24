<script setup>
const props = defineProps(['titulo'])
const emit = defineEmits(['queryFinalizada'])
let test = false;
function getFinalizada() {
    if (!test){
        test = true
    }else{
        test = false
    }
    emit('queryFinalizada', test)
}

</script>

<template>
    <div class="card text-center">
        <div class="card-header row">
            <div class=" col text-start">
                {{ titulo }}
            </div>
            <div class="col form-check form-switch text-start">
                <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault" @click="getFinalizada" v-model="test">
                <label class="form-check-label" for="flexSwitchCheckDefault">Finalizada</label>
            </div>
        </div>
        <slot name="conteudo"></slot>
        <div class="card-footer">
            <slot name="footer"></slot>
        </div>
    </div>
</template>