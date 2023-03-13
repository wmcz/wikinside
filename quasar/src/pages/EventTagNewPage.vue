<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="name" label="Tag name *" />

      <q-btn color="primary" type="submit">Submit</q-btn>
    </q-form>
    <q-list bottom bordered class="rounded-borders" style="max-width: 600px">
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
  name: 'EventTagNewPage',
  data() {
    return {
      name: null,
      tagdata: []
    }
  },
  methods: {
    onSubmit() {
      api.post('/tags/event-tags',
        {
          name: this.name,
          id: null,
          assignable: true,
          elementIds: [],
          parentId: null,
          childrenIds: []
        }).then((response) => this.tagdata.push(response.data))
    }
  },
  components: {
    TagLink
  }
})
</script>
