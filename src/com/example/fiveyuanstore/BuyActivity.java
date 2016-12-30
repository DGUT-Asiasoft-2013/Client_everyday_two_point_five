package com.example.fiveyuanstore;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
	SimpleTextInputCellFragment fragInputCellAddress;
	EditText fragInputCount, fragInputCellPhone;
	Float price;
	Goods goods;
	int num;
	float myWallet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);

		fragInputCellName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.name);
		fragInputCellPhone = (EditText) findViewById(R.id.phone);
		fragInputCellAddress = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.address);
		fragInputCount = (EditText) findViewById(R.id.count);

		TextView money = (TextView) findViewById(R.id.money);
		goods = (Goods) getIntent().getSerializableExtra("goods");
		price = goods.getPrice();
		money.setText("$" + price.toString());
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

		fragInputCellName.setLabelText("收货人姓名");
		fragInputCellName.setHintText("请输入收货人姓名");

		fragInputCellPhone.setHint("请输入联系电话");

		fragInputCellAddress.setLabelText("收货地址");
		fragInputCellAddress.setHintText("请输入收货地址");

		fragInputCount.setHint("购买数量");
	}

	void submit() {
		final int goods_amount = goods.getGoods_count();

		Request request1 = Server.requestBuilderWithPath("/me").get().build();
		Server.getClient().newCall(request1).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final User me = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<User>() {
					});
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (me != null) {
								myWallet = me.getMoney();
								String goods_id = goods.getGoods_id();
								String name = fragInputCellName.getText();
								String phone = fragInputCellPhone.getText().toString();
								String address = fragInputCellAddress.getText();
								String amount = fragInputCount.getText().toString();
								int myAmount = (Integer.parseInt(amount));

								if (myWallet < price * myAmount || myAmount > goods_amount) {
									// 钱包不够，或者输入的数量大于库存，则返回
									Toast.makeText(getApplication(), "余额不足，或者输入的数量大于库存,余额：" + myWallet,
											Toast.LENGTH_LONG).show();
								} else {
									showPayDialog(name, phone, address, amount, price, goods_id);
								}
							}
						}
					});

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getApplication(), "Error: " + arg1.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}

	
	void buy(String name,String phone, String address, String amount,float price, String goods_id){
		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("name",name)
				.addFormDataPart("phone",phone)
				.addFormDataPart("address", address)
				.addFormDataPart("amount", amount)
				.addFormDataPart("price", String.valueOf(price))
				.build();
		Integer id = goods.getId();
		Request request=  Server.requestBuilderWithPath("/buy/"+goods_id+"/"+id).post(requestBody).build();
		
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response res) throws IOException {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Toast.makeText(getApplication(), "付款成功", Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getApplication(), "购买失败" + e.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});

	}

	private void showPayDialog(final String name, final String phone, final String address, final String amount,
			final float price, final String goods_id) {

		AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
		num = Integer.parseInt(fragInputCount.getText().toString());
		Dialog.setTitle("付款");
		Dialog.setMessage("确认支付$" + price * num + "？");

		Dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				buy(name, phone, address, amount, price, goods_id);
				Toast.makeText(getApplication(), "付款成功", Toast.LENGTH_LONG).show();
				finish();
			}
		});
		Dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		Dialog.create().show();
	}
}
