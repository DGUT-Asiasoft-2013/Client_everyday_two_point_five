package com.example.fiveyuanstore.myProfiles.myData;

import com.example.fiveyuanstore.R;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class SetPlaceFragment extends Fragment{

	EditText editPlace;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_my_data_place, null);
			editPlace = (EditText) view.findViewById(R.id.edit_place);

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
					if (OnConfirmClickedListener2 != null) {
						OnConfirmClickedListener2.onConfirmClicked2();
						getActivity().onBackPressed();

					}
				}
			});

		}

		return view;
	}

	public String getText() {
		return editPlace.getText().toString();
	}

	public static interface OnConfirmClickedListener2 {
		void onConfirmClicked2();
	}

	OnConfirmClickedListener2 OnConfirmClickedListener2;

	public void setOnConfirmClickedListener2(OnConfirmClickedListener2 onConfirmClickedListener) {
		this.OnConfirmClickedListener2 = onConfirmClickedListener;
	}
	
	
}


