package com.example.fiveyuanstore.page;

import java.util.List;

import com.example.fiveyuanstore.AddProductActivity;
import com.example.fiveyuanstore.ChangeActivity;
import com.example.fiveyuanstore.CommentActivity;
import com.example.fiveyuanstore.OrderHandlerActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.StoreActivity;
import com.example.fiveyuanstore.customViews.CustomFAB;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.SaleItem;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SellerFragment extends Fragment {
	private CustomFAB order;
	View view;
	 View loadMore;
	 TextView txtLoadmore,price,payNumber; 
	 ListView listview;
	 Button change, down, getComment;
		 TextView txt_title;
		 List<Goods> data;
		 SaleItem si;
		 Goods goods;
		 Button addGoods;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 商品列表
		if(view == null){
			view = inflater.inflate(R.layout.fragment_seller, null);
			loadMore = inflater.inflate(R.layout.widget_load_root_more_btn, null);
			txtLoadmore =  (TextView) loadMore.findViewById(R.id.more_text);
			addGoods = (Button) view.findViewById(R.id.addProduct);
			order = (CustomFAB) view.findViewById(R.id.order);
			listview = (ListView) view.findViewById(R.id.list);
			listview.addFooterView(loadMore);
			listview.setAdapter(adapter);
			
			addGoods.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					addNewGoods();
				}
			});
			
			order.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent itt=  new Intent(getActivity(), OrderHandlerActivity.class);
					startActivity(itt);
				}
			});
			
		}
		return view;
	}
	
	
	protected void addNewGoods() {
		Intent itt = new Intent(getActivity(), AddProductActivity.class);
		startActivity(itt);
	}


	BaseAdapter adapter = new BaseAdapter(){



	

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			
			if(convertView == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_product_item, null);
				
			}
			else{
				view = convertView;
			}
			
			
			txt_title = (TextView) view.findViewById(R.id.title);
			price =(TextView) view.findViewById(R.id.price);
			payNumber  =(TextView) view.findViewById(R.id.payNumber);
			
			 change = (Button)  view.findViewById(R.id.change);
			 down = (Button)  view.findViewById(R.id.down);
			 getComment  = (Button)  view.findViewById(R.id.getComment);
			 
			 
			Goods pro = data.get(position);
			ProImgView avatar =(ProImgView) view.findViewById(R.id.avatar);
			txt_title.setText(pro.getTitle());
			price.setText( Float.toString(goods.getPrice()));
			payNumber.setText(goods.getPayNumber());
			
			change.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					change();
				}

			
			});
			
			down.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					down();
				}
			
			});
			
			getComment.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getComment();
				}

			});
			
		
			
			try {
				avatar.load(si.getGoods());
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("aaaavatar",e.toString());
			}
		
			return view;
		}

		@Override
		public int getCount() {
			return data == null ? 0 :data.size();
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
	//订单处理
	private void orderHandler() {
		Intent itt = new Intent(getActivity(), OrderHandlerActivity.class);
		startActivity(itt);
	}
	//修改
		 void change() {
			Intent itt = new Intent(getActivity(), ChangeActivity.class);
			startActivity(itt);
			
		}
	 //获得评论
		private void getComment() {
			Intent itt = new Intent(getActivity(), CommentActivity.class);
			startActivity(itt);
			
		}
	//下架
		 void down() {
			 new AlertDialog.Builder(getActivity()).setMessage("确定要下架改商品吗？")
			 	.setPositiveButton("取消",new DialogInterface.OnClickListener() {
				

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				})
		 		.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "商品已下架", Toast.LENGTH_LONG).show();
				}
			}).show();
		}
	//Intent 点击进入详情页面
	
	public void listviewClicked(int position) {
		Goods pro = data.get(position);
		Intent itt = new Intent(getActivity(), StoreActivity.class);
		itt.putExtra("data", pro);
	     startActivity(itt);
		
	}
	
	
	
}
