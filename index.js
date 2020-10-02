import { NativeModules } from 'react-native';

const { SplashAnimations } = NativeModules;


export function hide() {
    SplashAnimations.hide();
}

export default { hide };
