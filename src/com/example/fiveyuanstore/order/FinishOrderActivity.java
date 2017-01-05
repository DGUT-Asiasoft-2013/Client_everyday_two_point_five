package com.example.fiveyuanstore.order;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.StoreActivity;
import com.example.fiveyuanstore.R.id;
import com.example.fiveyuanstore.R.layout;
import com.example.fiveyuanstore.entity.MyOrder;
import com.example.fiveyuanstore.goods.CommentActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FinishOrderActivity extends Activity {
	Button comment, backOn;
	private MyOrder order;
	
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish_order);
		
		order = (MyOrder) getIntent().getSerializableExtra("myOrder");
		init();
	}

	private void init() {
		comment = (Button) findViewById(R.id.commentBtn);
		backOn = (Button) findViewById(R.id.backOn);
		
		comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itt = new Intent(FinishOrderActivity.this, CommentActivity.class);
				itt.putExtra("myOrder", order);
				startActivity(itt);
				finish();
			}
		});
		
		backOn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itt = new Intent(FinishOrderActivity.this, StoreActivity.class);
				startActivity(itt);
				finish();
			}
		});
	};
	
	
}
