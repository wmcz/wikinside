<template>
  <q-list top bordered class="rounded-borders">
    <q-item class="q-py-none q-pl-none">
      <q-item-label header>{{ $t(elemtype + '.many') }}</q-item-label>
      <q-space/>
      <q-input side dense input-class="text-right" style="float: right" class="q-pt-xs" v-model="filter"
               :label="$t('filter')">
        <template v-slot:append>
          <q-icon v-if="filter !== ''" name="clear" class="cursor-pointer" @click="resetFilter"/>
          <q-icon v-else name="search"/>
        </template>
      </q-input>
    </q-item>
    <q-table :rows="elems" :row-key="name" grid :loading="loading" :filter="filter"
             :pagination="{ rowsPerPage: 10}">
      <template v-slot:item="props">
        <component :is="linkFromElemType()" :key="props.row.name" :suppresstags="!badges" :suppresselems="!badges" v-bind="props.row" right-icon="clear"
                   @deleteElem="(id) => $emit('removeElem', id)"/>
      </template>
      <template v-slot:no-data>
        {{ $t(elemtype + '.none') }}
      </template>
    </q-table>
    <div v-if="input" class="q-mb-md q-mx-md q-mt-none">
      <component :is="selectFromElemType()" :label="$t(elemtype + '.add')" ref="elemSelect"/>
      <q-btn class="q-mr-sm" color="primary" :label="$t('submit')" @click="$emit('addElem', $refs.elemSelect.selected);$refs.elemSelect.selected=[];input=false"/>
      <q-btn outline color="primary" :label="$t('cancel')" @click="input = false"/>
    </div>
    <q-btn v-else class="q-mb-md q-ml-md" color="primary" :label="$t(elemtype + '.add')" @click="input = true"/>
  </q-list>
</template>

<script>
import EventSelect from "components/EventSelect.vue";
import UserSelect from "components/UserSelect.vue";
import EventLink from "components/EventLink.vue";
import UserLink from "components/UserLink.vue";
import TagLink from "components/TagLink.vue";
import TagSelect from "components/TagSelect.vue";

export default {
  name: 'ElemList',
  components: {
    EventLink,
    UserLink,
    TagLink,
    EventSelect,
    UserSelect,
    TagSelect
  },
  props: {
    elems: {
      type: Array,
      required: true
    },
    data: {
      type: Array,
      required: true
    },
    elemtype: {
      type: String,
      required: true
    },
    loading: {
      type: Boolean
    },
    badges: {
      type: Boolean
    }
  },
  data() {
    return {
      filter: '',
      input: false
    }
  },
  methods: {
    linkFromElemType() {
      switch (this.elemtype) {
        case 'event': return 'EventLink'
        case 'user':  return 'UserLink'
        case 'tag': return 'TagLink'
      }
    },
    selectFromElemType() {
      switch (this.elemtype) {
        case 'event': return 'EventSelect'
        case 'user':  return 'UserSelect'
        case 'tag': return 'TagSelect'
      }
    },
    resetFilter() {
      this.filter = ''
    },
  }
}
</script>
