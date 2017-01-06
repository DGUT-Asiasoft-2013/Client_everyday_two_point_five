package com.example.fiveyuanstore.myProfiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.entity.UserInformation;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.example.fiveyuanstore.inputcells.ChangePictureActivity;
import com.example.fiveyuanstore.myProfiles.myData.CustomDatePicker;
import com.example.fiveyuanstore.myProfiles.myData.SetBirthFragment;

import com.example.fiveyuanstore.myProfiles.myData.SetBirthFragment.OnConfirmClickedListener1;
import com.example.fiveyuanstore.myProfiles.myData.SetPlaceFragment;
import com.example.fiveyuanstore.myProfiles.myData.SetPlaceFragment.OnConfirmClickedListener2;
import com.example.fiveyuanstore.myProfiles.myData.SetSexActivtiy;
import com.example.fiveyuanstore.myProfiles.myData.SetWhatsUpFragment;
import com.example.fiveyuanstore.myProfiles.myData.SetWhatsUpFragment.OnConfirmClickedListener3;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
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
	private CustomDatePicker customDatePicker1, customDatePicker2;

	TextView account;
	TextView email;
	TextView name;
	TextView sex;
	TextView birth;
	TextView place;
	TextView whats_up;
	FragmentManager frg_mng1;
	FragmentManager frg_mng2;
	FragmentManager frg_mng3;
	FragmentTransaction ft1;
	FragmentTransaction ft2;
	FragmentTransaction ft3;
	SetBirthFragment birthFrag=new SetBirthFragment();
	SetPlaceFragment placeFrag=new SetPlaceFragment();
	SetWhatsUpFragment whatsupFrag=new SetWhatsUpFragment();

	DateFormat fmtDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
	Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);  
	
    //当点击DatePickerDialog控件的设置按钮时，调用该方法  
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()  
    {  
        @Override  
        public void onDateSet(DatePicker view, int year, int monthOfYear,  
                int dayOfMonth) {  
            //修改日历控件的年，月，日  
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致  
            dateAndTime.set(Calendar.YEAR, year);  
            dateAndTime.set(Calendar.MONTH, monthOfYear);  
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);      
            //将页面TextView的显示更新为最新时间  
            
            //upDateDate();     
            initDatePicker();
        }          
    };
    
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
		

		initDatePicker();
		
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
		findViewById(R.id.change_birth).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				goNext(1);
//				DatePickerDialog  dateDlg = new DatePickerDialog(MyDataActivity.this,  
//                        d,  
//                        dateAndTime.get(Calendar.YEAR),  
//                        dateAndTime.get(Calendar.MONTH),  
//                        dateAndTime.get(Calendar.DAY_OF_MONTH));  
//               
//                dateDlg.show();  
				customDatePicker1.show(birth.getText().toString());
			}
		});
		//upDateDate();
		
		birthFrag.setOnConfirmClickedListener1(new OnConfirmClickedListener1() {
			
			@Override
			public void onConfirmClicked1() {
				myInfor.setBirth(birthFrag.getText());
				changeInformation();
				
				
			}
		});
		findViewById(R.id.change_place).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext(2);
			}
		});
		placeFrag.setOnConfirmClickedListener2(new OnConfirmClickedListener2() {
			
			@Override
			public void onConfirmClicked2() {
				myInfor.setPlace(placeFrag.getText());
				changeInformation();	
			}
		});
		findViewById(R.id.change_whats_up).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext(3);
			}
		});
		whatsupFrag.setOnConfirmClickedListener3(new OnConfirmClickedListener3() {
			
			@Override
			public void onConfirmClicked3() {
				myInfor.setWhats_up(whatsupFrag.getText());
				changeInformation();
				
				
			}
		});
	}
	
	void goNext(int i){
		

		switch (i) {
		case 1:
			frg_mng1=getFragmentManager();
			ft1=frg_mng1.beginTransaction();
			ft1.setCustomAnimations(R.animator.slide_in_right,
					             R.animator.slide_out_left,
						         R.animator.slide_in_left,
				                 R.animator.slide_out_right);
			ft1.replace(R.id.container,birthFrag ).addToBackStack(null).commit();
			break;
		case 2:
			frg_mng2=getFragmentManager();
			ft2=frg_mng2.beginTransaction();
			ft2.setCustomAnimations(R.animator.slide_in_right,
					             R.animator.slide_out_left,
						         R.animator.slide_in_left,
				                 R.animator.slide_out_right);
			ft2.replace(R.id.container,placeFrag ).addToBackStack(null).commit();
			break;
		case 3:
			frg_mng3=getFragmentManager();
			ft3=frg_mng3.beginTransaction();
			ft3.setCustomAnimations(R.animator.slide_in_right,
					             R.animator.slide_out_left,
						         R.animator.slide_in_left,
				                 R.animator.slide_out_right);
			ft3.replace(R.id.container,whatsupFrag ).addToBackStack(null).commit();
			break;
		default:
			break;
		}
		
		
		
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
				myInfor.setSex("男");
  
			else if (resultCode == RESULT_FEMALE)
				myInfor.setSex("女");

			
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

	private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        birth.setText(now.split(" ")[0]);
       

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
            	birth.setText(time.split(" ")[0]);
            }
        }, "1900-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                
            }
        }, "1900-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }
    private void upDateDate() {  
    	birth.setText(fmtDate.format(dateAndTime.getTime()));  
        } 
    
}
