<template>
 <q-page class="flex flex-center">
   <div class="q-gutter-md">
     <h3 class="q-mb-sm">{{ eventdata.name }}</h3>
     <q-list bordered class="rounded-borders">
       <q-item>
         <q-item-section avatar>
            <q-icon color="primary" name="date_range"/>
         </q-item-section>
         <q-item-section>
           {{ eventdata.startDate }} — {{ eventdata.endDate }}
         </q-item-section>
       </q-item>
       <q-item v-if="eventdata.hashtag">
         <q-item-section avatar>
           <q-icon color="primary" class="text-weight-bolder">#</q-icon>
         </q-item-section>
         <q-item-section class="text-weight-bold">
           {{ eventdata.hashtag }}
         </q-item-section>
       </q-item>
     </q-list>
     <q-list top bordered class="rounded-borders" style="min-width: 600px">
       <q-item-label header>{{ $t('tag.many') }}</q-item-label>
       <q-input class="q-pa-md" ref="tagFilterRef" v-model="tagfilter" :label="$t('filter')">
         <template v-slot:append>
           <q-icon v-if="tagfilter !== ''" name="clear" class="cursor-pointer" @click="resetTagFilter" />
         </template>
       </q-input>
       <q-table :rows="taglist" :row-key="name" grid style="max-width: 600px" :loading="tagloading" :filter="tagfilter" :pagination="{ rowsPerPage: 10}">
         <template v-slot:item="props">
           <TagLink elemtype="event" :key="props.row.name" suppresselems v-bind="props.row" right-icon="clear" @deleteTag="(id) => removeTag(id)" style="width: 600px"/>
         </template>
         <template v-slot:no-data>
           {{ $t('tag.none') }}
         </template>
       </q-table>
       <div v-if="taginput" class="q-mb-md q-mx-md q-mt-none">
         <TagSelect  url="tags/event-tags" :label="$t('tag.add')" ref="tagSelect"/>
         <q-btn class="q-mr-sm" color="primary" :label="$t('submit')" @click="onTagSubmit"/>
         <q-btn outline color="primary" :label="$t('cancel')" @click="taginput = false"/>
       </div>
       <q-btn v-else class="q-mb-md q-ml-md" color="primary" :label="$t('tag.add')" @click="taginput = true"/>
     </q-list>

     <q-list top bordered class="rounded-borders" style="min-width: 600px">
       <q-item-label header>{{ $t('user.many') }}</q-item-label>
       <q-input class="q-pa-md" ref="userFilterRef" v-model="userfilter" :label="$t('filter')">
         <template v-slot:append>
           <q-icon v-if="userfilter !== ''" name="clear" class="cursor-pointer" @click="resetUserFilter" />
         </template>
       </q-input>
       <q-table :rows="userlist" :row-key="name" grid style="max-width: 600px" :loading="userloading" :filter="userfilter" :pagination="{ rowsPerPage: 10}">
         <template v-slot:item="props">
           <UserLink :key="props.row.username" supresstags v-bind="props.row" right-icon="clear" @deleteUser="(id) => removeUser(id)" style="width: 600px"/>
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
import TagSelect from "components/TagSelect.vue";
import TagLink from "components/TagLink.vue";
import UserLink from "components/UserLink.vue";
import {getErrorMessage} from "src/util";

function updateUsers(self) {
  api
    .put('events', self.eventdata)
    .then((response) => {
      self.eventdata = response.data
      self.userlist = self.userdata.filter(u => self.eventdata.userIds.includes(u.id))})
    .catch(error => self.$q.notify(self.$t(getErrorMessage(error))))
}

function updateTags(self) {
  api
    .put('events', self.eventdata)
    .then((response) => {
      self.eventdata = response.data
      self.taglist = self.tagdata.filter(t => self.eventdata.tagIds.includes(t.id))})
    .catch(error => self.$q.notify(self.$t(getErrorMessage(error))))
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
    api
      .get('events/' + useRoute().params.id)
      .then((response) => {
        this.eventdata = response.data
        api
          .get('tags/event-tags')
          .then((tagresponse) => {
            this.tagdata = tagresponse.data
            this.taglist = this.tagdata.filter(t => response.data.tagIds.includes(t.id))
            this.tagloading = false
          })
          .catch(error => {
            this.tagloading = false
            this.$q.notify(this.$t(getErrorMessage(error)))
          })
        !response.data.userIds.length ? this.userloading = false :
        api
          .get('users')
          .then((userresponse) => {
            this.userdata = userresponse.data
            this.userlist = this.userdata.filter(u => response.data.userIds.includes(u.id))
            this.userloading = false
          })
          .catch(error => {
            this.userloading = false
            this.$q.notify(this.$t(getErrorMessage(error)))
          })
      })
      .catch(error => {
        this.tagloading = false
        this.userloading = false
        this.$q.notify(this.$t(getErrorMessage(error)))
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
