package com.example.fiveyuanstore.goodslist;

import java.io.IOException;
import java.util.List;


import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.GoodsListNoItem;

import com.example.fiveyuanstore.entity.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class GoodsListActivity extends Activity {

	ListView list;
	List<GoodsListNoItem> order;
	int page=0;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_goods_list);
		list = (ListView) findViewById(R.id.all_goods_list);
		
		list.setAdapter(listAdapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		reload();
	}
	
	 void reload() {
			Request request = Server.requestBuilderWithPath("/sellerGoodsList")
					.get()
					.build();
			
			Server.getClient().newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, Response res) throws IOException {
					try {
						
						final Page<GoodsListNoItem> data = new ObjectMapper().readValue(res.body().string(),
								new TypeReference<Page<GoodsListNoItem>>(){});
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								GoodsListActivity.this.order =  data.getContent();
								GoodsListActivity.this.page = data.getNumber();
								listAdapter.notifyDataSetChanged();
							}
						});
					
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(Call arg0, final IOException e) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			});
	}
	 
	 BaseAdapter listAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null){
				
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_goods_list_item, null);
			}else {
				view = convertView;
			}
			
			GoodsListNoItem goodslist=order.get(position);
			
			ProImgView goodsListImg=(ProImgView)view.findViewById(R.id.goodsListImg);
			TextView goodListName=(TextView)view.findViewById(R.id.goodListName);
			
			try {
				goodsListImg.load(goodslist);
			} catch (Exception e) {
				e.printStackTrace();
			}
			goodListName.setText(goodslist.getGoods_list_name());
			
			
			
			
			
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return order.get(position);
		}
		
		@Override
		public int getCount() {
			return order == null ?0 :order.size();
		}
	};
}
