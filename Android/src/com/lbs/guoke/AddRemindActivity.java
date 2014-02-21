package com.lbs.guoke;

import com.lbs.guoke.fragment.AddRemindFragment;
import com.neo.tools.RingTong;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
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

	@Override
	// 当结果返回后判断并执行操作
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			selectRing(data);
		}
	}

	private void selectRing(Intent data) {
		try {
			Uri pickedUri = data
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			arFragment.setRing(pickedUri.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
