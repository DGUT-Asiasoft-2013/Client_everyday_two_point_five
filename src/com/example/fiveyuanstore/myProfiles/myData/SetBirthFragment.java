package com.example.fiveyuanstore.myProfiles.myData;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.fragment.PasswordRecoverStep1Fragment.OnGoNextListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SetBirthFragment extends Fragment {
	EditText editBirth;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_my_data_birth, null);
			editBirth = (EditText) view.findViewById(R.id.edit_birth);

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
					if (OnConfirmClickedListener1 != null) {
						OnConfirmClickedListener1.onConfirmClicked1();
						getActivity().onBackPressed();

					}
				}
			});

		}

		return view;
	}

	public String getText() {
		return editBirth.getText().toString();
	}

	public static interface OnConfirmClickedListener1 {
		void onConfirmClicked1();
	}

	OnConfirmClickedListener1 OnConfirmClickedListener1;

	public void setOnConfirmClickedListener1(OnConfirmClickedListener1 onConfirmClickedListener1) {
		this.OnConfirmClickedListener1 = onConfirmClickedListener1;
	}
	
	
}
