<template>
  <q-page class="flex flex-center">
    <q-list bordered class="rounded-borders" style="max-width: 600px">
      <q-item-label header>User</q-item-label>
      <UserLink />
    </q-list>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'
import UserLink from "components/UserLink.vue";
import { api } from 'boot/axios'


export default defineComponent({
  name: 'UsersPage',
  components: {
    UserLink
  },
  setup() {
    api.get('/users').then((response) => console.log(response.data))
  }

})
</script>
