/**
 * 
 */
package com.example.fiveyuanstore;

import com.example.fiveyuanstore.fragment.widgets.PictureView;

import android.app.Activity;
import android.os.Bundle;
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
		
		TextView name=(TextView) findViewById(R.id.username);//买家名字
		TextView title=(TextView) findViewById(R.id.title);//商品名
		TextView date=(TextView) findViewById(R.id.date);
		PictureView img=(PictureView) findViewById(R.id.img);//商品图片
		ListView listView=(ListView) findViewById(R.id.goods_comment);
	}

}
