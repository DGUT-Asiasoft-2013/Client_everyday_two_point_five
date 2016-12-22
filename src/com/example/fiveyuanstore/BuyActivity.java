package com.example.fiveyuanstore;

import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BuyActivity extends Activity {
	
	SimpleTextInputCellFragment fragInputCellName;
	SimpleTextInputCellFragment fragInputCellPhone;
	SimpleTextInputCellFragment fragInputCellAddress;

	Float price;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub//
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		
		fragInputCellName=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.name);
		fragInputCellPhone=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.phone);
		fragInputCellAddress=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.address);
		TextView money=(TextView) findViewById(R.id.money);
		price=getIntent().getFloatExtra("money",-1f);
		money.setText("$"+price.toString());
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submit();
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		fragInputCellName.setLabelText("收货人姓名");
		fragInputCellName.setHintText("请输入收货人姓名");
		
		fragInputCellPhone.setLabelText("联系电话");
		fragInputCellPhone.setHintText("请输入联系电话");
		
		fragInputCellAddress.setLabelText("收货地址");
		fragInputCellAddress.setHintText("请输入收货地址");
		
	}
	
	void submit(){
		//price
		fragInputCellName.getText();
		fragInputCellPhone.getText();
		fragInputCellAddress.getText();
		
		finish();
	}
}
