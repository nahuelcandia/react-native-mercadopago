# Mercadopago React Native Wrapper

## Getting started

`$ npm install react-native-mercadopago --save`

### Mostly automatic installation

`$ react-native link react-native-mercadopago`

#### iOS

1. Drag the file /../node_modules/react-native-mercadopago/ios/MercadoPagoSDK.framework to your main project.
2. In General Tab for your project add MercadoPagoSDK.framework to the Embedded Binaries

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-mercadopago` and add `RNMercadopago.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNMercadopago.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Drag the file /../node_modules/react-native-mercadopago/ios/MercadoPagoSDK.framework to your main project.
5. In General Tab for your project add MercadoPagoSDK.framework to the Embedded Binaries.

#### Android

Work in Progress

## Usage
```javascript
import Mercadopago from 'react-native-mercadopago';

...
    let publicKey = 'TEST-ad365c37-8012-4014-84f5-6c895b3f8e0a';
    let prefId = '176234066-fc6d5d5e-2671-4073-ab49-362a98b720b5';

    Mercadopago.startCheckout(publicKey, prefId, null, false, (payment) => { this._success(payment)}, (error) => { this._failure(error) });
...
```
  