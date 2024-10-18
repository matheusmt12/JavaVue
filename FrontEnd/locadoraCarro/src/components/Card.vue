<script setup>
    const props = defineProps(['titulo'])
</script>

<template>
    <div class="card text-center">
        <div class="card-header">
            {{ titulo }}
        </div>
            <slot name="conteudo"></slot>
        <div class="card-footer">
            <slot name="footer"></slot>
        </div>
    </div>
</template>