package com.example.fiveyuanstore;

import java.io.IOException;

import com.example.fiveyuanstore.api.MD5;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {

	SharedPreferences sharedPreferences;
	String uName = null;
	String uPassWord = null;
	public Boolean auto;
	static SharedPreferences shared;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sharedPreferences = getSharedPreferences("saveUserNamePwd", Context.MODE_PRIVATE);

		uName = sharedPreferences.getString("username", "");
		uPassWord = sharedPreferences.getString("password", "");

		shared = getSharedPreferences("auto", Context.MODE_PRIVATE);

	}

	protected void auto_true() {
		super.onStop();
		SharedPreferences sharedPreferences;
		sharedPreferences = getSharedPreferences("auto", Context.MODE_PRIVATE);

		Editor editor = sharedPreferences.edit();
		editor.putString("auto", "true");
		editor.commit();
	}

	public static void auto_false() {
		// super.onStop();

		Editor editor = shared.edit();
		editor.putString("auto", "false");
		editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();

		sharedPreferences = getSharedPreferences("auto", Context.MODE_PRIVATE);

		if (sharedPreferences.getString("auto", "").equals("true")) {
			auto = true;
		} else {
			auto = false;
		}

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (auto) {
					login();
				} else {
					auto_true();
					startLoginActivity();
				}
			}
		}, 1000);

	}

	void startLoginActivity() {
		Intent itnt = new Intent(this, LoginActivity.class);
		startActivity(itnt);
		finish();
	}

	void login() {
		String account = uName;
		String my_psw = MD5.getMD5(uPassWord);

		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("account", account).addFormDataPart("passwordHash", my_psw);

		RequestBody requestBody = builder.build();

		OkHttpClient client = Server.getClient();

		Request request = Server.requestBuilderWithPath("login").method("POST", requestBody).post(requestBody).build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response res) throws IOException {

				final User user = new ObjectMapper().readValue(res.body().string(), User.class);
				runOnUiThread(new Runnable() {
					public void run() {
						goLogin();
					}
				});

			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {
					public void run() {
					}
				});
			}
		});
	}

	void goLogin() {
		Intent itnt = new Intent(this, StoreActivity.class);
		startActivity(itnt);
		finish();
	}
}
