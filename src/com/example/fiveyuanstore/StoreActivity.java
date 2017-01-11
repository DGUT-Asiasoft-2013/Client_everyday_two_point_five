package com.example.fiveyuanstore;

import com.example.fiveyuanstore.page.CommodityFragment;
import com.example.fiveyuanstore.page.DealFragment;
import com.example.fiveyuanstore.page.MyProfileFragment;
import com.example.fiveyuanstore.page.SellerFragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class StoreActivity extends Activity {
	
	CommodityFragment contentGoodList=new CommodityFragment();
	DealFragment contentDealPage=new DealFragment();
	SellerFragment contentSellerPage=new SellerFragment();
	MyProfileFragment contentMyProfile=new MyProfileFragment();
	
	ImageView tab_storeList;
	ImageView tab_dealMessage;
	ImageView tab_sellerList;
	ImageView tab_me;
	int num;
	int point=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		
		tab_storeList=(ImageView)findViewById(R.id.tab_storeList);
		tab_dealMessage=(ImageView)findViewById(R.id.tab_dealMessage);
		tab_sellerList=(ImageView)findViewById(R.id.tab_sellerList);
		tab_me=(ImageView)findViewById(R.id.tab_me);
		
		changeContentFragment(0);
		
		tab_storeList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeContentFragment(0);			
			}
		});
		
		tab_dealMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeContentFragment(1);				
			}
		});
		
		tab_sellerList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeContentFragment(2);				
			}
		});
		
		tab_me.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeContentFragment(3);			
			}
		});
					
	}
	
	void changeContentFragment(int index){
		Fragment newFrag=null;
		
		switch (index) {
		case 0:
			num=0;
			newFrag = contentGoodList;
			tab_storeList.setImageResource(R.drawable.ic_page_1_on);
			tab_dealMessage.setImageResource(R.drawable.ic_page_2_off);
			tab_sellerList.setImageResource(R.drawable.ic_page_3_off);
			tab_me.setImageResource(R.drawable.ic_page_4_off);
			break;
		case 1:
			num=1;
			newFrag = contentDealPage;
			tab_storeList.setImageResource(R.drawable.ic_page_1_off);
			tab_dealMessage.setImageResource(R.drawable.ic_page_2_on);
			tab_sellerList.setImageResource(R.drawable.ic_page_3_off);
			tab_me.setImageResource(R.drawable.ic_page_4_off);
			break;
		case 2:
			num=2;
			newFrag = contentSellerPage;
			tab_storeList.setImageResource(R.drawable.ic_page_1_off);
			tab_dealMessage.setImageResource(R.drawable.ic_page_2_off);
			tab_sellerList.setImageResource(R.drawable.ic_page_3_on);
			tab_me.setImageResource(R.drawable.ic_page_4_off);
			break;
		case 3:
			num=3;
			newFrag = contentMyProfile;
			tab_storeList.setImageResource(R.drawable.ic_page_1_off);
			tab_dealMessage.setImageResource(R.drawable.ic_page_2_off);
			tab_sellerList.setImageResource(R.drawable.ic_page_3_off);
			tab_me.setImageResource(R.drawable.ic_page_4_on);
			break;
			
		default:break;
		}
		if(newFrag==null)return;
		
		if(point<=num){
		getFragmentManager().beginTransaction()
		.setCustomAnimations(R.animator.slide_in_right,
	             R.animator.slide_out_left)
		.replace(R.id.contnet, newFrag).commit();
		
		}else{
			
			getFragmentManager().beginTransaction()
			.setCustomAnimations(R.animator.slide_in_left,
	                R.animator.slide_out_right)
			.replace(R.id.contnet, newFrag).commit();
		}
		point=num;
	}
}
