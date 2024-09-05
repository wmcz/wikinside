<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
    <q-form top
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input :rules="[ val => val && val.length > 0 || '']" v-model="name" :label="$t('tag.name') + ' *'" />

      <UserSelect ref="userSelect"/>

      <TagSelect ref="parentSelect" parent url="tags/user-tags"/>

      <q-input v-model="color" :label="$t('tag.color') + $t('optional')">
        <template v-slot:append>
          <q-icon name="colorize" class="cursor-pointer">
            <q-popup-proxy>
              <q-color v-model="color"/>
            </q-popup-proxy>
          </q-icon>
        </template>
      </q-input>

      <q-btn color="primary" type="submit">{{ $t('submit') }}</q-btn>
    </q-form>
    <q-list v-if="tagdata.length" bottom bordered class="rounded-borders">
      <TagLink v-for="tag in tagdata" :key="tag.name" elemtype="user" v-bind="tag" :elems="tag.userIds"/>
    </q-list>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'
import TagLink from "components/TagLink.vue";
import {api} from "boot/axios";
import UserSelect from "components/UserSelect.vue";
import TagSelect from "components/TagSelect.vue";
import {getErrorMessage} from "src/util";

export default defineComponent({
  name: 'UserTagNewPage',
  data() {
    return {
      name: null,
      color: null,
      tagdata: [],
      parent: null,
      parentdata: null,
      parentoptions: null
    }
  },
  methods: {
    onSubmit() {
      api
        .post('/tags/user-tags', {
          name: this.name,
          id: null,
          color: this.color,
          userIds: this.$refs.userSelect.selected.map(s => s.id),
          eventIds: [],
          parentId: this.$refs.parentSelect.selected ? this.$refs.parentSelect.selected.id : null,
          childrenIds: []
        })
        .then((response) => this.tagdata.push(response.data))
        .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
    },
  },
  components: {
    UserSelect,
    TagSelect,
    TagLink
  }
})
</script>
