package com.lbs.guoke;

import com.lbs.guoke.fragment.AddRemindFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class AddRemindActivity extends BaseActivity {
	private AddRemindFragment arFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_site);
		initUI(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initUI(Bundle savedInstanceState) {
		if (findViewById(R.id.content_frame) != null) {
			if (savedInstanceState != null) {
				return;
			}
			arFragment = new AddRemindFragment();
			arFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_frame, arFragment).commit();
		}
	}
}
