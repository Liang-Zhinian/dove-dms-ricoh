import I18n, {getLanguages} from 'react-native-i18n';

import en from './locales/en';
import zh from './locales/zh';
import zh_Hans from './locales/zh-Hans';
import zh_Hant from './locales/zh-Hans';

I18n.defaultLocale = 'en';
I18n.fallbacks = true;

I18n.translations = {
    en,
    zh,
    'zh-Hans': zh_Hans,
    'zh-Hant': zh_Hant
};

export default I18n;

export const translate = (scope) => {
    return I18n.t(scope);
};

