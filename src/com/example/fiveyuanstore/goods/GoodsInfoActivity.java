package com.example.fiveyuanstore.goods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.StoreActivity;
import com.example.fiveyuanstore.R.drawable;
import com.example.fiveyuanstore.R.id;
import com.example.fiveyuanstore.R.layout;
import com.example.fiveyuanstore.R.string;
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
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.onekeyshare.ShareAllGird;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class GoodsInfoActivity extends Activity implements OnClickListener{
	Goods goods;
	ListView listView;
	Button change, down, freshComment;
	TextView money,name,title,date,content;
	List<Comment> comments;
	ProImgView img;
	View btnLoadMore;
	protected Integer page = 0;
	TextView textLoadMore;
	Button shareGuiBtn;
	//定义图片存放的地址  
    public static String TEST_IMAGE;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_goods_info);
		//分享设置
		AbstractWeibo.initSDK(this);	
		initImagePath();      
		
		btnLoadMore = LayoutInflater.from(this).inflate(R.layout.widget_load_root_more_btn,null);
		textLoadMore = (TextView) btnLoadMore.findViewById(R.id.more_text);

		shareGuiBtn = (Button)findViewById(R.id.btnShareAllGui);
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

		initData();
		
	}
	
	  /** 
     * 初始化分享的图片 
     */  
    private void initImagePath() {  
        try {//判断SD卡中是否存在此文件夹  
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())  
                    && Environment.getExternalStorageDirectory().exists()) {  
                TEST_IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pic.png";  
            }  
            else {  
                TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath() + "/pic.png";  
            }  
            File file = new File(TEST_IMAGE);  
            //判断图片是否存此文件夹中  
            if (!file.exists()) {  
                file.createNewFile();  
                Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.pic);  
                FileOutputStream fos = new FileOutputStream(file);  
                pic.compress(CompressFormat.JPEG, 100, fos);  
                fos.flush();  
                fos.close();  
            }  
        } catch(Throwable t) {  
            t.printStackTrace();  
            TEST_IMAGE = null;  
        }  
    }  

	private void initData(){
		//chushihua
		shareGuiBtn.setOnClickListener(this);
		textLoadMore.setOnClickListener(this);

		change.setOnClickListener(this);

		down.setOnClickListener(this);

		freshComment.setOnClickListener(this);
	}
	

	
	@Override
	public void onClick(View v){
		switch(v.getId()){
		  case R.id.btnShareAllGui:  
	            showGrid(false);  
	            break;  
		  case R.id.more_text:
			  loadmore();
			  break;
		  case R.id.change:
			  change();
			  break;
		  case R.id.down:
			  down();
			  break;
		  case R.id.freshComment:
			  getComment();
			  break;
		default:
			break;
		}
	}
	
    /** 
     * 使用快捷分享完成图文分享 
     */  
    private void showGrid(boolean silent) {  
        Intent i = new Intent(this, ShareAllGird.class);  
        // 分享时Notification的图标  
        i.putExtra("notif_icon", R.drawable.ic_launcher);  
        // 分享时Notification的标题  
        i.putExtra("notif_title", this.getString(R.string.app_name));  
  
        // title标题，在印象笔记、邮箱、信息、微信（包括好友和朋友圈）、人人网和QQ空间使用，否则可以不提供  
        i.putExtra("title", this.getString(R.string.share));  
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用，否则可以不提供  
        i.putExtra("titleUrl", "http://sharesdk.cn");  
        // text是分享文本，所有平台都需要这个字段  
        i.putExtra("text", this.getString(R.string.share_content));  
        // imagePath是本地的图片路径，所有平台都支持这个字段，不提供，则表示不分享图片  
        i.putExtra("imagePath", GoodsInfoActivity.TEST_IMAGE);  
        // url仅在微信（包括好友和朋友圈）中使用，否则可以不提供  
        i.putExtra("url", "http://sharesdk.cn");  
        // thumbPath是缩略图的本地路径，仅在微信（包括好友和朋友圈）中使用，否则可以不提供  
        i.putExtra("thumbPath", GoodsInfoActivity.TEST_IMAGE);  
        // appPath是待分享应用程序的本地路劲，仅在微信（包括好友和朋友圈）中使用，否则可以不提供  
        i.putExtra("appPath", GoodsInfoActivity.TEST_IMAGE);  
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供  
        i.putExtra("comment", this.getString(R.string.share));  
        // site是分享此内容的网站名称，仅在QQ空间使用，否则可以不提供  
        i.putExtra("site", this.getString(R.string.app_name));  
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用，否则可以不提供  
        i.putExtra("siteUrl", "http://sharesdk.cn");  
  
        // 是否直接分享  
        i.putExtra("silent", silent);  
        this.startActivity(i);  
    }  
	
    
    /** 
     * 将action转换为String 
     */  
    public static String actionToString(int action) {  
        switch (action) {  
            case AbstractWeibo.ACTION_AUTHORIZING: return "ACTION_AUTHORIZING";  
            case AbstractWeibo.ACTION_GETTING_FRIEND_LIST: return "ACTION_GETTING_FRIEND_LIST";  
            case AbstractWeibo.ACTION_FOLLOWING_USER: return "ACTION_FOLLOWING_USER";  
            case AbstractWeibo.ACTION_SENDING_DIRECT_MESSAGE: return "ACTION_SENDING_DIRECT_MESSAGE";  
            case AbstractWeibo.ACTION_TIMELINE: return "ACTION_TIMELINE";  
            case AbstractWeibo.ACTION_USER_INFOR: return "ACTION_USER_INFOR";  
            case AbstractWeibo.ACTION_SHARE: return "ACTION_SHARE";  
            default: {  
                return "UNKNOWN";  
            }  
        }  
    }  
      
    protected void onDestroy() {  
        //结束ShareSDK的统计功能并释放资源  
        AbstractWeibo.stopSDK(this);  
        super.onDestroy();  
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
									GoodsInfoActivity.this.comments = comment.getContent();
								}else{

									comments.addAll(comment.getContent());
								}

								GoodsInfoActivity.this.page = comment.getNumber();
								btnLoadMore.setEnabled(true);
								textLoadMore.setText("显示更多");
								adapter.notifyDataSetInvalidated();

							}
						});
					}else{
						GoodsInfoActivity.this.comments = comment.getContent();
						GoodsInfoActivity.this.page = comment.getNumber();
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
