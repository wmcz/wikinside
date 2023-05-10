<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
      <q-list top bordered class="rounded-borders">
        <q-item class="q-py-none q-pl-none">
          <q-item-label header>{{ $t('project.many') }}</q-item-label>
          <q-space />
          <q-input  side dense input-class="text-right" style="float: right" class="q-pt-xs" ref="filterRef" v-model="filter" :label="$t('filter')">
            <template v-slot:append>
              <q-icon v-if="filter !== ''" name="clear" class="cursor-pointer" @click="resetFilter" />
              <q-icon v-else name="search"/>
            </template>
          </q-input>
        </q-item>
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

