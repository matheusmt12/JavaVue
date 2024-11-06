<script setup>
    let props = defineProps(['titulo', 'clsAlert','message'])

</script>

<template>
    <div :class="clsAlert" role="alert">
        <Strong>{{ titulo }}: </Strong>{{ message }}
    </div>
</template>