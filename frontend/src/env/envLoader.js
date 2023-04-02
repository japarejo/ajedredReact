import environmentDev from './env.dev.json';
import environmentProd from './env.prod.json';

export const envLoader = () => {
    const env = process.env.REACT_APP_STAGE;
    
    if(env === 'dev'){
        return environmentDev;
    }
    if(env === 'prod'){
        return environmentProd;
    }


    return environmentDev;
}