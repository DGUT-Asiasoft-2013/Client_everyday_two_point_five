package com.example.fiveyuanstore.fragment.list;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.fragment.list.TextListFragment.OnNewClickedListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PageCommodityClassifyFragment extends Fragment{


	ImageView image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_page_commodity_classify, container);

		image=(ImageView)view.findViewById(R.id.classify_ic);
		view.setOnClickListener(new OnClickListener() {			//点击事件
			
			@Override
			public void onClick(View v) {
				onClicked();
				
			}

		
		});
		return view;
		
		
	}

	
	public void setLabelImage(int resource) {
		image.setImageResource(resource);
	}
	
	public static interface OnNewClickedListener{
		void onNewClicked();
	}
	
	OnNewClickedListener onNewClickedListener;
	
	public void setOnNewClickedListener(OnNewClickedListener listener){
		this.onNewClickedListener = listener;
	}
	
	void onClicked() {
		if(onNewClickedListener!=null)
			onNewClickedListener.onNewClicked();
		
	}
	
}
