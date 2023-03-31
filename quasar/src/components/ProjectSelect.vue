<template>
  <q-select label="Projects *" multiple use-chips use-input counter v-model="selected" :rules="[val => val && val.length > 0]" :options="options" option-value="id" option-label="name" @filter="filterProjects"/>
</template>

<script>
import {api} from "boot/axios";

export default {
  name: "ProjectSelect",
  data() {
    return {
      data: null,
      options: null,
      selected: []
    }
  },
  methods: {
    filterProjects(val, update, abort) {
      const self = this
      if (self.data === null) {
        update(() => {
          api.get('/projects').then((response) => {
            self.data = response.data
            self.options = self.data})})
      } else {
        update(() => self.options = self.data.filter((p) => p.name.toLowerCase().includes(val.toLowerCase()) || p.path.toLowerCase().includes(val.toLowerCase())))
      }
    }
  }
}
</script>
