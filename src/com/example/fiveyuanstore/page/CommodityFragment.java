package com.example.fiveyuanstore.page;

import java.io.IOException;
import java.util.List;

import com.example.fiveyuanstore.GoodsActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.GoodsListNoItem;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.fragment.list.PageCommodityClassifyFragment;
import com.example.fiveyuanstore.fragment.list.PageCommodityClassifyFragment.OnNewClickedListener;
import com.example.fiveyuanstore.goodslist.GoodsListActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import okhttp3.Request;
import okhttp3.Response;
import android.widget.ListView;
import android.widget.TextView;

public class CommodityFragment extends Fragment {

	View view;
	ListView listView;
	PageCommodityClassifyFragment snack, clothing, fruit, others;

	EditText search_text;
	List<GoodsListNoItem> data;
	int page = 0;
	View headerView;

	ImageView search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_commodity, null);
			headerView = inflater.inflate(R.layout.comm_header_view, null);

			search_text = (EditText) headerView.findViewById(R.id.search_text);
			listView = (ListView) view.findViewById(R.id.goods_list);

			search = (ImageView) headerView.findViewById(R.id.btn_search);
			fruit = (PageCommodityClassifyFragment) getFragmentManager().findFragmentById(R.id.fruit);
			snack = (PageCommodityClassifyFragment) getFragmentManager().findFragmentById(R.id.snack);
			clothing = (PageCommodityClassifyFragment) getFragmentManager().findFragmentById(R.id.clothing);
			others = (PageCommodityClassifyFragment) getFragmentManager().findFragmentById(R.id.others);

			fruit.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent intent = new Intent(getActivity(), GoodsActivity.class);
					intent.putExtra("type", "sort");
					intent.putExtra("sort", "fruit");
					startActivity(intent);
				}
			});

			snack.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent intent = new Intent(getActivity(), GoodsActivity.class);
					intent.putExtra("type", "sort");
					intent.putExtra("sort", "snack");
					startActivity(intent);
				}
			});

			clothing.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent intent = new Intent(getActivity(), GoodsActivity.class);
					intent.putExtra("type", "sort");
					intent.putExtra("sort", "clothing");
					startActivity(intent);
				}
			});
			others.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent intent = new Intent(getActivity(), GoodsActivity.class);
					intent.putExtra("type", "sort");
					intent.putExtra("sort", "other");
					startActivity(intent);
				}
			});

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);
				}
			});

			search.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), GoodsActivity.class);
					intent.putExtra("type", "searchText");
					intent.putExtra("searchText", search_text.getText().toString());
					startActivity(intent);

				}
			});

			listView.addHeaderView(headerView);

			listView.setAdapter(goodsListAdapter);

		}

		return view;
	}

	void allgoodslist() {
		Request request = Server.requestBuilderWithPath("/allGoodsList").get().build();

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
							CommodityFragment.this.data = data.getContent();
							CommodityFragment.this.page = data.getNumber();
							goodsListAdapter.notifyDataSetChanged();
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
						new AlertDialog.Builder(getActivity()).setMessage(e.getMessage()).show();
					}
				});
			}
		});
	}

	BaseAdapter goodsListAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {

				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_goods_list_item, null);
			} else {
				view = convertView;
			}

			GoodsListNoItem goodslist = data.get(position);

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
			return data.get(position);
		}

		@Override
		public int getCount() {
			return data == null ? 0 : data.size();
		}
	};

	void onItemClicked(int position) {
		GoodsListNoItem pos = data.get(position - 1);

		Intent itnt = new Intent(this.getActivity(), GoodsListActivity.class);
		itnt.putExtra("id", pos.getId());
		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();

		fruit.setLabelImage(R.drawable.ic_frult);
		snack.setLabelImage(R.drawable.ic_snacks);
		clothing.setLabelImage(R.drawable.ic_clothes);
		
		allgoodslist();

	}

}