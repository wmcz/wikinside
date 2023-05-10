<template>
  <q-item style="width:100%">

    <q-item-section top>
      <q-item-label lines="1">
        <span class="text-h6 q-pr-xs">{{ name }}</span>

      </q-item-label>
      <q-item-label caption lines="1">
        {{ path }}
      </q-item-label>
    </q-item-section>
  </q-item>
</template>

<script>
export default {
  name: "ProjectLink",
  props: {
    name: {
      type: String,
      required: true
    },
    id: {
      type: Number,
      required: true,
    },
    path: {
      type: String,
      required:true
    }
  }
}
</script>

