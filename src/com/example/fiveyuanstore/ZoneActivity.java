package com.example.fiveyuanstore;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.GoodsListNoItem;
import com.example.fiveyuanstore.entity.MyOrder;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.entity.UserInformation;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.example.fiveyuanstore.goods.GoodsContentActivity;
import com.example.fiveyuanstore.goods.GoodsInfoActivity;
import com.example.fiveyuanstore.goodslist.GoodsListActivity;
import com.example.fiveyuanstore.myProfiles.MyDataActivity;
import com.example.fiveyuanstore.page.MyProfileFragment;
import com.example.fiveyuanstore.page.SellerFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ZoneActivity extends Activity{
	int id;
	AvatarView avatar;
	TextView name;
	TextView sex;
	TextView whatsUp;
	ListView listView;
	TextView txtLoadmore;
	User user;
	UserInformation myInfor;
	View loadMore;
	int set_page = 0;
	// 1
	List<Goods> data1;
	Integer page1 = 0;

	// 3
	List<GoodsListNoItem> order3;
	int page3 = 0;
	TextView textPage1;
	TextView textPage2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zone);
		
		id=(int) getIntent().getSerializableExtra("id");
		avatar=(AvatarView)findViewById(R.id.avatar);
		name=(TextView)findViewById(R.id.user_name);
		sex=(TextView)findViewById(R.id.user_sex);
		whatsUp=(TextView)findViewById(R.id.user_whats_up);
		listView = (ListView)findViewById(R.id.zone_list);
		loadMore =LayoutInflater.from(this).inflate(R.layout.widget_load_root_more_btn, null);
		txtLoadmore = (TextView) loadMore.findViewById(R.id.more_text);
		listView.addFooterView(loadMore);
		listView.setAdapter(adapter_goodsInfo);
		textPage1= (TextView) findViewById(R.id.btn_goods);
		textPage2= (TextView) findViewById(R.id.btn_goods_list);

		

		findViewById(R.id.btn_zone_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		findViewById(R.id.btn_goods_list).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ta的清单
				set_page = 1;
				reload1();
				page1 = 0;
				listView.removeAllViewsInLayout();
				adapter_goodsInfo.notifyDataSetInvalidated();
				listView.setAdapter(adapter_goodsInfo);
				textPage2.setTextColor(Color.parseColor("#ff5337"));
				textPage2.setBackground(getResources().getDrawable(R.drawable.filter));
				textPage1.setTextColor(Color.parseColor("#000000"));
				textPage1.setBackground(null);
				
			}
		});
		findViewById(R.id.btn_goods).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ta的商品
				set_page = 3;
				reload3();
				page3 = 0;
				listView.removeAllViewsInLayout();
				listAdapter_goodsList.notifyDataSetInvalidated();
				listView.setAdapter(listAdapter_goodsList);
				textPage1.setTextColor(Color.parseColor("#ff5337"));
				textPage1.setBackground(getResources().getDrawable(R.drawable.filter));
				textPage2.setTextColor(Color.parseColor("#000000"));
				textPage2.setBackground(null);
				
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (set_page) {
				case 1:
					onItemClicked_1(position);
					break;
				case 3:
					onItemClicked_3(position);
					break;
				default:
					break;
				}

			}
		});
		txtLoadmore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (set_page) {
				case 1:
					loadMore1();
					break;
				case 3:
					loadMore3();
					break;
				default:
					break;
				}
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		reload();

	}
	
	void reload(){
		switch (set_page) {
		case 1:
			reload1();
			page1 = 0;
			listView.removeAllViewsInLayout();
			adapter_goodsInfo.notifyDataSetInvalidated();
			listView.setAdapter(adapter_goodsInfo);
			break;
		case 3:
			reload3();
			page3 = 0;
			listView.removeAllViewsInLayout();
			listAdapter_goodsList.notifyDataSetInvalidated();
			listView.setAdapter(listAdapter_goodsList);
			break;
		case 0:
			getMe();
			set_page=1;
			break;
		default:
			break;
		}
	}
	
	void getMe(){
		OkHttpClient client = Server.getClient();
		Request request = Server.requestBuilderWithPath("/user/"+id).get().build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, Response res) throws IOException {
				try {
					final User user = new ObjectMapper().readValue(res.body().string(),
							new TypeReference<User>() {
							});

						runOnUiThread(new Runnable() {

							public void run() {
								ZoneActivity.this.user=user;
								avatar.load(user);
								name.setText(user.getUser_name());
								getInformation();
								reload1();
								page1 = 0;
								listView.removeAllViewsInLayout();
								adapter_goodsInfo.notifyDataSetInvalidated();
								listView.setAdapter(adapter_goodsInfo);
								
							}
						});
					
				} catch (final Exception e) {
					Toast.makeText(getApplicationContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
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
	
	void getInformation(){

		
		Request request = Server.requestBuilderWithPath("/getInformation/" + id).get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				try {
					final UserInformation infor = new ObjectMapper().readValue(res.body().string(),
							new TypeReference<UserInformation>(){});

					runOnUiThread(new Runnable() {
						public void run() {
							ZoneActivity.this.myInfor = infor;
							sex.setText(infor.getSex());

							if(infor.getWhats_up()==""||infor.getWhats_up()==null)
								whatsUp.setText("这个用户真懒，ta还没写签名~！");
							else
								whatsUp.setText("个性签名："+infor.getWhats_up());
						}
					});

				} catch (final Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getApplication(), arg1.getMessage(), Toast.LENGTH_LONG).show();

					}
				});

			}
		});
	}
	
	
	

	// 1
	void reload1() {

		Request request = Server.requestBuilderWithPath("/goodsById/"+id).get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				try {
					final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Goods>>() {
							});

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							ZoneActivity.this.page1 = data.getNumber();
							ZoneActivity.this.data1 = data.getContent();
							adapter_goodsInfo.notifyDataSetInvalidated();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				Log.d("SellerFragment", e.getMessage());
			}
		});
	}

	void loadMore1() {
		loadMore.setEnabled(false);
		txtLoadmore.setText("载入中...");
		Request request = Server.requestBuilderWithPath("/goodsById/"+id + (page1 + 1)).get().build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						loadMore.setEnabled(true);
						txtLoadmore.setText("加载更多");

					}
				});
				try {
					final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Goods>>() {
							});
					if (data.getNumber() > page1) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (ZoneActivity.this.data1 == null) {
									ZoneActivity.this.data1 = data.getContent();
								} else {
									ZoneActivity.this.data1.addAll(data.getContent());
								}
								ZoneActivity.this.page1 = data.getNumber();
								adapter_goodsInfo.notifyDataSetInvalidated();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				Log.d("SellerFragment", e.getMessage());
				loadMore.setEnabled(true);
				txtLoadmore.setText("加载更多");
			}
		});
	}

	BaseAdapter adapter_goodsInfo = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fragment_inputcell_goods, null);
			} else {
				view = convertView;
			}
			TextView textContent = (TextView) view.findViewById(R.id.text);
			TextView goodsName = (TextView) view.findViewById(R.id.goods_name);
			TextView money = (TextView) view.findViewById(R.id.money);
			TextView textDate = (TextView) view.findViewById(R.id.date);
			ProImgView img = (ProImgView) view.findViewById(R.id.goods_img);

			Goods goods = data1.get(position);

			textContent.setText("商品简介： " + goods.getText());
			goodsName.setText("              " + goods.getTitle());
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
			return data1.get(position);
		}

		@Override
		public int getCount() {
			return data1 == null ? 0 : data1.size();
		}
	};
	
	void onItemClicked_1(int position) {
		Goods goods = data1.get(position);
		Intent itnt = new Intent(this, GoodsContentActivity.class);
		itnt.putExtra("pos", (Serializable) goods);
		startActivity(itnt);
	}
	//3
	void reload3() {
		Request request = Server.requestBuilderWithPath("findSellerGoodsList/"+id).get().build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				try {

					final Page<GoodsListNoItem> data = new ObjectMapper().readValue(res.body().string(),
							new TypeReference<Page<GoodsListNoItem>>() {
							});
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							ZoneActivity.this.order3 = data.getContent();
							ZoneActivity.this.page3 = data.getNumber();
							listAdapter_goodsList.notifyDataSetChanged();
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
						Log.d("SellerFragment", e.getMessage());
					}
				});
			}
		});
	}

	void loadMore3() {
		loadMore.setEnabled(false);
		txtLoadmore.setText("载入中...");

		Request request = Server.requestBuilderWithPath("findSellerGoodsList/"+id + (page3++)).get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response res) throws IOException {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loadMore.setEnabled(true);
						txtLoadmore.setText("加载更多");

						try {
							final Page<GoodsListNoItem> data = new ObjectMapper().readValue(res.body().string(),
									new TypeReference<Page<MyOrder>>() {
									});
							if (data.getNumber() > page3) {

								if (order3 == null) {
									order3 = data.getContent();

								} else {
									order3.addAll(data.getContent());
								}

								page3 = data.getNumber();
								listAdapter_goodsList.notifyDataSetChanged();

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
						loadMore.setEnabled(true);
						txtLoadmore.setText("加载更多");
					}
				});
			}
		});
	}

	BaseAdapter listAdapter_goodsList = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {

				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_goods_list_item, null);
			} else {
				view = convertView;
			}

			GoodsListNoItem goodslist = order3.get(position);

			ProImgView goodsListImg = (ProImgView) view.findViewById(R.id.goodsListImg);
			TextView goodListName = (TextView) view.findViewById(R.id.goodListName);

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
			return order3.get(position);
		}

		@Override
		public int getCount() {
			return order3 == null ? 0 : order3.size();
		}
	};

	void onItemClicked_3(int position) {
		int id = order3.get(position).getId();
		Intent itnt = new Intent(this, GoodsListActivity.class);
		itnt.putExtra("id", id);
		startActivity(itnt);
	}
}
