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
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.example.fiveyuanstore.inputcells.ChangePictureActivity;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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

	AvatarView avatar;
	byte[] pngData;

	User myuser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_my_data);
		final String myName = (String) getIntent().getSerializableExtra("name");
		final String myEmail = (String) getIntent().getSerializableExtra("email");
		myuser = (User) getIntent().getSerializableExtra("user");

		TextView account = (TextView) findViewById(R.id.my_account);
		TextView email = (TextView) findViewById(R.id.my_email);
		TextView name = (TextView) findViewById(R.id.my_name);
		avatar = (AvatarView) findViewById(R.id.avatar);

		account.setText(myuser.getAccount());
		email.setText(myuser.getEmail());
		name.setText(myuser.getUser_name());
		avatar.load(myuser);

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
			Intent itnt=new Intent(this,ChangePictureActivity.class);
			itnt.putExtra("uid", String.valueOf(myuser.getId()));
			itnt.putExtra("img", bmp);
			startActivity(itnt);
			
		} else if (requestCode == REQUESTCODE_ALBUM) {

			try {
				Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());				
				Intent itnt=new Intent(this,ChangePictureActivity.class);
				itnt.putExtra("uid", String.valueOf(myuser.getId()));
				itnt.putExtra("img", bmp);
				startActivity(itnt);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
