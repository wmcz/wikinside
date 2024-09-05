<template>
 <q-page class="flex flex-center">
   <div class="q-gutter-md">
     <div style="display: flex">
       <q-item v-if="!nameinput">
         <q-item-label class="text-h3">
           {{ eventdata.name }}
         </q-item-label>
         <q-btn flat side class="q-ml-sm q-pa-none"><q-icon color="primary" class="q-mt-md" name="edit" @click="nameinput = !nameinput;name=eventdata.name"></q-icon></q-btn>
         </q-item>
         <div v-else>
           <q-input class="text-h3 q-mx-md q-ml-sm" v-model="name">
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
           {{ new Date(Date.parse(eventdata.startDate)).toLocaleDateString() }} — {{ new Date(Date.parse(eventdata.endDate)).toLocaleDateString() }}
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
       <q-item v-if="projects.length > 0">
         <q-item-section avatar>
           <q-icon color="primary" name="public"/>
         </q-item-section>
         <q-item-section class="text-weight-bold">
           <q-select v-if="projectinput" :label="$t('project.many')" multiple use-chips use-input counter v-model="projectselect" :options="projectoptions" option-value="id" option-label="name" @filter="filterProjects"/>
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
       <q-item v-if="eventdata.category">
         <q-item-section avatar>
           <q-icon color="primary" class="text-weight-bolder">#</q-icon>
         </q-item-section>
         <q-item-section v-if="hashtaginput">
           <q-input v-model="hashtag"/>
         </q-item-section>
         <q-item-section v-else class="text-weight-bold">
           <a style='color: black' v-if="eventdata.strat === 'PHOTO'" :href="'https://commons.wikimedia.org/wiki/' + eventdata.category">
             {{ eventdata.category.substring(eventdata.category.indexOf(':') + 1) }}
           </a>
           <div v-else>
             {{ eventdata.category }}
           </div>
         </q-item-section>
         <q-item-section side>
           <div>
             <q-btn v-if="hashtaginput" color="primary" :label="$t('submit')" @click="onHashtagSubmit"/>
             <q-btn flat color="primary" :label="hashtaginput ? $t('cancel') : $t('edit')" @click="hashtaginput=!hashtaginput"/>
           </div>
         </q-item-section>
       </q-item>
       <q-item v-if="usertaglist.length > 0 || usertaginput">
          <q-item-section avatar>
            <q-icon color="primary" name="groups"/>
          </q-item-section>
          <q-item-section>
            <q-item-label style="align-content:center">
              <q-select v-if="usertaginput" :label="$t('tag.many')" multiple use-chips use-input counter v-model="usertagselect" :options="usertagoptions" option-value="id" option-label="name" @filter="filterUserTags"/>
              <TagBadge v-else class="q-mr-xs" v-for="tag in usertaglist" :key="tag.name" :id="tag.id" :name="tag.name" v-bind="tag" elemtype="user"/>
            </q-item-label>
          </q-item-section>
         <q-item-section side>
           <div>
             <q-btn v-if="usertaginput" color="primary" :label="$t('submit')" @click="onUserTagSubmit"/>
             <q-btn flat color="primary" :label="usertaginput ? $t('cancel') : $t('edit')" @click="usertaginput=!usertaginput"/>
           </div>
         </q-item-section>
       </q-item>
       <q-item v-else>
         <q-item-section/>
         <q-item-section side>
           <q-btn size='xs' flat :label="$t('event.new_usertag')" color="primary" @click="usertaginput = true"></q-btn>
         </q-item-section>
       </q-item>
     </q-list>

     <ImpactList :url="'events/' + $route.params.id" ref="impactref"/>

     <ElemList :loading="tagloading" :elems="taglist" :data="tagdata" elemtype="tag"
               @addElem="(selected) => onTagSubmit(selected)"  @removeElem="(id) => removeTag(id)"/>

     <q-toggle v-model="summary" :label="$t('tag.group')" class="q-pb-none"/>

     <ElemList v-if="!summary" class="q-mt-none" :loading="userloading" :elems="userlist" :data="userdata" elemtype="user" badges
               @addElem="(selected) => onUserSubmit(selected)"  @removeElem="(id) => removeUser(id)"/>

     <SummaryList v-else class="q-mt-none" :users="userlist" :tags="[...new Set(userlist.flatMap(u => u.tags))]" :loading="userloading"/>
   </div>
 </q-page>
</template>

<script>
import {api} from "boot/axios";
import {useRoute} from "vue-router";
import {getErrorMessage} from "src/util";
import ImpactList from "components/ImpactList.vue";
import TagBadge from "components/TagBadge.vue";
import ElemList from "components/ElemList.vue";
import SummaryList from "components/SummaryList.vue";

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
    SummaryList,
    TagBadge,
    ImpactList,
    ElemList
  },
  data() {
    return {
      tagfilter: '',
      userfilter: '',
      eventdata: {},
      tagdata: [],
      taglist: [],
      usertagdata: [],
      usertaglist: [],
      usertagselect: [],
      usertagoptions: [],
      usertaginput: false,
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
      hashtaginput: false,
      hashtag: '',
      nameinput:false,
      name: '',
      summary: false
    }
  },
  mounted() {
    api
      .get('events/' + useRoute().params.id)
      .then((response) => {
        this.eventdata = response.data
        this.hashtag = response.data.category
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
          api
            .get('tags/user-tags')
            .then((usertagresponse) => {
              this.usertagdata = usertagresponse.data
              this.usertaglist = this.usertagdata.filter(t => response.data.userTagIds.includes(t.id))
              this.usertagselect = this.usertaglist

              api
                .get('users')
                .then((userresponse) => {
                  this.userdata = userresponse.data
                  this.userlist = this.userdata.filter(u => response.data.userIds.includes(u.id)).map(
                    function (item) { return {
                      username: item.username,
                      id: item.id,
                      tags: [...new Set(item.inherentTagIds.concat(item.eventTagIds))].map(id => usertagresponse.data.find(e => id === e.id))
                    }}
                  )
                  this.userloading = false
                })
                .catch(error => {
                  console.log(error)
                  this.userloading = false
                  this.$q.notify(this.$t(getErrorMessage(error)))
                })
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
    onUserSubmit(users) {
      this.userloading = true
      this.eventdata.userIds.push(...users)
      updateUsers(this)
      this.userloading = false
    },
    onTagSubmit(tags) {
      this.tagloading = true
      this.eventdata.tagIds.push(...tags)
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
    onHashtagSubmit() {
      this.eventdata.category = this.hashtag;
      submit(this, (response) => {
        this.eventdata.category = response.data.category
      })
      this.hashtaginput = false
    },
    onUserTagSubmit() {
      this.eventdata.userTagIds = this.usertagselect.map(t => t.id);
      submit(this, (response) => {
        this.usertaglist = this.usertagdata.filter(t => response.data.userTagIds.includes(t.id))
      })
      this.usertaginput = false;
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
    },
    filterUserTags(val, update, abort) {
      update(() => this.usertagoptions = this.usertagdata.filter((u) => u.name.toLowerCase().includes(val.toLowerCase())))
    }
  }
}
</script>
