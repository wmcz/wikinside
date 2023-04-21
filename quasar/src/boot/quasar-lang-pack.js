import {Quasar} from "quasar";

const langList = import.meta.glob('../../node_modules/quasar/lang/(cs|en-US).mjs')

export default () => {
  const langIso = 'cs' // ... some logic to determine it (use Cookies Plugin?)

  try {
    langList[ `../../node_modules/quasar/lang/${ langIso }.mjs` ]().then(lang => {
      Quasar.lang.set(lang.default)
    })
  }
  catch (err) {
    console.log(err)
  }
}
