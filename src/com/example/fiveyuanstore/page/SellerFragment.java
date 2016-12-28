
package com.example.fiveyuanstore.page;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.example.fiveyuanstore.AddProductActivity;
import com.example.fiveyuanstore.ChangeActivity;
import com.example.fiveyuanstore.CommentActivity;
import com.example.fiveyuanstore.GoodsContentActivity;
import com.example.fiveyuanstore.GoodsInfo;
import com.example.fiveyuanstore.OrderHandlerActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.StoreActivity;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SellerFragment extends Fragment {

	View view;
	View loadMore;
	List<Goods> data;
	EditText search_txt;
	String searchTxt;
	Integer page = 0;
	ListView listview;
	TextView txtLoadmore;
	Button addGoods, search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 商品列表
		if (view == null) {

			view = inflater.inflate(R.layout.fragment_seller, null);
			loadMore = inflater.inflate(R.layout.widget_load_root_more_btn, null);

			txtLoadmore = (TextView) loadMore.findViewById(R.id.more_text);
			addGoods = (Button) view.findViewById(R.id.addProduct);
			search = (Button) view.findViewById(R.id.search);
			search_txt = (EditText) view.findViewById(R.id.searchText);

			listview = (ListView) view.findViewById(R.id.list);
			listview.addFooterView(loadMore);
			listview.setAdapter(adapter);

			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);
				}
			});
			// 加载更多
			txtLoadmore.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					loadMore();
				}
			});

			search.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 搜索
					search();
				}
			});

			addGoods.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					addNewGoods();
					reload();
				}
			});
		}
		return view;
	}

	void onItemClicked(int position) {
		Goods goods = data.get(position);
		Intent itnt = new Intent(this.getActivity(), GoodsInfo.class);
		itnt.putExtra("data", (Serializable) goods);
		startActivity(itnt);
	}

	void loadMore() {
		Request request = Server.requestBuilderWithPath("/goods/" + (page + 1)).get().build();

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
					if (data.getNumber() > page) {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (SellerFragment.this.data == null) {
									SellerFragment.this.data = data.getContent();
								} else {
									SellerFragment.this.data.addAll(data.getContent());
								}
								SellerFragment.this.page = data.getNumber();
								adapter.notifyDataSetInvalidated();
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
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		reload();
	}

	void reload() {

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
							SellerFragment.this.page = data.getNumber();
							SellerFragment.this.data = data.getContent();
							adapter.notifyDataSetInvalidated();
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

	public void search() {
		searchTxt = search_txt.getText().toString();
		MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("text",
				searchTxt);

		RequestBody requestBody = body.build();
		Request request = Server.requestBuilderWithPath("/search").post(requestBody).build();

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
							SellerFragment.this.page = data.getNumber();
							SellerFragment.this.data = data.getContent();
							adapter.notifyDataSetInvalidated();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				Log.d("SellerFragment", e.getMessage());
			}
		});
	}

	protected void addNewGoods() {
		Intent itt = new Intent(getActivity(), AddProductActivity.class);
		startActivity(itt);
	}

	BaseAdapter adapter = new BaseAdapter() {

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

			Goods goods = data.get(position);

			textContent.setText("商品简介： " + goods.getText());
			goodsName.setText("商品名称： " + goods.getTitle());
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

	// 订单处理
	private void orderHandler() {
		Intent itt = new Intent(getActivity(), OrderHandlerActivity.class);
		startActivity(itt);
	}

	// 修改
	void change() {
		Intent itt = new Intent(getActivity(), ChangeActivity.class);
		startActivity(itt);
	}

	// 获得评论
	private void getComment() {
		Intent itt = new Intent(getActivity(), CommentActivity.class);
		startActivity(itt);
	}

	// 下架
	void down() {
		new AlertDialog.Builder(getActivity()).setMessage("确定要下架改商品吗？")
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getActivity(), "商品已下架", Toast.LENGTH_LONG).show();
					}
				}).show();
	}

}
