package com.lbs.guoke;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.fragment.AddSiteFragment;
import com.lbs.guoke.fragment.MySiteListFragment;
import com.lbs.guoke.fragment.RemindListFragment;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends SlidingBaseActivity {
	private static final String[] CONTENT = new String[] { "提醒", "我的地盘" };
	private static final int[] ICONS = new int[] { R.drawable.bg_bell,
			R.drawable.bg_place, };

	private static RemindListFragment remindList;
	private static MySiteListFragment mysiteList;

	public static int REQUEST_ADD_SITE = 0;
	public static int REQUEST_ADD_REMIND = 1;
	public static int REQUEST_SELECT_RING = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSlidingMenu().setMode(SlidingMenu.RIGHT);
		getSlidingMenu().setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setContentView(R.layout.activity_main);

		initUI();
	}

	@Override
	public void onStop() {
		super.onStop();
		RemindModuleManager.instance().saveRemindData();
	}

	public void onStart() {
		super.onStart();
		if (mysiteList != null)
			mysiteList.updateAdapter();
		if (remindList != null)
			remindList.updateAdapter();
	}

	@Override
	public void onBackPressed() {
		GuoKeApp.setMainHandler(null);
		GuoKeApp.getApplication().exit();
		super.onBackPressed();
	}

	@Override
	// 当结果返回后判断并执行操作
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
			case 0:
			case 1:
				mysiteList.updateAdapter();
				remindList.updateAdapter();
				break;
			default:
				break;
			}
		}
	}

	private void initUI() {
		FragmentPagerAdapter adapter = new MainAdapter(
				getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		GuoKeApp.setMainHandler(mainHandler);
		remindList = new RemindListFragment();
		mysiteList = new MySiteListFragment();
	}

	class MainAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
		public MainAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				if (remindList == null)
					remindList = new RemindListFragment();
				return remindList;
			} else if (position == 1) {
				if (mysiteList == null)
					mysiteList = new MySiteListFragment();
				return mysiteList;
			}
			return remindList;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getIconResId(int index) {
			return ICONS[index];
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}

	private Handler mainHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GuoKeApp.GUOKE_REMIND_UPDATE:
				remindList.updateAdapter();
				break;
			case GuoKeApp.GUOKE_SITE_UPDATE:
				mysiteList.updateAdapter();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "关于路佳");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			goToAboutActivity();
		}
		return true;
	}

	private void goToAboutActivity() {
		Intent i = new Intent(this, tempDialogActivity.class);
		startActivity(i);
	}
}
