<template>
  <q-list top bordered class="rounded-borders" style="min-width: 600px">
    <q-item-label header>{{ elemname }}</q-item-label>
    <q-input class="q-pa-md" ref="filterRef" v-model="filter" label="Filter">
      <template v-slot:append>
        <q-icon v-if="filter !== ''" name="clear" class="cursor-pointer" @click="resetFilter" />
      </template>
    </q-input>
    <q-table :rows="elemdata" :row-key="name" grid style="max-width: 600px" :loading="loading" :filter="filter" :pagination="{ rowsPerPage: 10}">
      <template v-slot:item="props">
        <slot name="elem" style="width: 600px"/>
      </template>
      <template v-slot:no-data>
        No {{ elemname.toLowerCase() }}
      </template>
    </q-table>
  </q-list>
</template>

<script>
export default {
  name: "ElemList",
  props: {
    elemname: {
      type: String,
      required: true
    },
    linkname: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      elemdata: [],
    }
  }
}
</script>
