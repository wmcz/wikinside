<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="username" label="User name *" />
      <q-input v-model="tag" label="Tags" hint="this one doesn't work yet"/>

      <q-btn color="primary" type="submit">Submit</q-btn>
    </q-form>
    <q-list bottom bordered class="rounded-borders" style="max-width: 600px">
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
      userdata: []
    }
  },
  methods: {
    onSubmit() {
      api.post('/users',
        {
          username: this.username,
          id: null,
          tagIds: [],
          eventIds: []
        }).then((response) => this.userdata.push(response.data))
    }
  },
  components: {
    UserLink
  }
})
</script>
