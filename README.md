# Mercadopago React Native Wrapper

## Getting started

`$ npm install react-native-mercadopago --save`

### Mostly automatic installation

`$ react-native link react-native-mercadopago`

#### iOS

1. Drag the files from /node_modules/react-native-mercadopago/ios/*.framework to the Frameworks group of your project
2. In General Tab for your project add the new frameworks from previous step to the Embedded Binaries.
3. In Build Settings, make sure that Embedded Content Contains Swift Code is YES.
4. Set Enable Bitcode to NO.

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-mercadopago` and add `RNMercadopago.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNMercadopago.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. In General Tab for your project add the new frameworks from previous step to the Embedded Binaries.
5. In Build Settings, make sure that Embedded Content Contains Swift Code is YES.
6. Set Enable Bitcode to NO.


#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.shovelapps.rnmercadopago.RNMercadopagoPackage;` to the imports at the top of the file
  - Add `new RNMercadopagoPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-mercadopago'
  	project(':react-native-mercadopago').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-mercadopago/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-mercadopago')
  	```


## Usage
```javascript
import RNMercadopago from 'react-native-mercadopago';

...
    let publicKey = 'TEST-ad365c37-8012-4014-84f5-6c895b3f8e0a';
    let prefId = '176234066-fc6d5d5e-2671-4073-ab49-362a98b720b5';

    RNMercadopago.startCheckout(publicKey, prefId, null, false, (payment) => { this._success(payment)}, (error) => { this._failure(error) });
...
```
  