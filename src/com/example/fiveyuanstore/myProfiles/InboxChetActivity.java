package com.example.fiveyuanstore.myProfiles;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.fiveyuanstore.AddProductActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InboxChetActivity extends Activity {
	
	ListView listView;
	Boolean isSend=false;
	int int_test=8;
	String str_text;
	EditText inboxSendText;
	Goods goods;
	User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		goods=(Goods) getIntent().getSerializableExtra("goods");
		if(goods==null)
			user=(User) getIntent().getSerializableExtra("user");
		else
			user=goods.getUser();
		
		setContentView(R.layout.activity_inbox_chat);
		listView=(ListView)findViewById(R.id.inbox_chat_list);
		inboxSendText=(EditText)findViewById(R.id.inbox_send_text);
		listView.setAdapter(listAdapter);
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView.setStackFromBottom(true);		//滚动到最后一行
		
		findViewById(R.id.btn_inbox_send).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				send();
				
			}

			
		});
		
	}

	private void send() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		MultipartBody.Builder builder=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("content", inboxSendText.getText().toString())
				.addFormDataPart("send_id", user.getId()+"")
				.addFormDataPart("createDate",curDate.toString());
		RequestBody requestBody = builder.build();
		OkHttpClient client = Server.getClient();
				
		Request request = Server.requestBuilderWithPath("/addInbox")
				.method("post", null)
				.post(requestBody)
				.build();	
		
	   	 final ProgressDialog progressD = new ProgressDialog(this);
	   	 progressD.setCancelable(false);
	   	 progressD.setTitle("提示");
	   	 progressD.setMessage("请稍后");
	   	 progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	   	 progressD.setCanceledOnTouchOutside(false);
	   	 progressD.show();
	   	 
	   	 client.newCall(request).enqueue(new Callback() {
	 		
	 		@Override
	 		public void onResponse(Call arg0, final Response arg1) throws IOException {
	 			runOnUiThread(new Runnable() {
	 				
	 				@Override
	 				public void run() {
	 					//add succeed
	 					  progressD.dismiss();
	 					  
	 						Toast.makeText(getApplication(), "发送成功", Toast.LENGTH_LONG).show();
	 						
	 				}

	 				
	 			});
	 		}
	 		
	 		@Override
	 		public void onFailure(Call arg0, final IOException arg1) {
	 			runOnUiThread(new Runnable() {
	 				
	 				@Override
	 				public void run() {
	 					 progressD.dismiss();
	 					  
	 						Log.d("AddProductActivity", arg1.toString());
	 						new AlertDialog.Builder(InboxChetActivity.this)
	 								.setNegativeButton("OK", null)
	 								.setTitle("添加失败")
	 								.setMessage(arg1.getMessage())
	 								.show();
	 						
	 				}
	 			});
	 			
	 		}
	 	});
	   	 
	}
	
	BaseAdapter listAdapter=new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				if (position%2==0){
					view = inflater.inflate(R.layout.widget_inbox_chat_box_right, null);
					str_text="你是谁？";
				}else{
					view = inflater.inflate(R.layout.widget_inbox_chat_box_left, null);
					str_text="你猜猜~";
				}
			}else
				view=convertView;
			
			TextView chatBoxTime=(TextView)view.findViewById(R.id.chat_box_time);
			TextView inboxChat=(TextView)view.findViewById(R.id.inbox_chat);
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			chatBoxTime.setText(str);
			
			inboxChat.setText(str_text);
			
			
			
			
			
			
			
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return int_test;
		}
	};
}
