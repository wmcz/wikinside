<template>
  <q-select label="Tags (optional)" multiple use-chips use-input counter v-model="selected" :options="tagoptions" option-value="id" option-label="name" @filter="filterTags"/>
</template>

<script>
import {api} from "boot/axios";

export default {
  name: "TagSelect",
  data() {
    return {
      tagdata: null,
      tagoptions: null,
      selected: []
    }
  },
  props: {
    url: {
      type: String,
      required: true
    }
  }
  ,
  methods: {
    filterTags(val, update, abort) {
      const self = this
      if (self.tagdata === null) {
        update(() => {
          api.get(this.url).then((response) => {
            self.tagdata = response.data.map(t => {return {
              name: t.name,
              id: t.id
            }})
            self.tagoptions = self.tagdata})})
      } else {
        update(() => self.tagoptions = self.tagdata.filter((u) => u.name.toLowerCase().includes(val.toLowerCase())))
      }
    }
  }
}
</script>
