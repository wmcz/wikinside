<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
      style="max-width: 600px"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="username" :label="$t('user.name') + ' *'" />
      <TagSelect ref="tagselect" url="tags/user-tags"/>
      <q-btn color="primary" type="submit">{{ $t('submit') }}</q-btn>
    </q-form>
    <q-list bottom bordered class="rounded-borders" style="min-width: 600px">
      <UserLink v-for="user in userdata" :key="user.username" v-bind="user"/>
    </q-list>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'
import UserLink from "components/UserLink.vue";
import {api} from "boot/axios";
import TagSelect from "components/TagSelect.vue";
import {getErrorMessage} from "src/util";

export default defineComponent({
  name: 'UsersNewPage',
  data() {
    return {
      username: null,
      userdata: []
    }
  },
  methods: {
    onSubmit() {
      api
        .post('/users', {
          username: this.username,
          id: null,
          tagIds: this.$refs.tagselect.selected === null ? [] : this.$refs.tagselect.selected.map(s => s.id),
          eventIds: []
        })
        .then((response) => this.userdata.push({
          username: response.data.username,
          id: response.data.id,
          tags: response.data.tagIds.map(i => this.tagdata.find(e => e.id === i))
        }))
        .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))

    },
  },
  components: {
    TagSelect,
    UserLink
  }
})
</script>
