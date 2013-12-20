package com.lbs.guoke;

import com.lbs.guoke.fragment.AddSiteFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class AddSiteActivity extends FragmentActivity {
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
		AddSiteFragment asFragment = (AddSiteFragment) getSupportFragmentManager()
				.findFragmentById(R.id.add_site);
		if (savedInstanceState != null) {
			asFragment.setStatus(savedInstanceState.getInt("status"));
		}
	}
}
