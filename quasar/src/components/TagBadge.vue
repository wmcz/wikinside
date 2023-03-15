<template>
  <q-badge rounded color="primary" :label="name" />
</template>

<script>
export default {
  name: "TagBadge",
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
