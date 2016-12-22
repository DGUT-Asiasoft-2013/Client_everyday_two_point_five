package com.example.fiveyuanstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try{
			Thread.sleep(5000);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {		
		super.onResume();
		startLoginActivity();
		
	}
	
	void startLoginActivity(){
		Intent itnt=new Intent(this,LoginActivity.class);
		startActivity(itnt);
		finish();
	}
}
