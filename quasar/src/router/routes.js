
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '',               component: () => import('pages/IndexPage.vue') },
      { path: '/user',          component: () => import('pages/UsersPage.vue') },
      { path: '/user/:id',      component: () => import('pages/UserDetailPage.vue'), name: 'user' },
      { path: '/user/new',      component: () => import('pages/UsersNewPage.vue') },
      { path: '/user/tag',      component: () => import('pages/UserTagsPage.vue') },
      { path: '/user/tag/new',  component: () => import('pages/UserTagNewPage.vue') },
      { path: '/event',         component: () => import('pages/EventsPage.vue') },
      { path: '/event/:id',     component: () => import('pages/EventDetailPage.vue'), name: 'event' },
      { path: '/event/new',     component: () => import('pages/EventsNewPage.vue') },
      { path: '/event/tag',     component: () => import('pages/EventTagsPage.vue') },
      { path: '/event/tag/new', component: () => import('pages/EventTagNewPage.vue') },
      { path: '/project',       component: () => import('pages/ProjectsPage.vue') },
      { path: '/project/new',   component: () => import('pages/ProjectsNewPage.vue') },


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
