<template>
 <q-page class="flex flex-center">
   <div class="q-gutter-md">
     <div style="display: flex">
       <h3 v-if="!nameinput" class="q-mb-sm" @dblclick="nameinput = !nameinput;name=eventdata.name">
         {{ eventdata.name }}
       </h3>
       <div v-else>
         <q-input class="text-h3 q-mt-xl" v-model="name">
           <template v-slot:after>
             <q-btn color="primary" :label="$t('submit')" @click="onNameSubmit"/>
             <q-btn color="primary" flat :label="$t('cancel')" @click="nameinput = !nameinput"/>
           </template>
         </q-input>
       </div>
     </div>
     <q-list bordered class="rounded-borders">
       <q-item>
         <q-item-section avatar>
            <q-icon color="primary" name="date_range"/>
         </q-item-section>
         <q-item-section>
           {{ eventdata.startDate }} — {{ eventdata.endDate }}
         </q-item-section>
         <q-item-section side>
           <q-btn color="primary" flat :label="dateinput ? $t('cancel') : $t('edit')" @click="dateinput=!dateinput"/>
           <q-popup-proxy @before-hide="dateinput = false">
            <q-date landscape range today-btn v-model="date" mask="YYYY-MM-DD">
              <q-btn style="float: right" flat color="primary" :label="$t('submit')" @click="onDateSubmit"/>
            </q-date>
           </q-popup-proxy>
         </q-item-section>
       </q-item>
       <q-item>
         <q-item-section avatar>
           <q-icon color="primary" name="public"/>
         </q-item-section>
         <q-item-section class="text-weight-bold">
           <q-select v-if="projectinput" :label="$t('project.many')" multiple use-chips use-input:counter v-model="projectselect" :options="projectoptions" option-value="id" option-label="name" @filter="filterProjects"/>
           <q-item-label lines="1" v-else>
             <q-badge class="q-mr-xs" rounded v-for="project in projects" :label="project.name" :key="project.name"/>
           </q-item-label>
         </q-item-section>
         <q-item-section side>
           <div>
            <q-btn v-if="projectinput" color="primary" :label="$t('submit')" @click="onProjectSubmit"/>
            <q-btn flat color="primary" :label="projectinput ? $t('cancel') : $t('edit')" @click="projectinput=!projectinput"/>
           </div>
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

     <ImpactList :url="'events/' + $route.params.id" ref="impactref"/>

     <q-list top bordered class="rounded-borders">
       <q-item class="q-py-none q-pl-none">
         <q-item-label header>{{ $t('tag.many') }}</q-item-label>
         <q-space />
         <q-input  side dense input-class="text-right" style="float: right" class="q-pt-xs" v-model="tagfilter" :label="$t('filter')">
           <template v-slot:append>
             <q-icon v-if="tagfilter !== ''" name="clear" class="cursor-pointer" @click="resetTagFilter" />
             <q-icon v-else name="search"/>
           </template>
         </q-input>
       </q-item>
       <q-table :rows="taglist" :row-key="name" grid  :loading="tagloading" :filter="tagfilter" :pagination="{ rowsPerPage: 10}">
         <template v-slot:item="props">
           <TagLink elemtype="event" :key="props.row.name" suppresselems v-bind="props.row" right-icon="clear" @deleteTag="(id) => removeTag(id)"/>
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

     <q-list top bordered class="rounded-borders">
       <q-item class="q-py-none q-pl-none">
         <q-item-label header>{{ $t('user.many') }}</q-item-label>
         <q-space />
         <q-input  side dense input-class="text-right" style="float: right" class="q-pt-xs" v-model="userfilter" :label="$t('filter')">
           <template v-slot:append>
             <q-icon v-if="userfilter !== ''" name="clear" class="cursor-pointer" @click="resetUserFilter" />
             <q-icon v-else name="search"/>
           </template>
         </q-input>
       </q-item>
       <q-table :rows="userlist" :row-key="name" grid :loading="userloading" :filter="userfilter" :pagination="{ rowsPerPage: 10}">
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
import TagSelect from "components/TagSelect.vue";
import TagLink from "components/TagLink.vue";
import UserLink from "components/UserLink.vue";
import {getErrorMessage} from "src/util";
import ImpactList from "components/ImpactList.vue";

function submit(self, then) {
  self.$refs.impactref.showDisclaimer = true
  api
    .put('events', self.eventdata)
    .then(then)
    .catch(error => self.$q.notify(self.$t(getErrorMessage(error))))
}

function updateUsers(self) {
  submit(self, (response) => {
    self.eventdata = response.data
    self.userlist = self.userdata.filter(u => self.eventdata.userIds.includes(u.id))})
}

function updateTags(self) {
  submit(self, (response) => {
    self.eventdata = response.data
    self.taglist = self.tagdata.filter(t => self.eventdata.tagIds.includes(t.id))})
}

function updateName(self) {
  submit(self, (response) => {
    self.eventdata.name = response.data.name
    self.nameinput = false;
  })
}

export default {
  name: "EventDetailPage",
  components: {
    ImpactList,
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
      date: null,
      projectdata: [],
      projectoptions: [],
      projects: [],
      projectselect: [],
      tagloading: true,
      userloading: true,
      taginput: false,
      userinput: false,
      dateinput: false,
      projectinput: false,
      nameinput:false,
      name: '',
    }
  },
  mounted() {
    api
      .get('events/' + useRoute().params.id)
      .then((response) => {
        this.eventdata = response.data
        this.date = {from: this.eventdata.startDate, to: this.eventdata.endDate}
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
        api
          .get('projects')
          .then((projectresponse) => {
            this.projectdata = projectresponse.data
            this.projects = this.projectdata.filter(p => response.data.projectIds.includes(p.id))
            this.projectselect = this.projects
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
    onDateSubmit() {
      this.eventdata.startDate = typeof this.date === "string" ? this.date : this.date.from
      this.eventdata.endDate = typeof this.date === "string" ? this.date : this.date.to
      submit(this, (response) => {
        this.eventdata.startDate = response.data.startDate
        this.eventdata.endDate = response.data.endDate
      })
    },
    onProjectSubmit() {
      this.eventdata.projectIds = this.projectselect.map(p => p.id);
      submit(this, (response) => {
        this.projects = this.projectdata.filter(p => response.data.projectIds.includes(p.id))
      })
      this.projectinput = false
    },
    onNameSubmit() {
      this.eventdata.name = this.name
      updateName(this)
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
    },
    filterProjects(val, update, abort) {
      update(() => this.projectoptions = this.projectdata.filter((u) => u.name.toLowerCase().includes(val.toLowerCase())))
    }
  }
}
</script>
