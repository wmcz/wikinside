<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
      style="max-width: 600px"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="username" label="User name *" />
      <q-select label="Tags (optional)" multiple use-chips use-input counter v-model="selected" :options="tagoptions" option-value="id" option-label="name" @filter="filterTags"/>

      <q-btn color="primary" type="submit">Submit</q-btn>
    </q-form>
    <q-list bottom bordered class="rounded-borders" style="min-width: 600px">
      <UserLink v-for="user in userdata" :key="user.username" v-bind="user"/>
    </q-list>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'
import UserLink from "components/UserLink.vue";
import {api} from "boot/axios";

export default defineComponent({
  name: 'UsersNewPage',
  data() {
    return {
      username: null,
      userdata: [],
      tagdata: null,
      selected: [],
      tagoptions: null
    }
  },
  methods: {
    onSubmit() {
      api.post('/users',
        {
          username: this.username,
          id: null,
          tagIds: this.selected.map(s => s.id),
          eventIds: []
        }).then((response) => this.userdata.push({
          username: response.data.username,
          id: response.data.id,
          tags: response.data.tagIds.map(i => this.tagdata.find(e => e.id === i))
        }
      ))
    },
    filterTags(val, update, abort) {
      const self = this
      if (self.tagdata === null) {
        update(() => {
          api.get('/tags/user-tags').then((response) => {
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
    UserLink
  }
})
</script>
