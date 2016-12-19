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
	
	TextView textView;					//显示用户名
	ProgressBar progress;				//显示载入图案
	AvatarView avatar;					//显示用户头像
	TextListFragment inbox,wallet,password_changes;//私信、钱包、密码修改
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view==null){
			view=inflater.inflate(R.layout.fragment_page_my_profile, null);
			textView=(TextView)view.findViewById(R.id.text);
			progress=(ProgressBar)view.findViewById(R.id.progress);
			avatar=(AvatarView)view.findViewById(R.id.avatar);
			inbox=(TextListFragment)getFragmentManager().findFragmentById(R.id.inbox);
			wallet=(TextListFragment)getFragmentManager().findFragmentById(R.id.wallet);
			password_changes=(TextListFragment)getFragmentManager().findFragmentById(R.id.password_changes);
		}
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		inbox.setLabelText("私信");
		wallet.setLabelText("钱包");
		password_changes.setLabelText("修改密码");
		
		textView.setVisibility(View.GONE);			//隐藏
		progress.setVisibility(View.VISIBLE);		//显示
		
		//从服务器获取信息
		
		textView.setVisibility(View.VISIBLE);
		progress.setVisibility(View.GONE);
		
		textView.setText("进击的机长");
		textView.setTextColor(Color.BLACK);
		
		avatar.load();
	}
}
