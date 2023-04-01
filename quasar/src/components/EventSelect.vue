<template>
  <q-select :label="$t('event.many') + $t('optional')" multiple use-chips use-input counter v-model="selected" :options="eventoptions" option-value="id" option-label="name" @filter="filterEvents"/>
</template>

<script>
import {api} from "boot/axios";
import {getErrorMessage} from "src/util";

export default {
  name: "EventSelect",
  data() {
    return {
      eventdata: null,
      eventoptions: null,
      selected: []
    }
  },
  methods: {
    filterEvents(val, update, abort) {
      const self = this
      if (self.eventdata === null) {
        update(() => {
          api
            .get('/events')
            .then((response) => {
              self.eventdata = response.data.map(e => {return {
                name: e.name,
                id: e.id
              }})
              self.eventoptions = self.eventdata})
            .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
        })
      } else {
        update(() => self.eventoptions = self.eventdata.filter((e) => e.name.toLowerCase().includes(val.toLowerCase())))
      }
    }
  }
}
</script>
