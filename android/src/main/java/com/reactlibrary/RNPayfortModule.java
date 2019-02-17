
package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

  private final ReactApplicationContext reactContext;






    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            Log.d("itworks", "it works "+requestCode+" "+resultCode+" "+data);
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
  public void initializePayment(Float amount, String email) {
      Toast.makeText(getReactApplicationContext(), "data from app is "+amount+" email is "+email, Toast.LENGTH_LONG).show();
      fortCallback = FortCallback.Factory.create();
      // requestForPayfortPayment(22.00);
  }

    private void requestForPayfortPayment(Double etAmount) {
        PayFortData payFortData = new PayFortData();
            payFortData.amount = String.valueOf((int) (etAmount* 100));// Multiplying with 100, bcz amount should not be in decimal format
            payFortData.command = PayFortPayment.PURCHASE;
            payFortData.currency = PayFortPayment.CURRENCY_TYPE;
            payFortData.customerEmail = "readyandm@ggg.cc";
            payFortData.language = PayFortPayment.LANGUAGE_TYPE;
            payFortData.merchantReference = String.valueOf(System.currentTimeMillis());

            Log.d("reactContext", "reactContext74 "+reactContext.getCurrentActivity());

            PayFortPayment payFortPayment = new PayFortPayment(reactContext.getCurrentActivity(), this.fortCallback, this);
            payFortPayment.requestForPayment(payFortData);
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      Log.d("second","second time");
    }

    @Override
    public void onPaymentRequestResponse(int responseType, PayFortData responseData) {
      Log.d("resp1","resp1 "+responseData);
        if (responseType == PayFortPayment.RESPONSE_GET_TOKEN) {
            Toast.makeText(getReactApplicationContext(), "Token not generated", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Token not generated");
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_CANCEL) {
            Toast.makeText(getReactApplicationContext(), "Payment cancelled", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment cancelled");
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_FAILURE) {
            Toast.makeText(getReactApplicationContext(), "Payment failed", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment failed");
        } else {
            Toast.makeText(getReactApplicationContext(), "Payment successful", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment successful");
        }

    }
}