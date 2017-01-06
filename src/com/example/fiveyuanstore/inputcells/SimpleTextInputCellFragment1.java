package com.example.fiveyuanstore.inputcells;

import com.example.fiveyuanstore.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class SimpleTextInputCellFragment1 extends Fragment{
	
	EditText edit;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view=inflater.inflate(R.layout.fragment_inputcell_simpletext1, container);
		
		edit=(EditText) view.findViewById(R.id.edit);
		
		return view;
	}
	
	
	public void setHintText(String hintText) {
		edit.setHint(hintText);
		
	}
	
	public String getText(){
		return edit.getText().toString();
	}
	
	public void setIsPassword(boolean isPassword){
		if(isPassword){
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}
	}
	
	public void setText(String str){
		edit.setText(str);
		
	}
	

	public void setIsPsw(boolean isPassword){
		if(isPassword){
			edit.setInputType( EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
		}else{
			edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		}
	}

}
