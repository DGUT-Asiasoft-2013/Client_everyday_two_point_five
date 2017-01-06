package com.example.fiveyuanstore.goods;

import java.io.IOException;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Comment;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class CommentInfoActivity extends Activity implements OnClickListener{
	public Button like;
	public Button down;
	public TextView count_num;

	public boolean isDowned = false, isLiked = false;
	public int downNum = 0, likeNum = 0;
	public Comment comment;
	public Integer com_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.widget_comment);
		comment = (Comment) getIntent().getSerializableExtra("data");
		com_id = (Integer) getIntent().getSerializableExtra("id");
		  Toast.makeText(this, "id is: "+com_id, Toast.LENGTH_LONG).show();
		  initData();
	}

	public void initData() {
		like = (Button) findViewById(R.id.comment_like);
		down = (Button) findViewById(R.id.comment_down);
		count_num = (TextView) findViewById(R.id.comment_count_num);
		
		like.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Likes();
				Toast.makeText(CommentInfoActivity.this, "点赞成功", Toast.LENGTH_LONG).show();
			}
		});
		down.setOnClickListener(this);
		
		TextView textComment = (TextView) findViewById(R.id.list_comment_text);
		TextView textAuthorName = (TextView) findViewById(R.id.username);
		TextView textDate = (TextView) findViewById(R.id.date);
		AvatarView avatar = (AvatarView) findViewById(R.id.avatar);

		
	
		textComment.setText(comment.getText());
		textAuthorName.setText(comment.getAuthor().getUser_name());
		avatar.load(comment.getAuthor());
		Log.d("com_id", ""+com_id);
		String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", comment.getCreateDate()).toString();
		textDate.setText(dateStr);		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.like:
				Likes();
				Toast.makeText(CommentInfoActivity.this, "点赞成功", Toast.LENGTH_LONG).show();
				break;
			case R.id.down:
				down();
				Toast.makeText(CommentInfoActivity.this, "踩贴成功", Toast.LENGTH_LONG).show();
				break;
		    default:
		    	break;
			}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reload();

	}
	void reload() {
		
		checkLiked();
		reloadLikes();
		reloadDowns();
		checkDowned();
		onCheckLikedResult(isLiked);
		onCheckDownedResult(isDowned);
		
		
	}
	

	//点赞
	void Likes() {
		MultipartBody body = new MultipartBody.Builder().addFormDataPart("likes", String.valueOf(!isLiked)).build();
		Log.d("com_id", ""+com_id);
		Request request = Server.requestBuilderWithPath("comment/" + com_id + "/likes").post(body).build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {

				try {
					/*<html>\n<head>\n<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>\n<title>Error 400 Bad Request</title>\n</head>\n<body><h2>HTTP ERROR 400</h2>\n<p>Problem accessing /storecenter/api/comment/null/likes. Reason:\n<pre>    Bad Request</pre></p><hr /><i><small>Powered by Jetty://</small></i><br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n<br/>                                                \n\n</body>\n</html>\n*/
					String responseString = arg1.body().string();
					final Integer count = new ObjectMapper().readValue(responseString, new TypeReference<Integer>(){});
					likeNum = count;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onReloadLikesResult(likeNum,downNum);
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
						Toast.makeText(CommentInfoActivity.this, "操作失败", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}
	
	
	//踩  
		private void down() {
		/*	 oooO ↘┏━┓ ↙ Oooo 
			 
		*/
			MultipartBody body = new MultipartBody.Builder().addFormDataPart("downs", String.valueOf(!isDowned)).build();

			Request request = Server.requestBuilderWithPath("comment/" + com_id + "/downs").post(body).build();

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
								onReloadLikesResult(likeNum,downNum);
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
							Toast.makeText(CommentInfoActivity.this, "操作失败", Toast.LENGTH_LONG).show();
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
				count_num.setText("-"+count);
			}
		}
		
		 
	    void checkLiked() {
			Request request = Server.requestBuilderWithPath("comment/" + com_id + "/isliked").get().build();
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
			Request request = Server.requestBuilderWithPath("comment/" + com_id + "/isDowned").get().build();
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
							onCheckLikedResult(false);
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
			Request request = Server.requestBuilderWithPath("comment/" + com_id + "/likes").get().build();
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
							Toast.makeText(CommentInfoActivity.this, "操作失败", Toast.LENGTH_LONG).show();
						}
					});
				}
			});
		}

		
		void reloadDowns() {
			Request request = Server.requestBuilderWithPath("comment/" + com_id + "/downs").get().build();
			Server.getClient().newCall(request).enqueue(new Callback() {

				@Override
				public void onResponse(Call arg0, Response arg1) throws IOException {
					try {
						String responseString = arg1.body().string();
						Integer count = new ObjectMapper().readValue(responseString, Integer.class);
						downNum = count;
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
							Toast.makeText(CommentInfoActivity.this, "操作失败", Toast.LENGTH_LONG).show();
						}
					});
				}
			});
			
		}
	    

}
