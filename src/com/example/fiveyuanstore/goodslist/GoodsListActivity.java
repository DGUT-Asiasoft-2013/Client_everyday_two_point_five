package com.example.fiveyuanstore.goodslist;

import java.io.IOException;
import java.io.Serializable;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.ZoneActivity;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.GoodsListNoItem;
import com.example.fiveyuanstore.entity.GoodsListWithItem;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.example.fiveyuanstore.goods.GoodsContentActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoodsListActivity extends Activity {

	ListView list;
	ProImgView goodsListImg;
	TextView goodListName;
	TextView goodListText;
	int id;
	TextView sellerName;
	AvatarView avatar;
	GoodsListWithItem order;
	User user;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		id=(int) getIntent().getSerializableExtra("id");
		setContentView(R.layout.activity_goods_list);
		list = (ListView) findViewById(R.id.goodsItemList);
		
		sellerName=(TextView)findViewById(R.id.seller_name);
		avatar=(AvatarView)findViewById(R.id.avatar);
		
		goodListName=(TextView)findViewById(R.id.goodListName);
		goodListText=(TextView)findViewById(R.id.goodListText);
		goodsListImg=(ProImgView)findViewById(R.id.goodsListImg);
		list.setAdapter(listAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onItemClicked(position);
			}
		});
		findViewById(R.id.seller).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itnt = new Intent(GoodsListActivity.this, ZoneActivity.class);
				itnt.putExtra("id", user.getId());
				startActivity(itnt);
			}
		});
		
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		reload();
		
		
	}
	void onItemClicked(int position) {
		Goods pos = order.getGoods_list_item().get(position);

		Intent itnt = new Intent(this, GoodsContentActivity.class);
		itnt.putExtra("pos", (Serializable) pos);
		startActivity(itnt);
	}
	void reload() {
		Request request = Server.requestBuilderWithPath("/GoodsList/"+id)
				.get()
				.build();
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				try {
					
					final GoodsListWithItem data = new ObjectMapper().readValue(res.body().string(),
							new TypeReference<GoodsListWithItem>(){});
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							GoodsListActivity.this.order = data;
							Log.d("11111", data.getGoods_list_name());
							goodListName.setText(order.getGoods_list_name());
							goodListText.setText(order.getGoods_list_text());
							goodsListImg.load(order);
							listAdapter.notifyDataSetChanged();
							getMe();
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
	
	void getMe(){
		OkHttpClient client = Server.getClient();
		Request request = Server.requestBuilderWithPath("/user/"+order.getSeller_id()).get().build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, Response res) throws IOException {
				try {
					final User user = new ObjectMapper().readValue(res.body().string(),
							new TypeReference<User>() {
							});

						runOnUiThread(new Runnable() {

							public void run() {
								GoodsListActivity.this.user=user;
								avatar.load(user);
								sellerName.setText(user.getUser_name());
								
							}
						});
					
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), arg1.getMessage(),
								Toast.LENGTH_SHORT).show();
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
				view = inflater.inflate(R.layout.fragment_inputcell_goods, null);
			}else {
				view = convertView;
			}
			TextView textContent = (TextView) view.findViewById(R.id.text);
			TextView goodsName = (TextView) view.findViewById(R.id.goods_name);
			TextView money = (TextView) view.findViewById(R.id.money);
			TextView textDate = (TextView) view.findViewById(R.id.date);
			ProImgView img = (ProImgView)view.findViewById(R.id.goods_img);
			
			Goods goods = order.getGoods_list_item().get(position);
			
			textContent.setText(goods.getText());
			goodsName.setText("              "+goods.getTitle());
			money.setText("$" + Float.toString(goods.getPrice()));

			img.load(goods);

			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", goods.getCreateDate()).toString();
			textDate.setText(dateStr);
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
			return order.getGoods_list_item().get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return order==null?0:order.getGoods_list_item().size();
		}
	};
}
