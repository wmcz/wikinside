<template>
<q-item>

  <q-item-section top>
    <q-item-label lines="1">
      <span class="text-h6 q-pr-xs ellipsis fit">{{ name }}</span>
    </q-item-label>
    <q-item-label caption lines="1">
      {{ elems.length }} {{ elemname }}
    </q-item-label>
  </q-item-section>
  <q-item-section side>
    <div class="text-grey-8 q-gutter-xs">
      <q-btn class="gt-xs" size="12px" flat dense round :icon="rightIcon ? rightIcon : 'delete'" @click.stop="$emit('deleteTag', id)" />
    </div>
  </q-item-section>
</q-item>
</template>

<script>
export default {
  name: "TagLink",
  props: {
    name: {
      type: String,
      required: true
    },

    id: {
      type: Number,
      required: true
    },

    elems: {
      type: Array,
      required: true
    },
    elemname: {
      type: String,
      required: true,
    },
    rightIcon: {
      type: String
    }
  }
}
</script>
