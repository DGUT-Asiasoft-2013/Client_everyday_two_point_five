package com.example.fiveyuanstore;

import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;




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
				goLogin();
				
			}
		});
		
		findViewById(R.id.btn_forgot_password).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goRecoverPassword();
				
			}
		});
		
        fragAccount=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);

	}
	
	
	@Override
	protected void onResume() {
		
		super.onResume();
		fragAccount.setLabelText("�û���");
		fragAccount.setHintText("�������û���");
		
		fragPassword.setLabelText("����");
		fragPassword.setHintText("����������");
		fragPassword.setIsPassword(true);
	}
	
	void goLogin(){
		Intent itnt=new Intent(this,StoreActivity.class);
		startActivity(itnt);
	}
	
	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}
	
	void goRecoverPassword(){
		Intent itnt=new Intent(this,PasswordRecoverActivity.class);
		startActivity(itnt);
	}
}
