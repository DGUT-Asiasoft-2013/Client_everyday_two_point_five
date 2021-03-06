package com.example.fiveyuanstore.myProfiles;

import java.io.IOException;

import com.example.fiveyuanstore.LoginActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.R.id;
import com.example.fiveyuanstore.R.layout;
import com.example.fiveyuanstore.TransactionActivity;
import com.example.fiveyuanstore.api.Server;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WalletActivity extends Activity {

	TextView remaining_sum;
	Float money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);
		remaining_sum = (TextView) findViewById(R.id.remaining_sum);
		money=getIntent().getFloatExtra("money", -1);
		
		findViewById(R.id.transactions).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WalletActivity.this, TransactionActivity.class);				
				startActivity(intent);
				
			}
		});
		
		findViewById(R.id.btn_recharge).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 充值按钮
				recharge();				
				remaining_sum.setText("$" + money);
			}
		});

		findViewById(R.id.btn_withdraw).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 提现按钮
				withdraw();
				remaining_sum.setText("$" + money);
			}

		});
		
		findViewById(R.id.btn_wallet_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		remaining_sum.setText("$" + money);
	}

	void recharge() {

		AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
		final EditText editText = new EditText(this);
		normalDialog.setTitle("请输入充值金额");
		normalDialog.setIcon(android.R.drawable.ic_dialog_info);
		normalDialog.setView(editText);
		normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Float addmoney=Float.parseFloat(editText.getText().toString());
				money += addmoney;			
				server(money);
				serverRecharge("充值",addmoney);
				reload();
			}
		});
		normalDialog.setNegativeButton("取消", null);
		normalDialog.show();
	}

	void withdraw() { // 提现

		AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
		final EditText editText = new EditText(this);
		normalDialog.setTitle("请输入提现金额");
		normalDialog.setIcon(android.R.drawable.ic_dialog_info);
		normalDialog.setView(editText);
		normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Float addmoney=Float.parseFloat(editText.getText().toString());
				money -= addmoney;
				server(money);
				serverRecharge("提现",addmoney);
				reload();
			}
		});
		normalDialog.setNegativeButton("取消", null);
		normalDialog.show();
	}

	void reload() {
		remaining_sum.setText("$" + money);
	}

	void server(final Float sum) {
		OkHttpClient client = Server.getClient();

		MultipartBody.Builder body = 
				new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("money","" + sum);

		Request request = Server.requestBuilderWithPath("/money/recharge")
				.method("post", null)
				.post(body.build())
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {

			}
		});
	}
	void serverRecharge(String state,Float price){
		
	RequestBody requestBody = new MultipartBody.Builder()
			.setType(MultipartBody.FORM)
			.addFormDataPart("state",state)
			.addFormDataPart("money",String.valueOf(price))
			.build();
	
	Request request=  Server.requestBuilderWithPath("/confirmRecord/").post(requestBody).build();
	
	Server.getClient().newCall(request).enqueue(new Callback() {
		
		@Override
		public void onResponse(Call arg0, final Response res) throws IOException {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {				
					Toast.makeText(getApplication(), "成功", Toast.LENGTH_LONG).show();
				}
			});
		}
		
		@Override
		public void onFailure(Call arg0,final IOException e) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getApplication(), "失败"+ e.getMessage(), Toast.LENGTH_LONG).show();
				}
			});
		}
	});
	
	}
}
