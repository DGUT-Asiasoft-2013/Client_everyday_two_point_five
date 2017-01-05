package com.example.fiveyuanstore.share;

import java.util.HashMap;

import com.example.fiveyuanstore.api.JsonUtils;
import com.example.fiveyuanstore.goods.GoodsInfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.WeiboActionListener;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.R.id;
import com.example.fiveyuanstore.R.layout;
import com.example.fiveyuanstore.R.string;

/**
 * @author yangyu
 *
 */
public class GetInforActivity  extends Activity implements Callback, OnClickListener, WeiboActionListener {
	
	//������������ֶ���
	private TitleLayout llTitle;
	
	private Button sinaBt,renrenBt,qzoneBt,tengxunBt;
	
	private Handler handler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		handler = new Handler(this);
		
		setContentView(R.layout.activity_userinfo);
		AbstractWeibo.initSDK(this);				
		initView();
		
		initData();
					
	}
	
	/**
	 * ��ʼ�����
	 */
	private void initView(){
		//�õ�����������
		llTitle = (TitleLayout) findViewById(R.id.llTitle);

		//�õ���ť����
		sinaBt    = (Button) findViewById(R.id.btnSw);
		renrenBt  = (Button) findViewById(R.id.btnRr);
		qzoneBt   = (Button) findViewById(R.id.btnQz);
		tengxunBt = (Button) findViewById(R.id.btnTc);
		
		
	}
	
	/**
	 * ��ʼ������
	 */
	private void initData(){
		//���������÷��ذ�ť����
		llTitle.getBtnBack().setOnClickListener(this);
		//���ñ������ı����ı�
		llTitle.getTvTitle().setText(R.string.get_my_info);
		
		//���ü���
		sinaBt.setOnClickListener(this);
		renrenBt.setOnClickListener(this);
		qzoneBt.setOnClickListener(this);
		tengxunBt.setOnClickListener(this);
	}
	
	/**
	 * �����ť��ȡ��Ȩ�û�������
	 */
	@Override
	public void onClick(View v) {
		if (v.equals(llTitle.getBtnBack())) {
			finish();
			return;
		}
		
		String name = null;		
		
		switch (v.getId()) {
		case R.id.btnSw:
			name = SinaWeibo.NAME;
			break;
		case R.id.btnTc:
			name = TencentWeibo.NAME;
			break;
		case R.id.btnRr:
			name = Renren.NAME;
			break;
		case R.id.btnQz:
			name = QZone.NAME;
			break;
		}	
		
		if (name != null) {
			AbstractWeibo weibo = AbstractWeibo.getWeibo(this, name);
			weibo.setWeiboActionListener(this);
			String account = null;
			
			weibo.showUser(account);
		}
	}

	public void onComplete(AbstractWeibo weibo, int action,HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);
		
		Message msg2 = new Message();
		msg2.what = 1;
		JsonUtils ju = new JsonUtils();
		String json = ju.fromHashMap(res);
		msg2.obj = ju.format(json);
		handler.sendMessage(msg2);
	}

	public void onError(AbstractWeibo weibo, int action, Throwable t) {
		t.printStackTrace();
		
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);
	}

	public void onCancel(AbstractWeibo weibo, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);
	}

	/** chu li xing xi */
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case 1: {
				Intent i = new Intent(this, ShowInforActivity.class);
				i.putExtra("data", String.valueOf(msg.obj));
				startActivity(i);
			}
			break;
			default: {
				AbstractWeibo weibo = (AbstractWeibo) msg.obj;
				String text = GoodsInfoActivity.actionToString(msg.arg2);
				switch (msg.arg1) {
					case 1: { // �ɹ�
						text = weibo.getName() + " completed at " + text;
					}
					break;
					case 2: { // ʧ��
						text = weibo.getName() + " caught error at " + text;
					}
					break;
					case 3: { // ȡ��
						text = weibo.getName() + " canceled at " + text;
					}
					break;
				}
				
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return false;
	}

	
}
