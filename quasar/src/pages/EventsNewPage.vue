<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="name" :label="$t('event.name') + ' *'" />
      <StratSelect ref="stratSelect" v-model="strat"/>
      <ProjectSelect :style="{visibility: strat === 'PHOTO' ? 'hidden' : 'visible'}" ref="projectSelect"/>
      <UserSelect ref="userSelect" v-if="strat === 'MANUAL'"/>
      <q-input :rules="[ val => val && val.length > 0 || '']" v-else :label="$t('event.' + strat.toLowerCase()) + ' *'" v-model="hashtag" :hint="$t('event.'+ strat.toLowerCase() + '_hint')"/>
      <TagSelect ref="tagSelect" url="tags/event-tags"/>
      <TagSelect ref="userTagSelect" url="tags/user-tags"/>

      <div>
        <q-field :label="$t('event.dates')" :hint="$t('event.dates_hint')" stack-label borderless>
          <template v-slot:control>
            <q-date v-model="date" landscape today-btn range mask="YYYY-MM-DD"/>
          </template>
        </q-field>
      </div>

      <q-btn color="primary" type="submit">{{ $t('submit') }}</q-btn>

    </q-form>
    <q-list bottom bordered class="rounded-borders">
      <EventLink v-for="event in eventdata" :key="event.name" v-bind="event"/>
    </q-list>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'
import EventLink from "components/EventLink.vue";
import {api} from "boot/axios";
import UserSelect from "components/UserSelect.vue";
import TagSelect from "components/TagSelect.vue";
import ProjectSelect from "components/ProjectSelect.vue";
import {getErrorMessage} from "src/util";
import StratSelect from "components/StratSelect.vue";

export default defineComponent({
  name: 'EventsNewPage',
  data() {
    return {
      name: null,
      eventdata: [],
      date: null,
      strat: 'HASHTAG',
      hashtag: null,
      showProject: true,
    }
  },
  methods: {
    onSubmit() {
      api
        .post('/events', {
          name: this.name,
          id: null,
          tagIds: this.$refs.tagSelect.selected === null ? [] : this.$refs.tagSelect.selected.map(s => s.id),
          userTagIds: this.$refs.userTagSelect.selected === null ? [] : this.$refs.userTagSelect.selected.map(s => s.id),
          projectIds: this.$refs.projectSelect.selected.map(p => p.id),
          userIds: this.strat !== 'MANUAL' || this.$refs.userSelect.selected === null ? [] : this.$refs.userSelect.selected.map(s => s.id),
          strat: this.strat,
          category: this.strat === 'PHOTO' && !this.hashtag.startsWith('Category:') ? 'Category:' + this.hashtag : this.hashtag,
          startDate: typeof this.date === "string" ? this.date : this.date.from,
          endDate: typeof this.date === "string" ? this.date : this.date.to
        })
        .then((response) => this.eventdata.push({
          name: response.data.name,
          id: response.data.id,
          tags: response.data.tagIds.map(i => this.$refs.tagSelect.tagdata.find(e => e.id === i))
        }))
        .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
    },
  },
  components: {
    StratSelect,
    ProjectSelect,
    TagSelect,
    UserSelect,
    EventLink
  }
})
</script>
