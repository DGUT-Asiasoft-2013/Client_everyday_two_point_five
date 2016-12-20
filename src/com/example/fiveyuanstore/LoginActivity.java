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
		fragAccount.setLabelText("用户名");
		fragAccount.setHintText("请输入用户名");

		fragPassword.setLabelText("密码");
		fragPassword.setHintText("请输入密码");
		fragPassword.setIsPassword(true);
	}

	void login(){
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
	   	 progressD.setTitle("提示");
	   	 
	   	 
	   	 
	   	 progressD.setMessage("正在登陆");
	   	 progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	   	 progressD.setCanceledOnTouchOutside(false);
	   	 progressD.show();
	   	 
	   		client.newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, final Response res) throws IOException {
					runOnUiThread(new Runnable() {
						  public void run() {
							  progressD.dismiss();
							  try { 
								  final String resBody = res.body().string();
							  if(resBody != null ){
								
								ObjectMapper mapper = new ObjectMapper();  
						        final User user = mapper.readValue(resBody, User.class); 
						    	goLogin();
						    	Toast.makeText(getApplicationContext(), "welcome , "+user.getUser_name(), Toast.LENGTH_SHORT).show(); 
							       
						        
						        
								
							  }
							  } catch (IOException e) {
									e.printStackTrace();
								}
							
							  
							  
							
						  }
						});
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
		
		
		Intent itnt = new Intent(this, StoreActivity.class);
		startActivity(itnt);
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
