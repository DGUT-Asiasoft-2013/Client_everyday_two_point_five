package com.example.fiveyuanstore;

import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class AddProductActivity extends Activity {
	SimpleTextInputCellFragment frag1,frag2, frag3, frag4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_product);
		
		frag1 =(SimpleTextInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag1);
		frag2 =(SimpleTextInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag2);
		frag3 =(SimpleTextInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag3);
		frag4 =(SimpleTextInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag4);
		
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		frag1.setLabelText("名称");
		frag1.setHintText("名称");
		frag1.setIsPsw(false);

		
		frag2.setLabelText("价格");
		frag2.setHintText("价格");
		frag2.setIsPsw(false);
		
		frag3.setLabelText(" 库存 ");
		frag3.setHintText(" 库存 ");
		frag3.setIsPsw(false);
		
		frag4.setLabelText("描述");
		frag4.setHintText("描述");
		frag4.setIsPsw(false);
		
		
		
	}
}
