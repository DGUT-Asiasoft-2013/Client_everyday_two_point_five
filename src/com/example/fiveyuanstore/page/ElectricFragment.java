package com.example.fiveyuanstore.page;

import com.example.fiveyuanstore.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ElectricFragment extends Fragment {
	View view = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view == null ){
			view  = inflater.inflate(R.layout.fragment_elec, null);
		}
		return view;
	}
}
