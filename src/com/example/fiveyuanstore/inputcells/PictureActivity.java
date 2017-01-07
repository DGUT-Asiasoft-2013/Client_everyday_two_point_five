package com.example.fiveyuanstore.inputcells;

import android.app.Activity;
import android.content.Context;  
import android.os.Bundle;
import android.widget.ImageView;  
import java.util.ArrayList;

import com.example.fiveyuanstore.R;  
  
  
  
public class PictureActivity extends Activity {  
  
  
    private ArrayList<ImageView> imageViews;  
    private int[] ids = {R.drawable.image1, R.drawable.image1, R.drawable.image1};  
    private PictureActivity mContext;  
    private ImageAutoScroll imageAutoScroll;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.image_scroll);  
        initView();  
        initData();  
    }  
  
    private void initView() {  
        imageAutoScroll = (ImageAutoScroll) findViewById(R.id.my_image_auto_scroll);  
    }  
  
    private void initData() {  
        mContext = PictureActivity.this;  
        imageViews = new ArrayList<ImageView>();  
        for (int i = 0; i < ids.length; i++) {  
            ImageView imageView = new ImageView(mContext);  
            imageView.setBackgroundResource(ids[i]);  
            imageViews.add(imageView);  
        }  
        imageAutoScroll.setDate(imageViews);  
    }  
  
  
}