
// import { NativeModules } from 'react-native';

// const { RNPayfort } = NativeModules;

// module.exports = RNPayfort;


import * as React from "react";
import { NativeModules } from 'react-native';


export class PayFort {
	public static initializePayment(amount: Number, email: String, language: "en" | "ar", merchant_id: String, accesscode: String, requestphrase: String, responsephrase: String, currencytype: String, callBack : (code : Number, message : String) => Function ) {
		NativeModules.RNPayfort.initializePayment(amount, email, language, merchant_id, accesscode, requestphrase, responsephrase, currencytype, callBack);
	}
}