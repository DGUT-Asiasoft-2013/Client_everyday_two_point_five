package com.example.fiveyuanstore.inputcells;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePictureActivity extends Activity {

	byte[] pngData;
	Integer uid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_picture);
		Bitmap bmp = getIntent().getParcelableExtra("img");
		uid=Integer.valueOf(getIntent().getStringExtra("uid"));
		ImageView imageview = (ImageView) findViewById(R.id.image);
		imageview.setImageBitmap(bmp);
		saveBitmap(bmp);
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submit();		
				finish();
			}
		});
	}

	void saveBitmap(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, baos);
		pngData = baos.toByteArray();
	}
	
	public byte[] getPngData() {
		return pngData;
	}
	
	void submit() {
		OkHttpClient client = Server.getClient();
		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		if (getPngData() != null) {
			requestBodyBuilder
			.addFormDataPart("uid", String.valueOf(uid))
			.addFormDataPart("avatar", "avatar",
					RequestBody.create(MediaType.parse("image/png"), getPngData()));
		}
		Request request = Server.requestBuilderWithPath("change_avatar")
				.method("post", null)
				.post(requestBodyBuilder.build())
				.build();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
					
					}
				});				
			}
			
			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
					}
				});				
			}
		});
	}
}
