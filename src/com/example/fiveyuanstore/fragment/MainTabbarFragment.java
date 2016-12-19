package com.example.fiveyuanstore.fragment;


import com.example.fiveyuanstore.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;

public class MainTabbarFragment extends Fragment {
	
	View tabStore, tabDeal, tabSeller, tabMe;
	View[] tabs;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_tabbar, null);
		
		
		tabStore = view.findViewById(R.id.tab_storeList);
		tabDeal = view.findViewById(R.id.tab_dealMessage);
		tabSeller = view.findViewById(R.id.tab_sellerList);
		tabMe = view.findViewById(R.id.tab_me);
			
		
		tabs = new View[] {
				tabStore, tabDeal, tabSeller, tabMe
		};
		
		for(final View tab : tabs){
			tab.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onTabClicked(tab);
				}
			});			
		}
		
		
		return view;
	}
	
	public static interface OnTabSelectedListener{
		void onTabSelected(int index);
	}
	
	OnTabSelectedListener onTabSelectedListener;
	
	public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener){
		this.onTabSelectedListener=onTabSelectedListener;
	}
	
	public void setSelectItem(int index){
		if(index>=0 && index<tabs.length){
			onTabClicked(tabs[index]);
		}
	}
	
	void onTabClicked(View tab){
		
		int selectedIndex=-1;
		
		for(int i=0;i<tabs.length;i++){
			View otherTab=tabs[i];
			if(otherTab==tab){
				otherTab.setSelected(true);
				selectedIndex=i;
			}else{
			otherTab.setSelected(false);//boolean
			}
		}
		if(onTabSelectedListener!=null && selectedIndex>=0){
			onTabSelectedListener.onTabSelected(selectedIndex);
		}
	}

	public int getSelectedIndex() {
		for (int i = 0; i < tabs.length; i++) {
			if(tabs[i].isSelected()) return i;
		}
		return -1;
	}
}
