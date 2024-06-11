export default {
  app_name: 'WIKINSIDE',
  404: 'Page not found',
  add: 'Add',
  filter: 'Filter',
  submit: 'Submit',
  cancel: 'Cancel',
  edit: 'Edit',
  home: 'Home',
  optional: ' (optional)',
  user: {
    one: 'User',
    many: 'Users',
    none: 'No users',
    add: 'Add users',
    name: 'User name'
  },
  event: {
    one: 'Event',
    many: 'Events',
    none: 'No events',
    add: 'Add events',
    name: 'Event name',
    strategy: {
      label: 'Event type',
      manual: 'With manual user selection',
      hashtag: 'With a hashtag',
      photo: "Photo category"
    },
    hashtag: 'Hashtag',
    photo: 'Photo category name',
    hashtag_hint: 'Works with or without the # sign, e.g. both \'#WikiGap\' and \'WikiGap\' are the same',
    photo_hint: 'E.g. \'Category:Cows\'',
    dates: 'Dates',
    dates_hint: 'Double click for single day events',
    new_usertag: 'Assign tags to participants'
  },
  tag: {
    one: 'Category',
    many: 'Categories',
    user: 'User categories',
    none: 'No categories',
    event: 'Event categories',
    name: 'Category name',
    add: 'Add categories',
    parent: 'Parent category',
    no_children: 'No child categories',
    no_parent: 'No parent category',
    color: 'Color',
    assign_color: 'Assign color',
    new_child: 'Create child',
    from_event: 'From events: '
  },
  project: {
    one: 'Project',
    many: 'Projects',
    name: 'Project name',
    path: 'Project path',
    path_hint: 'Example: en.wikipedia.org'
  },
  impact: {
    createdPages: 'newly created pages',
    editedPages: 'edited pages (including newly created ones)',
    edits: 'edits',
    byteDiff: 'added bytes',
    users: 'active participants',
    events: 'events participated',
    images: 'images uploaded',
    usages: 'pages using uploaded images',
    disclaimer: 'Impact is not automatically updated after changes to events. To recalculate, please wait a moment, and then refresh the page. Processing the changes usually does not take more than a few seconds, but may take longer in some cases.'
  },
  notification: {
    400: 'ERROR: Request is malformed or otherwise incorrect. Are all fields filled correctly?',
    500: 'ERROR: Server-side error; try refreshing the website.',
    network_error: 'ERROR: Could not connect to server.',
    generic_error: 'ERROR: Something has gone wrong; try refreshing the website.'
  },
  footer: {
    first: " Created by ",
    wmLink: "Wikimedia Czech Republic.",
    second: " Our source code is freely available on ",
    gitHubLink: "GitHub.",
    third: " Found an issue? ",
    reportLink:"Report it here.",
    fourth: ""
  }
}
