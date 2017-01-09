package com.example.fiveyuanstore.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.fiveyuanstore.api.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

		// ����appע�ᵽ΢��
		api.registerApp(Constants.APP_ID);
	}
}
