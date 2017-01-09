package com.example.fiveyuanstore.goods;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.goods.GoodClassifyFragment.OnConfirmClickedListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class GoodFilterFragment extends Fragment {
	View view;
	EditText text1;
	EditText text2;
	float flo1 = 0;
	float flo2 =Float.MAX_VALUE;
	Boolean isConfirm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_goods_filter, null);
			text1 = (EditText) view.findViewById(R.id.editText1);
			text2 = (EditText) view.findViewById(R.id.editText2);
		}
		view.findViewById(R.id.layout_out).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(flo1!=0)
					text1.setText(String.valueOf(flo1));
				else
					text1.setText("");
				if(flo2!=Float.MAX_VALUE)
					text2.setText(String.valueOf(flo2));
				else
					text2.setText("");
				getActivity().onBackPressed();
			}
		});
		view.findViewById(R.id.layout_in).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		view.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float f1=0,f2=0;
				if(text1.getText().toString().equals("")||text1.getText()==null)
					f1=0;
					 
				else
					f1 = Float.parseFloat(text1.getText().toString());
				if(text2.getText().toString().equals("")||text2.getText()==null)
					f2=Float.MAX_VALUE;
				else
					
				f2 = Float.parseFloat(text2.getText().toString());
				if (f2 == 0) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getActivity(), "最高价不能为0哦", Toast.LENGTH_SHORT).show();

						}
					});
				} else if (f2 >= f1) {
					if (OnFilterConfirmClickedListener != null) {
						isConfirm = true;
						flo1 = f1;
						flo2 = f2;
						OnFilterConfirmClickedListener.onFilterConfirmClicked();
						getActivity().onBackPressed();
					}
				} else
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getActivity(), "输入的价格必须是从低到高噢~", Toast.LENGTH_SHORT).show();

						}
					});
			}

		});

		view.findViewById(R.id.btn_cancle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (OnFilterConfirmClickedListener != null) {
					isConfirm = false;
					flo1 =0;
					flo2=Float.MAX_VALUE;
					text1.setText("");
					text2.setText("");
					OnFilterConfirmClickedListener.onFilterConfirmClicked();
					getActivity().onBackPressed();
				}
			}
		});

		return view;
	}

	public boolean isConfirm() {
		return isConfirm;
	}

	public float getText1() {
		return flo1;
	}

	public float getText2() {
		return flo2;
	}

	public static interface OnFilterConfirmClickedListener {
		void onFilterConfirmClicked();
	}

	OnFilterConfirmClickedListener OnFilterConfirmClickedListener;

	public void setOnFilterConfirmClickedListener(OnFilterConfirmClickedListener onConfirmClickedListener) {
		this.OnFilterConfirmClickedListener = onConfirmClickedListener;
	}
}
