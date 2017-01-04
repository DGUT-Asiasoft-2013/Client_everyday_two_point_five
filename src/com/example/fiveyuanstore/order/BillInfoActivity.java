package com.example.fiveyuanstore.order;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.R.id;
import com.example.fiveyuanstore.R.layout;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.MyOrder;
import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

public class BillInfoActivity extends Activity {
	MyOrder order;
	int  position = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bill_info);
		order =  (MyOrder) getIntent().getSerializableExtra("orders");

		ProImgView proImg = (ProImgView) findViewById(R.id.proImg);
		TextView orderId = (TextView) findViewById(R.id.orderid);
		TextView goods_num =(TextView) findViewById(R.id.orderNum);
		TextView title = (TextView) findViewById(R.id.title);
		TextView date = (TextView) findViewById(R.id.date);
		TextView status = (TextView) findViewById(R.id.status);
		TextView name = (TextView) findViewById(R.id.text);
		TextView buyerName = (TextView) findViewById(R.id.name);
		TextView phone = (TextView) findViewById(R.id.phone);
		TextView adress = (TextView) findViewById(R.id.adress);
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
		buyerName.setText("购买用户： "+order.getName());
		phone.setText("电话： "+order.getPhone());
		adress.setText("地址： "+order.getAddress());
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


	}


	

}
