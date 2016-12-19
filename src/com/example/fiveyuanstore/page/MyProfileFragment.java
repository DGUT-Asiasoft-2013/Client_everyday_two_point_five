package com.example.fiveyuanstore.page;



import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.fragment.list.TextListFragment;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyProfileFragment extends Fragment {
	View view;
	
	TextView textView;					//用户名
	ProgressBar progress;				//载入图案
	AvatarView avatar;					//用户图片
	TextListFragment inbox,wallet,password_changes;//私信，钱包，修改密码按钮；
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view==null){
			view=inflater.inflate(R.layout.fragment_page_my_profile, null);
			textView=(TextView)view.findViewById(R.id.text);
			progress=(ProgressBar)view.findViewById(R.id.progress);
			avatar=(AvatarView)view.findViewById(R.id.avatar);
			inbox=(TextListFragment)getFragmentManager().findFragmentById(R.id.inbox);
			wallet=(TextListFragment)getFragmentManager().findFragmentById(R.id.inbox);
			password_changes=(TextListFragment)getFragmentManager().findFragmentById(R.id.password_changes);
		}
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		inbox.setListText("私信");
		wallet.setListText("钱包");
		inbox.setListText("修改密码按钮");
		
		textView.setVisibility(View.GONE);			//隐藏用户名
		progress.setVisibility(View.VISIBLE);		//显示载入图案
		
		//服务器上提取信息
		
		textView.setVisibility(View.VISIBLE);
		progress.setVisibility(View.GONE);
		
		textView.setText("进击的机长");
		textView.setTextColor(Color.BLACK);
		
		avatar.load();
	}
}
