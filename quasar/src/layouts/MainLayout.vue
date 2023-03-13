<template>
  <q-layout view="hHh Lpr lFf">
    <q-header>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="toggleLeftDrawer"
        />

        <q-toolbar-title>
          Quasar App
        </q-toolbar-title>

        <div>Quasar v{{ $q.version }}</div>
      </q-toolbar>
      <div>
        <div class="text-subtitle1 q-pa-sm" >{{ today }}</div>
      </div>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
    >
      <q-list>
        <!--<q-item-label header>
          Essential Links
        </q-item-label>-->

        <NavLink
          v-for="link in navLinks"
          :key="link.title"
          v-bind="link"
        />
      </q-list>
    </q-drawer>

    <q-page-container>
      <Suspense>
        <router-view />
      </Suspense>
    </q-page-container>
  </q-layout>
</template>

<script>
import {computed, defineComponent, ref} from 'vue'
import NavLink from 'components/NavLink.vue'
import {date} from 'quasar'

const linksList = [
{
    title: 'Home',
    icon: 'home',
    link: "/"
  },
  {
    title: 'Users',
    icon: 'person',
    link: '/user'
  },
  {
    title: 'User tags',
    icon: 'sell',
    link: '/user/tag'
  },
  {
    title: 'Events',
    icon: 'fact_check',
    link: '/event'
  },
]

export default defineComponent({
  name: 'MainLayout',

  components: {
    NavLink
  },

  setup () {
    const leftDrawerOpen = ref(false)
    const today =  computed(() => {
      return date.formatDate(Date.now(), 'YYYY-MM-DD')
    })

    return {
      navLinks: linksList,
      leftDrawerOpen,
      toggleLeftDrawer () {
        leftDrawerOpen.value = !leftDrawerOpen.value
      },
      today
    }
  }
})
</script>
