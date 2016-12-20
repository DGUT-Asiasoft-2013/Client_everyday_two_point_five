package com.example.fiveyuanstore.myProfiles;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

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
			new AlertDialog.Builder(this)
			.setMessage("修改成功")
			.show();
		}else{
			new AlertDialog.Builder(this)
			.setMessage("两次密码不正确")
			.show();
		}
		
	}
}
