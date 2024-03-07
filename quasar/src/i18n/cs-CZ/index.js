export default {
  app_name: 'WIKINSIDE',
  404: 'Stránka nenalezena',
  add: 'Přidat',
  filter: 'Filtrovat',
  submit: 'Potvrdit',
  cancel: 'Zrušit',
  edit: 'Upravit',
  home: 'Domů',
  optional: ' (nepovinné)',
  user: {
    one: 'Uživatel',
    many: 'Uživatelé',
    none: 'Žádní uživatelé',
    add: 'Přidat uživatele',
    name: 'Jméno uživatele'
  },
  event: {
    one: 'Událost',
    many: 'Události',
    none: 'Žádné události',
    add: 'Přidat události',
    name: 'Jméno události',
    strategy: {
      label: 'Strategie výběru uživatel',
      manual: 'Manuální',
      auto: 'Automatická (z hashtagu)'
    },
    hashtag: 'Hashtag',
    hashtag_hint: 'Funguje i bez symbolu #, tj. \'#WikiGap\' a \'WikiGap\' fungují stejně',
    dates: 'Data',
    dates_hint: 'Klikněte dvakrát pro jednodenní události'
  },
  tag: {
    one: 'Kategorie',
    many: 'Kategorie',
    none: 'Žádné kategorie',
    user: 'Uživatelské kategorie',
    event: 'Kategorie událostí',
    name: 'Jméno kategorie',
    add: 'Přidat kategorie',
    parent: 'Nadřazená kategorie',
    no_children: 'Žádné podřazené kategorie',
    no_parent: 'Žádná nadřazená kategorie',
    color: 'Barva',
    assign_color: 'Přiřadit barvu'
  },
  project: {
    one: 'Projekt',
    many: 'Projekty',
    name: 'Jméno projektu',
    path: 'Cesta projektu',
    path_hint: 'Příklad: en.wikipedia.org'
  },
  impact: {
    createdPages: 'nově vytvořených stránek',
    editedPages: 'změněných stránek (včetně vytvořených)',
    edits: 'editací',
    byteDiff: 'přidaných bajtů',
    users: 'aktivních účastníků',
    events: 'zúčastněných událostí',
    disclaimer: 'Změny se neprojevují na dopadu automaticky. Pro přepočet po změně vlastností prosím chvíli vyčkejte, a poté obnovte stránku. Zpracování změn obvykle netrvá déle než několik vteřin, ale v některých případech může být pomalejší.'
  },
  notification: {
    400: 'ERROR: Požadavek je špatně zformulovaný nebo jinak nesprávný. Jsou všechna pole vyplněna správně?',
    500: 'ERROR: Chyba na straně serveru; zkuste obnovit stránku.',
    network_error: 'ERROR: Nelze se připojit k serveru.',
    generic_error: 'ERROR: Něco se pokazilo; zkuste obnovit stránku.'
  },
  footer: {
    first: "Vytvořeno spolkem ",
    wmLink: "Wikimedia Česká republika.",
    second: " Náš zdrojový kód je volně dostupný na ",
    gitHubLink: "GitHubu.",
    third: "Nalezli jste chybu? ",
    reportLink:"Ohlašte ji zde.",
    fourth: ""
  }
}
