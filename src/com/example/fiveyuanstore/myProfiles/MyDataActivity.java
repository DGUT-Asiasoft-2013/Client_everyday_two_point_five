package com.example.fiveyuanstore.myProfiles;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.entity.GoodsListNoItem;
import com.example.fiveyuanstore.entity.Page;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.entity.UserInformation;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.example.fiveyuanstore.inputcells.ChangePictureActivity;
import com.example.fiveyuanstore.myProfiles.myData.SetSexActivtiy;
import com.example.fiveyuanstore.page.MyProfileFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.LongDeserializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
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

public class MyDataActivity extends Activity {

	final int REQUESTCODE_CAMERA = 1;
	final int REQUESTCODE_ALBUM = 2;
	
	final int REQUESTCODE_SEX=1001;
	final int RESULT_MALE=11;
	final int RESULT_FEMALE=12;

	AvatarView avatar;
	byte[] pngData;

	User myuser;
	UserInformation myInfor;

	TextView account;
	TextView email;
	TextView name;
	TextView sex;
	TextView birth;
	TextView place;
	TextView whats_up;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_my_data);
		final String myName = (String) getIntent().getSerializableExtra("name");
		final String myEmail = (String) getIntent().getSerializableExtra("email");


		account = (TextView) findViewById(R.id.my_account);
		email = (TextView) findViewById(R.id.my_email);
		name = (TextView) findViewById(R.id.my_name);
		avatar = (AvatarView) findViewById(R.id.avatar);
		sex = (TextView) findViewById(R.id.my_sex);
		birth = (TextView) findViewById(R.id.my_birth);
		place = (TextView) findViewById(R.id.my_place);
		whats_up = (TextView) findViewById(R.id.my_whats_up);
		

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// change Avatar
		findViewById(R.id.change_avatar).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTypeDialog();

			}
		});

		// change name
		findViewById(R.id.change_name).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itnt = new Intent(MyDataActivity.this, NameChangeActivity.class);
				itnt.putExtra("name", myName);
				startActivity(itnt);

			}
		});

		// change email
		findViewById(R.id.change_email).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itnt = new Intent(MyDataActivity.this, EmailChangeActivity.class);
				itnt.putExtra("email", myEmail);
				startActivity(itnt);

			}
		});
		findViewById(R.id.change_sex).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MyDataActivity.this, SetSexActivtiy.class);
				
				startActivityForResult(intent, REQUESTCODE_SEX);
				
			}


		});
	}

	private void showTypeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_Transparent));
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_select_photo, null);
		TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
		TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);

		// 相册
		tv_select_gallery.setOnClickListener(new OnClickListener() {// 在相册中选取
			@Override
			public void onClick(View v) {
				Intent itnt = new Intent(Intent.ACTION_PICK);
				itnt.setType("image/*");
				startActivityForResult(itnt, REQUESTCODE_ALBUM);
				dialog.dismiss();
			}
		});

		// 拍照
		tv_select_camera.setOnClickListener(new OnClickListener() {// 调用照相机
			@Override
			public void onClick(View v) {
				Intent itnt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(itnt, REQUESTCODE_CAMERA);
			}
		});
		dialog.setView(view);
		dialog.show();
		dialog.getWindow().setLayout(650, 450);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED)
			return;

		if (requestCode == REQUESTCODE_CAMERA) {
			Bitmap bmp = (Bitmap) data.getExtras().get("data");
			Intent itnt = new Intent(this, ChangePictureActivity.class);
			itnt.putExtra("uid", String.valueOf(myuser.getId()));
			itnt.putExtra("img", bmp);
			startActivity(itnt);

		} else if (requestCode == REQUESTCODE_ALBUM) {

			try {
				Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
				Intent itnt = new Intent(this, ChangePictureActivity.class);
				itnt.putExtra("uid", String.valueOf(myuser.getId()));
				itnt.putExtra("img", bmp);
				startActivity(itnt);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//sex
		if(requestCode == REQUESTCODE_SEX ){
			if(resultCode == RESULT_MALE)
				myInfor.setSex("male");
  
			else if (resultCode == RESULT_FEMALE)
				myInfor.setSex("female");

			
			changeInformation();
			
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		reload();
		
		
	}

	void reload() {

		Request request = Server.requestBuilderWithPath("me")
				.get()
				.build();
		
		Server.getClient().newCall(request).enqueue(new Callback(){

			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				try {
					final User user = new ObjectMapper().readValue(res.body().string(),
							new TypeReference<User>(){});

					runOnUiThread(new Runnable() {
						public void run() {
							MyDataActivity.this.myuser = user;
							account.setText(myuser.getAccount());
							email.setText(myuser.getEmail());
							name.setText(myuser.getUser_name());
							avatar.load(myuser);
							getInformation();
						}
					});

				} catch (final Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getApplication(), arg1.getMessage(), Toast.LENGTH_LONG).show();

					}
				});

			}
		});
	}
	
	void getInformation(){

		
		Request request = Server.requestBuilderWithPath("/getInformation/" + myuser.getId()).get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				try {
					final UserInformation infor = new ObjectMapper().readValue(res.body().string(),
							new TypeReference<UserInformation>(){});

					runOnUiThread(new Runnable() {
						public void run() {
							MyDataActivity.this.myInfor = infor;
							sex.setText(infor.getSex());
							if(infor.getBirth()==""||infor.getBirth()==null)
								birth.setHint("还没填写生日耶~");
							else
								birth.setText(infor.getBirth());
							if(infor.getPlace()==""||infor.getPlace()==null)
								place.setHint("还没填写地区耶~");
							else
								place.setText(infor.getPlace());
							if(infor.getWhats_up()==""||infor.getWhats_up()==null)
								whats_up.setHint("还没填写个性签名耶~");
							else
								whats_up.setText(infor.getWhats_up());
						}
					});

				} catch (final Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getApplication(), arg1.getMessage(), Toast.LENGTH_LONG).show();

					}
				});

			}
		});
	}


	void changeInformation(){
		MultipartBody.Builder builder=new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("sex", myInfor.getSex())
				.addFormDataPart("birth", myInfor.getBirth()==null? "":myInfor.getBirth())
				.addFormDataPart("place", myInfor.getPlace()==null? "":myInfor.getPlace())
				.addFormDataPart("whats_up", myInfor.getWhats_up()==null? "":myInfor.getWhats_up());

		RequestBody requestBody=builder.build();
		OkHttpClient client=Server.getClient();
		
		Request request=Server.requestBuilderWithPath("changeInformation")
				.method("POST", requestBody)
				.post(requestBody)
				.build();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						reload();
					}
				});
				
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), "修改失败",   
		                        Toast.LENGTH_SHORT).show(); 
					}
				});
	
				
			}
		});
	}

}
