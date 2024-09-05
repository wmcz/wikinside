<template>
  <router-link :to="{name: elemtype + 'tag', params: {id: id}}">
    <q-badge rounded :style="color ? `background: ${color}` : `background: primary`" :label="name"/>
  </router-link>
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
    },
    elemtype: {
      type: String,
      required: true
    },
    color: {
      type: String
    }
  }
}
</script>
