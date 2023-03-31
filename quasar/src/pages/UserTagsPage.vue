<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-list top bordered class="rounded-borders" style="min-width: 600px">
      <q-item-label header>{{ $t('tag.user') }}</q-item-label>
      <q-input class="q-pa-md" ref="filterRef" v-model="filter" :label="$t('filter')">
        <template v-slot:append>
          <q-icon v-if="filter !== ''" name="clear" class="cursor-pointer" @click="resetFilter" />
        </template>
      </q-input>
        <q-tree
          :nodes="tree"
          node-key="id"
          :filter="filter"
          :filter-method="node => node.name.toLowerCase().includes(filter.toLowerCase())"
        >
          <template v-slot:default-header="prop">
            <TagLink :elems="[...prop.node.users]" :name="prop.node.name" :id="prop.node.id" :elemname="$t('user.many').toLowerCase()" @deleteTag="(id) => deleteTag(id)"/>
          </template>-->
        </q-tree>
      </q-list>

    <q-btn bottom to="/user/tag/new" color="primary">{{ $t('add') }}</q-btn>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, ref} from 'vue'
import TagLink from "components/TagLink.vue";
import { api } from 'boot/axios'

function deleteNode(id, current, topLevel) {
  current.forEach((e) => {
    if (e.id === id) {
      e.children.forEach(c => topLevel.push(c))
      current.splice(current.indexOf(e), 1)
    } else {
      deleteNode(id, e.children, topLevel)
    }
  })
}

function union(set1, set2) {
  set2.forEach(e => set1.add(e))
}

function pluckChildren(elem, array) {
  elem.childrenIds.forEach((c) => {

    const child = array.find(e => c === e.id)
    if (child === undefined) return

    pluckChildren(child, array)
    elem.children.push(child)
    union(elem.users, child.users)
    array.splice(array.indexOf(child), 1)
  })
}
export default defineComponent({
  data() {
    return {
      tree: [],
      filter: '',
      filterRef: null
    }
        },
  name: 'UserTagsPage',
  components: {
    TagLink
  },
  mounted() {
    api.get('/tags/user-tags').then((response) => {
      this.tree = this.treeify(response.data.map(function(item) {return {name: item.name,
                                                                         id: item.id,
                                                                         users: new Set(item.elementIds),
                                                                         children: [],
                                                                         childrenIds: item.childrenIds,
                                                                         parentId: item.parentId}}))})
  },
  methods: {
    treeify: function(tags) {
      const res = []
      while (tags.length > 0) {
        const tag = tags.find(t => t.parentId === null)
        tags.splice(tags.indexOf(tag), 1)
        pluckChildren(tag, tags)
        res.push(tag)
      }
      return res
    },
    resetFilter: function() {
      this.filter.value = ''
      this.filterRef.focus()
    },
    deleteTag: function(id) {
    api.delete('tags/user-tags/' + id).then(deleteNode(id, this.tree, this.tree))

    }
  }

})
</script>
