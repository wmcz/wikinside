<template>
<q-item>

  <q-item-section top>
    <q-item-label lines="1">
      <span class="text-h6 q-pr-xs">{{ username }}</span>
      <span class="text-caption">{{ id }}</span>

    </q-item-label>
    <q-item-label caption lines="1">
      Tags: a b c
    </q-item-label>
  </q-item-section>
  <q-item-section top side>
    <div class="text-grey-8 q-gutter-xs">
      <q-btn class="gt-xs" size="12px" flat dense round icon="delete" @click.stop="$emit('deleteUser', id)"/>
    </div>
  </q-item-section>
</q-item>
</template>

<script>
export default {
  name: "UserLink",
  props: {
    username: {
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
