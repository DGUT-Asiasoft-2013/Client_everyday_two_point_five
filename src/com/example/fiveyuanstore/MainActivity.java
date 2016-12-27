package com.example.fiveyuanstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}
	
	@Override
	protected void onResume() {		
		super.onResume();
		
		Handler handler=new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startLoginActivity();				
			}
		}, 5000);
		startLoginActivity();
		
	}
	
	void startLoginActivity(){
		Intent itnt=new Intent(this,LoginActivity.class);
		startActivity(itnt);
		finish();
	}
}
