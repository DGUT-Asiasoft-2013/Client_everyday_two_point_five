package com.example.fiveyuanstore.goods;

import java.io.IOException;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.StoreActivity;
import com.example.fiveyuanstore.R.id;
import com.example.fiveyuanstore.R.layout;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.MyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class CommentActivity extends Activity {

	private Button comment_btn;
	private EditText editText;
	private MyOrder order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		comment_btn = (Button) findViewById(R.id.send_comment);
		editText = (EditText) findViewById(R.id.text);
		order = (MyOrder) getIntent().getSerializableExtra("myOrder");
	}


	@Override
	protected void onResume() {
		super.onResume();

		comment_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendComment();
			}

		});
	}

	void sendComment() {
		String text = editText.getText().toString();
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("text", text)
				.addFormDataPart("goods_id", ""+order.getGoods().getId())
				.build();

		Request req = Server.requestBuilderWithPath("/goods/"+order.getOrder_num()+"/comments")
				.post(body)
				.build();

		Server.getClient().newCall(req)
		.enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				try {
					runOnUiThread(new Runnable() {
					
						@Override
						public void run() {
								CommentActivity.this.onResponse();
							
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						CommentActivity.this.onFail(e);

					}
				});
			}
		});
	}

	protected void onFail(IOException e) {
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();

	}

	protected void onResponse() {
		
		Toast.makeText(CommentActivity.this, "評論成功", Toast.LENGTH_LONG).show();
		Intent itt = new Intent(CommentActivity.this, StoreActivity.class);
		startActivity(itt);
		finish();

	}

}
