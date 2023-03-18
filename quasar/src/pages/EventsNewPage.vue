<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
      style="max-width: 600px"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="name" label="Event name *" />
      <q-select label="Tags (optional)" multiple use-chips use-input counter v-model="selected" :options="tagoptions" option-value="id" option-label="name" @filter="filterTags"/>
      <UserSelect ref="userSelect"/>

      <div>
        <q-field label="Dates" hint="Double click for single day events" stack-label borderless>
          <template v-slot:control>
            <q-date v-model="date" landscape today-btn range mask="YYYY-MM-DD"/>
          </template>
        </q-field>
      </div>

      <q-btn color="primary" type="submit">Submit</q-btn>

    </q-form>
    <q-list bottom bordered class="rounded-borders" style="min-width: 600px">
      <EventLink v-for="event in eventdata" :key="event.name" v-bind="event"/>
    </q-list>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'
import EventLink from "components/EventLink.vue";
import {api} from "boot/axios";
import UserSelect from "components/UserSelect.vue";

export default defineComponent({
  name: 'EventsNewPage',
  data() {
    return {
      name: null,
      eventdata: [],
      tagdata: null,
      selected: [],
      tagoptions: null,
      date: null,
    }
  },
  methods: {
    onSubmit() {
      api.post('/events',
        {
          name: this.name,
          id: null,
          tagIds: this.selected.map(s => s.id),
          projectIds: [],
          userIds: this.$refs.userSelect.selected.map(s => s.id)
        }).then((response) => this.eventdata.push({
        name: response.data.name,
        id: response.data.id,
        tags: response.data.tagIds.map(i => this.tagdata.find(e => e.id === i))
      }))
    },
    filterTags(val, update, abort) {
      const self = this
      if (self.tagdata === null) {
        update(() => {
          api.get('/tags/event-tags').then((response) => {
            self.tagdata = response.data.map(t => {
              return {
                name: t.name,
                id: t.id
              }
            })
            self.tagoptions = self.tagdata
          })
        })
      } else {
        update(() => self.tagoptions = self.tagdata.filter(t => t.name.toLowerCase().includes(val.toLowerCase())))
      }
    }
  },
  components: {
    UserSelect,
    EventLink
  }
})
</script>
