<template>
<q-item router-link :to="{name: 'user', params: { id: id }}">

  <q-item-section top>
    <q-item-label lines="1">
      <span class="text-h6 q-pr-xs">{{ username }}</span>
      <span class="text-caption">{{ id }}</span>

    </q-item-label>
    <q-item-label v-if="!supresstags" caption lines="1">
      <TagBadge class="q-mr-xs" v-for="tag in tags" :key="tag.name" :id="tag.id" :name="tag.name" v-bind="tag" elemtype="user"/>
      <caption v-if="!tags.length">{{ $t('tag.none') }}</caption>
    </q-item-label>
  </q-item-section>
  <q-item-section side>
    <div class="text-grey-8 q-gutter-xs">
      <q-btn class="gt-xs" size="12px" flat dense round :icon="rightIcon ? rightIcon : 'delete'" @click.prevent="$emit('deleteUser', id)"/>
    </div>
  </q-item-section>
</q-item>
</template>

<script>
import TagBadge from "components/TagBadge.vue";

export default {
  name: "UserLink",
  components: {TagBadge},
  props: {
    username: {
      type: String,
      required: true
    },
    id: {
      type: Number,
      required: true
    },
    tags: {
      type: Array,
      required: true
    },
    supresstags: {
      type: Boolean
    },
    rightIcon: {
      type: String
    }
  }
}
</script>
