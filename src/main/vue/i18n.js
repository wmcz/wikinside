import cs from './locales/cs.json'
import en from './locales/en.json'
import { createI18n } from 'vue-i18n'

export default createI18n({
    legacy: false,
    allowComposition: true,
    locale: import.meta.env.VITE_DEFAULT_LOCALE,
    fallbackLocale: import.meta.env.VITE_FALLBACK_LOCALE,
    messages: {
        cs: cs,
        en: en
    }
})