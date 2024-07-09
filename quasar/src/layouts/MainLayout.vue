<template>
  <q-layout view="hHh Lpr lff">
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

        <q-toolbar-title class="logo-typography">
          {{ $t('app_name')}}
        </q-toolbar-title>

        <div>0.3</div>
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
        <router-view class="q-pa-md" />
      </Suspense>
    </q-page-container>
    <q-footer class="transparent text-grey-8 text-center">
      {{ $t('footer.first') }}
      <a href="https://www.wikimedia.cz/" class="text-primary" v-text="$t('footer.wmLink')"></a>
      {{ $t('footer.second') }}
      <a href="https://github.com/wmcz/statistics-tool" class="text-primary" v-text="$t('footer.gitHubLink')"></a>
      {{ $t('footer.third') }}
      <a href="https://github.com/wmcz/statistics-tool/issues" class="text-primary" v-text="$t('footer.reportLink')"></a>
      {{ $t('footer.fourth') }}
    </q-footer>
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
        icon: 'groups',
        link: '/user/tag'
      },
      {
        title: t('event.many'),
        icon: 'fact_check',
        link: '/event'
      },
      {
        title: t('tag.event'),
        icon: 'dashboard',
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
