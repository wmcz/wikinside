<template>
  <q-table separator="none" :loading="loading" bordered flat
    :rows="tags.map(t => {return {tags: t, users: users.map(u=>u.tags.flatMap(tt => tt.id)).filter(l => l.includes(t.id)).length}})"
    :pagination="{rowsPerPage: 10, sortBy: 'users', descending: true}"
    :columns="[{
      name: 'tag',
      label: '',
      field: 'tag'
    },{
      name: 'users',
      label: '',
      field: 'users',
    }]"
    visible-columns="tag"
  >
    <template v-slot:header>
      <q-item-label header> {{ $t('tag.many') }} </q-item-label>
    </template>
    <template v-slot:body-cell-tag="props">
      <TagLink router-link :to="{name: 'usertag', params: {id: props.row.tags.id}}" :v-bind="props.row.tags.id" elemtype="user" :name="props.row.tags.name" :color="props.row.tags.color" suppresselems suppressSide>
        <div class="text-weight-bold text-primary text-h6">
          {{ props.row.users }}
        </div>
      </TagLink>
    </template>
    <template v-slot:no-data>
      {{ $t('tag.none') }}
    </template>
  </q-table>
</template>

<script>
import TagLink from "components/TagLink.vue";

export default {
  name: "SummaryList",
  components: {TagLink},
  props: {
    tags: {
      type: Array,
      required: true
    },
    users: {
      type: Array,
      required: true
    },
    loading: {
      type: Boolean
    }
  }
}
</script>
