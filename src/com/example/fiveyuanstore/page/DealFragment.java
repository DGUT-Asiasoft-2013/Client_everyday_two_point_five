package com.example.fiveyuanstore.page;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.fiveyuanstore.R;

import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DealFragment extends Fragment {
	View view;
	ListView listView;

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
			
			if(convertView==null){
				LayoutInflater inflater=LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_buyer_order_item,null);
			}else{
				view=convertView;
			}
			
			TextView sellerName=(TextView)view.findViewById(R.id.seller_name);			//卖家名
			TextView statusText=(TextView)view.findViewById(R.id.status_text);			//订单状态
			TextView proName=(TextView)view.findViewById(R.id.pro_name);				//商品名称
			TextView createDate=(TextView)view.findViewById(R.id.create_date);			//购买时间
			TextView countText=(TextView)view.findViewById(R.id.count_text);			//金额
			Button btnStatusChanges=(Button)view.findViewById(R.id.btn_status_changes);	//状态更改按钮
			
			//从服务器获取信息
			
			sellerName.setText("小二机长");
			statusText.setText("正在派送中");
			proName.setText("芝华仕头等舱 功能沙发 美式沙发真皮 小户型客厅沙发组合8753 ");
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			createDate.setText(str);
			
			countText.setText("合计：9999元");
			btnStatusChanges.setText("确认收货");
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return 2;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}
	};
}
