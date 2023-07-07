<template>
<q-item style="width: 100%" router-link :to="{name: elemtype + 'tag', params: {id: id}}">

  <q-item-section top>
    <q-item-label lines="1">
      <span class="text-h6 q-pr-xs ellipsis fit">{{ name }}</span>
      <q-badge v-if="color" :style="`background: ${color}`"/>
    </q-item-label>
    <q-item-label v-if="!suppresselems" caption lines="1">
      {{ elems.length }} {{ $t(elemtype + '.many').toLowerCase() }}
    </q-item-label>
  </q-item-section>
  <q-item-section side>
    <div class="text-grey-8 q-gutter-xs">
      <q-btn class="gt-xs" size="12px" flat dense round :icon="rightIcon ? rightIcon : 'delete'" @click.prevent="$emit('deleteTag', id)" />
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
      type: Array
    },
    elemtype: {
      type: String,
      required: true,
    },
    rightIcon: {
      type: String
    },
    suppresselems: {
      type: Boolean
    },
    color: {
      type: String
    }
  }
}
</script>
