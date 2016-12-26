package com.example.fiveyuanstore.myProfiles;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Inbox;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
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

	String send_name;
	TextView inboxChatTo;
	AvatarView avatarView;
	User user;
	List<Inbox> data;
	int page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("0000","1223");
		//获取联络人
		send_name=(String) getIntent().getSerializableExtra("name");

		setContentView(R.layout.activity_inbox_chat);
		listView=(ListView)findViewById(R.id.inbox_chat_list);
		inboxSendText=(EditText)findViewById(R.id.inbox_send_text);
		inboxChatTo=(TextView)findViewById(R.id.inbox_chat_to);
		avatarView=(AvatarView)findViewById(R.id.avatar);
		listView.setAdapter(listAdapter);
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView.setStackFromBottom(true);		//滚动到最后一行

		inboxChatTo.setText("与 "+send_name+" 对话");

		findViewById(R.id.btn_inbox_send).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				send();

			}


		});

	}

	private void send() {		
		
		MultipartBody.Builder builder=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("content", inboxSendText.getText().toString())
				.addFormDataPart("send_name", send_name);

		RequestBody requestBody = builder.build();
		OkHttpClient client = Server.getClient();
		Request request = Server.requestBuilderWithPath("addInbox")
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
						inboxSendText.setText("");
						progressD.dismiss();
						Toast.makeText(getApplication(), "发送成功", Toast.LENGTH_LONG).show();
						reload(0);
						listAdapter.notifyDataSetInvalidated();

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
			int i=data.size();
			Inbox inbox=data.get(i-position-1);

			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			if (inbox.getSend_name().equals(send_name)){
				view = inflater.inflate(R.layout.widget_inbox_chat_box_right, null);					
			}else{
				view = inflater.inflate(R.layout.widget_inbox_chat_box_left, null);
			}
			str_text=inbox.getInboxContent();


			TextView chatBoxTime=(TextView)view.findViewById(R.id.chat_box_time);
			TextView inboxChat=(TextView)view.findViewById(R.id.inbox_chat);

			String time=DateFormat.format("yyyy-MM-dd hh:mm:ss",inbox.getCreateDate()).toString();
			chatBoxTime.setText(time);

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
			return data.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data == null ? 0 : data.size();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

		reload(0);
	}


	void reload(int page) {


		MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("text",
				send_name);

		RequestBody requestBody = body.build();
		Request request = Server
				.requestBuilderWithPath("/inboxgetchat")
				.method("POST", requestBody)
				.post(requestBody)
				.build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				try {
					final Page<Inbox> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Inbox>>() {
					});

					InboxChetActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {

							InboxChetActivity.this.page = data.getNumber();
							InboxChetActivity.this.data = data.getContent();
							listAdapter.notifyDataSetInvalidated();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				Log.d("SellerFragment", e.getMessage());
			}
		});






	}



}
