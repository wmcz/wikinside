<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-list top bordered class="rounded-borders" style="min-width: 600px">
      <q-item-label header>Events</q-item-label>
        <EventLink v-for="event in eventdata" :key="event.name" v-bind="event" @deleteEvent="(id) => deleteEvent(id)"/>
    </q-list>

    <q-btn bottom to="/event/new" color="primary">Add</q-btn>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'
import EventLink from "components/EventLink.vue";
import { api } from 'boot/axios'


export default defineComponent({
  data() {
    return {
      eventdata: []
    }
        },
  name: 'EventsPage',
  components: {
    EventLink
  },
  mounted() {
    api.get('/events').then((response) => {
      this.eventdata = response.data.map(function(item) {return {name: item.name, id: item.id}})})
  },
  methods: {
    deleteEvent: function (id) {
      api.delete('/events/' + id).then(
        this.eventdata.splice(
          this.eventdata.indexOf(
            this.eventdata.find(e => e.id === id)),
          1))

    }
}

})
</script>
