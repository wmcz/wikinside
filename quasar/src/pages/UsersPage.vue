<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-list top bordered class="rounded-borders">
      <q-item class="q-py-none q-pl-none">
        <q-item-label header>{{ $t('user.many') }}</q-item-label>
        <q-space />
        <q-input  side dense input-class="text-right" style="float: right" class="q-pt-xs" ref="filterRef" v-model="filter" :label="$t('filter')">
          <template v-slot:append>
            <q-icon v-if="filter !== ''" name="clear" class="cursor-pointer" @click="resetFilter" />
            <q-icon v-else name="search"/>
          </template>
        </q-input>
      </q-item>
      <q-table :rows="userdata" :row-key="username" grid :loading="loading" :filter="filter" :pagination="{ rowsPerPage: 10}">
        <template v-slot:item="props">
          <UserLink :key="props.row.username" v-bind="props.row" @deleteUser="(id) => deleteUser(id)"/>
        </template>
      </q-table>
    </q-list>

    <q-btn bottom to="/user/new" color="primary">{{ $t('add') }}</q-btn>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'
import UserLink from "components/UserLink.vue";
import { api } from 'boot/axios'
import { getErrorMessage } from 'src/util'

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
    api
      .get('/tags/user-tags')
      .then((response) => {
        this.tagdata = response.data.map(function(item) {return {name: item.name, id: item.id}})})
      .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
    api
      .get('/users')
      .then((response) => {
        this.userdata = response.data.map(
          function(item) {return {
            username: item.username,
            id: item.id,
            tags: item.tagIds.map(id => self.$data.tagdata.find(e => id === e.id))}})
        this.loading = false
      })
      .catch(error => {
        this.$q.notify(this.$t(getErrorMessage(error)))
        this.loading = false
      })
  },
  methods: {
    deleteUser: function (id) {
      api
        .delete('/users/' + id)
        .then(
          this.userdata.splice(
            this.userdata.indexOf(
              this.userdata.find(e => e.id === id)),
            1))
        .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
    },
    resetFilter: function () {
      this.filter = ''
    }
  }
})
</script>
