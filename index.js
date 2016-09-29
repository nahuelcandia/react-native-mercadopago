
import { NativeModules } from 'react-native';

const { RNMercadopago } = NativeModules;

export default {
  startCheckout: function(publicKey, prefId, color, blackFont, success, failure) {
    RNMercadopago.startCheckout([publicKey, prefId, color, blackFont], success, failure);
  }
};
