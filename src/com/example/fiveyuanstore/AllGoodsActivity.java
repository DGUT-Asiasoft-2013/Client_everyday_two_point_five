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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AllGoodsActivity extends Activity {
	private ListView listView;
	private View btnLoadMore;
	private TextView textLoadMore;
	List<Goods> data;
	EditText search_text;
	int page = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_all_goods);
		listView = (ListView) findViewById(R.id.goods_list);
		
		btnLoadMore = LayoutInflater.from(this).inflate(R.layout.widget_load_more_button, null);
		textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
		search_text = (EditText) findViewById(R.id.search_text);
		listView.addFooterView(btnLoadMore);
		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onItemClicked(position);

			}
		});
		btnLoadMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadmore();
			}
		});
		

	findViewById(R.id.btn_search).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search();

			}
		});
	}
	
	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(AllGoodsActivity.this);
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
			goodsName.setText(goods.getTitle());
			money.setText("$" + Float.toString(goods.getPrice()));

			img.load(goods);

			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", goods.getCreateDate()).toString();
			textDate.setText(dateStr);

			return view;
		}

		@Override
		public long getItemId(int position) {
			return position;

		}

		@Override
		public Object getItem(int position) {
			return data.get(position);

		}

		@Override
		public int getCount() {
			return data == null ? 0 : data.size();

		}
	};

	void onItemClicked(int position) {
		Goods pos = data.get(position);

		Intent itnt = new Intent(this, GoodsContentActivity.class);
		itnt.putExtra("pos", (Serializable) pos);
		startActivity(itnt);
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
							listAdapter.notifyDataSetInvalidated();
							AllGoodsActivity.this.page = data.getNumber();
							AllGoodsActivity.this.data = data.getContent();
						}
					});
				} catch (final Exception e) {

			 runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(AllGoodsActivity.this).setMessage(e.getMessage()).show();
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				 runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(AllGoodsActivity.this).setMessage(e.getMessage()).show();

					}
				});

			}
		});

	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reload();
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
	
void search() {
		
		MultipartBody.Builder body=new 
				MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("text", search_text.getText().toString());
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
							AllGoodsActivity.this.page = data.getNumber();
							AllGoodsActivity.this.data = data.getContent();
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
}
