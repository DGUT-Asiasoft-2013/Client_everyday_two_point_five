package com.example.fiveyuanstore.goods;

import java.io.IOException;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Comment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.os.Bundle;
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
	private Button like;
	private Button down;
	private TextView count_num;

	private boolean isDowned = false, isLiked = false;
	private int downNum = 0, likeNum = 0;
	private Comment comment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.widget_comment);
		comment = (Comment) getIntent().getSerializableExtra("data");
		like = (Button) findViewById(R.id.like);
		down = (Button) findViewById(R.id.down);
		count_num = (TextView) findViewById(R.id.count_num);
		
		like.setOnClickListener(this);
		  down.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.like:
				Likes();
				break;
			case R.id.down:
				down();
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

		Request request = Server.requestBuilderWithPath("comment/" + comment.getId() + "/likes").post(body).build();

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

			Request request = Server.requestBuilderWithPath("comment/" + comment.getId() + "/downs").post(body).build();

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
			Request request = Server.requestBuilderWithPath("comment/" + comment.getId() + "/isliked").get().build();
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
			Request request = Server.requestBuilderWithPath("comment/" + comment.getId() + "/isDowned").get().build();
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
			Request request = Server.requestBuilderWithPath("comment/" + comment.getId() + "/likes").get().build();
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
			Request request = Server.requestBuilderWithPath("comment/" + comment.getId() + "/downs").get().build();
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
