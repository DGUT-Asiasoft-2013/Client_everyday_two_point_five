
package com.example.fiveyuanstore.page;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.GoodsListNoItem;
import com.example.fiveyuanstore.entity.MyOrder;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.goods.AddProductActivity;
import com.example.fiveyuanstore.goods.GoodsInfoActivity;
import com.example.fiveyuanstore.goodslist.AddGoodsListActivity;
import com.example.fiveyuanstore.goodslist.GoodsListActivity;
import com.example.fiveyuanstore.order.OrderInfoActivity;
import com.example.fiveyuanstore.page.fragment.SellerAddItemFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SellerFragment extends Fragment {

	View view;
	View loadMore;

	ListView listview;
	TextView txtLoadmore;
	int set_page = 1;
	// 1
	List<Goods> data1;
	Integer page1 = 0;
	// 2
	List<MyOrder> order2;
	int page2 = 0;
	// 3
	List<GoodsListNoItem> order3;
	int page3 = 0;
	
	TextView textPage1;
	TextView textPage2;
	TextView textPage3;
	
	SellerAddItemFragment frag=new SellerAddItemFragment();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 商品列表
		if (view == null) {

			view = inflater.inflate(R.layout.fragment_seller, null);
			loadMore = inflater.inflate(R.layout.widget_load_root_more_btn, null);

			txtLoadmore = (TextView) loadMore.findViewById(R.id.more_text);
			textPage1= (TextView) view.findViewById(R.id.seller_page_1);
			textPage2= (TextView) view.findViewById(R.id.seller_page_2);
			textPage3= (TextView) view.findViewById(R.id.seller_page_3);

			listview = (ListView) view.findViewById(R.id.list);
			listview.addFooterView(loadMore);
			listview.setAdapter(adapter_goodsInfo);

			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					switch (set_page) {
					case 1:
						onItemClicked_1(position);
						break;
					case 2:
						onItemClicked_2(position);
						break;
					case 3:
						onItemClicked_3(position);
						break;
					default:
						break;
					}

				}
			});
			// 加载更多
			txtLoadmore.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					switch (set_page) {
					case 1:
						loadMore1();
						break;
					case 2:
						loadMore2();
						break;
					case 3:
						loadMore3();
						break;
					default:
						break;
					}
				}
			});

			textPage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					set_page = 1;
					reload1();
					page1 = 0;
					listview.removeAllViewsInLayout();
					adapter_goodsInfo.notifyDataSetInvalidated();
					listview.setAdapter(adapter_goodsInfo);
					changeText();
				}
			});
			textPage2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					set_page = 2;
					reload2();
					page2 = 0;
					listview.removeAllViewsInLayout();
					adapter_orderHandler.notifyDataSetInvalidated();
					listview.setAdapter(adapter_orderHandler);
					changeText();

				}
			});
			textPage3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					set_page = 3;
					reload3();
					page3 = 0;
					listview.removeAllViewsInLayout();
					listAdapter_goodsList.notifyDataSetInvalidated();
					listview.setAdapter(listAdapter_goodsList);
					changeText();
				}
			});
			view.findViewById(R.id.btn_add).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getFragmentManager().beginTransaction()
							.setCustomAnimations(R.animator.slide_in_right,
							             R.animator.slide_out_left,
								         R.animator.slide_in_left,
						                 R.animator.slide_out_right)
							.replace(R.id.container,frag, "TWO").addToBackStack(null).commit();
					
				}
			});

		}
		return view;
	}


	@Override
	public void onResume() {
		super.onResume();
		reload();

	}
	
	void changeText(){
		switch (set_page) {
		case 1:
			textPage1.setTextColor(Color.parseColor("#ff5337"));
			textPage1.setBackground(getResources().getDrawable(R.drawable.filter));
			textPage2.setTextColor(Color.parseColor("#000000"));
			textPage2.setBackground(null);
			textPage3.setTextColor(Color.parseColor("#000000"));
			textPage3.setBackground(null);
			break;
		case 2:
			textPage2.setTextColor(Color.parseColor("#ff5337"));
			textPage2.setBackground(getResources().getDrawable(R.drawable.filter));
			textPage1.setTextColor(Color.parseColor("#000000"));
			textPage1.setBackground(null);
			textPage3.setTextColor(Color.parseColor("#000000"));
			textPage3.setBackground(null);
			break;
		case 3:
			textPage3.setTextColor(Color.parseColor("#ff5337"));
			textPage3.setBackground(getResources().getDrawable(R.drawable.filter));
			textPage2.setTextColor(Color.parseColor("#000000"));
			textPage2.setBackground(null);
			textPage1.setTextColor(Color.parseColor("#000000"));
			textPage1.setBackground(null);
			break;

		default:
			break;
		}
	}
	
	void reload(){
		changeText();
		switch (set_page) {
		case 1:
			reload1();
			page1 = 0;
			listview.removeAllViewsInLayout();
			adapter_goodsInfo.notifyDataSetInvalidated();
			listview.setAdapter(adapter_goodsInfo);
			break;
		case 2:
			reload2();
			page2 = 0;
			listview.removeAllViewsInLayout();
			adapter_orderHandler.notifyDataSetInvalidated();
			listview.setAdapter(adapter_orderHandler);
			break;
		case 3:
			reload3();
			page3 = 0;
			listview.removeAllViewsInLayout();
			listAdapter_goodsList.notifyDataSetInvalidated();
			listview.setAdapter(listAdapter_goodsList);
			break;
		default:
			break;
		}
	}

	// 1
	void reload1() {

		Request request = Server.requestBuilderWithPath("/goods").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				try {
					final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Goods>>() {
							});

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							SellerFragment.this.page1 = data.getNumber();
							SellerFragment.this.data1 = data.getContent();
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
		Request request = Server.requestBuilderWithPath("/goods/" + (page1 + 1)).get().build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {

				getActivity().runOnUiThread(new Runnable() {
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
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (SellerFragment.this.data1 == null) {
									SellerFragment.this.data1 = data.getContent();
								} else {
									SellerFragment.this.data1.addAll(data.getContent());
								}
								SellerFragment.this.page1 = data.getNumber();
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
				LayoutInflater inflater = LayoutInflater.from(getActivity());
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

	// 订单处理

	void onItemClicked_1(int position) {
		Goods goods = data1.get(position);
		Intent itnt = new Intent(this.getActivity(), GoodsInfoActivity.class);
		itnt.putExtra("data", (Serializable) goods);
		startActivity(itnt);
	}

	// 2
	void reload2() {
		Request request = Server.requestBuilderWithPath("/order").get().build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				try {

					final Page<MyOrder> data = new ObjectMapper().readValue(res.body().string(),
							new TypeReference<Page<MyOrder>>() {
							});
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							SellerFragment.this.order2 = data.getContent();
							SellerFragment.this.page2 = data.getNumber();
							adapter_orderHandler.notifyDataSetChanged();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Log.d("SellerFragment", e.getMessage());
					}
				});
			}
		});
	}

	void loadMore2() {
		loadMore.setEnabled(false);
		txtLoadmore.setText("载入中...");

		Request request = Server.requestBuilderWithPath("/order/" + (page2++)).get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response res) throws IOException {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loadMore.setEnabled(true);
						txtLoadmore.setText("加载更多");

						try {
							final Page<MyOrder> data = new ObjectMapper().readValue(res.body().string(),
									new TypeReference<Page<MyOrder>>() {
									});
							if (data.getNumber() > page2) {

								if (order2 == null) {
									order2 = data.getContent();

								} else {
									order2.addAll(data.getContent());
								}

								page2 = data.getNumber();
								adapter_orderHandler.notifyDataSetChanged();

							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loadMore.setEnabled(true);
						txtLoadmore.setText("加载更多");
					}
				});
			}
		});
	}

	BaseAdapter adapter_orderHandler = new BaseAdapter() {

		@Override
		public int getCount() {
			return order2 == null ? 0 : order2.size();
		}

		@Override
		public Object getItem(int position) {
			return order2.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_order_item, null);

			}

			MyOrder orders = order2.get(position);
			ProImgView proImg = (ProImgView) view.findViewById(R.id.proImg);
			TextView orderId = (TextView) view.findViewById(R.id.orderid);
			TextView goods_num = (TextView) view.findViewById(R.id.orderNum);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView date = (TextView) view.findViewById(R.id.date);
			TextView status = (TextView) view.findViewById(R.id.status);

			try {
				proImg.load(orders.getGoods());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			orderId.setText("订单编号： " + orders.getOrder_num());
			try {
				goods_num.setText("订单数量: " + orders.getAmount());
			} catch (Exception e) {
				// TODO Auto-generated catch block orders.getAmount()
				e.printStackTrace();
			}
			title.setText(orders.getGoods().getTitle());
			switch (orders.getStatus()) {
			case 0:
				status.setText("已确认收货");
				break;
			case 1:
				status.setText("已付款，等待操作");
				break;

			case 2:
				status.setText("已发货");
				break;

			case 3:
				status.setText("已取消");
				break;
			default:
				status.setText("订单状态： 未知状态");
				break;

			}

			String dateStr = DateFormat.format("yy-MM-dd hh:mm", orders.getCreateDate()).toString();
			date.setText("创建日期: " + dateStr);
			return view;
		}

	};

	void onItemClicked_2(int position) {

		MyOrder orders = order2.get(position);

		Intent itnt = new Intent(this.getActivity(), OrderInfoActivity.class);
		itnt.putExtra("orders", (Serializable) orders);
		startActivity(itnt);
	}

	void reload3() {
		Request request = Server.requestBuilderWithPath("/sellerGoodsList").get().build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				try {

					final Page<GoodsListNoItem> data = new ObjectMapper().readValue(res.body().string(),
							new TypeReference<Page<GoodsListNoItem>>() {
							});
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							SellerFragment.this.order3 = data.getContent();
							SellerFragment.this.page3 = data.getNumber();
							listAdapter_goodsList.notifyDataSetChanged();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				getActivity().runOnUiThread(new Runnable() {

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

		Request request = Server.requestBuilderWithPath("/order/" + (page3++)).get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response res) throws IOException {
				getActivity().runOnUiThread(new Runnable() {

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
								adapter_orderHandler.notifyDataSetChanged();

							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				getActivity().runOnUiThread(new Runnable() {

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
		Intent itnt = new Intent(this.getActivity(), GoodsListActivity.class);
		itnt.putExtra("id", id);
		startActivity(itnt);
	}
}
