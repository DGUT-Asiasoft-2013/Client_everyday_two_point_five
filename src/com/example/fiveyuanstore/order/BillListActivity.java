package com.example.fiveyuanstore.order;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.R.id;
import com.example.fiveyuanstore.R.layout;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.MyOrder;
import com.example.fiveyuanstore.entity.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class BillListActivity extends Activity {
	TextView txtLoadmore;
	ProImgView  proImg;
	List<MyOrder> order;
	ListView list;
	View loadMoreView;
	int page =0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 订单处理  LWJ
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_bill_list);
		
		list = (ListView) findViewById(R.id.list);
		
		loadMoreView = LayoutInflater.from(this).inflate(R.layout.widget_load_root_more_btn, null);
	
		txtLoadmore =  (TextView) loadMoreView.findViewById(R.id.more_text);
		list.addFooterView(loadMoreView);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onItemClicked(position);
			}
		});
		
		txtLoadmore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadmore();
			}
		});
		
		
		reload();
	}
	


	void onItemClicked(int position) {
		MyOrder orders = order.get(position);

		Intent itnt = new Intent(BillListActivity.this, BillInfoActivity.class);
		itnt.putExtra("orders", (Serializable) orders);
		startActivity(itnt);
	}
	

	@Override
	public void onResume() {
		super.onResume();
		
		reload();
	}

	 void reload() {
			Request request = Server.requestBuilderWithPath("/order")
					.get()
					.build();
			
			Server.getClient().newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, Response res) throws IOException {
					try {
						
						final Page<MyOrder> data = new ObjectMapper().readValue(res.body().string(), new TypeReference<Page<MyOrder>>(){});
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								BillListActivity.this.order =  data.getContent();
								BillListActivity.this.page = data.getNumber();
								adapter.notifyDataSetChanged();
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
	
	 
	 void loadmore() {
		loadMoreView.setEnabled(false);
		txtLoadmore.setText("载入中...");
		
		Request request = Server.requestBuilderWithPath("/order/"+(page++)).get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, final Response res) throws IOException {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						loadMoreView.setEnabled(true);
						txtLoadmore.setText("加载更多");
						
						try {
							final Page<MyOrder> data = new ObjectMapper().readValue(res.body().string(), new TypeReference<Page<MyOrder>>() {});
							if(data.getNumber()> page){
						
										if(order == null){
											order =  data.getContent();
											
										}else{
											order.addAll(data.getContent());
										}
										
										page = data.getNumber();
										adapter.notifyDataSetChanged();
								
							}
						
						
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
			}
			
			@Override
			public void onFailure(Call arg0, IOException e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						loadMoreView.setEnabled(true);
						txtLoadmore.setText("加载更多");
					}
				});
			}
		});
	}
	 
	 BaseAdapter adapter = new BaseAdapter(){

		@Override
		public int getCount() {
			return order == null ?0 :order.size();
		}

		@Override
		public Object getItem(int position) {
			return order.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null){
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_bill_list_item,null);
				
			}
			
			MyOrder orders = order.get(position);
			ProImgView proImg = (ProImgView) view.findViewById(R.id.proImg);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView date = (TextView) view.findViewById(R.id.date);
			TextView status = (TextView) view.findViewById(R.id.status);
			TextView price =  (TextView) view.findViewById(R.id.price);
			TextView orderNum = (TextView) view.findViewById(R.id.orderNum);
			proImg.load(orders.getGoods());
			orderNum.setText("订单数量: "+ orders.getAmount());
			title.setText("商品名称: "+orders.getGoods().getTitle());
			switch(orders.getStatus()){
				case 0:
					status.setText("订单状态： 确认收货");
					break;
				case 1:
					status.setText("订单状态： 已付款");
					break;
					
				case 2:
					status.setText("订单状态： 已发货");
					break;
					
				case 3: 
					status.setText("订单状态： 已取消");
					break;
				default:
					status.setText("订单状态： 未知状态");
					break;
					
			}
			
			
			String dateStr = DateFormat.format("yy-MM-dd hh:mm", orders.getCreateDate()).toString();
			date.setText("创建日期: "+dateStr);
			price.setText("￥"+orders.getPrice());
			return view;
		}
		 
	 };

	
}
