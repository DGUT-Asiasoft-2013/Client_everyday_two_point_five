package com.example.fiveyuanstore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.app.Fragment;
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

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author yangyu
 *	授权登陆功能
 */
public class AuthActivity  extends FragmentActivity  implements Callback, OnClickListener, WeiboActionListener {
	//����CheckedTextView����
	private CheckedTextView	 sinaCt,qzoneCt,tengxunCt,renrenCt;
	
	//����Handler����
	private Handler handler;

	//�������������
	private TitleLayout llTitle;

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
				String  randomNum = String.valueOf(new Random().nextInt(1000));
				if (userName == null || userName.length() <= 0 || "null".equals(userName)) {
					// ���ƽ̨�Ѿ���Ȩȴû���õ��ʺ����ƣ����Զ���ȡ�û����ϣ��Ի�ȡ����
					userName = getWeiboName(weibos[i]);
					//���ƽ̨�¼�����
					weibos[i].setWeiboActionListener(this);
					//��ʾ�û����ϣ�null��ʾ��ʾ�Լ�������
					weibos[i].showUser(null);
				}
				ctv.setText(userName);
				accessLogin(userName, randomNum);
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
	 * ��Ȩ��ȡ����Ȩ�İ�ť��������¼�
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
		}
		
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
		AbstractWeibo weibo = (AbstractWeibo) msg.obj;
		String text = GoodsInfoActivity.actionToString(msg.arg2);

		switch (msg.arg1) {
			case 1: { // success
				text = weibo.getName() + " completed at " + text;
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
				
				
				
				
				Intent itt = new Intent(AuthActivity.this, StoreActivity.class);
				startActivity(itt);
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
			ctv.setText(userName);
		}
		return false;
	}

	private void accessLogin(String name, String id) {
		//授权登陆

		MultipartBody.Builder builder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("user_name",name)
				.addFormDataPart("user_id",id);

		RequestBody requestBody = builder.build();

		OkHttpClient client = Server.getClient();


		Request request =Server.requestBuilderWithPath("access_login")
				.post(requestBody)
				.build();
		
		client.newCall(request).enqueue(new okhttp3.Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				
				 final User str = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<User>(){});
				
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (str != null)
					Toast.makeText(AuthActivity.this, "授权成功", Toast.LENGTH_LONG).show();
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
