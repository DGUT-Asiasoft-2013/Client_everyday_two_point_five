package com.example.fiveyuanstore;

import java.io.IOException;
import java.util.List;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Comment;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.SaleItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class GoodsInfo extends Activity {
	SaleItem goods;
	View headerview;
	TextView title,text,price,payNumber;
	
	Button change, down,getComment;
	 ListView list;
	 ProImgView avatar;
	 TextView txtDate;
	 List<Comment> comments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_goods_info);
		
		 goods = (SaleItem)getIntent().getSerializableExtra("data");
		 headerview = LayoutInflater.from(this).inflate(R.layout.widget_change_item,null);
		  
		  title = (TextView) headerview.findViewById(R.id.title);
		  text = (TextView) headerview.findViewById(R.id.text);
		  price = (TextView) headerview.findViewById(R.id.price);
		  payNumber =  (TextView) headerview.findViewById(R.id.payNumber);
		  avatar = (ProImgView) headerview.findViewById(R.id.avatar);
		  list = (ListView) findViewById(R.id.list);
		  txtDate = (TextView) headerview.findViewById(R.id.date);
		  
		  title.setText(goods.getTitle());
		  text.setText(goods.getText());
		  price.setText(Float.toString(goods.getPrice()));
		  payNumber.setText(goods.getPayNumber());
		  String dateStr = DateFormat.format("yyy-MM-dd hh:mm", goods.getCreateDate()).toString();
		  txtDate.setText(dateStr);
		  
		  
		  avatar.load(goods.getGoods());
		  list.addHeaderView(headerview);
		  
		  change = (Button) headerview.findViewById(R.id.change);
		  down =  (Button) headerview.findViewById(R.id.down);
		  getComment =  (Button) headerview.findViewById(R.id.getComment);
		  
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
				// 刷新评论
				getComment();
			}
		});
	}
	
	protected void getComment() {
	reload();
	}

	protected void down() {
		//下架
Request request = Server.requestBuilderWithPath("/goods/"+ goods.getId()+"/deleteGoods").delete().build();
		
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "删除"+goods.getTitle()+"成功", Toast.LENGTH_LONG).show();
						
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0,final IOException e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						toAllGoods();
					}

					
				});
			}
		});
		
	}

	public void change() {
		Intent itt = new Intent(this, ChangeActivity.class);
		startActivity(itt);
	}
	
	 void toAllGoods() {
		Intent itt = new Intent(this, StoreActivity.class);
		startActivity(itt);
	}
	BaseAdapter adapter = new BaseAdapter(){
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if(view ==null){
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_comment_item, null);
			}
			
			Comment comment =  comments.get(position);
			TextView textContent =(TextView) view.findViewById(R.id.text);
			TextView textDate = (TextView) view.findViewById(R.id.date);
			ProImgView avatar = (ProImgView) view.findViewById(R.id.avatar);
		
			textContent.setText(comment.getContent());
			avatar.load(comment.getAuthor().getUser_name());
			
			
			String dateStr = DateFormat.format("yy-MM-dd hh:mm", comment.getCreateDate()).toString();
			textDate.setText(dateStr);
			
			return view;
		}
	
		@Override
		public int getCount() {
			return comments == null ? 0 :comments.size();
		}

		@Override
		public Object getItem(int position) {
			return comments.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	};
	
	@Override
	protected void onResume() {
		super.onResume();
		reload();
	}

	private void reload() {
		Request request = Server.requestBuilderWithPath("/goods/"+ goods.getId()+"/comments").get().build();
		
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplication(), "刷新列表成功", Toast.LENGTH_SHORT).show();					
					}
				});
				
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplication(), "刷新失败， "+arg1.toString(), Toast.LENGTH_SHORT).show();			
						
					}
				});
			}
		});
	}

}
