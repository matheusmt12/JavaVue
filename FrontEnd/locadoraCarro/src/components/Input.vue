<script setup>
    defineProps(['idAtt','forLabel','titulo','tituloAjuda','idAttAjuda'])
</script>

<template>

    <label class="form-label" :for="forLabel">{{ titulo }}</label>
    <slot></slot>
    <small :id="idAttAjuda" class="form-text text-muted">{{ tituloAjuda }}</small>
</template>