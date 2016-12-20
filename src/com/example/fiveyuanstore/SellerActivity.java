package com.example.fiveyuanstore;

import java.io.IOException;
import java.util.List;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.entity.SaleItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SellerActivity extends Activity {
	List<Goods> data;
	protected int page = 0;
	TextView title, payNumber,price;
	ProImgView avatar;
	EditText txt1;
	 Button search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_seller);
		txt1 =(EditText) findViewById(R.id.searchText);
		search = (Button)findViewById(R.id.search);
		title = (TextView) findViewById(R.id.title);
		payNumber = (TextView) findViewById(R.id.payNumber);
		price = (TextView) findViewById(R.id.price);
		
		avatar = (ProImgView) findViewById(R.id.avatar);
		
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				search();
			}
		});
	}
	

	BaseAdapter adapter = new BaseAdapter(){

		
		@Override
		public int getCount() {
			
			return data == null ? 0 :data.size();
		}

		@Override
		public Object getItem(int pos) {
			return data.get(pos);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@SuppressLint("InflateParams")
		@Override
		public View getView(int pos, View contentView, ViewGroup parent) {
			View view = null;
			
			if(contentView == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				
				view = inflater.inflate(R.layout.widget_product_item, null);
		}else{
				view  = contentView;
			}
			
			
			Goods goods = data.get(pos);
			SaleItem sale = new SaleItem();
			
			title.setText(goods.getTitle());
			price.setText(Float.toString(goods.getPrice()));
			payNumber.setText(goods.getPayNumber());
			
			avatar.load(sale.getGoods());
			
			
			return view;
		}
		
	};



	@Override
	public void onResume() {
		super.onResume();
		reload();
	}

	 void reload() {
		 Request request = Server.requestBuilderWithPath("/goods").build();
		 
		 Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						try {
							final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(),new TypeReference<Page<Goods>>(){});
							SellerActivity.this.page  = data.getNumber();
							SellerActivity.this.data = data.getContent();
							adapter.notifyDataSetInvalidated();
						} catch (IOException e) {
							e.printStackTrace();
							Log.d("SellerActivity", e.getMessage());
						}
						
						
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.d("SellerActivity", e.getMessage());
					}
				});
				
			}
		});
		
	}
	
		private void search() {
		
			 String searchText1 = txt1.getText().toString();
			 MultipartBody.Builder body = new MultipartBody.Builder()
					 .setType(MultipartBody.FORM)
					 .addFormDataPart("text", searchText1)
					 ;
			 RequestBody requestBody = body.build();
			 
			 Request request = Server.requestBuilderWithPath("/search")
					 .method("POST", requestBody)
					 .post(requestBody)
					 .build();
			 
			  
			  Server.getClient().newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, Response arg1) throws IOException {
					try {
						final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Goods>>(){});
					
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								SellerActivity.this.page   = data.getNumber();
								SellerActivity.this.data = data.getContent();
								adapter.notifyDataSetInvalidated();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("Fragment_Search_page", e.getMessage());
					}
				
				
				}
				
				@Override
				public void onFailure(Call arg0, IOException e) {
					Log.d("Fragment_Search_page", e.getMessage());
					
				}
			});
		}
		 
		
}
