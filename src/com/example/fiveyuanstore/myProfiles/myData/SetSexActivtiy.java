package com.example.fiveyuanstore.myProfiles.myData;

import com.example.fiveyuanstore.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SetSexActivtiy extends Activity{

	Button Male;
	Button Female;
	Button Cancle;
	LinearLayout layout;
	
	final int RESULT_MALE=11;
	final int RESULT_FEMALE=12;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_set_sex);
		Male=(Button)findViewById(R.id.btn_male);
		Female=(Button)findViewById(R.id.btn_female);
		Cancle=(Button)findViewById(R.id.btn_cancle);
		layout=(LinearLayout)findViewById(R.id.sex_layout);
		
        layout.setOnClickListener(new OnClickListener() {  
            
            public void onClick(View v) {  
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",   
                        Toast.LENGTH_SHORT).show();   
            }  
        });  
        
        Male.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(RESULT_MALE, intent);
				
				finish();
			}
		});
        
        Female.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(RESULT_FEMALE, intent);
				
				finish();
			}
		});
        
        Cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				
			}
		});

	}
	
    @Override  
    public boolean onTouchEvent(MotionEvent event){  
        finish();  
        return true;  
    }


	
	


}
