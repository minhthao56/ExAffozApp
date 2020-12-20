/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, { useEffect } from 'react';
import {
  SafeAreaView,
  StatusBar,
  Button,
  NativeModules,
  PermissionsAndroid,
} from 'react-native';

const App = () => {
  const {WifiModule} = NativeModules;

  const hanldScan = async (): Promise<void> => {
   
   try {
     const resutl =await WifiModule.StartScanWifi();
     console.log(resutl);
   } catch (error) {
     console.log(error);
     
   }
    console.log('hanldScan');
  };


  const hanldConnectWifi = async(): Promise<void>=>{
    
    try {
      const ConnectWifi =await WifiModule.ConnectWifi();
      console.log("ConnectWifi", ConnectWifi);
    } catch (error) {
      console.log("errorConnect",error);
      
    }
     console.log('hanldConnectWifi');
  }

  const permissionsAndroid = async () => {
    try {
      const grantedFineLoction = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
        {
          title: 'Wifi',
          message: 'FINE_LOCATION',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        },
      );

      if (grantedFineLoction === PermissionsAndroid.RESULTS.GRANTED) {
        WifiModule.ShowText('FINE_LOCATION', WifiModule.LONG_LENGTH);
      } else {
        WifiModule.ShowText('ERROR FINE_LOCATION', WifiModule.LONG_LENGTH);
      }

      const grantedCoarseLocation = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION,
        {
          title: 'Wifi',
          message: 'COARSE_LOCATION',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        },
      );
      if (grantedCoarseLocation === PermissionsAndroid.RESULTS.GRANTED) {
        WifiModule.ShowText('COARSE_LOCATION', WifiModule.LONG_LENGTH);
      } else {
        WifiModule.ShowText('ERROR COARSE_LOCATION', WifiModule.LONG_LENGTH);
      }
    } catch (error) {
      WifiModule.ShowText('ERROR ACCESS_ALL_LOCATION', WifiModule.LONG_LENGTH);
    }
  };



  useEffect(()=>{
    permissionsAndroid();
  },[])

  return (
    <>
      <StatusBar />
      <SafeAreaView>
        <Button title="Scan" onPress={hanldScan} />
        <Button title="Connect Wifi" onPress={hanldConnectWifi} />
      </SafeAreaView>
    </>
  );
};

export default App;
