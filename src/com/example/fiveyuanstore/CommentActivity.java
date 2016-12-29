package com.example.fiveyuanstore;

import java.io.IOException;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.MyOrder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
				.build();

		Request req = Server.requestBuilderWithPath("/goods/"+order.getGoods().getId()+"/"+order.getOrder_num()+"/comments")
				.post(body)
				.build();

		Server.getClient().newCall(req)
		.enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					String resBody = arg1.body().string();
					@Override
					public void run() {
						CommentActivity.this.onResponse(resBody);
					}
				});
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

	protected void onResponse(String text) {

		new AlertDialog.Builder(this).setMessage(text)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
			}
		}).show();
	}

}
