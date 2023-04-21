<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
      <q-list top bordered class="rounded-borders">
        <q-item-label header>{{ $t('project.many') }}</q-item-label>
        <q-input class="q-pa-md" ref="filterRef" v-model="filter" :label="$t('filter')">
          <template v-slot:append>
            <q-icon v-if="filter !== ''" name="clear" class="cursor-pointer" @click="resetFilter" />
          </template>
        </q-input>
        <q-table :rows="data" :row-key="name" grid :loading="loading" :filter="filter" :pagination="{ rowsPerPage: 10}">
          <template v-slot:item="props">
            <ProjectLink :key="props.row.name" v-bind="props.row"/>
          </template>
        </q-table>
      </q-list>

      <q-btn bottom to="/project/new" color="primary">{{ $t('add') }}</q-btn>
    </div>
  </q-page>
</template>

<script>
import ProjectLink from "components/ProjectLink.vue";
import {api} from "boot/axios";
import {getErrorMessage} from "src/util";

export default {
  name: "ProjectsPage",
  components: {
    ProjectLink
  },
  data() {
    return {
      data: [],
      loading: true,
      filter: ''
    }
  },
  mounted() {
    api
      .get('projects')
      .then((response) => {
        this.data = response.data
        this.loading = false
      })
      .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
  },
  methods: {
    resetFilter: function () {
      this.filter = ''
    }
  }
}
</script>

