package com.example.fiveyuanstore.page;

import java.io.IOException;

import com.example.fiveyuanstore.LoginActivity;
import com.example.fiveyuanstore.MainActivity;
import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.fragment.list.TextListFragment;
import com.example.fiveyuanstore.fragment.list.TextListFragment.OnNewClickedListener;
import com.example.fiveyuanstore.fragment.widgets.AvatarView;
import com.example.fiveyuanstore.myProfiles.InboxActivity;
import com.example.fiveyuanstore.myProfiles.MyDataActivity;
import com.example.fiveyuanstore.myProfiles.PasswordChangeActivity;
import com.example.fiveyuanstore.myProfiles.WalletActivity;
import com.example.fiveyuanstore.order.BillListActivity;
import com.example.fiveyuanstore.order.OrderHandlerActivity;
import com.example.fiveyuanstore.share.GetInforActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyProfileFragment extends Fragment {
	View view;
	String userName;
	String userEmail;
	User myuser;

	TextView textView; // 显示用户名
	ProgressBar progress; // 显示载入图案
	AvatarView avatar; // 显示用户头像
	TextListFragment inbox, wallet, password_changes, order_handler, bill_list, my_data;// 私信、钱包、密码修改,
																						// 訂單處理，
																						// 帳單查詢
	Float money;
	TextListFragment access_info;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_my_profile, null);
			textView = (TextView) view.findViewById(R.id.text);
			progress = (ProgressBar) view.findViewById(R.id.progress);
			avatar = (AvatarView) view.findViewById(R.id.avatar);
			inbox = (TextListFragment) getFragmentManager().findFragmentById(R.id.inbox);
			wallet = (TextListFragment) getFragmentManager().findFragmentById(R.id.wallet);
			password_changes = (TextListFragment) getFragmentManager().findFragmentById(R.id.password_changes);
			order_handler = (TextListFragment) getFragmentManager().findFragmentById(R.id.order_handler);
			bill_list = (TextListFragment) getFragmentManager().findFragmentById(R.id.bill_list);
			my_data = (TextListFragment) getFragmentManager().findFragmentById(R.id.my_data);

			access_info = (TextListFragment) getFragmentManager().findFragmentById(R.id.access_info);

			inbox.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent intent = new Intent(getActivity(), InboxActivity.class);
					intent.putExtra("name", userName);
					startActivity(intent);

				}
			});
			wallet.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent intent = new Intent(getActivity(), WalletActivity.class);
					intent.putExtra("money", money);
					startActivity(intent);

				}
			});
			password_changes.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent intent = new Intent(getActivity(), PasswordChangeActivity.class);
					startActivity(intent);

				}
			});
			order_handler.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent intent = new Intent(getActivity(), OrderHandlerActivity.class);
					startActivity(intent);
				}

			});

			bill_list.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent intent = new Intent(getActivity(), BillListActivity.class);
					startActivity(intent);
				}

			});

			my_data.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent itnt = new Intent(getActivity(), MyDataActivity.class);
					itnt.putExtra("name", userName);
					itnt.putExtra("email", userEmail);
					itnt.putExtra("user", myuser);
					startActivity(itnt);
				}
			});

			access_info.setOnNewClickedListener(new OnNewClickedListener() {

				@Override
				public void onNewClicked() {
					Intent intent = new Intent(getActivity(), GetInforActivity.class);
					startActivity(intent);

				}
			});

			view.findViewById(R.id.btn_log_off).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showNormalDialog();

				}
			});
		}
		return view;
	}

	private void showNormalDialog() {
		/*
		 * @setIcon 设置对话框图标
		 * 
		 * @setTitle 设置对话框标题
		 * 
		 * @setMessage 设置对话框消息提示 setXXX方法返回Dialog对象，因此可以链式设置属性
		 */
		final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());

		normalDialog.setTitle("注销");
		normalDialog.setMessage("是否确定注销?");
		normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				MainActivity.auto_false();
				LoginActivity.onNotSaveContent();
				Intent itnt=new Intent(getActivity(),LoginActivity.class);
				startActivity(itnt);
				getActivity().finish();
			}
		});
		normalDialog.setNegativeButton("返回", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ...To-do
			}
		});
		// 显示
		normalDialog.show();
	}

	@Override
	public void onResume() {
		super.onResume();

		inbox.setLabelText("私信");
		wallet.setLabelText("钱包");
		password_changes.setLabelText("修改密码");
		order_handler.setLabelText("订单处理");
		bill_list.setLabelText("我的账单");
		my_data.setLabelText("个人资料");
		access_info.setLabelText("授权信息");

		inbox.setLabelImage(R.drawable.ic_inbox);
		wallet.setLabelImage(R.drawable.ic_wallet);
		password_changes.setLabelImage(R.drawable.ic_password_changes);
		order_handler.setLabelImage(R.drawable.ic_order_handler);
		bill_list.setLabelImage(R.drawable.ic_order_handler);
		my_data.setLabelImage(R.drawable.ic_order_handler);

		textView.setVisibility(View.GONE); // 隐藏
		progress.setVisibility(View.VISIBLE); // 显示

		// 从服务器获取信息

		OkHttpClient client = Server.getClient();
		Request request = Server.requestBuilderWithPath("me").get().build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				try {
					final User user = new ObjectMapper().readValue(arg1.body().bytes(), User.class);
					if (user == null) { // 如果user 为空， 则为授权登陆
						toAccess();

					} else {
						getActivity().runOnUiThread(new Runnable() {

							public void run() {
								MyProfileFragment.this.onResponse(arg0, user);
							}
						});
					}
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							MyProfileFragment.this.onFailuer(arg0, e);
						}
					});
				}
			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						MyProfileFragment.this.onFailuer(arg0, arg1);
					}
				});
			}
		});
	}

	protected void toAccess() {
		OkHttpClient client = Server.getClient();
		Request request = Server.requestBuilderWithPath("access_me").get().build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final User user = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<User>() {
					});

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// 获得授权账户的名字，id
							userName = user.getUser_name();
							progress.setVisibility(View.GONE);
							textView.setVisibility(View.VISIBLE);
							textView.setTextColor(Color.WHITE);
							textView.setText(user.getUser_name());
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getActivity(), "出错：" + arg1.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}

	protected void onResponse(Call arg0, User user) {
		progress.setVisibility(View.GONE);
		avatar.load(user);
		textView.setVisibility(View.VISIBLE);
		textView.setTextColor(Color.WHITE);
		textView.setText(user.getUser_name());
		userName = user.getUser_name();
		userEmail = user.getEmail();
		money = user.getMoney();
		myuser = user;

	}

	protected void onFailuer(Call arg0, Exception e) {
		textView.setVisibility(View.VISIBLE);
		progress.setVisibility(View.GONE);
		textView.setTextColor(Color.RED);
		textView.setText(e.getMessage());
		userName = "null";

	}

}
