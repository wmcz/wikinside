<template>
<q-item style="width: 100%" clickable :focused="ticked" class="rounded-borders">

  <q-item-section top>
    <q-item-label lines="1">
      <span class="text-h6 q-pr-xs ellipsis fit">{{ name }}</span>
      <q-badge v-if="color" :style="`background: ${color}`"/>
    </q-item-label>
    <q-item-label v-if="false" caption lines="1">
      {{ elems.length }} {{ $t(elemtype + '.many').toLowerCase() }}
    </q-item-label>
  </q-item-section>
  <q-item-section side>
    <slot name="right"/>
  </q-item-section>
</q-item>
</template>

<script>
export default {
  name: "TagElem",
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
    },
    ticked: {
      type: Boolean
    }
  }
}
</script>
