
package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fort.android.sdk.base.callbacks.FortCallback;


public class RNPayfortModule extends ReactContextBaseJavaModule implements IPaymentRequestCallBack {

    public FortCallBackManager fortCallback = null;

    public Callback callback = null;

  private final ReactApplicationContext reactContext;

  private boolean valid = false;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            super.onActivityResult(activity, requestCode, resultCode, data);
            if (requestCode == PayFortPayment.RESPONSE_PURCHASE) {
                fortCallback.onActivityResult(requestCode, resultCode, data);
            }
        }
    };

  public RNPayfortModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(mActivityEventListener);
  }

  @Override
  public String getName() {
    return "RNPayfort";
  }


  @ReactMethod
  public void initializePayment(Double amount, String email, String language, String merchant_id, String accesscode, String requestphrase, String responsephrase, String currencytype, Callback success) {
      callback = success;
      Log.e("errordata", "amount "+amount+" email "+email+" lang "+language+" merchid "+merchant_id+" access "+accesscode+" req "+requestphrase+" res "+responsephrase+" curr "+currencytype);
      if(amount instanceof Double && amount != null) {
          if(email instanceof String && email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
              if(language.equals("en") || language.equals("ar")) {
                  if(merchant_id instanceof String && merchant_id != null) {
                      if(accesscode instanceof String && accesscode != null) {
                          if(requestphrase instanceof String && requestphrase != null) {
                              if(responsephrase instanceof String && responsephrase != null) {
                                  if(currencytype instanceof String && currencytype.length() == 3) {
                                      valid = true;
                                  } else {invokeCallBack(0, "INVALID_CURRENCY_CODE");}
                              } else {invokeCallBack(0, "INVALID_RESPONSE_PHRASE");}
                          } else {invokeCallBack(0, "INVALID_REQUEST_PHRASE");}
                      } else {invokeCallBack(0, "INVALID_ACCESSCODE");}
                  } else {invokeCallBack(0, "INVALID_MERCHANT_ID");}
              } else {invokeCallBack(0, "INVALID_LANGUAGE");}
          } else {invokeCallBack(0, "INVALID_EMAIL");}
      } else {
          invokeCallBack(0, "INVALID_AMOUNT");
      }
      if(valid) {
          fortCallback = FortCallback.Factory.create();
          requestForPayfortPayment(amount, email, language, merchant_id, accesscode, requestphrase, responsephrase, currencytype);
      }
  }

  private void invokeCallBack(Integer code, String message) {
      callback.invoke(code, message);
  }

    private void requestForPayfortPayment(Double amount, String email, String language, String merchant_id, String accesscode, String requestphrase, String responsephrase, String currencytype) {
        PayFortData payFortData = new PayFortData();
            payFortData.amount = String.valueOf((int) (amount* 100));// Multiplying with 100, bcz amount should not be in decimal format
            payFortData.command = PayFortPayment.PURCHASE;
            payFortData.currency = currencytype;
            payFortData.customerEmail = email;
            payFortData.language = language;
            payFortData.merchantReference = String.valueOf(System.currentTimeMillis());

            PayFortPayment payFortPayment = new PayFortPayment(reactContext.getCurrentActivity(), this.fortCallback, this);
            payFortPayment.setCredentials(merchant_id, accesscode, requestphrase, responsephrase, currencytype, language);
            payFortPayment.requestForPayment(payFortData);
        }


    @Override
    public void onPaymentRequestResponse(int responseType, PayFortData responseData) {
//      callback.invoke("Response from module");
        if (responseType == PayFortPayment.RESPONSE_GET_TOKEN) {
            Toast.makeText(getReactApplicationContext(), "Token not generated", Toast.LENGTH_SHORT).show();
            callback.invoke(responseType, "Token not generated");
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_CANCEL) {
            Toast.makeText(getReactApplicationContext(), "Payment cancelled", Toast.LENGTH_SHORT).show();
            callback.invoke(responseType, "Payment cancelled");
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_FAILURE) {
            Toast.makeText(getReactApplicationContext(), "Payment failed", Toast.LENGTH_SHORT).show();
            callback.invoke(responseType, "Payment Failed");
        } else {
            Toast.makeText(getReactApplicationContext(), "Payment successful", Toast.LENGTH_SHORT).show();
            callback.invoke(responseType, "Payment Successful");
        }

    }
}