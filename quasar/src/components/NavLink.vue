<template>

  <q-item
  clickable
  v-ripple
  exact
  :to="link">
            <q-item-section avatar>
              <q-icon :name="icon" />
            </q-item-section>

            <q-item-section>
              {{ title }}
            </q-item-section>
          </q-item>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'NavLink',
  props: {
    title: {
      type: String,
      required: true
    },

    icon: {
      type: String,
      required: true
    },

    link: {
      type: String,
      default: '#'
    }

  }
})
</script>
