<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="name" label="Event name *" />
      <q-input v-model="tag" label="Tags" hint="this one doesn't work yet"/>

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

export default defineComponent({
  name: 'EventsNewPage',
  data() {
    return {
      name: null,
      eventdata: []
    }
  },
  methods: {
    onSubmit() {
      api.post('/events',
        {
          name: this.name,
          id: null,
          tagIds: [],
          eventIds: [],
          projectIds: [],
          userIds: []
        }).then((response) => this.eventdata.push(response.data))
    }
  },
  components: {
    EventLink
  }
})
</script>
