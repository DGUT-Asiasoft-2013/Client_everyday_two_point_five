package com.example.fiveyuanstore.fragment;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class PasswordRecoverStep2Fragment extends Fragment {
	SimpleTextInputCellFragment verify;
	SimpleTextInputCellFragment password;
	SimpleTextInputCellFragment password_repeat;
	
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view==null){
			view = inflater.inflate(R.layout.fragment_password_recover_step2, null);
			
			verify=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verify);
			password=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
			password_repeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		
		view.findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmitClicked();			
			}
		});
		
		}		
		return view;
	}
	
	public String getText(){
		return password.getText();
	}
	
	public static interface OnSubmitClickedListener{
		void onSubmitClicked();
	}
	
	OnSubmitClickedListener OnSubmitClickedListener;
	
	public void setOnSubmitClickedListener(OnSubmitClickedListener onSubmitClickedListener){
	this.OnSubmitClickedListener=onSubmitClickedListener;
	}
	
	void onSubmitClicked(){
		if(password.getText().equals(password_repeat.getText())){
			if(OnSubmitClickedListener!=null){
			OnSubmitClickedListener.onSubmitClicked();
			}
		}else{
			new AlertDialog.Builder(getActivity())
			.setMessage("两次输入密码不一致")
			.show();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		verify.setLabelText("用户名");
		verify.setHintText("请输入用户名");
		
		password.setLabelText("新密码");
		password.setHintText("请输入新密码");
		password.setIsPassword(true);
	
		password_repeat.setLabelText("确认密码");
		password_repeat.setHintText("请输入确认密码");
		password_repeat.setIsPassword(true);
	}
}
