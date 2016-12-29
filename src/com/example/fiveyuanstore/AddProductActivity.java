package com.example.fiveyuanstore;

import com.example.fiveyuanstore.inputcells.PictureInputCellFragment;

import java.io.IOException;

import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddProductActivity extends Activity {
	SimpleTextInputCellFragment frag1, frag4;
	PictureInputCellFragment frag5;
	Button publish;
	EditText frag2,frag3;
    private Spinner spinner;
    private TextView view;
    private ArrayAdapter<CharSequence> adapter;
    String sort;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_product);
		/*名称 
		 *价格 
		 *库存
		 *描述
		 *图片*/
		
		frag1 =(SimpleTextInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag1);
		frag2 =(EditText) findViewById(R.id.frag2);
		frag3 =(EditText) findViewById(R.id.frag3);
		frag4 =(SimpleTextInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag4);
		frag5 =(PictureInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag5);
		
		publish = (Button) findViewById(R.id.publish);
		
		  spinner = (Spinner) findViewById(R.id.Spinner01);
	        //将可选内容与ArrayAdapter连接起来
	       // adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
		  adapter = ArrayAdapter
	                .createFromResource(this, R.array.sort,
	                        android.R.layout.simple_spinner_item);
	        //设置下拉列表的风格
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	         
	        //将adapter 添加到spinner中
	        spinner.setAdapter(adapter);
	         
	        //添加事件Spinner事件监听  
	        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
	         
	        //设置默认值
	        spinner.setVisibility(View.VISIBLE);
		
		publish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				publishGoods();
			}
		});
	}
	
	//使用数组形式操作
    class SpinnerSelectedListener implements OnItemSelectedListener{
 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            sort =  spinner.getSelectedItem().toString();
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
	
	protected void publishGoods() {
		String title = frag1.getText(),
				text = frag4.getText(),
				price = frag2.getText().toString(),
				goods_count = frag3.getText().toString();
		if( sort != null){
		switch(sort){
		case "服饰":
			sort = "clothing";
			break;
		case "水果":
			sort= "fruit";
			break;
		case "零食":
			sort = "snack";
			break;
		default:
			break;
		
		}}
		else{
			sort = "other";
		}
		
	  	 
	   	 MultipartBody.Builder builder = new MultipartBody.Builder()
	   		        .setType(MultipartBody.FORM)
	   		        .addFormDataPart("title",title)
	   		        .addFormDataPart("text",text)
	   		        .addFormDataPart("price",price)
	   		        .addFormDataPart("goods_count",goods_count)
	   		     .addFormDataPart("sort",sort);
        
      	 if(frag5.getPngData() != null){
      		 builder
      		 .addFormDataPart(
      				 "goods_img","goods_img",
      				 RequestBody
      				 .create(MediaType.parse("image/png"), 
      						 frag5.getPngData()));
      	 }
      	 
      	 
  	 OkHttpClient client = Server.getClient();
  	 
  	 Request request = Server.requestBuilderWithPath("/addGoods")
				.method("post", null)
				.post(builder.build())
				.build();
		
   	 
   	 final ProgressDialog progressD = new ProgressDialog(AddProductActivity.this);
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
						new AlertDialog.Builder(AddProductActivity.this)
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

		
	
		frag2.setHint("价格");

		
		frag3.setHint(" 库存 ");
		
		frag4.setLabelText("描述");
		frag4.setHintText("描述");
		frag4.setIsPsw(false);
		
		
		
	}
}
