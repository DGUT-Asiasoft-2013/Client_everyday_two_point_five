package com.example.fiveyuanstore.page;

import com.example.fiveyuanstore.R;

import android.app.Fragment;
import  android.view.View ;
public class FruitFragment extends Fragment {
	View view;
	public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
		
		if (view == null){
			view = inflater.inflate(R.layout.fragment_fruit, null);
		}
		
		
		return view;
		
		
	};
}
