package com.example.fiveyuanstore;

import java.io.IOException;
import java.util.List;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.MyOrder;
import com.example.fiveyuanstore.entity.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class OrderHandlerActivity extends Activity {
	TextView title,  goods_num,orderId, txtLoadmore;
	Button sureSendGoods,cancleOrder;
	ProImgView  proImg;
	List<MyOrder> order;
	ListView list;
	View loadMoreView;
	int page =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 订单处理  LWJ
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_oder_handler);
		
		list = (ListView) findViewById(R.id.list);
		goods_num =(TextView) list.findViewById(R.id.orderNum);
		title = (TextView) list.findViewById(R.id.title);
		orderId = (TextView) list.findViewById(R.id.orderid);
		
		sureSendGoods = (Button) list.findViewById(R.id.sureSendGoods);
		cancleOrder = (Button) list.findViewById(R.id.cancleOrder);
		
		proImg = (ProImgView) list.findViewById(R.id.proImg);
		
		 loadMoreView = LayoutInflater.from(this).inflate(R.layout.widget_load_root_more_btn, null);
			 list.addFooterView(loadMoreView);
		txtLoadmore =  (TextView) loadMoreView.findViewById(R.id.more_text);
		
		txtLoadmore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadmore();
			}
		});
		
		reload();
	}
	

	

	

	@Override
	public void onResume() {
		super.onResume();
		
		reload();
	}

	 void reload() {
			Request request = Server.requestBuilderWithPath("order")
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
								OrderHandlerActivity.this.order =  data.getContent();
								OrderHandlerActivity.this.page = data.getNumber();
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
		
		Request request = Server.requestBuilderWithPath("order/"+(page+1)).get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						loadMoreView.setEnabled(true);
						txtLoadmore.setText("加载更多");
					}
				});
				
				try {
					final Page<MyOrder> data = new ObjectMapper().readValue(res.body().string(), new TypeReference<Page<MyOrder>>() {});
					if(data.getNumber()> page){
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(order == null){
									order =   data.getContent();
									
								}else{
									order.addAll(data.getContent());
								}
								
								page = data.getNumber();
								
								adapter.notifyDataSetChanged();
							}
						});
					}
				
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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
			// TODO Auto-generated method stub
			return order == null ?0 :order.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return order.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (view == null){
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_order_item,null);
				
			}
			
			MyOrder orders = order.get(position);
			ProImgView proImg = (ProImgView) view.findViewById(R.id.proImg);
			TextView orderId = (TextView) view.findViewById(R.id.orderid);
			TextView goods_num =(TextView) view.findViewById(R.id.orderNum);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView date = (TextView) view.findViewById(R.id.date);
			/*Button sureSendGoods = (Button) view.findViewById(R.id.sureSendGoods);
			Button cancleOrder = (Button) view.findViewById(R.id.cancleOrder);*/
			
			proImg.load(orders.getGoods());
			orderId.setText(orders.getOrder_num());
			goods_num.setText(orders.getCount());
			title.setText(orders.getTitle());
			
			
			String dateStr = DateFormat.format("yy-MM-dd hh:mm", orders.getCreateDate()).toString();
			date.setText(dateStr);
			return view;
		}
		 
	 };

	
}
