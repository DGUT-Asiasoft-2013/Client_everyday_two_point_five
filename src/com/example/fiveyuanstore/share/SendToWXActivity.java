package com.example.fiveyuanstore.share;

import java.io.File;
import java.net.URL;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Constants;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.Goods;
import com.example.fiveyuanstore.uikit.CameraUtil;
import com.example.fiveyuanstore.uikit.MMAlert;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXEmojiObject;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.openapi.SendAuth;

public class SendToWXActivity extends Activity {

	private static final int THUMB_SIZE = 150;

	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	private IWXAPI api;
	
	private static final int MMAlertSelect1  =  0;
	private static final int MMAlertSelect2  =  1;
	private static final int MMAlertSelect3  =  2;
	public Goods goods;
	private CheckBox isTimelineCb;//是否分享到朋友圈
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		
		setContentView(R.layout.send_to_wx);
		goods = (Goods) getIntent().getSerializableExtra("goods");
		initView();
	}

	private void initView() {

		isTimelineCb = (CheckBox) findViewById(R.id.is_timeline_cb);
		isTimelineCb.setChecked(false);
		
		// send to weixin
		findViewById(R.id.send_text).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
								
				final EditText editor = new EditText(SendToWXActivity.this);
				editor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				editor.setText(R.string.send_text_default+ goods.getTitle()+"  "+Server.serverAddress+"api/goodsById/"+goods.getId());
								
				MMAlert.showAlert(SendToWXActivity.this, "分享商品", editor, getString(R.string.app_share), getString(R.string.app_cancel), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = editor.getText().toString();
						if (text == null || text.length() == 0) {
							return;
						}
						
						// ��ʼ��һ��WXTextObject����
						WXTextObject textObj = new WXTextObject();
						textObj.text = text;

						// ��WXTextObject�����ʼ��һ��WXMediaMessage����
						WXMediaMessage msg = new WXMediaMessage();
						msg.mediaObject = textObj;
						// �����ı����͵���Ϣʱ��title�ֶβ�������
						// msg.title = "Will be ignored";
						msg.description = text;

						// ����һ��Req
						SendMessageToWX.Req req = new SendMessageToWX.Req();
						req.transaction = buildTransaction("text"); // transaction�ֶ�����Ψһ��ʶһ������
						req.message = msg;
						req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
						
						// ����api�ӿڷ������ݵ�΢��
						api.sendReq(req);
						finish();
					}
				}, null);
			}
		});




		findViewById(R.id.send_webpage).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {				
				MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_webpage),
						SendToWXActivity.this.getResources().getStringArray(R.array.send_webpage_item),
						null, new MMAlert.OnAlertSelectId(){

					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						case MMAlertSelect1:
							WXWebpageObject webpage = new WXWebpageObject();
							webpage.webpageUrl = Server.serverAddress+"api/goodsById/"+goods.getId();
							WXMediaMessage msg = new WXMediaMessage(webpage);
							msg.title = "每天两块五超值商品 "+goods.getTitle();
							msg.description = "商品详情 "+goods.getText();
							Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);// goods.getGoods_img()
							msg.thumbData = Util.bmpToByteArray(thumb, true);
							
							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("webpage");
							req.message = msg;
							req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
							api.sendReq(req);
							
							finish();
							break;
						default:
							break;
						}
					}
				});
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case 0x101: {
			final WXAppExtendObject appdata = new WXAppExtendObject();
			final String path = CameraUtil.getResultPhotoPath(this, data, SDCARD_ROOT + "/tencent/");
			appdata.filePath = path;
			appdata.extInfo = "this is ext info";

			final WXMediaMessage msg = new WXMediaMessage();
			msg.setThumbImage(Util.extractThumbNail(path, 150, 150, true));
			msg.title = "this is title";
			msg.description = "this is description";
			msg.mediaObject = appdata;
			
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("appdata");
			req.message = msg;
			req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
			api.sendReq(req);
			
			finish();
			break;
		}
		default:
			break;
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}
