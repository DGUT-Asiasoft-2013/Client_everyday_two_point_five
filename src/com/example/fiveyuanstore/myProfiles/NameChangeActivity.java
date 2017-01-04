package com.example.fiveyuanstore.myProfiles;

import java.io.IOException;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NameChangeActivity extends Activity {

	EditText name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_name);
		String myName=(String) getIntent().getSerializableExtra("name");
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				save();
				finish();	
			}
		});
		
		name=(EditText) findViewById(R.id.name);
		name.setText(myName);
	}
	
	void save(){
		String newName=name.getText().toString();
		MultipartBody.Builder builder=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("user_name", newName);
		
		RequestBody requestBody=builder.build();
		OkHttpClient client=Server.getClient();
		
		Request request=Server.requestBuilderWithPath("nameChanges")
				.method("POST", requestBody)
				.post(requestBody)
				.build();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
