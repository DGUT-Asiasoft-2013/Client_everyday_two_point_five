package com.example.fiveyuanstore.inputcells;

import java.util.ArrayList;  
import java.util.List;  
import java.util.Timer;  
import java.util.TimerTask;

import com.example.fiveyuanstore.R;

import android.animation.ArgbEvaluator;  
import android.content.Context;  
import android.os.AsyncTask;  
import android.os.Handler;  
import android.os.Message;  
import android.support.v4.view.PagerAdapter;  
import android.support.v4.view.ViewPager;  
import android.util.AttributeSet;  
import android.util.Log;  
import android.util.TypedValue;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.ImageView;  
import android.widget.LinearLayout;  
import android.widget.RelativeLayout;  
import android.widget.TextView;  
  
  
    
    
  
public class ImageAutoScroll extends RelativeLayout{    
    private Context mContext;    
    private ViewPager mViewPager;    
    private LinearLayout mTabs;    
    private List<ImageView> imageViewsUser;    
    private List<ImageView> mImageViews;    
    private List<TextView> tabs;    
    private int size;    
    private int oldPostion;    
    private int currentPostion;    
    private boolean isStop;    
    private Handler handler;   
    ImageView imageFrist;  
    ImageView imageEnd;  
    int dotSize;    
    
    public ImageAutoScroll(Context context) {    
        super(context);    
    }    
    
    public ImageAutoScroll(Context context, AttributeSet attrs) {    
        super(context, attrs);    
        mContext = context;    
        initView();    
        initListening();    
    }    
    
    private void initView() {    
        dotSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,    
                mContext.getResources().getDisplayMetrics());    
        mViewPager = new ViewPager(mContext);    
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,    
                LayoutParams.MATCH_PARENT);    
        mViewPager.setLayoutParams(layoutParams);    
        mTabs = new LinearLayout(mContext);    
        layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);    
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);    
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);    
        layoutParams.bottomMargin = dotSize;    
        layoutParams.rightMargin = dotSize;    
        mTabs.setLayoutParams(layoutParams);    
        addView(mViewPager);    
        addView(mTabs);    
    
    }    
    
    public void setDate(List<ImageView> imageViews) {    
        this.imageViewsUser = imageViews;    
          
        if (imageViews == null || imageViews.size() < 1)  
            throw new IllegalArgumentException("参数为空或长度为零");  
          
        mImageViews = new ArrayList<ImageView>();    
        tabs = new ArrayList<TextView>();    
        size = imageViews.size();    
        handler = new Handler() {    
            @Override    
            public void handleMessage(Message msg) {    
                switch (msg.what) {    
                    case 0:    
                        mViewPager.setCurrentItem(size, false);    
                        break;    
                    case 1:    
                        mViewPager.setCurrentItem(1, false);    
                        break;  
                    case 888:  
                        imageFrist.setBackgroundDrawable(imageViewsUser.get(size - 1).getDrawable());  
                        imageFrist.invalidate();  
                        Log.i("hu", "888");  
                        if (imageFrist.getBackground() == null)  
                            handler.sendEmptyMessageDelayed(888, 1000);  
                        break;  
                    case 999:  
                        imageEnd.setBackgroundDrawable(imageViewsUser.get(0).getDrawable());   
                        Log.i("hu", "999");  
                        imageEnd.invalidate();  
                        if (imageEnd.getBackground() == null)  
                            handler.sendEmptyMessageDelayed(999, 1000);  
                        break;  
                }    
            }    
        };    
          
        if (size == 1) {  
            mImageViews.add(imageViews.get(0));  
            return;  
        }   
         
    
        for (int i = 0; i < size + 2; i++) {    
            if (i == 0) {    
                imageFrist = new ImageView(mContext);  
                if (imageViewsUser.get(size - 1).getBackground() != null) {  
                    imageFrist.setBackgroundDrawable(imageViewsUser.get(size - 1).getBackground());  
                } else {  
                    handler.sendEmptyMessageDelayed(888, 1000);  
                }  
                mImageViews.add(imageFrist);    
            } else if (i == size + 1) {    
                imageEnd = new ImageView(mContext);    
                if (imageViewsUser.get(0).getBackground() != null) {  
                    imageEnd.setBackgroundDrawable(imageViewsUser.get(0).getBackground());    
                } else {  
                    handler.sendEmptyMessageDelayed(999, 1000);  
                }  
                mImageViews.add(imageEnd);    
               
            } else {    
                mImageViews.add(imageViewsUser.get(i - 1));    
            }    
            mImageViews.get(i).setScaleType(ImageView.ScaleType.FIT_XY);    
        }    
    
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dotSize, dotSize);    
        layoutParams.leftMargin = dotSize/2;    
    
        for (int i = 0; i < size; i++) {    
            TextView textView = new TextView(mContext);    
            textView.setLayoutParams(layoutParams);    
            if (i == 0) {    
                textView.setBackgroundResource(R.drawable.btn_top_pressed);    
            } else {    
                textView.setBackgroundResource(R.drawable.dot_normal);    
            }    
            tabs.add(textView);    
            mTabs.addView(textView);    
        }    
    
        mViewPager.setAdapter(new MyAdapter());    
        if (size > 1) {    
            mViewPager.setCurrentItem(1);    
            oldPostion = 0;    
            new Timer().schedule(new TimerTask() {    
                @Override    
                public void run() {    
                    if (!isStop) {    
                        if (currentPostion + 1 > size + 1) {    
                            currentPostion = 1;    
                        }    
                        currentPostion++;    
                        handler.post(new Runnable() {    
                            @Override    
                            public void run() {    
                                new AutoScroll().execute(currentPostion);    
                            }    
                        });    
                    }    
                }    
            }, 3000, 3000);    
        }    
    }    
    
    //是否暂停轮播    
    public boolean isStop() {    
        return isStop;    
    }    
    
    public void setIsStop(boolean isStop) {    
        this.isStop = isStop;    
    }    
    
    private void initListening() {    
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());    
    }    
    
    class MyPageChangeListener implements ViewPager.OnPageChangeListener {    
    
        @Override    
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {    
    
        }    
    
        @Override    
        public void onPageSelected(int position) {    
            currentPostion = position;    
            tabs.get(oldPostion).setBackgroundResource(R.drawable.dot_normal);    
            if (position == 0) {    
                handler.sendEmptyMessageDelayed(0, 300);    
                tabs.get(size - 1).setBackgroundResource(R.drawable.btn_top_pressed);    
    
                oldPostion = size - 1;    
    
            } else if (position == size + 1) {    
                handler.sendEmptyMessageDelayed(1, 300);    
                tabs.get(0).setBackgroundResource(R.drawable.btn_top_pressed);    
                oldPostion = 0;    
            } else {    
                tabs.get(currentPostion - 1).setBackgroundResource(R.drawable.btn_top_pressed);    
                oldPostion = currentPostion - 1;    
            }    
    
    
        }    
    
        @Override    
        public void onPageScrollStateChanged(int state) {    
    
        }    
    }    
    
    
    
    class MyAdapter extends PagerAdapter {    
    
        @Override    
        public int getCount() {    
            return mImageViews.size();    
        }    
    
        @Override    
        public Object instantiateItem(ViewGroup container, int position) {    
            container.addView(mImageViews.get(position));    
            return mImageViews.get(position);    
        }    
    
        @Override    
        public boolean isViewFromObject(View view, Object object) {    
            return view == object;    
        }    
    
        @Override    
        public void destroyItem(ViewGroup container, int position, Object object) {    
            container.removeView(mImageViews.get(position));    
        }    
    }    
    
    class AutoScroll extends AsyncTask<Integer, Integer, Integer> {    
    
        @Override    
        protected Integer doInBackground(Integer... params) {    
            return params[0];    
        }    
    
        @Override    
        protected void onPostExecute(Integer integer) {    
            mViewPager.setCurrentItem(integer, true);    
        }    
    }    
    
} 