
# react-native-payfort

## Getting started

`$ npm install react-native-payfort --save`

### Mostly automatic installation

`$ react-native link react-native-payfort`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-payfort` and add `RNPayfort.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNPayfort.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNPayfortPackage;` to the imports at the top of the file
  - Add `new RNPayfortPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-payfort'
  	project(':react-native-payfort').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-payfort/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-payfort')
  	```

## Usage
```javascript
import RNPayfort from 'react-native-payfort';

// TODO: What to do with the module?
RNPayfort;
```
  