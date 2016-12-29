package com.example.fiveyuanstore.page;

import com.example.fiveyuanstore.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShoesBagFragment extends Fragment {
	View view = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view == null ){
			view  = inflater.inflate(R.layout.fragment_shoes_bag, null);
		}
		return view;
	}
}
