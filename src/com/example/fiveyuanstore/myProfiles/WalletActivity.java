package com.example.fiveyuanstore.myProfiles;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.R.id;
import com.example.fiveyuanstore.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WalletActivity extends Activity {

	TextView remaining_sum;
	int sum=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);
		remaining_sum=(TextView)findViewById(R.id.remaining_sum);
		findViewById(R.id.btn_recharge).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//充值按钮
				recharge();
				
			}


		});
		
		findViewById(R.id.btn_withdraw).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 提现按钮
				withdraw();
				
			}


		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		remaining_sum.setText(sum+"¥");
	}
	
	void recharge() {						//充值
		sum++;
		remaining_sum.setText(sum+"¥");
		
	}
	
	void withdraw() {						// 提现
		sum--;
		remaining_sum.setText(sum+"¥");
		
	}
}
