package com.example.fiveyuanstore;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Comment;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
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
	Goods goods;
	ListView listView;
	Button change, down, freshComment;
	TextView money,name,title,date,content;
	List<Comment> comments;
	ProImgView img;
	View btnLoadMore;
	protected Integer page = 0;
	TextView textLoadMore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_goods_info);

		btnLoadMore = LayoutInflater.from(this).inflate(R.layout.widget_load_root_more_btn,null);
		textLoadMore = (TextView) btnLoadMore.findViewById(R.id.more_text);


		name=(TextView) findViewById(R.id.username);//卖家名字/
		title=(TextView) findViewById(R.id.title);//商品名
		date=(TextView) findViewById(R.id.date);
		content=(TextView) findViewById(R.id.text);
		img=(ProImgView) findViewById(R.id.img);//商品图片
		money=(TextView) findViewById(R.id.money);
		listView=(ListView) findViewById(R.id.goods_comment);

		goods=(Goods) getIntent().getSerializableExtra("data");

		title.setText(goods.getTitle()+"(库存："+goods.getGoods_count()+")");
		name.setText("卖家："+goods.getSale_name());
		String dateStr = DateFormat.format("yyyy-MM-dd hh:mm",goods.getCreateDate()).toString();
		date.setText(dateStr);
		img.load(goods);
		money.setText("$"+Float.toString(goods.getPrice()));
		content.setText("商品简介："+goods.getText());



		change = (Button) findViewById(R.id.change);
		down = (Button) findViewById(R.id.down);
		freshComment = (Button) findViewById(R.id.freshComment);
		listView.addFooterView(btnLoadMore);
		listView.setAdapter(adapter);

		textLoadMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//show more
				loadmore();
			}
		});

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

		freshComment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 刷新评论
				getComment();
			}
		});
	}

	void loadmore(){
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("载入中…");

		page++;
		reload(page, true);



	}



	protected void getComment() {
		reload(0, false);
	}

	protected void down() {
		// 下架
		Request request = Server.requestBuilderWithPath("/goods/" + goods.getId() + "/deleteGoods").delete().build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "删除" + goods.getTitle() + "成功", Toast.LENGTH_LONG)
						.show();

					}
				});
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
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
		itt.putExtra("data", (Serializable) goods);
		startActivity(itt);
	}

	void toAllGoods() {
		Intent itt = new Intent(this, StoreActivity.class);
		startActivity(itt);
	}

	BaseAdapter adapter = new BaseAdapter() {
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_comment_item, null);
			}

			Comment comment = comments.get(position);
			TextView textContent = (TextView) view.findViewById(R.id.text);
			TextView textDate = (TextView) view.findViewById(R.id.date);
			AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);
			TextView username = (TextView) view.findViewById(R.id.username);

			username.setText(comment.getAuthor().getUser_name());
			textContent.setText(comment.getText());
			avatar.load(comment.getAuthor());

			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", comment.getCreateDate()).toString();
			textDate.setText(dateStr);

			return view;
		}

		@Override
		public int getCount() {
			return comments == null ? 0 : comments.size();
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
		reload(0,false);
	}

	private void reload(final int page, final boolean isLoadmore) {
		Request request = Server.requestBuilderWithPath("goods/" + goods.getGoods_id() + "/comments/"+(page)).get().build();
		Log.d("goods.getGoods_id()", goods.getGoods_id());
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {

				try {
					final Page<Comment> comment = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Comment>>(){});
					if(comment.getNumber()>page && isLoadmore == true){
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplication(), "刷新列表成功", Toast.LENGTH_SHORT).show();
								if(comments==null){
									GoodsInfo.this.comments = comment.getContent();
								}else{

									comments.addAll(comment.getContent());
								}

								GoodsInfo.this.page = comment.getNumber();
								btnLoadMore.setEnabled(true);
								textLoadMore.setText("显示更多");
								adapter.notifyDataSetInvalidated();

							}
						});
					}else{
						GoodsInfo.this.comments = comment.getContent();
						GoodsInfo.this.page = comment.getNumber();
						adapter.notifyDataSetInvalidated();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
						Toast.makeText(getApplication(), "刷新失败， " + arg1.toString(), Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

}
