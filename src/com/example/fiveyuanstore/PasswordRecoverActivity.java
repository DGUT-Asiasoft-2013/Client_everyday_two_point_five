package com.example.fiveyuanstore;

import com.example.fiveyuanstore.fragment.PasswordRecoverStep1Fragment;
import com.example.fiveyuanstore.fragment.PasswordRecoverStep1Fragment.OnGoNextListener;
import com.example.fiveyuanstore.fragment.PasswordRecoverStep2Fragment;
import com.example.fiveyuanstore.fragment.PasswordRecoverStep2Fragment.OnSubmitClickedListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class PasswordRecoverActivity extends Activity {

	PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
	PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_recover);

		step1.setOnGoNextListener(new OnGoNextListener() {

			@Override
			public void onGoNext() {
				goStep2();

			}
		});

		step2.setOnSubmitClickedListener(new OnSubmitClickedListener() {

			@Override
			public void onSubmitClicked() {
				goSubmit();

			}
		});
		getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();
	}

	void goStep2() {
		getFragmentManager()
				.beginTransaction().setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left,
						R.animator.slide_in_left, R.animator.slide_out_right)
				.replace(R.id.container, step2).addToBackStack(null).commit();
	}

	void goSubmit() {

		
	}

	void onResponse() {
		new AlertDialog.Builder(this).setMessage("������ĳɹ���")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
					}
				}).show();
	}
}
