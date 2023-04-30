<template>
 <q-page class="flex flex-center">
   <div class="q-gutter-md">
     <h3 class="q-mb-sm">{{ data.name }}</h3>
     <q-list bordered class="rounded-borders">
       <q-item>
         <q-item-section avatar>
            <q-icon color="primary" name="north_west"/>
         </q-item-section>
         <q-item-section>
           <TagSelect parent url="tags/event-tags" :default-selected="data.parent" v-if="parentinput" ref="parentSelect"/>
           <q-item-label v-else-if="data.parent">
            <TagBadge v-bind="data.parent" elemtype="event"/>
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
           <TagSelect v-if="childinput" url="tags/event-tags" :default-selected="data.children" ref="childSelect"/>
           <q-item-label v-else-if="data.children.length" lines="1">
             <TagBadge class="q-mr-xs" v-for="tag in data.children" v-bind="tag" :key="tag.name" elemtype="event"/>
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

     <ImpactList :url="'tags/event-tags/' + $route.params.id"/>

     <q-list top bordered class="rounded-borders">
       <q-item class="q-py-none q-pl-none">
         <q-item-label header>{{ $t('event.many') }}</q-item-label>
         <q-space />
         <q-input  side dense input-class="text-right" style="float: right" class="q-pt-xs" v-model="filter" :label="$t('filter')">
           <template v-slot:append>
             <q-icon v-if="filter !== ''" name="clear" class="cursor-pointer" @click="resetFilter" />
             <q-icon v-else name="search"/>
           </template>
         </q-input>
       </q-item>
       <q-table :rows="list" :row-key="name" grid :loading="loading" :filter="filter" :pagination="{ rowsPerPage: 10}">
         <template v-slot:item="props">
           <EventLink :key="props.row.name" supressnotag v-bind="props.row" right-icon="clear" @deleteEvent="(id) => removeEvent(id)"/>
         </template>
         <template v-slot:no-data>
           {{ $t('event.none') }}
         </template>
       </q-table>
       <div v-if="eventinput" class="q-mb-md q-mx-md q-mt-none">
         <EventSelect :label="$t('event.add')" ref="eventSelect"/>
         <q-btn class="q-mr-sm" color="primary" :label="$t('submit')" @click="onEventSubmit"/>
         <q-btn outline color="primary" :label="$t('cancel')" @click="eventinput = false"/>
       </div>
       <q-btn v-else class="q-mb-md q-ml-md" color="primary" :label="$t('event.add')" @click="eventinput = true"/>
     </q-list>
   </div>
 </q-page>
</template>

<script>
import {api} from "boot/axios";
import {useRoute} from "vue-router";
import EventSelect from "components/EventSelect.vue";
import EventLink from "components/EventLink.vue";
import TagBadge from "components/TagBadge.vue";
import {getErrorMessage} from "src/util";
import TagSelect from "components/TagSelect.vue";
import ImpactList from "components/ImpactList.vue";

function update(self, response) {
  api
    .put('tags/event-tags', {
      name: self.data.name,
      id: self.data.id,
      childrenIds: self.data.children.map(c => c.id),
      elementIds: self.data.elems.map(e => e.id),
      parentId: self.data.parent === null ? null : self.data.parent.id
    })
    .then(response)
    .catch(error => self.$q.notify(self.$t(getErrorMessage(error))))
}

function changeTags(self, id, onFinish) {
  api
    .get('tags/event-tags')
    .then((response) => {
      self.tagdata = response.data
      self.data = self.tagdata.find(t => t.id == id)
      self.data.children = self.data.childrenIds.map(id => self.tagdata.find(t => t.id === id))
      self.data.parent = self.data.parentId === null ? null : self.tagdata.find(t => t.id === self.data.parentId)
      self.data.elems = self.data.elementIds.map(id => self.eventdata.find(u => u.id === id))
      self.list = self.data.elems
      onFinish.call()
    })
    .catch(error => {
      self.$q.notify(self.$t(getErrorMessage(error)))
      onFinish.call()
    })

}

function updateEvents(self) {
  update(self, (response) => {
    self.data.elems = response.data.elementIds.map(id => self.eventdata.find(u => u.id === id))
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

export default {
  name: "EventTagDetailPage",
  components: {
    ImpactList,
    TagSelect,
    EventLink,
    EventSelect,
    TagBadge
  },
  data() {
    return {
      tagdata: null,
      data: {
        name: '',
        elems: [],
        children: [],
        parent: null
      },
      filter: '',
      eventinput: false,
      parentinput: false,
      childinput: false,
      eventdata: null,
      list: [],
      loading: true,
    }
  },
  mounted() {
    const route = useRoute()
    const self = this

    api
      .get('events')
      .then((response) => {
        self.eventdata = response.data
        changeTags(self, route.params.id, () => {self.loading = false})
        self.loading = false
      })
      .catch(error => {
        this.$q.notify(this.$t(getErrorMessage(error)))
        this.loading = false
      })



  },
  methods: {
    onEventSubmit() {
      this.loading = true
      this.data.elems.push(...this.$refs.eventSelect.selected)
      updateEvents(this)
      this.$refs.eventSelect.selected = []
      this.eventinput = false
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
    resetFilter() {
      this.filter = ''
    },
    removeEvent(id) {
      this.eventloading = true
      this.data.elems.splice(this.data.elems.indexOf(id), 1)
      updateEvents(this)
      this.userloading = false
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
