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
          {{ $t('app_name')}}
        </q-toolbar-title>

        <div>0.2</div>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
    >
      <q-list style="height: 100%;">
        <!--<q-item-label header>
          Essential Links
        </q-item-label>-->

        <NavLink
          v-for="link in navLinks"
          :key="link.title"
          v-bind="link"
        />
        <NavLink style="position: absolute; bottom: 0; width: 100%" :title="$t('project.many')" icon="travel_explore" link="/project"/>
      </q-list>
    </q-drawer>

    <q-page-container>
      <Suspense>
        <router-view class="q-ma-md" />
      </Suspense>
    </q-page-container>
  </q-layout>
</template>

<script>
import {computed, defineComponent, ref} from 'vue'
import NavLink from 'components/NavLink.vue'
import {date} from 'quasar'
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'MainLayout',

  components: {
    NavLink
  },

  setup () {
    const { t } = useI18n()
    const leftDrawerOpen = ref(false)
    const today =  computed(() => {
      return date.formatDate(Date.now(), 'YYYY-MM-DD')
    })
    const linksList = [
      {
        title: t('home'),
        icon: 'home',
        link: "/"
      },
      {
        title: t('user.many'),
        icon: 'person',
        link: '/user'
      },
      {
        title: t('tag.user'),
        icon: 'sell',
        link: '/user/tag'
      },
      {
        title: t('event.many'),
        icon: 'fact_check',
        link: '/event'
      },
      {
        title: t('tag.event'),
        icon: 'sell',
        link: '/event/tag'
      },
    ]

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
