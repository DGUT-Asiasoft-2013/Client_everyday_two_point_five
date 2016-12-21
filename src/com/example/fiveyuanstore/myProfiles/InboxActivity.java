package com.example.fiveyuanstore.myProfiles;

import com.example.fiveyuanstore.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class InboxActivity extends Activity {
	
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		listView=(ListView)findViewById(R.id.inbox_list);
		listView.setAdapter(listAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onItemClicked();
				
			}


		});
	}
	 
	private void onItemClicked() {
		// TODO Auto-generated method stub
		Intent itnt=new Intent(this,InboxChetActivity.class);
		startActivity(itnt);
	}
	
	BaseAdapter listAdapter=new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_inbox_list_item, null);
			} else {
				view = convertView;
			}
			return view;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return 2;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}
	};

}
