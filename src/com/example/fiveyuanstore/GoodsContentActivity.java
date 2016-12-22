/**
 * 
 */
package com.example.fiveyuanstore;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.fiveyuanstore.customViews.ProImgView;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.myProfiles.InboxChetActivity;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author 
 *卖家商品页面
 */

public class GoodsContentActivity extends Activity {
	
	private Goods goods;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.goods_content);
		
		TextView name=(TextView) findViewById(R.id.username);//卖家名字/
		TextView title=(TextView) findViewById(R.id.title);//商品名
		TextView date=(TextView) findViewById(R.id.date);
		TextView content=(TextView) findViewById(R.id.text);
		ProImgView img=(ProImgView) findViewById(R.id.img);//商品图片
		TextView money=(TextView) findViewById(R.id.money);
		ListView listView=(ListView) findViewById(R.id.goods_comment);
		
		goods=(Goods) getIntent().getSerializableExtra("pos");
		
		title.setText(goods.getTitle()+"(库存："+goods.getGoods_count()+")");
		name.setText("卖家："+goods.getSale_name());
		String dateStr = DateFormat.format("yyyy-MM-dd hh:mm",goods.getCreateDate()).toString();
		date.setText(dateStr);
		img.load(goods);
		money.setText("$"+Float.toString(goods.getPrice()));
		content.setText("商品简介："+goods.getText());
		
		findViewById(R.id.btn_buy).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itnt=new Intent(GoodsContentActivity.this,BuyActivity.class);
				itnt.putExtra("goods",goods);
				startActivity(itnt);
				
			}
		});
		
		findViewById(R.id.call).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itnt=new Intent(GoodsContentActivity.this,InboxChetActivity.class);
				itnt.putExtra("name",goods.getSale_name());
				startActivity(itnt);
				
			}
		});
	}

}
