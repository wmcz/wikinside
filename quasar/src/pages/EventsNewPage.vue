<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="name" :label="$t('event.name') + ' *'" />
      <ProjectSelect ref="projectSelect"/>
      <TagSelect  ref="tagSelect" url="tags/event-tags"/>

      <div class="q-field__bottom q-pl-none">{{ $t('event.strategy.label') }}</div>
      <div class="q-mt-none">
        <span>{{ $t('event.strategy.manual') }}</span>
        <q-toggle color="primary" keep-color v-model="fromHashtag" name="fromHashtag" :label="$t('event.strategy.auto')"/>
      </div>
      <UserSelect ref="userSelect" v-if="!fromHashtag"/>
      <q-input :rules="[ val => val && val.length > 0 || '']" v-else :label="$t('event.hashtag') + ' *'" v-model="hashtag" :hint="$t('event.hashtag_hint')"/>

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
import { defineComponent } from 'vue'
import EventLink from "components/EventLink.vue";
import {api} from "boot/axios";
import UserSelect from "components/UserSelect.vue";
import TagSelect from "components/TagSelect.vue";
import ProjectSelect from "components/ProjectSelect.vue";
import {getErrorMessage} from "src/util";

export default defineComponent({
  name: 'EventsNewPage',
  data() {
    return {
      name: null,
      eventdata: [],
      date: null,
      fromHashtag: false,
      hashtag: null
    }
  },
  methods: {
    onSubmit() {
      api
        .post('/events', {
          name: this.name,
          id: null,
          tagIds: this.$refs.tagSelect.selected === null ? [] : this.$refs.tagSelect.selected.map(s => s.id),
          projectIds: this.$refs.projectSelect.selected.map(p => p.id),
          userIds: this.fromHashtag || this.$refs.userSelect.selected === null ? [] : this.$refs.userSelect.selected.map(s => s.id),
          hashtag: this.fromHashtag ? this.hashtag : null,
          startDate: typeof this.date === "string" ? this.date : this.date.from,
          endDate: typeof this.date === "string" ? this.date : this.date.to
        })
        .then((response) => this.eventdata.push({
          name: response.data.name,
          id: response.data.id,
          tags: response.data.tagIds.map(i => this.tagdata.find(e => e.id === i))
        }))
        .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
    },
  },
  components: {
    ProjectSelect,
    TagSelect,
    UserSelect,
    EventLink
  }
})
</script>
