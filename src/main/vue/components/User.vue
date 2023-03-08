<script setup>
import {useI18n} from 'vue-i18n'
import IdList from "@/components/IdList.vue";

const { t } = useI18n()

defineProps({
  name: String,
  id: Number,
  tagIds: Array,
  eventIds: Array
})
</script>

<template>
  <div>
    <div class="name"> {{ name }} </div>
    <div class="user-id"> {{ id }} </div>
  </div>
  <div>
    <IdList :name="t('user.tags')" :elems="tagIds"/>
    <IdList :name="t('user.participates')" :elems="eventIds"/>
  </div>
</template>

<style scoped>
.name {
  display: inline-grid;
  font-size: xx-large;
}

.user-id {
  display: inline-grid;
  font-size: x-small;
}

.id-list {
  display: inline-grid;
}
</style>