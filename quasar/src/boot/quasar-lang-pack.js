import {Quasar} from "quasar";

const langList = import.meta.glob('../../node_modules/quasar/lang/(cs|en-US).mjs')

export default () => {
  let langIso
  switch (process.env.DEFAULT_LOCALE) {
    case 'cs-CZ':
      langIso = process.env.DEFAULT_LOCALE.substring(0, 2);
      break;
    default:
      langIso = process.env.DEFAULT_LOCALE
  }

  try {
    langList[ `../../node_modules/quasar/lang/${ langIso }.mjs` ]().then(lang => {
      Quasar.lang.set(lang.default)
    })
  }
  catch (err) {
    console.log(err)
  }
}
