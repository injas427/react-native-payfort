
// import { NativeModules } from 'react-native';

// const { RNPayfort } = NativeModules;

// module.exports = RNPayfort;


import * as React from "react";
import { NativeModules } from 'react-native';

// module.exports = NativeModules.RNPayfort

export class TestTest {
	public static startPayment(amount : Number, email : String) {
		 NativeModules.RNPayfort.initializePayment(amount, email);
	}
}