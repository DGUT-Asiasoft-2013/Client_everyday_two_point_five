/**
 * 
 */
package com.example.fiveyuanstore.goods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.ZoneActivity;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Comment;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.example.fiveyuanstore.myProfiles.InboxChetActivity;
import com.example.fiveyuanstore.share.SendToWXActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.onekeyshare.ShareAllGird;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 卖家商品页面
 */

public class GoodsContentActivity extends Activity implements OnClickListener{

	private Goods goods;
	List<Comment> comments;
	int page=0;
	private Button  btn_buy;
	private Button call;
	private boolean isLiked;
	//定义图片存放的地址  
    public static String TEST_IMAGE;  
    Button like, down;
    ProImgView img;
    TextView count_num;
	private boolean isDowned = false;
	private int downNum = 0, likeNum = 0;
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.goods_content);
		//分享设置
		AbstractWeibo.initSDK(this);	
		initImage();     
		
		TextView name = (TextView) findViewById(R.id.username);// 卖家名字/
		TextView title = (TextView) findViewById(R.id.title);// 商品名
		TextView date = (TextView) findViewById(R.id.date);
		TextView content = (TextView) findViewById(R.id.text);
		TextView count= (TextView) findViewById(R.id.count);
		img = (ProImgView) findViewById(R.id.img);// 商品图片
		TextView money = (TextView) findViewById(R.id.money);
		 listView = (ListView) findViewById(R.id.goods_comment);
		like = (Button) findViewById(R.id.like);
		down = (Button) findViewById(R.id.down);
		count_num = (TextView) findViewById(R.id.count_num);
	

		goods = (Goods) getIntent().getSerializableExtra("pos");
		 count.setText("剩余："+goods.getGoods_count()); 
		title.setText("            "+goods.getTitle());
		name.setText("卖家：" + goods.getSale_name());
		String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", goods.getCreateDate()).toString();
		date.setText(dateStr);
		img.load(goods);
		money.setText("$" + Float.toString(goods.getPrice()));
		content.setText("商品简介：\n         " + goods.getText());

		btn_buy = (Button) findViewById(R.id.btn_buy);
		call = (Button) findViewById(R.id.call);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onItemClicked(position);
			}
		});
		listView.setAdapter(listAdapter);
		
		initData();
		reloadLikes();
	}
	 private void setListViewHeight(ListView listView, BaseAdapter adapter,
			 int count) {
			 int totalHeight = 0;
			 for (int i = 0; i < count; i++) {
			 View listItem = adapter.getView(i, null, listView);
			 listItem.measure(0, 0);
			 totalHeight += listItem.getMeasuredHeight();
			 }
			 ViewGroup.LayoutParams params = listView.getLayoutParams();
			 params.height = totalHeight + (listView.getDividerHeight() * count);
			 listView.setLayoutParams(params);
			 }
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reload();
		
	}

	// 订单处理

	void onItemClicked(int position) {
		Comment comment = comments.get(position);
		Intent itnt = new Intent(this, CommentInfoActivity.class);
		itnt.putExtra("data", (Serializable) comment);
		itnt.putExtra("id", (Serializable) comment.getId());
		startActivity(itnt);
	}
	
	  private void initData() {
		  findViewById(R.id.btnShareAllGui).setOnClickListener(this);
		  btn_buy.setOnClickListener(this);
		  like.setOnClickListener(this);
		  down.setOnClickListener(this);
		  findViewById(R.id.send_wx).setOnClickListener(this);
		  call.setOnClickListener(this);
		  findViewById(R.id.zone).setOnClickListener(this);
		  findViewById(R.id.btn_goods_content_back).setOnClickListener(this);
	}

	  //点击事件
		@Override
		public void onClick(View v) {
			Intent itnt;
			switch(v.getId()){
			case R.id.btn_goods_content_back:
				 finish();
				break;
			case R.id.zone:
				 itnt = new Intent(GoodsContentActivity.this, ZoneActivity.class);
				itnt.putExtra("id", goods.getUser().getId());
				startActivity(itnt);
				break;
			case R.id.btn_buy:
				 itnt = new Intent(GoodsContentActivity.this, BuyActivity.class);
				itnt.putExtra("goods", goods);
				startActivity(itnt);
				break;
				
			case R.id.btnShareAllGui:  //分享事件
		        showGrid(false);  
		        break; 
			case R.id.call:
				toCall();
				break;
				
			case R.id.like:
				Likes();
				if (isLiked){
					Toast.makeText(GoodsContentActivity.this, "取消赞成功", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(GoodsContentActivity.this, "点赞成功", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.down:
				down();
				if( !isDowned){
					Toast.makeText(GoodsContentActivity.this, "踩贴成功", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(GoodsContentActivity.this, "取消踩贴成功", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.send_wx:
				Intent itt = new Intent(this, SendToWXActivity.class);
				itt.putExtra("goods", goods);
				startActivity(itt);
		    default:
		    	break;
			}
		
		}
	  



	/** 
     * 初始化分享的图片 
     */  
		 private void initImage() {  
		        try {// 判断SD卡中是否存在此文件夹  
		            if (Environment.MEDIA_MOUNTED.equals(Environment  
		                    .getExternalStorageState())  
		                    && Environment.getExternalStorageDirectory().exists()) {  
		                File baseFile = new File(  
		                        Environment.getExternalStorageDirectory(), "share");  
		                if (!baseFile.exists()) {  
		                    baseFile.mkdir();  
		                }  
		                TEST_IMAGE = baseFile.getAbsolutePath() + "/pic.png";  
		            } else {  
		                TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath()  
		                        + "/picture.png";  
		            }  
		            File file = new File(TEST_IMAGE);  
		            // 判断图片是否存此文件夹中  
		            if (!file.exists()) {  
		                file.createNewFile();  
		                Bitmap pic = BitmapFactory.decodeResource(getResources(),  
		                        R.drawable.pic);  
		                FileOutputStream fos = new FileOutputStream(file);  
		                pic.compress(CompressFormat.JPEG, 100, fos);  
		                fos.flush();  
		                fos.close();  
		            }  
		        } catch (Throwable t) {  
		            t.printStackTrace();  
		            TEST_IMAGE = null;  
		        }  
		    }  

	//评论列表
    
	BaseAdapter listAdapter = new BaseAdapter() {
		@SuppressLint("InflateParams") // 标注忽略指定的警告
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.comment_item, null);
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



	void reload() {
		//分享设置
		AbstractWeibo.initSDK(this);	
		initImage();  
		
		
		reloadLikes();
		reloadDowns();
		checkLiked();
		checkDowned();
		onCheckLikedResult(isLiked);
		onCheckDownedResult(isDowned);
		
		Request request = Server.requestBuilderWithPath("/goods/" + goods.getId() + "/comments").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Comment> data = new ObjectMapper().readValue(arg1.body().string(),
													new TypeReference<Page<Comment>>() {});
					runOnUiThread(new Runnable() {
						public void run() {
							GoodsContentActivity.this.reloadData(data);
							setListViewHeight(listView,listAdapter,listAdapter.getCount());
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
		GoodsContentActivity.this.page = data.getNumber();
		GoodsContentActivity.this.comments = data.getContent();
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
        i.putExtra("text", this.getString(R.string.share_content)+goods.getTitle());  
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
    
    void checkLiked() {
		Request request = Server.requestBuilderWithPath("goods/" + goods.getId() + "/isLiked").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final String responseString = arg1.body().string();
					Log.d("check liked", responseString);

					final Boolean result = new ObjectMapper().readValue(responseString, Boolean.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckLikedResult(result);
						}
					});
				} catch (final Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckLikedResult(false);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onCheckLikedResult(false);
					}
				});
			}
		});
	}

    void checkDowned() {
		Request request = Server.requestBuilderWithPath("goods/" + goods.getId() + "/isDowned").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final String responseString = arg1.body().string();
					Log.d("check liked", responseString);

					final Boolean result = new ObjectMapper().readValue(responseString, Boolean.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckDownedResult(result);
						}
					});
				} catch (final Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckDownedResult(false);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onCheckDownedResult(false);
					}
				});
			}
		});
	}
    
	void onCheckLikedResult(boolean result) {
		isLiked = result;
		like.setBackgroundResource((isLiked)? R.drawable.like_click: R.drawable.like);
	}

	void onCheckDownedResult(boolean result) {
		isDowned = result;
		down.setBackgroundResource((isDowned)? R.drawable.down_click: R.drawable.down);
	}
	
	void reloadLikes() {
		Request request = Server.requestBuilderWithPath("goods/" + goods.getId() + "/likes").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					String responseString = arg1.body().string();
					final Integer count = new ObjectMapper().readValue(responseString, Integer.class);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							onReloadLikesResult(count, downNum);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(GoodsContentActivity.this, "操作失败", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}

	
	void reloadDowns() {
		Request request = Server.requestBuilderWithPath("goods/" + goods.getId() + "/downs").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					String responseString = arg1.body().string();
					Integer count = new ObjectMapper().readValue(responseString, Integer.class);
					downNum = count;
					onReloadLikesResult(likeNum, count);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(GoodsContentActivity.this, "操作失败", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		
	}
	
	void onReloadLikesResult(int likeNum, int downNum) {
		int count = likeNum - downNum;
		if (count >= 0) {
			count_num.setText(""+ count );
		} else {
			count_num.setText("-"+Math.abs(count));
		}
		onCheckLikedResult(isLiked);
		onCheckDownedResult(isDowned);
	}
    
	//踩  
	private void down() {
	/*	 oooO ↘┏━┓ ↙ Oooo 
		 
	*/
		MultipartBody body = new MultipartBody.Builder().addFormDataPart("downs", String.valueOf(!isDowned)).build();

		Request request = Server.requestBuilderWithPath("goods/" + goods.getId() + "/downs").post(body).build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {

				try {
					String responseString = arg1.body().string();
					final Integer count = new ObjectMapper().readValue(responseString, new TypeReference<Integer>(){});
					downNum = count;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getLikeNum();
							onReloadLikesResult(likeNum,downNum);
							isDowned = true;
							isLiked = false;
							reload();
							
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(GoodsContentActivity.this, "操作失败", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		

		
	}
	
	//点赞
		void Likes() {
			MultipartBody body = new MultipartBody.Builder().addFormDataPart("likes", String.valueOf(!isLiked)).build();

			Request request = Server.requestBuilderWithPath("goods/" + goods.getId() + "/likes").post(body).build();

			Server.getClient().newCall(request).enqueue(new Callback() {

				@Override
				public void onResponse(Call arg0, final Response arg1) throws IOException {

					try {
						String responseString = arg1.body().string();
						final Integer count = new ObjectMapper().readValue(responseString, new TypeReference<Integer>(){});
						likeNum = count;
					
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								getDownNum();
						
								onReloadLikesResult(likeNum,downNum);
								isLiked = true;
								isDowned = false;
								reload();
							
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Call arg0, IOException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(GoodsContentActivity.this, "操作失败", Toast.LENGTH_LONG).show();
						}
					});
				}
			});
		}
		
		void getLikeNum(){
			Request request = Server.requestBuilderWithPath("goods/" + goods.getId() + "/getLikeNum").build();
			Server.getClient().newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, final Response arg1) throws IOException {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String responseString;
							try {
								responseString = arg1.body().string();
								final Integer count = new ObjectMapper().readValue(responseString, new TypeReference<Integer>(){});
								likeNum = count;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					});
					
				}
				
				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(GoodsContentActivity.this, "操作失败", Toast.LENGTH_LONG).show();
						}
					});
				}
			});
		}
		

		void getDownNum(){
			Request request = Server.requestBuilderWithPath("goods/" + goods.getId() + "/getDownNum").build();
			Server.getClient().newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, final Response arg1) throws IOException {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String responseString;
							try {
								responseString = arg1.body().string();
								final Integer count = new ObjectMapper().readValue(responseString, new TypeReference<Integer>(){});
								downNum = count;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					});
					
				}
				
				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(GoodsContentActivity.this, "操作失败", Toast.LENGTH_LONG).show();
						}
					});
				}
			});
		}
	
}
