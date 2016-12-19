package com.example.fiveyuanstore.fragment.widgets;

import java.io.IOException;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;


public class AvatarView extends View {				//头像处理

	public AvatarView(Context context) {
		super(context);
	}

	public AvatarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	Paint paint;
	float srcWidth, srcHeight;
	
	public void setBitmap(Bitmap bmp){					//确定头像内容
		if(bmp==null) {
			paint = new Paint();
			paint.setColor(Color.GRAY);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(1);
		    paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
			paint.setAntiAlias(true);
		}else{
			paint = new Paint();
			paint.setShader(new BitmapShader(bmp, TileMode.REPEAT, TileMode.REPEAT));
			paint.setAntiAlias(true);
			
			srcWidth = bmp.getWidth();
			srcHeight = bmp.getHeight();	
		}
		invalidate();
	}
																//从service获取头像数据
//	public void load(User user){				
//		load(Server.serverAddress + user.getAvatar());
//	}
	
	public void load(/*String url*/){
		
		setBitmap(null);
		
				
//		OkHttpClient client = Server.getSharedClient();
//		
//		Request request = new Request.Builder()
//				.url(url)
//				.method("get", null)
//				.build();
//		
//		client.newCall(request).enqueue(new Callback() {
//			
//			@Override
//			public void onResponse(Call arg0, Response arg1) throws IOException {
//				try{
//					byte[] bytes = arg1.body().bytes();
//					final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//					mainThreadHandler.post(new Runnable() {
//						public void run() {
//							setBitmap(bmp);
//						}
//					});
//				}catch(Exception ex){
//					mainThreadHandler.post(new Runnable() {
//						public void run() {
//							setBitmap(null);
//						}
//					});
//				}
//			}
//			
//			@Override
//			public void onFailure(Call arg0, IOException arg1) {
//				mainThreadHandler.post(new Runnable() {
//					public void run() {
//						setBitmap(null);
//					}
//				});
//			}
//		});
	}

	@Override
	public void draw(Canvas canvas) {					//头像外框
		super.draw(canvas);
		if(paint!=null){
			canvas.save();
			
			float dstWidth = getWidth();
			float dstHeight = getHeight();
			
			float scaleX = srcWidth / dstWidth;
			float scaleY = srcHeight / dstHeight;

			canvas.scale(1/scaleX, 1/scaleY);

			canvas.drawCircle(srcWidth/2, srcHeight/2, Math.min(srcWidth, srcHeight)/2, paint);
			
			canvas.restore();
		}
		
	}
}
