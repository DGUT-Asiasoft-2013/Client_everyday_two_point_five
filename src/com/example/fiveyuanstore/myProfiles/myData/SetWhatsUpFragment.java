package com.example.fiveyuanstore.myProfiles.myData;

import com.example.fiveyuanstore.R;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class SetWhatsUpFragment extends Fragment{

	EditText editwhatsup;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_my_data_whats_up, null);
			editwhatsup = (EditText) view.findViewById(R.id.edit_whats_up);

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
			view.findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (OnConfirmClickedListener3 != null) {
						OnConfirmClickedListener3.onConfirmClicked3();
						getActivity().onBackPressed();

					}
				}
			});

		}

		return view;
	}

	public String getText() {
		return editwhatsup.getText().toString();
	}

	public static interface OnConfirmClickedListener3 {
		void onConfirmClicked3();
	}

	OnConfirmClickedListener3 OnConfirmClickedListener3;

	public void setOnConfirmClickedListener3(OnConfirmClickedListener3 onConfirmClickedListener) {
		this.OnConfirmClickedListener3 = onConfirmClickedListener;
	}
	
	
}
