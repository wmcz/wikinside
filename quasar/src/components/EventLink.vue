<template>
<q-item router-link :to="{name: 'event', params: {id: id}}" style="width: 100%">

  <q-item-section top>
    <q-item-label lines="1">
      <span class="text-h6 q-pr-xs">{{ name }}</span>

    </q-item-label>
    <q-item-label caption lines="1" >
      <TagBadge class="q-mr-xs" v-for="tag in tags" :key="tag.name" :id="tag.id" :name="tag.name" :color="tag.color" v-bind="tag" elemtype="event"/>
      <caption v-if="!supressnotag && !tags.length">{{ $t('tag.none') }}</caption>
    </q-item-label>
  </q-item-section>
  <q-item-section side>
    <div class="text-grey-8 q-gutter-xs">
      <q-btn class="gt-xs" size="12px" flat dense round :icon="rightIcon ? rightIcon : 'delete'" @click.prevent="$emit('deleteEvent', id)"/>
    </div>
  </q-item-section>
</q-item>
</template>

<script>
import TagBadge from "components/TagBadge.vue";

export default {
  name: "EventLink",
  components: {TagBadge},
  props: {
    name: {
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
    rightIcon: {
      type: String
    },
    supressnotag: {
      type: Boolean
    }
  }
}
</script>
