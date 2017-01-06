package com.example.fiveyuanstore.page.fragment;


import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.goods.AddProductActivity;
import com.example.fiveyuanstore.goodslist.AddGoodsListActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class SellerAddItemFragment extends Fragment {
	
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_seller_add_item, null);

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
			view.findViewById(R.id.add_goods).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent itt = new Intent(getActivity(), AddProductActivity.class);
					startActivity(itt);
					
					getActivity().onBackPressed();
				}
			});
			view.findViewById(R.id.add_goods_list).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent itnt = new Intent(getActivity(), AddGoodsListActivity.class);
					startActivity(itnt);
					getActivity().onBackPressed();
				}
			});
		}
		
		
		return view;
	}
	
	
	
	
	
	
	
	
	
	
	
	public static interface OnConfirmClickedListener {
		void onConfirmClicked();
	}

	OnConfirmClickedListener OnConfirmClickedListener;

	public void setOnConfirmClickedListener(OnConfirmClickedListener onConfirmClickedListener) {
		this.OnConfirmClickedListener= onConfirmClickedListener;
	}
}
