<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-list top bordered class="rounded-borders" style="min-width: 600px">
      <q-item-label header>Users</q-item-label>
        <UserLink v-for="user in userdata" :key="user.username" v-bind="user" @deleteUser="(id) => deleteUser(id)"/>
    </q-list>

    <q-btn bottom to="/user/new" color="primary">Add</q-btn>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'
import UserLink from "components/UserLink.vue";
import { api } from 'boot/axios'


export default defineComponent({
  data() {
    return {
      userdata: []
    }
        },
  name: 'UsersPage',
  components: {
    UserLink
  },
  mounted() {
    api.get('/users').then((response) => {
      this.userdata = response.data.map(function(item) {return {username: item.username, id: item.id}})})
  },
  methods: {
    deleteUser: function (id) {
      api.delete('/users/' + id).then(
        this.userdata.splice(
          this.userdata.indexOf(
            this.userdata.find(e => e.id === id)),
          1))

    }
  }
})
</script>
