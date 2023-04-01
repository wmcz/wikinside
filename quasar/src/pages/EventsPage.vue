<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-list top bordered class="rounded-borders" style="min-width: 600px">
      <q-item-label header>{{ $t('event.many') }}</q-item-label>
      <q-input class="q-pa-md" ref="filterRef" v-model="filter" :label="$t('filter')">
        <template v-slot:append>
          <q-icon v-if="filter !== ''" name="clear" class="cursor-pointer" @click="resetFilter" />
        </template>
      </q-input>
      <q-table :rows="eventdata" :row-key="name" grid style="max-width: 600px" :loading="loading" :filter="filter" :pagination="{ rowsPerPage: 10}">
        <template v-slot:item="props">
          <EventLink :key="props.row.name" v-bind="props.row" @deleteEvent="(id) => deleteEvent(id)" style="width: 600px"/>
        </template>
      </q-table>
    </q-list>

    <q-btn bottom to="/event/new" color="primary">{{ $t('add') }}</q-btn>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'
import EventLink from "components/EventLink.vue";
import { api } from 'boot/axios'
import {getErrorMessage} from "src/util";


export default defineComponent({
  data() {
    return {
      tagdata: [],
      eventdata: [],
      filter: '',
      loading: true
    }
  },
  name: 'EventsPage',
  components: {
    EventLink
  },
  mounted() {
    const self = this
    api
      .get('/tags/event-tags')
      .then((response) => {
      this.tagdata = response.data.map(function(item) {return {name: item.name, id: item.id}})})
      .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
    api
      .get('/events')
      .then((response) => {
        this.eventdata = response.data.map(
          function(item) {return {
            name: item.name,
            id: item.id,
            tags: item.tagIds.map(id => self.$data.tagdata.find(e => id === e.id))}})
        this.loading = false
      })
      .catch(error => {
        this.loading = false
        this.$q.notify(this.$t(getErrorMessage(error)))
      })
  },
  methods: {
    deleteEvent: function (id) {
      api
        .delete('/events/' + id)
        .then(
          this.eventdata.splice(
            this.eventdata.indexOf(
              this.eventdata.find(e => e.id === id)),
            1))
        .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))


    }
}

})
</script>
