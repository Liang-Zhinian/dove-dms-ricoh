import { getItem, setItem } from './services/storageService'

export default handle = async (error) => {
    console.log('handling error', error);
    var message = '';
    if (typeof error === 'string') {
        message = error;

    } else if (typeof error === 'object') {
        if (error.message) message = error.message;
        else message = JSON.stringify(error);
    }
    // alert(message);
    const key = 'APP_EXCEPTION';
    getItem(key)
        .then(value => {
            value += '%**********%' + (new Date()) + ' ===> ' + message;

            setItem(key, value);
        })
}