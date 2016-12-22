package com.example.fiveyuanstore;

import java.io.IOException;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BuyActivity extends Activity {
	
	SimpleTextInputCellFragment fragInputCellName;
	SimpleTextInputCellFragment fragInputCellPhone;
	SimpleTextInputCellFragment fragInputCellAddress;
	SimpleTextInputCellFragment fragInputCount;
	Float price;
	Goods goods;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub//
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		
		fragInputCellName=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.name);
		fragInputCellPhone=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.phone);
		fragInputCellAddress=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.address);
		fragInputCount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.count);
	
		TextView money=(TextView) findViewById(R.id.money);
		goods =(Goods) getIntent().getSerializableExtra("goods");
		price = goods.getPrice();
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
		
		fragInputCount.setLabelText("数量");
		fragInputCount.setHintText("购买数量");
		
	}
	
	void submit(){
		
		//price
		price = goods.getPrice();
		
		String goods_id = goods.getGoods_id();
	
		String name = 	fragInputCellName.getText();
		String phone = fragInputCellPhone.getText();
		String address = fragInputCellAddress.getText();
		String amount = fragInputCount.getText();
		
		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("name",name)
				.addFormDataPart("phone",phone)
				.addFormDataPart("address", address)
				.addFormDataPart("amount", amount)
				.addFormDataPart("goods_id", goods_id)
				.build();
		
		Request request=  Server.requestBuilderWithPath("/buy/"+goods_id).post(requestBody).build();
		
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, final Response res) throws IOException {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplication(), "购买成功", Toast.LENGTH_LONG).show();
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0,final IOException e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplication(), "购买失败"+ e.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		
	
		finish();
	}
}
