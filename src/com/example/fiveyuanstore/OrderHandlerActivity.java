package com.example.fiveyuanstore;

import com.example.fiveyuanstore.customViews.ProImgView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OrderHandlerActivity extends Activity {
	TextView title,  goods_num,orderId;
	Button sureSendGoods,cancleOrder;
	ProImgView  proImg;
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 订单处理  LWJ
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_oder_handler);
		
		list = (ListView) findViewById(R.id.list);
		goods_num =(TextView) list.findViewById(R.id.orderNum);
		title = (TextView) list.findViewById(R.id.title);
		orderId = (TextView) list.findViewById(R.id.orderid);
		
		sureSendGoods = (Button) list.findViewById(R.id.sureSendGoods);
		cancleOrder = (Button) list.findViewById(R.id.cancleOrder);
		
		proImg = (ProImgView) list.findViewById(R.id.proImg);
	}
}
