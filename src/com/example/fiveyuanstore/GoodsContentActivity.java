/**
 * 
 */
package com.example.fiveyuanstore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author 
 *卖家商品页面
 */

public class GoodsContentActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.goods_content);
		
		TextView name=(TextView) findViewById(R.id.username);//卖家名字/
		TextView title=(TextView) findViewById(R.id.title);//商品名
		TextView date=(TextView) findViewById(R.id.date);
		TextView content=(TextView) findViewById(R.id.text);
		//PictureView img=(PictureView) findViewById(R.id.img);//商品图片
		TextView money=(TextView) findViewById(R.id.money);
		ListView listView=(ListView) findViewById(R.id.goods_comment);
		
		name.setText("小二机长");
		title.setText("芝华仕头等舱 功能沙发 美式沙发真皮 小户型客厅沙发组合8753");		
		content.setText("芝华仕头等舱 功能沙发 美式沙发真皮 小户型客厅沙发组合8753");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		date.setText(str);
		money.setText("合计：9999元");
		
		findViewById(R.id.btn_buy).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itnt=new Intent(GoodsContentActivity.this,BuyActivity.class);
				startActivity(itnt);
				
			}
		});
	}

}
