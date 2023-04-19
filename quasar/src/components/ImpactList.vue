<template>
  <q-list bordered class="rounded-borders">
    <ImpactItem v-for="item in items" :key="item.name" v-bind="item"/>
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
      items: []
    }
  },
  components: {
    ImpactItem
  },
  mounted() {
    api
      .get(this.url + "/impact")
      .then(response =>
        Object.entries(response.data).forEach(([k, v]) => this.items.push({name: k, val: v}))
    ).catch(error => this.$q.notify(this.$t(getErrorMessage(error))))
  }
}
</script>
