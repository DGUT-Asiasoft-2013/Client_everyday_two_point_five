package com.example.fiveyuanstore.myProfiles;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.fiveyuanstore.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class InboxChetActivity extends Activity {
	
	ListView listView;
	Boolean isSend=false;
	int int_test=8;
	String str_text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox_chat);
		listView=(ListView)findViewById(R.id.inbox_chat_list);
		listView.setAdapter(listAdapter);
		//listView.smoothScrollToPosition(listView.getCount() - 1);
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView.setStackFromBottom(true);		//滚动到最后一行
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
