<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-list top bordered class="rounded-borders" style="min-width: 600px">
      <q-item-label header>Users</q-item-label>
      <q-input class="q-pa-md" ref="filterRef" v-model="filter" label="Filter">
        <template v-slot:append>
          <q-icon v-if="filter !== ''" name="clear" class="cursor-pointer" @click="resetFilter" />
        </template>
      </q-input>
      <q-table :rows="userdata" :row-key="username" grid style="max-width: 600px" :loading="loading" :filter="filter" :pagination="{ rowsPerPage: 10}">
        <template v-slot:item="props">
          <UserLink :key="props.row.username" v-bind="props.row" @deleteUser="(id) => deleteUser(id)" style="width: 600px"/>
        </template>
      </q-table>
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
      tagdata: [],
      userdata: [],
      filter: '',
      loading: true
    }
  },
  name: 'UsersPage',
  components: {
    UserLink
  },
  mounted() {
    const self = this
    api.get('/tags/user-tags').then((response) => {
      this.tagdata = response.data.map(function(item) {return {name: item.name, id: item.id}})})
    api.get('/users').then((response) => {
      this.userdata = response.data.map(
        function(item) {return {
          username: item.username,
          id: item.id,
          tags: item.tagIds.map(id => self.$data.tagdata.find(e => id === e.id))}})
      this.loading = false
    }, () => this.loading = false)
  },
  methods: {
    deleteUser: function (id) {
      api.delete('/users/' + id).then(
        this.userdata.splice(
          this.userdata.indexOf(
            this.userdata.find(e => e.id === id)),
          1))

    },
    resetFilter: function () {
      this.filter = ''
    }
  }
})
</script>
