<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
      style="max-width: 600px"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="name" :label="$t('project.name') + ' *'" />
      <q-input :rules="[ val => val && val.length > 0 && !val.startsWith('http') || '']" v-model="path" :label="$t('project.path') + ' *'" :hint="$t('project.path_hint')"/>
      <q-btn color="primary" type="submit">{{ $t('submit') }}</q-btn>
    </q-form>
    <q-list bottom bordered class="rounded-borders" style="min-width: 600px">
      <ProjectLink v-for="project in data" :key="project.name" v-bind="project"/>
    </q-list>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'
import {api} from "boot/axios";
import ProjectLink from "components/ProjectLink.vue";
import {getErrorMessage} from "src/util";

export default defineComponent({
  name: 'UsersNewPage',
  data() {
    return {
      name: null,
      path: null,
      data: []
    }
  },
  methods: {
    onSubmit() {
      api
        .post('/projects', {
          name: this.name,
          id: null,
          path: this.path
        })
        .then((response) => this.data.push(response.data))
        .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
    },
  },
  components: {
    ProjectLink
  }
})
</script>
