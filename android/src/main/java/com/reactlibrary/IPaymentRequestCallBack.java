package com.reactlibrary;

import android.content.Intent;

public interface IPaymentRequestCallBack {

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onPaymentRequestResponse(int responseType, PayFortData responseData);
}
