package com.example.fiveyuanstore.myProfiles;

import java.io.IOException;

import com.example.fiveyuanstore.LoginActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.MD5;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

public class PasswordChangeActivity extends Activity {
	
	SimpleTextInputCellFragment fragNewPassword;
	SimpleTextInputCellFragment fragNewPasswordRepeat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_changes);
		
		fragNewPassword=(SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_new_password);
		fragNewPasswordRepeat=(SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_new_password_repeat);
		
		findViewById(R.id.btn_password_changes).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				passwordChanges();				
			}
		});
	}
	
	@Override
	protected void onResume() {	
		super.onResume();
		fragNewPassword.setLabelText("新密码");
		fragNewPassword.setHintText("请输入新密码");
		fragNewPassword.setIsPassword(true);

		fragNewPasswordRepeat.setLabelText("新密码确认");
		fragNewPasswordRepeat.setHintText("再输入一次新密码");
		fragNewPasswordRepeat.setIsPassword(true);
	}
	
	private void passwordChanges() {
		if(fragNewPassword.getText().equals(fragNewPasswordRepeat.getText())){			
			passwordChangesToCenter();
		}else{
			new AlertDialog.Builder(this)
			.setMessage("两次密码不正确")
			.show();
		}
		
	}
	
	void passwordChangesToCenter(){
		String passwordHash=MD5.getMD5(fragNewPassword.getText());
		MultipartBody.Builder builder=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("passwordHash", passwordHash);
		
		RequestBody requestBody=builder.build();
		OkHttpClient client=Server.getClient();
		
		Request request=Server.requestBuilderWithPath("passwordChanges")
				.method("POST", requestBody)
				.post(requestBody)
				.build();
		final ProgressDialog progressD = new ProgressDialog(PasswordChangeActivity.this);
		progressD.setCancelable(false);
		progressD.setMessage("修改中...");
		progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressD.setCanceledOnTouchOutside(false);
		progressD.show();

		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							progressD.dismiss();
							Toast.makeText(getApplicationContext(), "修改密码成功", Toast.LENGTH_SHORT).show();
						}
					});
					
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressD.dismiss();

							Toast.makeText(PasswordChangeActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
						}});
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						progressD.dismiss();

						Toast.makeText(PasswordChangeActivity.this, arg1.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					}
				});
				
			}
		});
	}
}