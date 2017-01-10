package com.example.fiveyuanstore.myProfiles;

import java.io.IOException;
import java.util.List;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;

import com.example.fiveyuanstore.entity.InboxList;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.example.fiveyuanstore.page.CommodityFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class InboxActivity extends Activity {

	String myName;
	ListView listView;
	List<InboxList> data;
	int page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		myName = (String) getIntent().getSerializableExtra("name");
		setContentView(R.layout.activity_inbox);
		listView = (ListView) findViewById(R.id.inbox_list);
		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onItemClicked(position);

			}

		});
		findViewById(R.id.btn_inbox_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	private void onItemClicked(int position) {
		InboxList inboxList = data.get(position);
		Intent itnt = new Intent(this, InboxChetActivity.class);
		if (!myName.equals(inboxList.getRec_name()))
			itnt.putExtra("name", inboxList.getRec_name());

		else
			itnt.putExtra("name", inboxList.getSend_name());

		startActivity(itnt);
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_inbox_list_item, null);
			} else {
				view = convertView;
			}

			TextView inboxName = (TextView) view.findViewById(R.id.inbox_name);
			TextView inboxLastTime = (TextView) view.findViewById(R.id.inbox_last_time);
			TextView inboxLastMessage = (TextView) view.findViewById(R.id.inbox_last_message);
			AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);
			InboxList inboxList = data.get(position);

			if (!myName.equals(inboxList.getLast_inbox().getRec_user().getUser_name())) {
				inboxName.setText(inboxList.getLast_inbox().getRec_user().getUser_name());
				avatar.load(inboxList.getLast_inbox().getRec_user());
			} else {
				inboxName.setText(inboxList.getLast_inbox().getSend_user().getUser_name());
				avatar.load(inboxList.getLast_inbox().getSend_user());
			}

			String time = DateFormat.format("yyyy年MM月dd日   hh:mm:ss", inboxList.getLast_inbox().getCreateDate())
					.toString();
			inboxLastTime.setText(time);
			inboxLastMessage.setText(inboxList.getLast_inbox().getInboxContent());

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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reload();
	}

	void reload() {
		Request request = Server.requestBuilderWithPath("inbox").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<InboxList> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<InboxList>>() {
							});
					InboxActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							listAdapter.notifyDataSetInvalidated();
							InboxActivity.this.page = data.getNumber();
							InboxActivity.this.data = data.getContent();
						}
					});
				} catch (final Exception e) {

					InboxActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(InboxActivity.this).setMessage(e.getMessage()).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				InboxActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(InboxActivity.this).setMessage(e.getMessage()).show();

					}
				});

			}
		});

	}

}
