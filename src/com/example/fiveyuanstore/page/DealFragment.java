package com.example.fiveyuanstore.page;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.MyOrder;
import com.example.fiveyuanstore.entity.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DealFragment extends Fragment {
	View view;
	ListView listView;
	
	List<MyOrder> data;
	int page = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view==null){
			view=inflater.inflate(R.layout.fragment_page_deal, null);
			listView=(ListView)view.findViewById(R.id.deal_list);
			listView.setAdapter(listAdapter);
		}
		
		
		
		return view;
	}
	
	BaseAdapter listAdapter=new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=null;
			
			if (convertView == null) {
				LayoutInflater inflater=LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_buyer_order_item,null);
			} else {
				view = convertView;
			}
			ProImgView proImg = (ProImgView) view.findViewById(R.id.proImg);
			TextView sellerName=(TextView)view.findViewById(R.id.seller_name);			//卖家名
			TextView statusText=(TextView)view.findViewById(R.id.status_text);			//订单状态
			TextView proName=(TextView)view.findViewById(R.id.pro_name);				//商品名称
			TextView createDate=(TextView)view.findViewById(R.id.create_date);			//购买时间
			TextView countText=(TextView)view.findViewById(R.id.count_text);			//金额
			Button btnStatusChanges=(Button)view.findViewById(R.id.btn_status_changes);	//状态更改按钮
			ProImgView image=(ProImgView) view.findViewById(R.id.proImg);
			
			//从服务器获取信息
			MyOrder myOrder=data.get(position);
			
			image.load(myOrder.getGoods());
			
			final int pos=position;
			//状态
			switch (myOrder.getStatus()) {
			case 1:
				statusText.setText("状态1:卖家已付款");
				btnStatusChanges.setText("等待确认");
				btnStatusChanges.setEnabled(false);
				break;
			case 2:
				statusText.setText("状态2:已发货");
				btnStatusChanges.setText("确认收货");
				btnStatusChanges.setEnabled(true);
				
				break;
			case 3:
				statusText.setText("状态3:已取消");
				btnStatusChanges.setText("交易关闭");
				btnStatusChanges.setEnabled(false);
				break;
			case 0:
				statusText.setText("状态0:确认收货");
				btnStatusChanges.setText("订单完成");
				btnStatusChanges.setEnabled(false);
				break;
			default:
				statusText.setText("状态未知");
				btnStatusChanges.setText("状态未知");
				btnStatusChanges.setEnabled(false);
				break;
			}
			btnStatusChanges.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					confirm(pos);
					
					
				}
			});
			try{
				proImg.load(myOrder.getGoods());
			}catch(Exception e){
				
			}
			
			//标题
			sellerName.setText(myOrder.getGoods().getSale_name());
			// 名称
			proName.setText(myOrder.getGoods().getTitle());
			// 时间
			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", myOrder.getCreateDate()).toString();
			createDate.setText(dateStr);
			// 金额
			int amount = myOrder.getAmount();
			float price = myOrder.getGoods().getPrice();
			countText.setText("合计：" + amount + "件*" + price + "元=" + amount * price + "元");

			
			
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reload();
	}

	void reload() {
		Request request = Server.requestBuilderWithPath("order").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<MyOrder> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<MyOrder>>() {
							});
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							listAdapter.notifyDataSetInvalidated();
							DealFragment.this.page = data.getNumber();
							DealFragment.this.data = data.getContent();
						}
					});
				} catch (final Exception e) {
					new AlertDialog.Builder(getActivity()).setMessage(e.getMessage()).show();

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

	protected void confirm(int position) {
		// 确认收货
		MyOrder myOrder = data.get(position);
		String myOrderId = myOrder.getOrder_num();
		RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("order_id", myOrderId).build();
		Request request = Server.requestBuilderWithPath("/confirmGoods").post(body).build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				try {

					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity().getApplication(), "你已确认收货", Toast.LENGTH_LONG).show();
							listAdapter.notifyDataSetChanged();
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
						Toast.makeText(getActivity().getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}

}
