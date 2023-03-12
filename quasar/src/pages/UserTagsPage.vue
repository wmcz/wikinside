<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-list top bordered class="rounded-borders" style="max-width: 600px">
      <q-item-label header>User tags</q-item-label>
        <q-tree
          :nodes="tree"
          node-key="id"
        >
          <template v-slot:default-header="prop">
            <UserTagLink :users="[...prop.node.users]" :name="prop.node.name"/>
          </template>-->
        </q-tree>
        <template #fallback>
          Loading...
        </template>
    </q-list>

    <q-btn bottom to="/user/tag/new" color="primary">Add</q-btn>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'
import UserTagLink from "components/UserTagLink.vue";
import { api } from 'boot/axios'

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
      tree: []
    }
        },
  name: 'UserTagsPage',
  components: {
    UserTagLink
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
    }
  }

})
</script>
