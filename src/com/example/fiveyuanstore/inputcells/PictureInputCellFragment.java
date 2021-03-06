package com.example.fiveyuanstore.inputcells;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.example.fiveyuanstore.R;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PictureInputCellFragment extends Fragment{

	final int REQUESTCODE_CAMERA=1;
	final int REQUESTCODE_ALBUM=2;
	
	ImageView imageView;
	TextView labelText;
	TextView hintText;
	byte[] pngData;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view=inflater.inflate(R.layout.fragment_inputcell_picture, container);
		
		imageView=(ImageView) view.findViewById(R.id.image);
		labelText=(TextView) view.findViewById(R.id.label);
		hintText=(TextView) view.findViewById(R.id.hint);
		
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onImageViewClicked();
				
			}
		});
		
		return view;
	}
	
   void onImageViewClicked(){
	   
	   String[] items={"拍照","相册"};
	   
		new AlertDialog.Builder(getActivity())
		.setTitle(labelText.getText())
		.setItems(items,new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					takePhoto();
					break;
				case 1:
					pickFromAlbum();
					break;

				default:
					break;
				}				
			}
		})
		.setNegativeButton("取消", null)
		.show();
	}
	
   void takePhoto(){
	   Intent itnt=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	   startActivityForResult(itnt,REQUESTCODE_CAMERA);
   }
   
   void pickFromAlbum(){
	   Intent itnt=new Intent(Intent.ACTION_PICK);
	   itnt.setType("image/*");
	   startActivityForResult(itnt, REQUESTCODE_ALBUM);
   }
   
   
   void saveBitmap(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, baos);
		pngData = baos.toByteArray();
	}
   
   @Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	
	   if(resultCode ==Activity.RESULT_CANCELED)
		return;
	   
	   if(requestCode==REQUESTCODE_CAMERA){
		Bitmap bmp=(Bitmap) data.getExtras().get("data");
		saveBitmap(bmp);
			
		imageView.setImageBitmap(bmp);

	   }else if(requestCode==REQUESTCODE_ALBUM){
   
	try {
		Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
		imageView.setImageBitmap(bmp);
		saveBitmap(bmp);
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}	   
	   }
}
   
	public void setLabelText(String labelText) {
		this.labelText.setText(labelText);
	}
	
	public void setHintText(String hintText) {
		this.hintText.setHint(hintText);
		
	}
	public byte[] getPngData(){
		return pngData;
	}
	
	public static void resizeImage(String srcImgPath, String distImgPath,  
            int width, int height) throws IOException {  
//		param srcImgPath 原图片路径 
//	    param distImgPath  转换大小后图片路径 
//	    param width   转换后图片宽度 
//	    param height  转换后图片高度 
        File srcFile = new File(srcImgPath);  
        Image srcImg = ImageIO.read(srcFile);  
        BufferedImage buffImg = null;  
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        buffImg.getGraphics().drawImage(  
                srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,  
                0, null);  
  
        ImageIO.write(buffImg, "JPEG", new File(distImgPath));  
  
    }
}
