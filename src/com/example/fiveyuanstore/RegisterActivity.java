package com.example.fiveyuanstore;

import java.io.IOException;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.inputcells.PictureInputCellFragment;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {

	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellName;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	EditText fragInputEmailAddress;
	PictureInputCellFragment fragInputAvatar;
	TextView textEmail ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		fragInputCellAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.intput_account);
		fragInputCellName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);
		fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		fragInputEmailAddress =(EditText)findViewById(R.id.intput_email);
		fragInputAvatar = (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.picture);
		textEmail = (TextView) findViewById(R.id.text_email);
		
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		fragInputCellAccount.setLabelText("用户名");
		fragInputCellAccount.setHintText("请输入用户名");

		fragInputCellName.setLabelText("名字");
		fragInputCellName.setHintText("请输入名字");

		fragInputCellPassword.setLabelText("密码");
		fragInputCellPassword.setHintText("请输入密码");
		fragInputCellPassword.setIsPassword(true);

		fragInputCellPasswordRepeat.setLabelText("确认密码");
		fragInputCellPasswordRepeat.setHintText("请输入确认密码");
		fragInputCellPasswordRepeat.setIsPassword(true);

		textEmail.setText("电子邮箱");
		fragInputEmailAddress.setHint("请输入电子邮箱");
	}

	void submit() {

		String password = fragInputCellPassword.getText();
		String passwordRepeat = fragInputCellPasswordRepeat.getText();
		
		if (!password.equals(passwordRepeat)) {
			new AlertDialog
			.Builder(RegisterActivity.this)
			.setMessage("两次密码输入不一致")
			.setPositiveButton("好", null)
			.show();
			return;
		}
		
		password = MD5.getMD5(password);		
		
		String account = fragInputCellAccount.getText();
		String name = fragInputCellName.getText();
		String email = fragInputEmailAddress.getText().toString();
		
		OkHttpClient client = Server.getClient();
		
		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("name", name)
				.addFormDataPart("account", account)
				.addFormDataPart("email", email)
				.addFormDataPart("passwordHash", password);		

		if (fragInputAvatar.getPngData() != null) {
			requestBodyBuilder
			.addFormDataPart(
					"avatar", "avatar",
					RequestBody
					.create(MediaType.parse("image/png"),
							fragInputAvatar.getPngData()));
		}	

		Request request = Server.requestBuilderWithPath("register")
				.method("post", null)
				.post(requestBodyBuilder.build())
				.build();

		final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
		progressDialog.setMessage("Please Waitting...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		progressDialog.show();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {

				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();
						Toast.makeText(getApplication(), "注册成功", Toast.LENGTH_LONG).show();
						startLoginActivity();
					}

				});
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();

						new AlertDialog.Builder(RegisterActivity.this).setNegativeButton("OK", null).setTitle("注册失败")
								.setMessage(e.toString()).show();
					}
				});
			}

		});
	}

	void startLoginActivity() {

		Intent itnt = new Intent(this, LoginActivity.class);
		startActivity(itnt);
	}

}
