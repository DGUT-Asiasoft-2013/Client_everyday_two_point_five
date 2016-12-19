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
	
	TextView textView;					//�û���
	ProgressBar progress;				//����ͼ��
	AvatarView avatar;					//�û�ͼƬ
	TextListFragment inbox,wallet,password_changes;//˽�ţ�Ǯ�����޸����밴ť��
	
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
		
		inbox.setListText("˽��");
		wallet.setListText("Ǯ��");
		inbox.setListText("�޸����밴ť");
		
		textView.setVisibility(View.GONE);			//�����û���
		progress.setVisibility(View.VISIBLE);		//��ʾ����ͼ��
		
		//����������ȡ��Ϣ
		
		textView.setVisibility(View.VISIBLE);
		progress.setVisibility(View.GONE);
		
		textView.setText("�����Ļ���");
		textView.setTextColor(Color.BLACK);
		
		avatar.load();
	}
}
