<template>
  <q-list bordered class="rounded-borders">
    <ImpactItem v-for="item in items" :key="item.name" v-bind="item"/>
    <q-item v-if="showDisclaimer">
      <q-item-section avatar>
        <q-icon color="primary" name="info"/>
      </q-item-section>
      <q-item-section >
        <q-item-label caption> {{ $t('impact.disclaimer') }} </q-item-label>
      </q-item-section>
    </q-item>
  </q-list>
</template>

<script>
import ImpactItem from "components/ImpactItem.vue";
import {api} from "boot/axios";
import {getErrorMessage} from "src/util";

export default {
  props: {
    url: {
      type: String,
      required: true
    }
  },
  data() {
    return  {
      items: [],
      showDisclaimer: false
    }
  },
  components: {
    ImpactItem
  },
  mounted() {
    api
      .get(this.url + "/impact")
      .then(response => {
        Object.entries(response.data).forEach(([k, v]) => this.items.push({name: k, val: v}))
        if (this.items.every(i => i.val === 0)) this.showDisclaimer = true})
      .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
  }
}
</script>
