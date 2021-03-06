package com.example.fiveyuanstore;

import java.io.IOException;

import com.example.fiveyuanstore.api.MD5;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment1;
import com.example.fiveyuanstore.share.AuthActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity implements OnClickListener {

	SimpleTextInputCellFragment1 fragAccount;
	SimpleTextInputCellFragment1 fragPassword;
	// CheckBox checkBox1;
	private static final String FILE_NAME = "saveUserNamePwd";
	String usernameContent;
	String passwordContent;
	public static SharedPreferences sharedPreferences;
	static String userNameContent;
	static String passWordContent;

	TextView btn_register;
	ImageView btn_login;
	public static Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		TextView auth = (TextView) findViewById(R.id.btnLogin);
		btn_register = (TextView) findViewById(R.id.btn_register);
		btn_login = (ImageView) findViewById(R.id.btn_login);

		auth.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		btn_login.setOnClickListener(this);

		findViewById(R.id.btn_forgot_password).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goRecoverPassword();

			}
		});

		fragAccount = (SimpleTextInputCellFragment1) getFragmentManager().findFragmentById(R.id.input_account);
		fragPassword = (SimpleTextInputCellFragment1) getFragmentManager().findFragmentById(R.id.input_password);

		sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		// 从文件中获取保存的数据
		userNameContent = sharedPreferences.getString("username", "");
		passWordContent = sharedPreferences.getString("password", "");
		// 判断是否有数据存在，并进行相应处理
		if (userNameContent != null && !"".equals(userNameContent))
			fragAccount.setText(userNameContent);
		if (passWordContent != null && !"".equals(passWordContent))
			fragPassword.setText(passWordContent);
	}

	protected void onSaveContent() {
		super.onStop();
		usernameContent = fragAccount.getText();
		passwordContent = fragPassword.getText();
		// 获取SharedPreferences时，需要设置两参数
		// 第一个是保存的文件的名称，第二个参数是保存的模式（是否只被本应用使用）
		editor = sharedPreferences.edit();
		// 添加要保存的数据
		editor.putString("username", usernameContent);
		editor.putString("password", passwordContent);
		// 确认保存
		editor.commit();
	}

	public static void onNotSaveContent() {
		//super.onStop();		
		Editor editor = MainActivity.sharedPreferences.edit();
		// 添加要保存的数据
		editor.putString("password", "");
		// 确认保存
		editor.commit();
	}

	@Override
	protected void onResume() {

		super.onResume();
		fragAccount.setHintText("User Account");

		fragPassword.setHintText("......");
		fragPassword.setIsPassword(true);
	}

	void login() {
		String account = fragAccount.getText();
		String my_psw = MD5.getMD5(fragPassword.getText());

		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("account", account).addFormDataPart("passwordHash", my_psw);

		RequestBody requestBody = builder.build();

		OkHttpClient client = Server.getClient();

		Request request = Server.requestBuilderWithPath("login").method("POST", requestBody).post(requestBody).build();

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

				try {
					final User user = new ObjectMapper().readValue(res.body().string(), User.class);

					runOnUiThread(new Runnable() {
						public void run() {
							progressD.dismiss();
							goLogin();
							Toast.makeText(getApplicationContext(), "welcome , " + user.getUser_name(),
									Toast.LENGTH_SHORT).show();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressD.dismiss();

							Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
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
		Intent itnt = new Intent(this, StoreActivity.class);
		startActivity(itnt);
		finish();
	}

	void goRegister() {
		Intent itnt = new Intent(this, RegisterActivity.class);
		startActivity(itnt);
	}

	void goRecoverPassword() {
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			Intent itt = new Intent(LoginActivity.this, AuthActivity.class);
			startActivity(itt);
			break;

		case R.id.btn_register:
			goRegister();
			break;

		case R.id.btn_login:
			login();
			onSaveContent();

			break;

		default:
			break;
		}
	}

}
