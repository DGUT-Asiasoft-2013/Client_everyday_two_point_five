package com.example.fiveyuanstore;

import java.io.IOException;
import java.util.List;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.entity.Record;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class TransactionActivity extends Activity {

	List<Record> records;
	int page=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);

		ListView listView = (ListView) findViewById(R.id.transaction_list);
		listView.setAdapter(listAdapter);
		
		findViewById(R.id.btn_transaction_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();		
			}
		});
		
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_transaction, null);
			} else {
				view = convertView;
			}
			TextView money = (TextView) view.findViewById(R.id.money);
			TextView state = (TextView) view.findViewById(R.id.state);
			TextView date = (TextView) view.findViewById(R.id.date);

			Record record = records.get(position);

			money.setText(record.getMoney().toString());
			state.setText(record.getState());
			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", record.getCreateDate()).toString();
			date.setText(dateStr);

			return view;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return records.get(position).getId();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return records.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return records == null ? 0 : records.size();
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reload();
	}

	void reload() {

		Request request = Server.requestBuilderWithPath("record").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Record> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Record>>() {

							});
					runOnUiThread(new Runnable() {
						public void run() {
							TransactionActivity.this.reloadData(data);
						}
					});

				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							TransactionActivity.this.onFailure(e);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						TransactionActivity.this.onFailure(arg1);
					}
				});

			}
		});
	}
	protected void reloadData(Page<Record> data) {
		page = data.getNumber();
		records = data.getContent();
		listAdapter.notifyDataSetInvalidated();
	}
	
	void onFailure(Exception e) {
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
	}
}
