<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
      <h3>{{ userdata.username }}</h3>

      <q-list bordered class="rounded-borders">
        <q-item>
          <q-item-section avatar>
            <q-icon color="primary" name="perm_contact_calendar"/>
          </q-item-section>
          <q-item-section>
            {{ new Date(Date.parse(userdata.registration)).toLocaleString() }}
          </q-item-section>
        </q-item>
      </q-list>

      <ImpactList :url="'users/' + $route.params.id" ref="impactref"/>

      <TagSelect url="tags/user-tags" label="tag.user" ref="tagSelect" linkable @tagsSelected="(tags) => onTagSubmit(tags)" @tagDeleted="(id) => removeTag(id)">
        <template v-if="eventtaglist.length > 0" v-slot:disclaimer>
          <q-item class="q-my-none">
            <q-item-label caption class="q-pr-xs" style="align-content: center">
              {{ $t('tag.from_event') }}
            </q-item-label>
            <q-item-label>
              <TagBadge class="q-mr-xs" v-for="tag in eventtaglist" :key="tag.name" :id="tag.id" :name="tag.name" v-bind="tag" elemtype="user"/>
            </q-item-label>
          </q-item>
        </template>
      </TagSelect>

    <q-list top bordered class="rounded-borders">
      <q-item class="q-py-none q-pl-none">
        <q-item-label header>{{ $t('event.many') }}</q-item-label>
        <q-space />
        <q-input  side dense input-class="text-right" style="float: right" class="q-pt-xs" v-model="eventfilter" :label="$t('filter')">
          <template v-slot:append>
            <q-icon v-if="eventfilter !== ''" name="clear" class="cursor-pointer" @click="resetEventFilter" />
            <q-icon v-else name="search"/>
          </template>
        </q-input>
      </q-item>
      <q-table :rows="eventlist" :row-key="name" grid :loading="eventloading" :filter="eventfilter" :pagination="{ rowsPerPage: 10}">
        <template v-slot:item="props">
          <EventLink :key="props.row.name" suppresstags v-bind="props.row" right-icon="clear" @deleteElem="(id) => removeEvent(id)"/>
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
import {defineComponent} from 'vue'
import { api } from 'boot/axios'
import {useRoute} from "vue-router";
import EventLink from "components/EventLink.vue";
import TagSelect from "components/TagSelect.vue";
import EventSelect from "components/EventSelect.vue";
import {getErrorMessage} from "src/util";
import ImpactList from "components/ImpactList.vue";
import TagBadge from "components/TagBadge.vue";

function updateEvents(self) {
  self.$refs.impactref.showDisclaimer = true
  api
    .put('users', self.userdata)
    .then((response) => {
      self.userdata = response.data
      self.eventlist = self.eventdata.filter(e => self.userdata.eventIds.includes(e.id)).map(e => {
        return {
          name: e.name,
          id: e.id,
          tags: []
        }
      })
    })
    .catch(error => self.$q.notify(self.$t(getErrorMessage(error))))
}

function updateTags(self) {
  api
    .put('users', self.userdata)
    .then((response) => {
      self.userdata = response.data
      self.$refs.tagSelect.selected = self.userdata.inherentTagIds
    })
    .catch(error => self.$q.notify(self.$t(getErrorMessage(error))))

}


export default defineComponent({
  data() {
    return {
      eventfilter: '',
      userdata: {},
      tagdata: [],
      eventtaglist: [],
      eventdata: [],
      eventlist: [],
      tagloading: true,
      eventloading: true,
      eventinput: false
    }
  },
  name: 'UserDetailPage',
  components: {
    TagBadge,
    ImpactList,
    TagSelect,
    EventLink,
    EventSelect
  },
  mounted() {
    api
      .get('users/' + useRoute().params.id)
      .then((response) => {
        this.userdata = response.data
        this.$refs.tagSelect.selected = response.data.inherentTagIds
        api
          .get('tags/user-tags')
          .then((tagresponse) => {
            this.tagdata = tagresponse.data
            this.eventtaglist = this.tagdata.filter(t => response.data.eventTagIds.includes(t.id))
            this.tagloading = false
          })
          .catch(error => {
            this.tagloading = false
            this.$q.notify(this.$t(getErrorMessage(error)))
          })
        !response.data.eventIds.length ? this.eventloading = false :
        api
          .get('events')
          .then((eventresponse) => {
            this.eventdata = eventresponse.data
            this.eventlist = this.eventdata.filter(e => response.data.eventIds.includes(e.id)).map(e => {return {
              name: e.name,
              id: e.id,
              tags: []
            }})
            this.eventloading = false
          })
          .catch(error => {
            this.eventloading = false
            this.$q.notify(this.$t(getErrorMessage(error)))
          })

      })
      .catch(error => {
        this.eventloading = false
        this.tagloading = false
        this.$q.notify(this.$t(getErrorMessage(error)))
      })
  },
  methods: {
    resetEventFilter() {
      this.eventfilter = ''
    },
    onTagSubmit(tags) {
      this.tagloading = true
      this.userdata.inherentTagIds = tags
      updateTags(this)
      this.tagloading = false
    },
    onEventSubmit() {
      this.eventloading = true
      this.userdata.eventIds.push(...this.$refs.eventSelect.selected.map(e => e.id))
      updateEvents(this)
      this.eventloading = false
    },
    removeEvent(id) {
      this.eventloading = true
      this.userdata.eventIds.splice(this.userdata.eventIds.indexOf(id),1)
      updateEvents(this)
      this.eventloading = false
    },
    removeTag(id) {
      this.tagloading = true
      this.userdata.inherentTagIds.splice(this.userdata.inherentTagIds.indexOf(id), 1)
      updateTags(this)
      this.tagloading = false
    }
  }
})
</script>
