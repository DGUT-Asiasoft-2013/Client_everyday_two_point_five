package com.example.fiveyuanstore.goods;

import com.example.fiveyuanstore.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class GoodClassifyFragment extends Fragment {

	View view;
	String sort;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_goods_classify, null);

			view.findViewById(R.id.layout_out).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getActivity().onBackPressed();
				}
			});
			view.findViewById(R.id.layout_in).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});
			
			
			view.findViewById(R.id.all).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					sort="";
					if (OnConfirmClickedListener != null) {
						
						OnConfirmClickedListener.onConfirmClicked();
						getActivity().onBackPressed();
					}
				}
			});
			
			view.findViewById(R.id.clothing).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					sort="clothing";
					if (OnConfirmClickedListener != null) {
						
						OnConfirmClickedListener.onConfirmClicked();
						getActivity().onBackPressed();
					}
				}
			});
			view.findViewById(R.id.fruit).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					sort="fruit";
					if (OnConfirmClickedListener != null) {
						
						OnConfirmClickedListener.onConfirmClicked();
						getActivity().onBackPressed();
					}
				}
			});
			
			view.findViewById(R.id.sport).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					sort="sport";
					if (OnConfirmClickedListener != null) {
						
						OnConfirmClickedListener.onConfirmClicked();
						getActivity().onBackPressed();
					}
				}
			});
			
			view.findViewById(R.id.drink).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					sort="drink";
					if (OnConfirmClickedListener != null) {
						
						OnConfirmClickedListener.onConfirmClicked();
						getActivity().onBackPressed();
					}
				}
			});
			
			
			view.findViewById(R.id.snack).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					sort="snack";
					if (OnConfirmClickedListener != null) {
						
						OnConfirmClickedListener.onConfirmClicked();
						getActivity().onBackPressed();
					}
				}
			});

			view.findViewById(R.id.others).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					sort="others";
					if (OnConfirmClickedListener != null) {
						
						OnConfirmClickedListener.onConfirmClicked();
						getActivity().onBackPressed();
					}
				}
			});


		}
		return view;
	}
	
	public String getText(){
		return sort;
	}

	public static interface OnConfirmClickedListener {
		void onConfirmClicked();
	}

	OnConfirmClickedListener OnConfirmClickedListener;

	public void setOnConfirmClickedListener(OnConfirmClickedListener onConfirmClickedListener) {
		this.OnConfirmClickedListener = onConfirmClickedListener;
	}

}
