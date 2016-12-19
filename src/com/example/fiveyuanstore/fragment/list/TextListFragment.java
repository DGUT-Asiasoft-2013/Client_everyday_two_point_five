package com.example.fiveyuanstore.fragment.list;

import com.example.fiveyuanstore.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextListFragment extends Fragment {

	TextView label;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_text_list, container);
		label = (TextView) view.findViewById(R.id.label);
		return view;
	}



	public void setLabelText(String labelText) {
		label.setText(labelText);
	}
}