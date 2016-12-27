package com.example.fiveyuanstore;

import java.io.IOException;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity {

	SimpleTextInputCellFragment fragAccount;
	SimpleTextInputCellFragment fragPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		findViewById(R.id.btn_register).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goRegister();

			}
		});

		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login();

			}
		});

		findViewById(R.id.btn_forgot_password).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goRecoverPassword();

			}
		});

		fragAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);

	}

	@Override
	protected void onResume() {

		super.onResume();
		fragAccount.setLabelText(" ");
		fragAccount.setHintText("User Account");

		fragPassword.setLabelText(" ");
		fragPassword.setHintText("......");
		fragPassword.setIsPassword(true);
	}

	void login() {
		String account = fragAccount.getText();
		String my_psw = MD5.getMD5(fragPassword.getText());

		MultipartBody.Builder builder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("account",account)
				.addFormDataPart("passwordHash",my_psw);

		RequestBody requestBody = builder.build();

		OkHttpClient client = Server.getClient();


		Request request =Server.requestBuilderWithPath("login")
				.method("POST", requestBody)
				.post(requestBody)
				.build();

		final ProgressDialog progressD = new ProgressDialog(LoginActivity.this);
		progressD.setCancelable(false);
		progressD.setTitle("Tip");



		progressD.setMessage("Login...");
		progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressD.setCanceledOnTouchOutside(false);
		progressD.show();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response res) throws IOException {

				try{
					final User user = new ObjectMapper().readValue(res.body().string(), User.class);

					runOnUiThread(new Runnable() {
						public void run() {
							progressD.dismiss();
							goLogin();
							Toast.makeText(getApplicationContext(), "welcome , "+user.getUser_name(), Toast.LENGTH_SHORT).show();
						}});
				}catch(final Exception e){
					runOnUiThread(new Runnable() {
						public void run() {
							progressD.dismiss();

							Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
						}});
				}
			}

			@Override
			public void onFailure(Call arg0,final IOException e) {
				runOnUiThread(new Runnable() {
					public void run() {
						progressD.dismiss();

						Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		
	}

	void goLogin() {

//		ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0,view.getWidth(), view.getHeight());
//		ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());

		Intent itnt = new Intent(this, StoreActivity.class);
		startActivity(itnt);
		//overridePendingTransition(R.animator.zoom_in, R.animator.zoom_out);
	}

	void goRegister() {
		Intent itnt = new Intent(this, RegisterActivity.class);
		startActivity(itnt);
	}

	void goRecoverPassword() {
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}

}
