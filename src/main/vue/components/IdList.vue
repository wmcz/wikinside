<script setup>
  defineProps({
    name: String,
    elems: Array
  })
</script>


<template>
  <div class="id-list">
    <h3> {{ name }} </h3>
    <li v-for="elem in elems">
      {{ elem }}
    </li>
  </div>
</template>

<style scoped>

.id-list {
  display: inline-grid;
  padding: 10px;
}


</style>