package com.example.fiveyuanstore;

import java.io.IOException;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.MyOrder;
import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderInfoActivity extends Activity {
	MyOrder order;
	int  position = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_order_info);
		order =  (MyOrder) getIntent().getSerializableExtra("orders");
		final Button sureSendGoods = (Button) findViewById(R.id.sureSendGoods);
		final Button cancleOrder = (Button) findViewById(R.id.cancleOrder);
		ProImgView proImg = (ProImgView) findViewById(R.id.proImg);
		TextView orderId = (TextView) findViewById(R.id.orderid);
		TextView goods_num =(TextView) findViewById(R.id.orderNum);
		TextView title = (TextView) findViewById(R.id.title);
		TextView date = (TextView) findViewById(R.id.date);
		TextView status = (TextView) findViewById(R.id.status);
		TextView name = (TextView) findViewById(R.id.text);
		proImg.load(order.getGoods());
		orderId.setText("订单编号： "+order.getOrder_num());
		name.setText(" "+order.getGoods().getText());

		try {
			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", order.getGoods().getCreateDate()).toString();
			date.setText(""+ dateStr);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		goods_num.setText("订单数量: "+ order.getAmount());

		title.setText("商品名称"+order.getGoods().getTitle());
		switch(order.getStatus()){
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
				SendGoods();
				sureSendGoods.setEnabled(false);
			}
		});

		cancleOrder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//取消订单
				cancleOrder();
				cancleOrder.setEnabled(false);
				sureSendGoods.setEnabled(false);
			}
		});
	}


	protected void cancleOrder() {
		String myOrderId = order.getOrder_num();
		// 取消订单
		RequestBody body = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("order_id",myOrderId)
				.build();
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
			public void onFailure(Call arg0,final IOException e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}



	protected void SendGoods() {
		//不在取消状态
		if(order.getStatus() != 3 && order.getStatus() != 2){
	
		//  确认发货
		String myOrderId = order.getOrder_num();
		RequestBody body = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("order_id",myOrderId)
				.build();
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
			public void onFailure(Call arg0,final IOException e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		}
		else if(order.getStatus() ==2){
			Toast.makeText(getApplication(), "你已发货~不可重复发货哦~", Toast.LENGTH_LONG).show();
		}
	}




}
