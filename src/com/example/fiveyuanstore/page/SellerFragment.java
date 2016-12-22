
package com.example.fiveyuanstore.page;

import java.io.IOException;
import java.util.List;

import com.example.fiveyuanstore.AddProductActivity;
import com.example.fiveyuanstore.ChangeActivity;
import com.example.fiveyuanstore.CommentActivity;
import com.example.fiveyuanstore.OrderHandlerActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.StoreActivity;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.CustomFAB;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.entity.SaleItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 商品列表
		if (view == null) {

			view = inflater.inflate(R.layout.fragment_seller, null);
			loadMore = inflater.inflate(R.layout.widget_load_root_more_btn, null);

			TextView txtLoadmore = (TextView) loadMore.findViewById(R.id.more_text);
			Button addGoods = (Button) view.findViewById(R.id.addProduct);
			CustomFAB order = (CustomFAB) view.findViewById(R.id.order);
			Button search = (Button) view.findViewById(R.id.search);
			search_txt = (EditText) view.findViewById(R.id.searchText);

			ListView listview = (ListView) view.findViewById(R.id.list);
			listview.addFooterView(loadMore);
			listview.setAdapter(adapter);

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
				}
			});

			order.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent itt = new Intent(getActivity(), OrderHandlerActivity.class);
					startActivity(itt);
				}
			});

		}
		return view;
	}

	void loadMore() {
		reload(page++);
	}

	@Override
	public void onResume() {
		super.onResume();
		reload(0);
		Toast.makeText(getActivity(), "searchTxt is: " + search_txt.getText().toString(), Toast.LENGTH_LONG).show();

	}

	void reload(int page) {
		Request request = Server.requestBuilderWithPath("/feeds/" + (page)).get().build();

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
		Request request = Server
				.requestBuilderWithPath("/search")
				.method("POST", requestBody)
				.post(requestBody)
				.build();

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
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_product_item, null);

			} else {
				view = convertView;
			}

			TextView txt_title = (TextView) view.findViewById(R.id.title);
			TextView price = (TextView) view.findViewById(R.id.price);
			ProImgView img = (ProImgView) view.findViewById(R.id.goods_img);

			Goods pro = data.get(position);
			img.load(pro);
			txt_title.setText(pro.getTitle());
			float val = pro.getPrice();
			String priceText = Float.toString(val);
			price.setText(priceText);

			return view;
		}

		@Override
		public int getCount() {
			return data == null ? 0 : data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
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
	// Intent 点击进入详情页面

	public void listviewClicked(int position) {
		Goods pro = data.get(position);
		Intent itt = new Intent(getActivity(), StoreActivity.class);
		itt.putExtra("data", pro);
		startActivity(itt);

	}

}
