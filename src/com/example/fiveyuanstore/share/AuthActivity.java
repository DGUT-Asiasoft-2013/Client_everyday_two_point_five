package com.example.fiveyuanstore.share;

import java.io.IOException;
import java.util.HashMap;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.Toast;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.WeiboActionListener;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.fiveyuanstore.LoginActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.StoreActivity;
import com.example.fiveyuanstore.api.MD5;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.goods.GoodsInfoActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.mm.sdk.platformtools.Log;

/**
 * @author yangyu
 *	授权登陆功能
 */
public class AuthActivity  extends FragmentActivity  implements Callback, OnClickListener, WeiboActionListener {
	//����CheckedTextView����
	private CheckedTextView	 sinaCt,qzoneCt,tengxunCt,renrenCt;
	
	private AbstractWeibo weibo;
	//����Handler����
	private Handler handler;

	//�������������
	private TitleLayout llTitle;
	//是否登录成功
	private boolean isLoginSucceed =false;
	//参数 用户账户
	String account, psw, name,avatar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);
		AbstractWeibo.initSDK(this);	
		initView();
		
		initData();
	}

	/**
	 * ��ʼ�����
	 */
	private void initView(){
		//ʵ����Handler����������Ϣ�ص������ӿ�
		handler = new Handler(this);

		//�õ�����������
		llTitle = (TitleLayout) findViewById(R.id.llTitle);		
		
		//�õ��������
		sinaCt    = (CheckedTextView)findViewById(R.id.ctvSw);
		qzoneCt   = (CheckedTextView)findViewById(R.id.ctvQz);
		tengxunCt = (CheckedTextView)findViewById(R.id.ctvTc);
		renrenCt  = (CheckedTextView)findViewById(R.id.ctvRr);		
	}
	
	/**
	 * ��ʼ������
	 */
	private void initData(){
		llTitle.getBtnBack().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		llTitle.getTvTitle().setText("授权");
		
		//���ü���
		sinaCt.setOnClickListener(this);
		qzoneCt.setOnClickListener(this);
		tengxunCt.setOnClickListener(this);
		renrenCt.setOnClickListener(this);

		  //获取平台列表  
        AbstractWeibo[] weibos = AbstractWeibo.getWeiboList(AuthActivity.this);  
        
		for(int i = 0;i < weibos.length;i++){
			if (!weibos[i].isValid()) {
				continue;
			}
			
			CheckedTextView ctv = getView(weibos[i]);
			if (ctv != null) {
				ctv.setChecked(true);
				// �õ���Ȩ�û����û�����
				String userName = weibos[i].getDb().get("nickname"); 
				if (userName == null || userName.length() <= 0 || "null".equals(userName)) {
					// ���ƽ̨�Ѿ���Ȩȴû���õ��ʺ����ƣ����Զ���ȡ�û����ϣ��Ի�ȡ����
					userName = getWeiboName(weibos[i]);
					//���ƽ̨�¼�����
					weibos[i].setWeiboActionListener(this);
					//显示用户信息
					weibos[i].showUser(null);
				}
				ctv.setText(userName);
				name = userName;
			
				
			}
		}
	}
	
	/**
	 * ��CheckedTextView�������ʾ��Ȩ�û�������
	 */
	private CheckedTextView getView(AbstractWeibo weibo) {
		if (weibo == null) {
			return null;
		}
		
		String name = weibo.getName();
		if (name == null) {
			return null;
		}
		
		View v = null;
		if (SinaWeibo.NAME.equals(name)) {
			v = findViewById(R.id.ctvSw);
		}
		else if (TencentWeibo.NAME.equals(name)) {
			v = findViewById(R.id.ctvTc);
		}		
		else if (Renren.NAME.equals(name)) {
			v = findViewById(R.id.ctvRr);
		}
		else if (QZone.NAME.equals(name)) {
			v = findViewById(R.id.ctvQz);
		}		
		
		if (v == null) {
			return null;
		}
		
		if (! (v instanceof CheckedTextView)) {
			return null;
		}
		
		return (CheckedTextView) v;
	}
	
	/**
	 * �õ���Ȩ�û����û�����
	 */
	private String getWeiboName(AbstractWeibo weibo) {
		if (weibo == null) {
			return null;
		}
		
		String name = weibo.getName();
		if (name == null) {
			return null;
		}
		
		int res = 0;
		if (SinaWeibo.NAME.equals(name)) {
			res = R.string.sinaweibo;
		}
		else if (TencentWeibo.NAME.equals(name)) {
			res = R.string.tencentweibo;
		}		
		else if (Renren.NAME.equals(name)) {
			res = R.string.renren;
		}
		else if (QZone.NAME.equals(name)) {
			res = R.string.qzone;
		}		
		
		if (res == 0) {
			return name;
		}		
		return this.getResources().getString(res);
	}
	
	/**
	 * CHECK btn click
	 */
	@Override
	public void onClick(View v) {				
		AbstractWeibo weibo = getWeibo(v.getId());
		
		CheckedTextView ctv = (CheckedTextView) v;
		if (weibo == null) {
			ctv.setChecked(false);
			ctv.setText(R.string.not_yet_authorized);
			return;
		}
		
		if (weibo.isValid()) {
			weibo.removeAccount();
			ctv.setChecked(false);
			ctv.setText(R.string.not_yet_authorized);
			return;
		}/*else{
			goLogin(account, psw);
		}*/
		
		weibo.setWeiboActionListener(this);
		weibo.showUser(null);		
	}

	/**
	 * �����Ȩ
	 */
	private AbstractWeibo getWeibo(int vid) {
		String name = null;
		switch (vid) {
		// ��������΢������Ȩҳ��
		case R.id.ctvSw:
			name = SinaWeibo.NAME;
			break;
		// ������Ѷ΢������Ȩҳ��
		case R.id.ctvTc:
			name = TencentWeibo.NAME;
			break;
		// ��������������Ȩҳ��
		case R.id.ctvRr:
			name = Renren.NAME;
			break;
		// ����QQ�ռ����Ȩҳ��
		case R.id.ctvQz:
			name = QZone.NAME;
			break;
		}
		
		if (name != null) {
			return AbstractWeibo.getWeibo(this, name);
		}
		return null;
	}		

	/**
	 * ��Ȩ�ɹ��Ļص�
	 *  weibo - �ص���ƽ̨
	 *	action - ����������
	 *	res - ���������ͨ��res����
	 */
	@Override
	public void onComplete(AbstractWeibo weibo, int action,HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);	
		account = ""+ weibo.getPlatformId();
		name = weibo.getDb().get("nickname");
		psw = account;
		psw = MD5.getMD5(psw);
		avatar = res.get("figureurl_qq_2").toString();
		//Log.d("avatar", avatar);
		//System.out.println("avatar: "+avatar);
		accessLogin();
		if(isLoginSucceed){
			Intent itt = new Intent(AuthActivity.this, StoreActivity.class);
			startActivity(itt);
		}else{
			Toast.makeText(AuthActivity.this, "授权失败， 请使用账户登录	!", Toast.LENGTH_SHORT).show();
			Intent itt = new Intent(AuthActivity.this, LoginActivity.class);
			startActivity(itt);
		}
		
	}

	/**
	 *  授权失败的回调 
	 */
	@Override
	public void onError(AbstractWeibo weibo, int action, Throwable t) {
		t.printStackTrace();
		
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);	
	}
	
	@Override
    protected void onDestroy() {  
        //结束ShareSDK的统计功能并释放资源  
        AbstractWeibo.stopSDK(this);  
        super.onDestroy();  
    }  
	
	/**
	 * cancel
	 */
	@Override
	public void onCancel(AbstractWeibo weibo, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);	
	}

	/**  
     * 处理从授权页面返回的结果 
     *  
     * 如果获取到用户的名称，则显示名称；否则如果已经授权，则显示平台名称 
     */  
	@Override
	public boolean handleMessage(Message msg) {
		 weibo = (AbstractWeibo) msg.obj;
		String text = GoodsInfoActivity.actionToString(msg.arg2);

		switch (msg.arg1) {
			case 1: { // success
				text = weibo.getName() + " completed at " + text;
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
				
			}
			break;
			case 2: { // failed
				text = weibo.getName() + " caught error at " + text;
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
				return false;
			}
			case 3: { // cancle
				text = weibo.getName() + " canceled at " + text;
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		CheckedTextView ctv = getView(weibo);
		if (ctv != null) {
			ctv.setChecked(true);
			String userName = weibo.getDb().get("nickname"); // getAuthedUserName();
			if (userName == null || userName.length() <= 0
					|| "null".equals(userName)) {
				userName = getWeiboName(weibo);
			}
			 name = userName;
			ctv.setText(userName);
		}
		return false;
	}

	private void accessLogin() {
		//授权登陆
		if (account.length()>0 && psw.length()>0 && avatar.length()>0){
			/*if (psw.length() <10){
				psw = MD5.getMD5(psw);
			}else{*/
				register(account, psw,avatar);
			
		
		}
		Request request = Server.requestBuilderWithPath("/getUser/"+account).get().build();
		
		Server.getClient().newCall(request).enqueue(new okhttp3.Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				try {
					final User msg = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<User>(){});
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(msg != null){
								if(account != null && psw != null ){
									goLogin(account, psw);
								}else{
									 String psw = MD5.getMD5(account);
									 
									goLogin(account, psw);
								}
							
							}else{//not register
								if(account != null && psw != null ){
								register(name, account, psw);
								}else{
									 String psw = account;
									register(name, account, psw);
								}
							}
						}

					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(AuthActivity.this, "error: "+arg1.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}
	
	
	
	void register( final String account, final String psw, final String avatar){
		MultipartBody.Builder builder = new MultipartBody.Builder()
				.addFormDataPart("name",name)
				.addFormDataPart("account",account)
				.addFormDataPart("passwordHash", psw)
				.addFormDataPart("avatar", avatar)
				;

		RequestBody requestBody = builder.build();

		OkHttpClient client = Server.getClient();

		Request request =Server.requestBuilderWithPath("/access_register")
				.post(requestBody)
				.build();
		
		client.newCall(request).enqueue(new okhttp3.Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				
				 final User user = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<User>(){});
				
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (user != null)
					Toast.makeText(AuthActivity.this, "授权成功", Toast.LENGTH_LONG).show();
					goLogin(account, psw);
				}
			});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(AuthActivity.this, "授权失败", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}


	private void goLogin(String account, String psw) {
		// TODO Auto-generated method stub
		
		MultipartBody.Builder builder = new MultipartBody.Builder()
				.addFormDataPart("account",account)
				.addFormDataPart("passwordHash", psw);

		RequestBody requestBody = builder.build();

		OkHttpClient client = Server.getClient();
		Request request =Server.requestBuilderWithPath("/login")
				.post(requestBody)
				.build();
		
		client.newCall(request).enqueue(new okhttp3.Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				
				 final User user = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<User>(){});
				
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
						isLoginSucceed = true;
						Intent itnt = new Intent(AuthActivity.this, StoreActivity.class);
						startActivity(itnt);
						finish();
					Toast.makeText(AuthActivity.this, "授权成功,welcome, "+user.getUser_name(), Toast.LENGTH_LONG).show();
					
				}
			});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(AuthActivity.this, "授权失败", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}
	
}
