package com.example.fiveyuanstore.fragment;

import java.io.IOException;

import com.example.fiveyuanstore.R;
import com.example.fiveyuanstore.api.Server;
import com.example.fiveyuanstore.entity.User;
import com.example.fiveyuanstore.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordRecoverStep1Fragment extends Fragment {

	EditText fragEmail;
	View view;
	SimpleTextInputCellFragment account;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_password_recover_step1, null);

			fragEmail = (EditText) view.findViewById(R.id.input_email);
			account = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verify);

			view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (getAccount().equals("")) {
						Toast.makeText(getActivity(), "邮箱和用户名不一致", Toast.LENGTH_LONG).show();
					} else {
						goNext();
					}
				}
			});
		}

		return view;
	}

	public String getEmail() {
		return fragEmail.getText().toString();
	}

	public String getAccount() {
		return account.getText().toString();
	}

	@Override
	public void onResume() {
		super.onResume();

		account.setLabelText("用户名");
		account.setHintText("请输入用户名");

	}

	public static interface OnGoNextListener {
		void onGoNext();
	}

	OnGoNextListener onGoNextListener;

	public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
		this.onGoNextListener = onGoNextListener;
	}

	void goNext() {
		String account = getAccount();

		MultipartBody request_body = new MultipartBody.Builder().addFormDataPart("account", account).build();

		Request request = Server.requestBuilderWithPath("/passwordRec").post(request_body).build();

		Server.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// 验证输入的用户名与邮箱是否一致
				try {
					final User body = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<User>() {
					});

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

								String myEmail = body.getEmail();
								if (myEmail.equals(getEmail())) {
									if (onGoNextListener != null) {
										onGoNextListener.onGoNext();
									}
								} else {
									Toast.makeText(getActivity(), "邮箱和用户名不一致", Toast.LENGTH_LONG).show();
								}						
						}
					});

				} catch (Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getActivity(), "用户名不存在", Toast.LENGTH_LONG).show();							
						}
					});
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// 邮箱和用户名不一致
						Toast.makeText(getActivity(), "遇到网络问题或未知错误", Toast.LENGTH_LONG).show();
					}
				});

			}
		});

	}
}