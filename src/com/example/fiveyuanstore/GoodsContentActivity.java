/**
 * 
 */
package com.example.fiveyuanstore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Comment;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.example.fiveyuanstore.myProfiles.InboxChetActivity;
import com.example.fiveyuanstore.page.MyProfileFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 卖家商品页面
 */

public class GoodsContentActivity extends Activity {

	private Goods goods;
	List<Comment> comments;
	int page=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.goods_content);

		TextView name = (TextView) findViewById(R.id.username);// 卖家名字/
		TextView title = (TextView) findViewById(R.id.title);// 商品名
		TextView date = (TextView) findViewById(R.id.date);
		TextView content = (TextView) findViewById(R.id.text);
		ProImgView img = (ProImgView) findViewById(R.id.img);// 商品图片
		TextView money = (TextView) findViewById(R.id.money);
		ListView listView = (ListView) findViewById(R.id.goods_comment);

		listView.setAdapter(listAdapter);

		goods = (Goods) getIntent().getSerializableExtra("pos");

		title.setText(goods.getTitle() + "(库存：" + goods.getGoods_count() + ")");
		name.setText("卖家：" + goods.getSale_name());
		String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", goods.getCreateDate()).toString();
		date.setText(dateStr);
		img.load(goods);
		money.setText("$" + Float.toString(goods.getPrice()));
		content.setText("商品简介：" + goods.getText());

		findViewById(R.id.btn_buy).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itnt = new Intent(GoodsContentActivity.this, BuyActivity.class);
				itnt.putExtra("goods", goods);
				startActivity(itnt);

			}
		});

		findViewById(R.id.call).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toCall();

			}

		});
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_comment, null);
			} else {
				view = convertView;
			}

			TextView textComment = (TextView) view.findViewById(R.id.list_comment_text);
			TextView textAuthorName = (TextView) view.findViewById(R.id.username);
			TextView textDate = (TextView) view.findViewById(R.id.date);
			AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);

			Comment comment = comments.get(position);

			textComment.setText(comment.getText());
			textAuthorName.setText(comment.getAuthor().getUser_name());
			avatar.load(comment.getAuthor());

			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", comment.getCreateDate()).toString();
			textDate.setText(dateStr);

			return view;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return comments.get(position).getId();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return comments.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return comments == null ? 0 : comments.size();
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reload();
	}

	void reload() {

		Request request = Server.requestBuilderWithPath("/goods/" + goods.getId() + "/comments").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Comment> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Comment>>() {

							});
					runOnUiThread(new Runnable() {
						public void run() {
							GoodsContentActivity.this.reloadData(data);
						}
					});

				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							GoodsContentActivity.this.onFailure(e);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						GoodsContentActivity.this.onFailure(arg1);
					}
				});

			}
		});
	}

	protected void reloadData(Page<Comment> data) {
		page = data.getNumber();
		comments = data.getContent();
		listAdapter.notifyDataSetInvalidated();
	}

	void onFailure(Exception e) {
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
	}


	void toCall() {
		OkHttpClient client = Server.getClient();
		Request request = Server.requestBuilderWithPath("me")
				.method("GET", null)
				.build();
		
		final ProgressDialog progressD = new ProgressDialog(this);
		progressD.setCancelable(false);
		progressD.setTitle("提示");
		progressD.setMessage("请稍后");
		progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressD.setCanceledOnTouchOutside(false);
		progressD.show();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				progressD.dismiss();
				try {
					final User user = new ObjectMapper().readValue(arg1.body().bytes(), User.class);
					String myname = user.getUser_name();
					if (!myname.equals(goods.getSale_name())) {
						Intent itnts = new Intent(GoodsContentActivity.this, InboxChetActivity.class);
						itnts.putExtra("name", goods.getSale_name());
						startActivity(itnts);
					} else {
						GoodsContentActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								new AlertDialog.Builder(GoodsContentActivity.this)
								.setNegativeButton("OK", null)
								.setTitle("哎呀")
								.setMessage("不能给自己发私信哦")
								.show();
							}
						});
					}

				} catch (final Exception e) {
					e.printStackTrace();
					Log.d("AddProductActivity", arg1.toString());
					new AlertDialog.Builder(GoodsContentActivity.this)
					.setNegativeButton("OK", null)
					.setTitle("服务器好像出问题了_(:з」∠)_")
					.setMessage(e.getMessage())
					.show();
					
				}
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressD.dismiss();

						Log.d("AddProductActivity", arg1.toString());
						new AlertDialog.Builder(GoodsContentActivity.this)
						.setNegativeButton("OK", null)
						.setTitle("服务器好像出问题了额_(:з」∠)_")
						.setMessage(arg1.getMessage())
						.show();
						

					}
				});
				
			}
		});
	}
}
