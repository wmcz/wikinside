<template>
  <q-item>
    <q-item-section class="text-weight-bold text-primary text-h6 col-3"> {{ val.toLocaleString($i18n.locale) }} </q-item-section>
    <q-item-section> {{ $t('impact.' + name) }} </q-item-section>
  </q-item>
</template>

<script>
export default {
  name: "ImpactItem",
  props: {
    val: {
      type: Number,
      required: true
    },
    name: {
      type: String,
      required: true
    }
  }
}
</script>
