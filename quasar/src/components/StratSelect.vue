<template>
  <q-select outlined menu-self="bottom start" menu-anchor="top start" :label="$t('event.strategy.label')" v-model="selected" emit-value :options="options" option-value="id" map-options :option-label="option => $t(option.name)"/>
</template>

<script>
import {useI18n} from "vue-i18n";


export default {
  setup () {
    const {t} = useI18n()
    return {t}
  },
  name: "StratSelect",
  data() {
    return {
      options: [
        {
          name: 'event.strategy.manual',
          id: "MANUAL"
        },
        {
          name: 'event.strategy.hashtag',
          id: "HASHTAG"
        },
        {
          name: 'event.strategy.photo',
          id: "PHOTO"
        }
      ],
      selected: null
    }
  }
}
</script>
