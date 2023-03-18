<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="name" label="Tag name *" />

      <q-select label="Events (optional)" multiple use-chips use-input counter v-model="selected" :options="eventoptions" option-value="id" option-label="name" @filter="filterEvents"/>

      <TagSelect ref="parentSelect" parent url="tags/event-tags"/>

      <q-btn color="primary" type="submit">Submit</q-btn>
    </q-form>
    <q-list v-if="tagdata.length" bottom bordered class="rounded-borders" style="min-width: 600px">
      <TagLink v-for="tag in tagdata" :key="tag.name" elemname="users" v-bind="tag" :elems="tag.elementIds"/>
    </q-list>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'
import TagLink from "components/TagLink.vue";
import {api} from "boot/axios";
import TagSelect from "components/TagSelect.vue";

export default defineComponent({
  name: 'EventTagNewPage',
  data() {
    return {
      name: null,
      tagdata: [],
      eventdata: null,
      eventoptions: null,
      selected: []
    }
  },
  methods: {
    onSubmit() {
      api.post('/tags/event-tags',
        {
          name: this.name,
          id: null,
          assignable: true,
          elementIds: this.selected.map(s => s.id),
          parentId: this.$refs.parentSelect.selected,
          childrenIds: []
        }).then((response) => this.tagdata.push(response.data))
    },
    filterEvents(val, update, abort) {
      const self = this
      if (self.userdata === null) {
        update(() => {
          api.get('/events').then((response) => {
            self.eventdata = response.data.map(e => {return {
              name: e.name,
              id: e.id
            }})
            self.eventoptions = self.eventdata})})
      } else {
        update(() => self.eventoptions = self.eventdata.filter((e) => e.name.toLowerCase().includes(val.toLowerCase())))
      }
    },
  },
  components: {
    TagLink,
    TagSelect
  }
})
</script>
