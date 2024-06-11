<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-list top bordered class="rounded-borders">
      <q-item class="q-py-none q-pl-none">
        <q-item-label header>{{ $t('event.many') }}</q-item-label>
        <q-space />
        <q-input  side dense input-class="text-right" style="float: right" class="q-pt-xs" v-model="filter" :label="$t('filter')">
          <template v-slot:append>
            <q-icon v-if="filter !== ''" name="clear" class="cursor-pointer" @click="resetFilter" />
            <q-icon v-else name="search"/>
          </template>
        </q-input>
      </q-item>
      <q-table :rows="eventdata" :row-key="name" grid :loading="loading" :filter="filter" :pagination="{ rowsPerPage: 10}">
        <template v-slot:item="props">
          <EventLink :key="props.row.name" v-bind="props.row" @deleteElem="(id) => deleteEvent(id)"/>
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
    Promise.all([
    api
      .get('/tags/event-tags')
      .then((response) => {
      this.tagdata = response.data.map(function(item) {return {name: item.name, id: item.id, color: item.color}})})
      .catch(error => this.$q.notify(this.$t(getErrorMessage(error)))),
    api
      .get('/events')
      ])
      .then((response) => {
        this.eventdata = response[1].data.map(
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
    },
    resetFilter: function () {
      this.filter = ''
    }
}

})
</script>
