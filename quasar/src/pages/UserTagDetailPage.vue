<template>
 <q-page class="flex flex-center">
   <div class="q-gutter-md">
     <div style="display: flex">
     <h3 v-if="!nameinput" class="q-mb-sm" @dblclick="nameinput = !nameinput;name=data.name">
       {{ data.name }}
       </h3>
       <div v-else>
         <q-input class="text-h3 q-mt-xl" v-model="name">
           <template v-slot:after>
             <q-btn color="primary" :label="$t('submit')" @click="onNameSubmit"/>
             <q-btn color="primary" flat :label="$t('cancel')" @click="nameinput = !nameinput"/>
           </template>
         </q-input>
       </div>

       <q-avatar class="q-mt-xl q-ml-md" v-if="data.color" rounded :style="`background: ${data.color}`">
         <q-icon name="edit" size="xs" class="q-mt-lg q-ml-lg" color="white"/>
         <q-popup-proxy>
           <q-color v-model="data.color"
                    @change="onColorSubmit"/>
         </q-popup-proxy>
       </q-avatar>
       <q-btn flat color="primary" class="q-mr-md q-mb-none q-mt-lg" style="position: relative; float: right" v-else @click="assignColor" :label="$t('tag.assign_color')"/>
       </div>

       <q-list bordered class="rounded-borders">
       <q-item>
         <q-item-section avatar>
            <q-icon color="primary" name="north_west"/>
         </q-item-section>
         <q-item-section>
           <TagSelect parent url="tags/user-tags" :default-selected="data.parent" v-if="parentinput" ref="parentSelect"/>
           <q-item-label v-else-if="data.parent">
            <TagBadge v-bind="data.parent" elemtype="user"/>
           </q-item-label>
           <q-item-label v-else caption> {{ $t('tag.no_parent') }} </q-item-label>
         </q-item-section>
         <q-item-section side>
           <div>
            <q-btn v-if="parentinput" color="primary" :label="$t('submit')" @click="onParentSubmit"/>
            <q-btn color="primary" flat :label="parentinput ? $t('cancel') : $t('edit')" @click="parentinput=!parentinput"/>
           </div>
         </q-item-section>
       </q-item>
       <q-item>
         <q-item-section avatar>
           <q-icon color="primary" name="subdirectory_arrow_right"></q-icon>
         </q-item-section>
         <q-item-section class="text-weight-bold">
           <TagSelect v-if="childinput" url="tags/user-tags" :default-selected="data.children" ref="childSelect"/>
           <q-item-label v-else-if="data.children.length" lines="1">
             <TagBadge class="q-mr-xs" v-for="tag in data.children" v-bind="tag" :key="tag.name" elemtype="user"/>
           </q-item-label>
           <q-item-label v-else caption> {{ $t('tag.no_children') }} </q-item-label>
         </q-item-section>
         <q-item-section side>
           <div>
             <q-btn v-if="childinput" color="primary" :label="$t('submit')" @click="onChildSubmit"/>
            <q-btn color="primary" flat :label="childinput ? $t('cancel') : $t('edit')" @click="childinput=!childinput"/>
           </div>
         </q-item-section>
       </q-item>
     </q-list>

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
       <q-table :rows="list" :row-key="name" grid :loading="loading" :filter="filter" :pagination="{ rowsPerPage: 10}">
         <template v-slot:item="props">
           <UserLink :key="props.row.username" supresstags v-bind="props.row" right-icon="clear" @deleteUser="(id) => removeUser(id)"/>
         </template>
         <template v-slot:no-data>
           {{ $t('user.none') }}
         </template>
       </q-table>
       <div v-if="userinput" class="q-mb-md q-mx-md q-mt-none">
         <UserSelect :label="$t('user.add')" ref="userSelect"/>
         <q-btn class="q-mr-sm" color="primary" :label="$t('submit')" @click="onUserSubmit"/>
         <q-btn outline color="primary" :label="$t('cancel')" @click="userinput = false"/>
       </div>
       <q-btn v-else class="q-mb-md q-ml-md" color="primary" :label="$t('user.add')" @click="userinput = true"/>
     </q-list>
   </div>
 </q-page>
</template>

<script>
import {api} from "boot/axios";
import {useRoute} from "vue-router";
import UserSelect from "components/UserSelect.vue";
import UserLink from "components/UserLink.vue";
import TagBadge from "components/TagBadge.vue";
import {getErrorMessage} from "src/util";
import TagSelect from "components/TagSelect.vue";
import { colors } from 'quasar'

const { getPaletteColor } = colors

function update(self, response) {
  api
    .put('tags/user-tags', {
      name: self.data.name,
      id: self.data.id,
      color: self.data.color,
      childrenIds: self.data.children.map(c => c.id),
      elementIds: self.data.elems.map(e => e.id),
      parentId: self.data.parent === null ? null : self.data.parent.id
    })
    .then(response)
    .catch(error => self.$q.notify(self.$t(getErrorMessage(error))))
}

function changeTags(self, id, onFinish) {
  api
    .get('tags/user-tags')
    .then((response) => {
      self.tagdata = response.data
      self.data = self.tagdata.find(t => t.id == id)
      self.data.children = self.data.childrenIds.map(id => self.tagdata.find(t => t.id === id))
      self.data.parent = self.data.parentId === null ? null : self.tagdata.find(t => t.id === self.data.parentId)
      self.data.elems = self.data.elementIds.map(id => self.userdata.find(u => u.id === id))
      self.list = self.data.elems
      onFinish.call()
    })
    .catch(error => {
      self.$q.notify(self.$t(getErrorMessage(error)))
      onFinish.call()
    })

}

function updateUsers(self) {
  update(self, (response) => {
    self.data.elems = response.data.elementIds.map(id => self.userdata.find(u => u.id === id))
    self.list = self.data.elems
  })
}

function updateParent(self) {
  update(self, (response) => {
    self.data.parent = self.tagdata.find(t => t.id === response.data.parentId)
  })
}

function updateChildren(self) {
  update(self, (response) => {
    self.data.children = response.data.childrenIds.map(id => self.tagdata.find(t => t.id === id))
  })
}

function updateColor(self) {
  update(self, (response) => {
    self.data.color = response.data.color
  })
}

function updateName(self) {
  update(self, (response) => {
    self.data.name = response.data.name
    self.nameinput = false;
  })
}

export default {
  name: "UserTagDetailPage",
  components: {
    TagSelect,
    UserLink,
    UserSelect,
    TagBadge
  },
  data() {
    return {
      tagdata: null,
      data: {
        name: '',
        elems: [],
        children: [],
        parent: null,
        color: null
      },
      filter: '',
      userinput: false,
      parentinput: false,
      childinput: false,
      nameinput:false,
      name: '',
      userdata: null,
      list: [],
      loading: true,
    }
  },
  mounted() {
    const route = useRoute()
    const self = this

    api
      .get('users')
      .then((response) => {
        self.userdata = response.data
        changeTags(self, route.params.id, () => {self.loading = false})
        self.loading = false
      })
      .catch(error => {
        this.$q.notify(this.$t(getErrorMessage(error)))
        this.loading = false
      })



  },
  methods: {
    onUserSubmit() {
      this.loading = true
      this.data.elems.push(...this.$refs.userSelect.selected)
      updateUsers(this)
      this.$refs.userSelect.selected = []
      this.userinput = false
      this.loading = false
    },
    onParentSubmit() {
      this.data.parent = this.$refs.parentSelect.selected
      updateParent(this)
      this.parentinput = false
    },
    onChildSubmit() {
      this.data.children = this.$refs.childSelect.selected
      updateChildren(this)
      this.childinput = false
    },
    assignColor() {
      this.data.color = getPaletteColor('primary');
    },
    onColorSubmit() {
      updateColor(this)
    },
    onNameSubmit() {
      this.data.name = this.name
      updateName(this)
    },
    resetFilter() {
      this.filter = ''
    },
    removeUser(id) {
      this.loading = true
      this.data.elems.splice(this.data.elems.indexOf(id), 1)
      updateUsers(this)
      this.loading = false
    }
  },
  async beforeRouteUpdate(to, from) {
    changeTags(this, to.params.id, () => {})
    this.userinput = false
    this.parentinput = false
    this.childinput = false
  }
}
</script>
