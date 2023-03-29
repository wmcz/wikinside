<template>
 <q-page class="flex flex-center">
   <div class="q-gutter-md">
     <h3>{{ eventdata.name }}</h3>
     <q-list top bordered class="rounded-borders" style="min-width: 600px">
       <q-item-label header>Tags</q-item-label>
       <q-input class="q-pa-md" ref="tagFilterRef" v-model="tagfilter" label="Filter">
         <template v-slot:append>
           <q-icon v-if="tagfilter !== ''" name="clear" class="cursor-pointer" @click="resetTagFilter" />
         </template>
       </q-input>
       <q-table :rows="taglist" :row-key="name" grid style="max-width: 600px" :loading="tagloading" :filter="tagfilter" :pagination="{ rowsPerPage: 10}">
         <template v-slot:item="props">
           <TagLink :key="props.row.name" suppresselems v-bind="props.row" right-icon="clear" @deleteTag="(id) => removeTag(id)" style="width: 600px"/>
         </template>
         <template v-slot:no-data>
           No tags
         </template>
       </q-table>
       <div v-if="taginput" class="q-mb-md q-mx-md q-mt-none">
         <TagSelect  url="tags/event-tags" label="Add tags" ref="tagSelect"/>
         <q-btn class="q-mr-sm" color="primary" label="Submit" @click="onTagSubmit"/>
         <q-btn outline color="primary" label="Cancel" @click="taginput = false"/>
       </div>
       <q-btn v-else class="q-mb-md q-ml-md" color="primary" label="Add tags" @click="taginput = true"/>
     </q-list>

     <q-list top bordered class="rounded-borders" style="min-width: 600px">
       <q-item-label header>Users</q-item-label>
       <q-input class="q-pa-md" ref="userFilterRef" v-model="userfilter" label="Filter">
         <template v-slot:append>
           <q-icon v-if="userfilter !== ''" name="clear" class="cursor-pointer" @click="resetUserFilter" />
         </template>
       </q-input>
       <q-table :rows="userlist" :row-key="name" grid style="max-width: 600px" :loading="userloading" :filter="userfilter" :pagination="{ rowsPerPage: 10}">
         <template v-slot:item="props">
           <UserLink :key="props.row.username" supresstags v-bind="props.row" right-icon="clear" @deleteUser="(id) => removeUser(id)" style="width: 600px"/>
         </template>
         <template v-slot:no-data>
           No users
         </template>
       </q-table>
       <div v-if="userinput" class="q-mb-md q-mx-md q-mt-none">
         <UserSelect label="Add users" ref="userSelect"/>
         <q-btn class="q-mr-sm" color="primary" label="Submit" @click="onUserSubmit"/>
         <q-btn outline color="primary" label="Cancel" @click="userinput = false"/>
       </div>
       <q-btn v-else class="q-mb-md q-ml-md" color="primary" label="Add users" @click="userinput = true"/>
     </q-list>
   </div>
 </q-page>
</template>

<script>
import {api} from "boot/axios";
import {useRoute} from "vue-router";
import UserSelect from "components/UserSelect.vue";
import TagSelect from "components/TagSelect.vue";
import TagLink from "components/TagLink.vue";
import UserLink from "components/UserLink.vue";

function updateUsers(self) {
  api.put('events', self.eventdata).then((response) => {
    self.eventdata = response.data
    self.userlist = self.userdata.filter(u => self.eventdata.userIds.includes(u.id))
  })
}

function updateTags(self) {
  api.put('events', self.eventdata).then((response) => {
    self.eventdata = response.data
    self.taglist = self.tagdata.filter(t => self.eventdata.tagIds.includes(t.id))
  })
}

export default {
  name: "EventDetailPage",
  components: {
    UserLink,
    TagLink,
    TagSelect,
    UserSelect
  },
  data() {
    return {
      tagfilter: '',
      userfilter: '',
      eventdata: {},
      tagdata: [],
      taglist: [],
      userdata: [],
      userlist: [],
      tagloading: true,
      userloading: true,
      taginput: false,
      userinput: false
    }
  },
  mounted() {
    api.get('events/' + useRoute().params.id).then((response) => {
      this.eventdata = response.data
      api.get('tags/event-tags').then((tagresponse) => {
        this.tagdata = tagresponse.data
        this.taglist = this.tagdata.filter(t => response.data.tagIds.includes(t.id))
        this.tagloading = false
      })
      !response.data.userIds.length ? this.userloading = false :
        api.get('users').then((userresponse) => {
          this.userdata = userresponse.data
          this.userlist = this.userdata.filter(u => response.data.userIds.includes(u.id))
          this.userloading = false
        })
    })
  },
  methods: {
    onUserSubmit() {
      this.userloading = true
      this.eventdata.userIds.push(...this.$refs.userSelect.selected.map(u => u.id))
      updateUsers(this)
      this.userloading = false
    },
    onTagSubmit() {
      this.tagloading = true
      this.eventdata.tagIds.push(...this.$refs.tagSelect.selected.map(t => t.id))
      updateTags(this)
      this.tagloading = false
    },
    resetTagFilter() {
      this.tagfilter = ''
    },
    resetUserFilter() {
      this.userfilter = ''
    },
    removeTag(id) {
      this.tagloading = true
      this.eventdata.tagIds.splice(this.eventdata.tagIds.indexOf(id), 1)
      updateTags(this)
      this.tagloading = false
    },
    removeUser(id) {
      this.userloading = true
      this.eventdata.userIds.splice(this.eventdata.userIds.indexOf(id), 1)
      updateUsers(this)
      this.userloading = false
    }
  }
}
</script>
