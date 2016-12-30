package com.example.fiveyuanstore.page;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.example.fiveyuanstore.GoodsContentActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.fragment.list.PageCommodityClassifyFragment;
import com.example.fiveyuanstore.fragment.list.PageCommodityClassifyFragment.OnNewClickedListener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.widget.ListView;
import android.widget.TextView;

public class CommodityFragment extends Fragment {

	View view;
	ListView listView;
	PageCommodityClassifyFragment snack, clothing, fruit,others;

	View btnLoadMore;
	TextView textLoadMore;
	EditText search_text;
	List<Goods> data;
	int page = 0;
	TextView CommoditySortTime,CommoditySortPrice,CommoditySortCustom;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_commodity, null);

			btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
			search_text = (EditText) view.findViewById(R.id.search_text);
			listView = (ListView) view.findViewById(R.id.goods_list);

			fruit = (PageCommodityClassifyFragment) getFragmentManager().findFragmentById(R.id.fruit);
			snack = (PageCommodityClassifyFragment) getFragmentManager().findFragmentById(R.id.snack);
			clothing = (PageCommodityClassifyFragment) getFragmentManager().findFragmentById(R.id.clothing);
			others= (PageCommodityClassifyFragment) getFragmentManager().findFragmentById(R.id.others);

			listView.addFooterView(btnLoadMore);
			listView.setAdapter(listAdapter);
			
			CommoditySortTime=(TextView)view.findViewById(R.id.commodity_sort_time);
			CommoditySortPrice=(TextView)view.findViewById(R.id.commodity_sort_price);
			CommoditySortCustom=(TextView)view.findViewById(R.id.commodity_sort_custom);

			fruit.setOnNewClickedListener(new OnNewClickedListener() {
				
				@Override
				public void onNewClicked() {
					sortList("fruit");
				}
			});
			

			snack.setOnNewClickedListener(new OnNewClickedListener() {
				
				@Override
				public void onNewClicked() {
					sortList("snack");
				}
			});
			
			clothing.setOnNewClickedListener(new OnNewClickedListener() {
				
				@Override
				public void onNewClicked() {
					sortList("clothing");
				}
			});
			others.setOnNewClickedListener(new OnNewClickedListener() {
				
				@Override
				public void onNewClicked() {
					sortList("other");
				}
			});
			
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

			view.findViewById(R.id.btn_search).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					search();

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

		}
		return view;
	}

	protected void sortList(String sort) {
		Request request = Server.requestBuilderWithPath("goods/sort/"+sort).get().build();
		
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				
				try {
					final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Goods>>(){});
					
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							CommodityFragment.this.page = data.getNumber();
							CommodityFragment.this.data = data.getContent();
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
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(getActivity()).setMessage(e.getMessage()).show();
					}
				});
				
			}
		});
	}

	BaseAdapter listAdapter = new BaseAdapter() {

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

	void onItemClicked(int position) {
		Goods pos = data.get(position);

		Intent itnt = new Intent(this.getActivity(), GoodsContentActivity.class);
		itnt.putExtra("pos", (Serializable) pos);
		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		fruit.setLabelImage(R.drawable.ic_frult);
		snack.setLabelImage(R.drawable.ic_snacks);
		clothing.setLabelImage(R.drawable.ic_clothes);
		
		reload();
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
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							
							CommodityFragment.this.page = data.getNumber();
							CommodityFragment.this.data = data.getContent();
							listAdapter.notifyDataSetInvalidated();
						}
					});
				} catch (final Exception e) {

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
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(getActivity()).setMessage(e.getMessage()).show();

					}
				});

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
				getActivity().runOnUiThread(new Runnable() {

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

						getActivity().runOnUiThread(new Runnable() {

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
				getActivity().runOnUiThread(new Runnable() {

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

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							CommodityFragment.this.page = data.getNumber();
							CommodityFragment.this.data = data.getContent();
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