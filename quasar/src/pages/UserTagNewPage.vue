<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="name" label="Tag name *" />

      <q-select label="Users (optional)" multiple use-chips use-input counter v-model="selected" :options="useroptions" option-value="id" option-label="username" @filter="filterUsers"/>

      <q-select label="Parent tag (optional)" use-chips use-input counter v-model="parent" :options="parentoptions" option-value="id" option-label="name" @filter="filterParents"/>

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

export default defineComponent({
  name: 'UserTagNewPage',
  data() {
    return {
      name: null,
      tagdata: [],
      userdata: null,
      useroptions: null,
      selected: [],
      parent: null,
      parentdata: null,
      parentoptions: null,
    }
  },
  methods: {
    onSubmit() {
      api.post('/tags/user-tags',
        {
          name: this.name,
          id: null,
          assignable: true,
          elementIds: this.selected.map(s => s.id),
          parentId: this.parent ? this.parent.id : null,
          childrenIds: []
        }).then((response) => this.tagdata.push(response.data))
    },
    filterUsers(val, update, abort) {
      const self = this
      if (self.userdata === null) {
        update(() => {
          api.get('/users').then((response) => {
          self.userdata = response.data.map(u => {return {
            username: u.username,
            id: u.id
        }})
            self.useroptions = self.userdata})})
      } else {
        update(() => self.useroptions = self.userdata.filter((u) => u.username.toLowerCase().indexOf(val.toLowerCase()) > -1))
      }
    },
    filterParents(val, update, abort) {
      const self = this
      if (self.parentdata === null) {
        update(() => {
          api.get('/tags/user-tags').then((response) => {
            self.parentdata = response.data.map(p => {
              return {
                name: p.name,
                id: p.id
              }
            })
            self.parentoptions = self.parentdata
          })
        })
      } else {
        update(() => self.parentoptions = self.parentdata.filter((p) => p.name.toLowerCase().indexOf(val.toLowerCase()) > -1))
      }
    }
  },
  components: {
    TagLink
  }
})
</script>
