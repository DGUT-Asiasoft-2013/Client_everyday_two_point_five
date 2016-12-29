package com.example.fiveyuanstore.fragment.list;

import com.example.fiveyuanstore.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TextListFragment extends Fragment {

	TextView label;
	ImageView image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_text_list, container);
		label = (TextView) view.findViewById(R.id.label);
		image=(ImageView)view.findViewById(R.id.ic);
		view.setOnClickListener(new OnClickListener() {			//点击事件
			
			@Override
			public void onClick(View v) {
				onClicked();
				
			}

		
		});
		return view;
		
		
	}

	public void setLabelText(String labelText) {
		label.setText(labelText);
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