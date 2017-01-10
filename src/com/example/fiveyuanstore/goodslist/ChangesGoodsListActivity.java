package com.example.fiveyuanstore.goodslist;

import java.io.IOException;
import java.util.List;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.GoodsListNoItem;
import com.example.fiveyuanstore.entity.GoodsListWithItem;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.inputcells.PictureInputCellFragment;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

public class ChangesGoodsListActivity extends Activity {

	SimpleTextInputCellFragment edit_goods_list_name;
	SimpleTextInputCellFragment edit_goods_list_text;
	PictureInputCellFragment set_goods_list_image;
	Button btn_set_goods_list_item;
	TextView goods_list_item_text;
	Button btn_goods_list_confirm;
	List<Goods> data;
	String[] items;
	int[] int_items;
	boolean[] checkedItems;
	String good_list_item="";
	
	GoodsListWithItem goodsList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changes_goods_list);
		goodsList = (GoodsListWithItem) getIntent().getSerializableExtra("goodsList");
		edit_goods_list_name=(SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.intput_goods_list_name);
		edit_goods_list_text=(SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.intput_goods_list_text);
		set_goods_list_image=(PictureInputCellFragment)getFragmentManager().findFragmentById(R.id.set_goods_list_image);
		btn_set_goods_list_item=(Button)findViewById(R.id.btn_set_goods_list_item);
		goods_list_item_text=(TextView)findViewById(R.id.goods_list_item_text);
		btn_goods_list_confirm=(Button)findViewById(R.id.btn_goods_list_changes_confirm);
		edit_goods_list_name.setText(goodsList.getGoods_list_name());
		edit_goods_list_text.setText(goodsList.getGoods_list_text());
		findViewById(R.id.btn_changes_goods_list_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		btn_set_goods_list_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showList();
			}
		});
		btn_goods_list_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!edit_goods_list_name.getText().equals("")&&!edit_goods_list_text.getText().equals("")&&!good_list_item.equals(""))
					changes();
				else
					Toast.makeText(getApplication(), "以上填写不能为空哦", Toast.LENGTH_LONG).show();
				
			}
		});
		
		
	}
	
	void showList(){
		items = new String[data.size()];
		int_items = new int[data.size()];
		checkedItems = new boolean[data.size()];
		for (int i = 0; i < data.size(); i++) {
			items[i] ="商品: "+data.get(i).getTitle();
			int_items[i] = data.get(i).getId();
			checkedItems[i] = false;
		}
		final boolean[] save = checkedItems;
		AlertDialog . Builder builder = new AlertDialog.Builder(ChangesGoodsListActivity.this);
		builder.setTitle("请选择");
		builder.setMultiChoiceItems(items, checkedItems, new OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				checkedItems[which] = isChecked;

			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean isChoise=false;
				String str = "\n";
				good_list_item="";
				for (int i = 0; i < items.length; i++) {
					isChoise=true;
					if (checkedItems[i]){
						good_list_item = good_list_item + int_items[i] + "-";
						str+=items[i]+"\n";
					}
				}
				if(isChoise)
					goods_list_item_text.setText("你选择了：" + str);
				else
					goods_list_item_text.setText("请选择至少一件商品");

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				checkedItems = save;
			}
		});

		builder.show();
	}
	
	void changes() {
		String name=edit_goods_list_name.getText();
		String text=edit_goods_list_text.getText();
		String item=good_list_item;
		Log.d("123123", goodsList.getId().toString());
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("id", goodsList.getId().toString())
				.addFormDataPart("name", name)
				.addFormDataPart("text", text)
				.addFormDataPart("item", item);
		
		if (set_goods_list_image.getPngData() != null) {
			builder.addFormDataPart("goods_list_image", "goods_list_image",
					RequestBody.create(MediaType.parse("image/png"), set_goods_list_image.getPngData()));
		}
		OkHttpClient client = Server.getClient();

		Request request = Server.requestBuilderWithPath("/changesGoodsList").method("post", null).post(builder.build())
				.build();
		
		final ProgressDialog progressD = new ProgressDialog(ChangesGoodsListActivity.this);
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
	 					  progressD.dismiss();
	 					  
	 						Toast.makeText(getApplication(), "修改成功", Toast.LENGTH_LONG).show();
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
	 						new AlertDialog.Builder(ChangesGoodsListActivity.this)
	 								.setNegativeButton("OK", null)
	 								.setTitle("修改失败")
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
		edit_goods_list_name.setLabelText("清单名字");
		edit_goods_list_name.setHintText("请输入清单名字");
		edit_goods_list_text.setLabelText("清单详情");
		edit_goods_list_text.setHintText("请输入清单的详细介绍");
		getGoods();
	}

	void getGoods() {
		Request request = Server.requestBuilderWithPath("/goods").get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				try {
					final Page<Goods> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Goods>>() {
							});

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							ChangesGoodsListActivity.this.data = data.getContent();
							
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				Log.d("SellerFragment", e.getMessage());
			}
		});
		

		


	}
}
