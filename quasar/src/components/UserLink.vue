<template>
  <q-item>

  <q-item-section top>
    <q-item-label lines="1">
      <span class="text-h6">{{ name }}</span>
      <span class="text-caption">{{ id }}</span>
    </q-item-label>
    <q-item-label caption lines="1">
      Tags: a b c
    </q-item-label>
  </q-item-section>
</q-item>
</template>

<script>
export default {
  name: "UserLink",
  props: {
    name: {
      type: String,
      required: true
    },

    id: {
      type: Number,
      required: true
    }
  }
}
</script>

<style scoped>

</style>
