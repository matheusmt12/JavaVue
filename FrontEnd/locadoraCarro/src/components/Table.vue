<script setup>
    const array = defineProps(['dados'])
    const aux =0;
</script>

<template>
    <table class="table">
        <thead class="thead-light">
            <tr>
                <th scope="col">#</th>
                <th scope="col">First</th>
                <th scope="col">Last</th>
                <th scope="col">Handle</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="a in array">
                <td v-for="b in a">
                    {{ b }}
                </td>
            </tr>
        </tbody>
    </table>
</template>