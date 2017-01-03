package com.example.fiveyuanstore.goodslist;

import java.io.IOException;
import java.util.List;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.GoodsListNoItem;
import com.example.fiveyuanstore.entity.GoodsListWithItem;
import com.example.fiveyuanstore.entity.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
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
	ProImgView goodsListImg;
	TextView goodListName;
	TextView goodListText;
	int id;
	
	GoodsListWithItem order;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		id=(int) getIntent().getSerializableExtra("id");
		setContentView(R.layout.activity_goods_list);
		list = (ListView) findViewById(R.id.goodsItemList);
		goodListName=(TextView)findViewById(R.id.goodListName);
		goodListText=(TextView)findViewById(R.id.goodListText);
		list.setAdapter(listAdapter);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		reload();
		
		
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
