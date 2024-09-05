<template>
 <q-page class="flex flex-center">
   <div class="q-gutter-md">
     <div style="display: flex">
       <q-item v-if="!nameinput">
         <q-item-label class="text-h3">
           {{ data.name }}
         </q-item-label>
         <q-btn flat side class="q-ml-sm q-pa-none"><q-icon color="primary" class="q-mt-md" name="edit" @click="nameinput = !nameinput;name=data.name"></q-icon></q-btn>
       </q-item>
     <div v-else>
      <q-input class="text-h3 q-mt-md" v-model="name">
        <template v-slot:after>
       <q-btn color="primary" :label="$t('submit')" @click="onNameSubmit"/>
       <q-btn color="primary" flat :label="$t('cancel')" @click="nameinput = !nameinput"/>
        </template>
      </q-input>
     </div>

     <q-avatar class="q-mt-md q-ml-md" v-if="data.color" rounded :style="`background: ${data.color};`">
       <q-icon name="edit" size="xs" class="q-mt-lg q-ml-lg" color="white"/>
       <q-popup-proxy>
         <q-color v-model="data.color"
         @change="onColorSubmit"/>
       </q-popup-proxy>
     </q-avatar>
     <q-btn flat color="primary" class="q-ml-sm q-mt-md q-pa-none" style="position: relative; float: right" v-else @click="assignColor" :label="$t('tag.assign_color')"/>
     </div>

     <q-list bordered class="rounded-borders">
       <q-item>
         <q-item-section avatar>
            <q-icon color="primary" name="north_west"/>
         </q-item-section>
         <q-item-section>
           <TagSelect parent :url="'tags/' + elemtype + '-tags'" :default-selected="data.parent" v-if="parentinput" ref="parentSelect"/>
           <q-item-label v-else-if="data.parent">
            <TagBadge v-bind="data.parent" :elemtype="elemtype"/>
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
       <q-item >
         <q-item-section avatar>
           <q-icon color="primary" name="subdirectory_arrow_right"></q-icon>
         </q-item-section>
         <q-item-section class="text-weight-bold">
           <TagSelect v-if="childinput" :url="'tags/' + elemtype + '-tags'" :default-selected="data.children" ref="childSelect"/>
           <q-item-label v-else-if="data.children.length" lines="1">
             <TagBadge class="q-mr-xs" v-for="tag in data.children" v-bind="tag" :key="tag.name" :elemtype="elemtype"/>
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
       <q-item>
         <q-item-section>
           <q-input :rules="[ val => val && val.length > 0 || '']" v-if="newchildinput" :label="$t('tag.name')" v-model="childname"></q-input>
         </q-item-section>
         <q-item-section side>
           <q-btn :size="newchildinput ? 'md' : 'xs'" :flat="!newchildinput" :label="newchildinput ? $t('submit') : $t('tag.new_child')" color="primary" @click="onChildClick"></q-btn>
         </q-item-section>
       </q-item>
     </q-list>

     <ImpactList :url="'tags/' + elemtype + '-tags/' + $route.params.id" :key="$route.params.id"/>

     <ElemList v-if="elemtype === 'user'" :loading="loading" :elems="data.users"  :data="userdata"  elemtype="user"
               @addElem="(selected) => addElem(selected, data.users)"  @removeElem="(id) => removeElem(id, data.users)"/>

     <ElemList                            :loading="loading" :elems="data.events" :data="eventdata" elemtype="event"
               @addElem="(selected) => addElem(selected, data.events)" @removeElem="(id) => removeElem(id, data.events)"/>
   </div>
 </q-page>
</template>

<script>
import {api} from "boot/axios";
import {useRoute} from "vue-router";
import TagBadge from "components/TagBadge.vue";
import {getErrorMessage} from "src/util";
import TagSelect from "components/TagSelect.vue";
import ImpactList from "components/ImpactList.vue";
import {colors} from 'quasar'
import ElemList from "components/ElemList.vue";

const { getPaletteColor } = colors

function update(self, response) {
  api
    .put('tags/' + self.elemtype + '-tags', {
      name: self.data.name,
      id: self.data.id,
      color: self.data.color,
      childrenIds: self.data.children.map(c => c.id),
      userIds: self.data.users === null ? null : self.data.users.map(e => e.id),
      eventIds: self.data.events.map(e => e.id),
      parentId: self.data.parent === null ? null : self.data.parent.id
    })
    .then(response)
    .catch(error => self.$q.notify(self.$t(getErrorMessage(error))))
}

function changeTags(self, id, onFinish) {
  api
    .get('tags/' + self.elemtype + '-tags')
    .then((response) => {
      self.tagdata = response.data
      self.data = self.tagdata.find(t => t.id == id)
      self.data.children = self.data.childrenIds.map(id => self.tagdata.find(t => t.id === id))
      self.data.parent = self.data.parentId === null ? null : self.tagdata.find(t => t.id === self.data.parentId)
      self.data.users  = self.data.userIds  === null ? null : self.data.userIds.map(id => self.userdata.find(u => u.id === id))
      self.data.events = self.data.eventIds.map(id => self.eventdata.find(e => e.id === id))
      console.log(self.data.users)
      onFinish.call()
    })
    .catch(error => {
      self.$q.notify(self.$t(getErrorMessage(error)))
      console.log(error)
      onFinish.call()
    })

}

function updateElems(self) {
  update(self, (response) => {
    self.data.users  = response.data.userIds === null ? null : response.data.userIds.map(id => self.userdata.find(u => u.id === id))
    self.data.events = response.data.eventIds.map(id => self.eventdata.find(e => e.id === id))
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
  name: "TagDetailPage",
  components: {
    ElemList,
    ImpactList,
    TagSelect,
    TagBadge
  },
  props: {
    elemtype: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      tagdata: null,
      data: {
        name: '',
        users: [],
        events: [],
        children: [],
        parent: null,
        color: null
      },
      filter: '',
      parentinput: false,
      childinput: false,
      newchildinput: false,
      nameinput:false,
      name: '',
      childname: '',
      userdata: null,
      eventdata: null,
      loading: true,
      userloading: true,
      eventloading: true
    }
  },
  mounted() {
    const route = useRoute()
    const self = this

    api
      .get('users')
      .then((response) => {
        self.userdata = response.data
        changeTags(self, route.params.id, () => {self.userloading = false})
        self.userloading = false
        if (!self.eventloading) self.loading = false
      })
      .catch(error => {
        this.$q.notify(this.$t(getErrorMessage(error)))
        this.loading = false
      })
    api
      .get('events')
      .then((response) => {
        self.eventdata = response.data
        //changeTags(self, route.params.id, () => {self.eventloading = false})
        self.eventloading = false
        if (!self.userloading) self.loading = false
      })
      .catch(error => {
        this.$q.notify(this.$t(getErrorMessage(error)))
        this.loading = false
      })



  },
  methods: {
    addElem(selected, elemlist) {
      this.loading = true
      elemlist.push(...selected)
      updateElems(this)
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
    onChildClick() {
      if (this.newchildinput && this.childname !== '') {
        api.post('/tags/' + this.elemtype + '-tags', {
          name: this.childname,
          id: null,
          color: this.data.color,
          elementIds: [],
          parentId: this.data.id,
          childrenIds: []
        }).then(() => changeTags(this, this.data.id, () => {}))
      }
      this.childname = ''
      this.newchildinput = !this.newchildinput;
    },
    resetFilter() {
      this.filter = ''
    },
    removeElem(id, elemlist) {
      this.loading = true
      elemlist.splice(elemlist.indexOf(id), 1)
      updateElems(this)
      this.loading = false
    }
  },
  async beforeRouteUpdate(to, from) {
    changeTags(this, to.params.id, () => {})
    this.parentinput = false
    this.childinput = false
    this.newchildinput = false
  }
}
</script>
<script setup lang="ts">
</script>
