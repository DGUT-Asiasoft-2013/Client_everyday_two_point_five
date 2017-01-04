package com.example.fiveyuanstore;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.page.CommodityFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GoodsActivity extends Activity{

	
	String type;
	String sort;
	String searchText;
	TextView textLoadMore;
	List<Goods> data;
	int page = 0;
	View btnLoadMore;
	ListView goods_list;
	EditText search_text;
	ImageView search;
	TextView CommoditySortTime,CommoditySortPrice,CommoditySortCustom;
	Boolean isGetSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type=(String) getIntent().getSerializableExtra("type");
		isGetSearch=false;
		if(type.equals("sort")){
			sort=(String) getIntent().getSerializableExtra("sort");
			sortList(sort);
		}else if(type.equals("searchText")){
			searchText=(String) getIntent().getSerializableExtra("searchText");
			isGetSearch=true;
			search();
		}else{
			reload();
		}
		setContentView(R.layout.activity_goods);
		btnLoadMore=LayoutInflater.from(this).inflate(R.layout.widget_load_more_button, null);
		goods_list=(ListView)findViewById(R.id.goods_list);
		search_text = (EditText)findViewById(R.id.search_text);
		search =(ImageView)findViewById(R.id.btn_search);
		textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
		CommoditySortTime=(TextView)findViewById(R.id.commodity_sort_time);
		CommoditySortPrice=(TextView)findViewById(R.id.commodity_sort_price);
		CommoditySortCustom=(TextView)findViewById(R.id.commodity_sort_custom);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search();

			}
		});
		btnLoadMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadmore();
			}
		});
		goods_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onItemClicked(position);

			}
		});
		CommoditySortTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommoditySortTime.setText("时间排序▼");
				CommoditySortTime.setTextColor(Color.parseColor("#ff5337"));
				CommoditySortPrice.setText("价格排序");
				CommoditySortPrice.setTextColor(Color.parseColor("#000000"));
				CommoditySortCustom.setTextColor(Color.parseColor("#000000"));

			}
		});
		CommoditySortPrice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommoditySortTime.setText("时间排序");
				CommoditySortTime.setTextColor(Color.parseColor("#000000"));
				CommoditySortPrice.setText("价格排序▼");
				CommoditySortPrice.setTextColor(Color.parseColor("#ff5337"));
				CommoditySortCustom.setTextColor(Color.parseColor("#000000"));

			}
		});
		CommoditySortCustom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommoditySortTime.setText("时间排序");
				CommoditySortTime.setTextColor(Color.parseColor("#000000"));
				CommoditySortPrice.setText("价格排序");
				CommoditySortPrice.setTextColor(Color.parseColor("#000000"));
				CommoditySortCustom.setTextColor(Color.parseColor("#ff5337"));

			}
		});
		goods_list.setAdapter(listAdapter);
		goods_list.addFooterView(btnLoadMore);
	}
	
	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(GoodsActivity.this);
				view = inflater.inflate(R.layout.fragment_inputcell_goods, null);
			} else {
				view = convertView;
			}
			TextView textContent = (TextView) view.findViewById(R.id.text);
			TextView goodsName = (TextView) view.findViewById(R.id.goods_name);
			TextView money = (TextView) view.findViewById(R.id.money);
			TextView textDate = (TextView) view.findViewById(R.id.date);
			ProImgView img = (ProImgView)view.findViewById(R.id.goods_img);

			Goods goods = data.get(position);

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
			return data.get(position);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data == null ? 0 : data.size();

		}
	};
	protected void sortList(String sort) {
		Request request = Server.requestBuilderWithPath("goods/sort/"+sort).get().build();
		
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				
				try {
					final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Goods>>(){});
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							GoodsActivity.this.page = data.getNumber();
							GoodsActivity.this.data = data.getContent();
							listAdapter.notifyDataSetInvalidated();
							
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(GoodsActivity.this).setMessage(e.getMessage()).show();
					}
				});
				
			}
		});
	}
	void reload() {
		Request request = Server.requestBuilderWithPath("feeds").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Goods>>() {
							});
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							
							GoodsActivity.this.page = data.getNumber();
							GoodsActivity.this.data = data.getContent();
							listAdapter.notifyDataSetInvalidated();
						}
					});
				} catch (final Exception e) {
					e.printStackTrace();
				/*	getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							new AlertDialog.Builder(getActivity()).setMessage(e.getMessage()).show();
						}
					});*/
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Log.d("Fragment", e.getMessage());

					}
				});

			}
		});


	}
	
	void onItemClicked(int position) {
		Goods pos = data.get(position);

		Intent itnt = new Intent(this, GoodsContentActivity.class);
		itnt.putExtra("pos", (Serializable) pos);
		startActivity(itnt);
	}
	void search() {
		String sText;
		if(isGetSearch){
			sText=searchText;
			isGetSearch=false;
		}else
			sText=search_text.getText().toString();
		MultipartBody.Builder body=new 
				MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("text", sText);
		RequestBody requestBody=body.build();
		Request request=Server.requestBuilderWithPath("/search").post(requestBody).build();
	
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Goods>>() {
							});

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							GoodsActivity.this.page = data.getNumber();
							GoodsActivity.this.data = data.getContent();
							listAdapter.notifyDataSetInvalidated();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}			
				
			}
			
			@Override
			public void onFailure(Call arg0, IOException e) {
				Log.d("Fragment", e.getMessage());
				
			}
		});
	}
	
	void loadmore() {
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("载入中。。。");

		Request request = Server.requestBuilderWithPath("feeds/" + (page + 1)).get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");

					}
				});

				try {
					final Page<Goods> goods = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Goods>>() {
							});
					if (goods.getNumber() > page) {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (data == null) {
									data = goods.getContent();
								} else {
									data.addAll(goods.getContent());
								}
								page = goods.getNumber();
								listAdapter.notifyDataSetChanged();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");

					}
				});

			}
		});
	}
}
