package com.example.fiveyuanstore.order;

import java.io.IOException;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.ZoneActivity;
import com.example.fiveyuanstore.R.id;
import com.example.fiveyuanstore.R.layout;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.MyOrder;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.example.fiveyuanstore.page.MyProfileFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderInfoActivity extends Activity {
	MyOrder order;
	User buyerUser;
	int position = 0;
	int state = 0;
	TextView buyerNameInUser;
	AvatarView avatar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_order_info);
		order = (MyOrder) getIntent().getSerializableExtra("orders");
		buyerUser=order.getUser();
		TextView sureSendGoods = (TextView) findViewById(R.id.sureSendGoods);
		TextView cancleOrder = (TextView) findViewById(R.id.cancleOrder);
		ProImgView proImg = (ProImgView) findViewById(R.id.proImg);
		TextView orderId = (TextView) findViewById(R.id.orderid);
		TextView goods_num = (TextView) findViewById(R.id.orderNum);
		TextView title = (TextView) findViewById(R.id.title);
		TextView date = (TextView) findViewById(R.id.date);
		TextView status = (TextView) findViewById(R.id.status);
		TextView name = (TextView) findViewById(R.id.text);
		TextView buyerName = (TextView) findViewById(R.id.name);
		TextView phone = (TextView) findViewById(R.id.phone);
		TextView adress = (TextView) findViewById(R.id.adress);
		buyerNameInUser=(TextView) findViewById(R.id.buyer_name);
		avatar=(AvatarView) findViewById(R.id.avatar);
		
		proImg.load(order.getGoods());
		orderId.setText("订单编号： " + order.getOrder_num());
		name.setText(" " + order.getGoods().getText());
		findViewById(R.id.btn_order_info_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		try {
			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", order.getGoods().getCreateDate()).toString();
			date.setText("" + dateStr);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		goods_num.setText("订单数量：" + order.getAmount());
		buyerName.setText("购买用户：" + order.getName());
		phone.setText("电话：" + order.getPhone());
		adress.setText("地址：" + order.getAddress());
		title.setText("商品名称：" + order.getGoods().getTitle());
		switch (order.getStatus()) {
		case 0:
			status.setText("订单状态： 确认收货");
			break;
		case 1:
			status.setText("订单状态： 已付款");
			break;

		case 2:
			status.setText("订单状态： 已发货");
			break;

		case 3:
			status.setText("订单状态： 已取消");
			break;
		default:
			status.setText("订单状态： 未知状态");
			break;

		}

		sureSendGoods.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 确认发货
				if (state != 0) {
					SendGoods();
					state = 1;
				}
			}
		});

		cancleOrder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取消订单
				if (state < 2) {
					cancleOrder();
					state = 2;
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		reload();
	}
	
	void reload(){
		OkHttpClient client = Server.getClient();
		Request request = Server.requestBuilderWithPath("/user/"+order.getBuyer_id()).get().build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, Response res) throws IOException {
				try {
					final User user = new ObjectMapper().readValue(res.body().string(),
							new TypeReference<User>() {
							});

						runOnUiThread(new Runnable() {

							public void run() {
								OrderInfoActivity.this.buyerUser=user;
								avatar.load(buyerUser);
								buyerNameInUser.setText(buyerUser.getUser_name());
								
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

	protected void cancleOrder() {

		if (order.getStatus() != 0 && order.getStatus() != 3) {

			String myOrderId = order.getOrder_num();
			// 取消订单
			RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					.addFormDataPart("order_id", myOrderId).build();
			Request request = Server.requestBuilderWithPath("/cancleOrder").post(body).build();

			Server.getClient().newCall(request).enqueue(new Callback() {

				@Override
				public void onResponse(Call arg0, Response res) throws IOException {
					try {

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(getApplication(), "取消成功", Toast.LENGTH_LONG).show();
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
							Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			});
		}
	}

	protected void SendGoods() {
		// 不在取消状态
		if (order.getStatus() != 3 && order.getStatus() != 2 && order.getStatus() != 0) {

			// 确认发货
			String myOrderId = order.getOrder_num();
			RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					.addFormDataPart("order_id", myOrderId).build();
			Request request = Server.requestBuilderWithPath("/sendGoods").post(body).build();

			Server.getClient().newCall(request).enqueue(new Callback() {

				@Override
				public void onResponse(Call arg0, Response res) throws IOException {
					try {

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(getApplication(), "发货成功", Toast.LENGTH_LONG).show();

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
							Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			});
		} else if (order.getStatus() == 2) {
			Toast.makeText(getApplication(), "你已发货~不可重复发货哦~", Toast.LENGTH_LONG).show();
		} else if (order.getStatus() == 0) {
			Toast.makeText(getApplication(), "订单已经完成啦~", Toast.LENGTH_LONG).show();
		}
	}

}
