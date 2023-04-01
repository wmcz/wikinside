<template>
  <q-select :label=" (parent ? $t('tag.parent') : $t('tag.many')) + $t('optional')" :multiple="!parent" use-chips use-input :counter="!parent" v-model="selected" :options="tagoptions" option-value="id" option-label="name" @filter="filterTags"/>
</template>

<script>
import {api} from "boot/axios";
import {getErrorMessage} from "src/util";

export default {
  name: "TagSelect",
  data() {
    return {
      tagdata: null,
      tagoptions: null,
      selected: parent ? null : []
    }
  },
  props: {
    url: {
      type: String,
      required: true
    },
    parent: {
      type: Boolean
    }
  }
  ,
  methods: {
    filterTags(val, update, abort) {
      const self = this
      if (self.tagdata === null) {
        update(() => {
          api
            .get(this.url)
            .then((response) => {
              self.tagdata = response.data.map(t => {return {
                name: t.name,
                id: t.id
              }})
              self.tagoptions = self.tagdata})
            .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))})
      } else {
        update(() => self.tagoptions = self.tagdata.filter((u) => u.name.toLowerCase().includes(val.toLowerCase())))
      }
    }
  }
}
</script>
