package com.example.fiveyuanstore;

import java.io.IOException;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.inputcells.PictureInputCellFragment;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeActivity extends Activity {
	SimpleTextInputCellFragment frag1,frag2, frag3, frag4;
	PictureInputCellFragment frag5;
	Button publish;
	int pos=0;
	Goods goods;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_change);
		//获得页面传来的数据
		goods = (Goods) getIntent().getSerializableExtra("data");
		frag1 =(SimpleTextInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag1);
		frag2 =(SimpleTextInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag2);
		frag3 =(SimpleTextInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag3);
		frag4 =(SimpleTextInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag4);
		frag5 =(PictureInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag5);
		
		publish = (Button) findViewById(R.id.publish);
		
		publish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				publishGoods();
			}
		});
	}
	
	protected void publishGoods() {
		String title = frag1.getText(),
				text = frag4.getText(),
				price = frag2.getText(),
				goods_count = frag3.getText();
		
	  	 
	   	 MultipartBody.Builder builder = new MultipartBody.Builder()
	   		        .setType(MultipartBody.FORM)
	   		     .addFormDataPart("title",title)
	   		        .addFormDataPart("text",text)
	   		        .addFormDataPart("price",price)
	   		        .addFormDataPart("goods_count",goods_count);
        
      	 if(frag5.getPngData() != null){
      		 builder.addFormDataPart("goods_img","goods_img",
      				 RequestBody.create(MediaType.parse("image/png"), frag5.getPngData()));
      	 }
      	 
     RequestBody requestBody = builder.build();
  	 OkHttpClient client = Server.getClient();
  	 
  	 Request request = Server.requestBuilderWithPath("/goods/"+goods.getGoods_id()+"/changeGoods")
				.method("POST", requestBody)
				.post(requestBody)
				.build();
		
   	 
   	 final ProgressDialog progressD = new ProgressDialog(ChangeActivity.this);
   	 progressD.setCancelable(false);
   	 progressD.setTitle("提示");
   	 progressD.setMessage("请稍后");
   	 progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
   	 progressD.setCanceledOnTouchOutside(false);
   	 progressD.show();
   	 
   	 client.newCall(request).enqueue(new Callback() {
		
		@Override
		public void onResponse(Call arg0, final Response arg1) throws IOException {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					//add succeed
					  progressD.dismiss();
					  
						Toast.makeText(getApplication(), "添加成功", Toast.LENGTH_LONG).show();
						finish();
						
				}

				
			});
		}
		
		@Override
		public void onFailure(Call arg0, final IOException arg1) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					 progressD.dismiss();
					  
						Log.d("AddProductActivity", arg1.toString());
						new AlertDialog.Builder(ChangeActivity.this)
								.setNegativeButton("OK", null)
								.setTitle("添加失败")
								.setMessage(arg1.getMessage())
								.show();
						
				}
			});
			
		}
	});
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		
		frag1.setLabelText("名称");
		frag1.setHintText("名称");
		frag1.setIsPsw(false);

		
		frag2.setLabelText("价格");
		frag2.setHintText("价格");
		frag2.setIsPsw(false);
		
		frag3.setLabelText(" 库存 ");
		frag3.setHintText(" 库存 ");
		frag3.setIsPsw(false);
		
		frag4.setLabelText("描述");
		frag4.setHintText("描述");
		frag4.setIsPsw(false);
		
		
		
	}
	
}
