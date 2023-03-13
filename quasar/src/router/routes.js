
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '',              component: () => import('pages/IndexPage.vue') },
      { path: '/user',         component: () => import('pages/UsersPage.vue') },
      { path: '/user/new',     component: () => import('pages/UsersNewPage.vue') },
      { path: '/user/tag',     component: () => import('pages/UserTagsPage.vue') },
      { path: '/event',        component: () => import('pages/EventsPage.vue') },
      { path: '/event/tag',    component: () => import('pages/EventTagsPage.vue') },

    ]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes
