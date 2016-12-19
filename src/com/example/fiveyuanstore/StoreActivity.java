package com.example.fiveyuanstore;



import com.example.fiveyuanstore.fragment.MainTabbarFragment;
import com.example.fiveyuanstore.fragment.MainTabbarFragment.OnTabSelectedListener;
import com.example.fiveyuanstore.page.CommodityFragment;
import com.example.fiveyuanstore.page.DealFragment;
import com.example.fiveyuanstore.page.MyProfileFragment;
import com.example.fiveyuanstore.page.SellerFragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class StoreActivity extends Activity {
	
	CommodityFragment contentGoodList=new CommodityFragment();
	DealFragment contentDealPage=new DealFragment();
	SellerFragment contentSellerPage=new SellerFragment();
	MyProfileFragment contentMyProfile=new MyProfileFragment();
	
	MainTabbarFragment tabbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		
		tabbar=(MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {
			
			@Override
			public void onTabSelected(int index) {
				changeContentFragment(index);				
			}
		});				
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		if(tabbar.getSelectedIndex()<0){
			tabbar.setSelectItem(0);
		}
	}
	
	void changeContentFragment(int index){
		Fragment newFrag=null;
		
		switch (index) {
		case 0:newFrag=contentGoodList;break;
		case 1:newFrag=contentDealPage;break;
		case 2:newFrag=contentSellerPage;break;
		case 3:newFrag=contentMyProfile;break;
			
		default:break;
		}
		if(newFrag==null)return;
		
		getFragmentManager().beginTransaction().replace(R.id.contnet, newFrag).commit();
	}
}
