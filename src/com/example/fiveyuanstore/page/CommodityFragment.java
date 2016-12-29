package com.example.fiveyuanstore.page;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.fiveyuanstore.AllGoodsActivity;
import com.example.fiveyuanstore.GoodsContentActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommodityFragment extends Fragment {

	View view;

	View btnLoadMore;
	TextView textLoadMore;
	
	ImageView btn1, btn2, btn3, btn4, btn5, btn6;
	
	FruitFragment fruit = new FruitFragment();
	SnackFragment snack = new SnackFragment();
	ClothingFragment clothing = new ClothingFragment();
	ElectricFragment elec = new ElectricFragment();
	ShoesBagFragment shoesbag = new ShoesBagFragment();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_commodity, null);

			btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
			
			
			btn1 = (ImageView) view.findViewById(R.id.btn1);
			btn2 = (ImageView) view.findViewById(R.id.btn2);
			btn3 = (ImageView) view.findViewById(R.id.btn3);
			btn4 = (ImageView) view.findViewById(R.id.btn4);
			btn5 = (ImageView) view.findViewById(R.id.btn5);
			btn6 = (ImageView) view.findViewById(R.id.btn6);
			
			
			btn1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					sort(1);
				}
			});
			btn2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					sort(2);
				}
			});
			btn3.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			sort(3);
		}
	});
			btn4.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			sort(4);
		}
	});
			btn5.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			sort(5);
		}
	});
			btn6.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			goAllGoods();
		}
	});
			
		

		}
		return view;
	}
	
	protected void goAllGoods() {
		Intent itt = new Intent(getActivity(), AllGoodsActivity.class);
		startActivity(itt);
	}

	void sort(int i){
		Fragment frag = null;
		
		switch(i){
		case 1:
				frag = snack;
			break;
		case 2:
			frag = fruit;
			break;
		case 3:
			frag = clothing;
			break;
			
		case 4:
			frag = elec;
			break;
			
		case 5:
				frag = shoesbag;
			break;
		
		default:
				break;
		}
		
		if (frag == null) return; //保护措施，当frag为空时，返回
		 
		 getFragmentManager()
		 .beginTransaction()
		 .replace(R.id.SortContnet, frag)
		 .commit();
		 

		//Toast.makeText(getActivity(), "敬請期待", Toast.LENGTH_LONG).show();
	}

/*	BaseAdapter listAdapter = new BaseAdapter() {

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
			ProImgView img = (ProImgView)view.findViewById(R.id.goods_img);

			Goods goods = data.get(position);

			textContent.setText(goods.getText());
			goodsName.setText(goods.getTitle());
			money.setText("$" + Float.toString(goods.getPrice()));

			img.load(goods);

			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", goods.getCreateDate()).toString();
			textDate.setText(dateStr);

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

	void onItemClicked(int position) {
		Goods pos = data.get(position);

		Intent itnt = new Intent(this.getActivity(), GoodsContentActivity.class);
		itnt.putExtra("pos", (Serializable) pos);
		startActivity(itnt);
	}*/

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	
	

	
}
