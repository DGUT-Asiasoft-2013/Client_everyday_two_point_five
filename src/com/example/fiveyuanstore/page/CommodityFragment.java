package com.example.fiveyuanstore.page;

import com.example.fiveyuanstore.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CommodityFragment extends Fragment{
	
	View view;
	ListView listView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view==null){
			view=inflater.inflate(R.layout.fragment_page_commodity, null);
		}
		return view;
	}

}
