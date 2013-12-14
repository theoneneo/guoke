package com.lbs.guoke;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lbs.guoke.db.DBTools;
import com.lbs.guoke.fragment.MySiteListFragment;
import com.lbs.guoke.fragment.RemindListFragment;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends BaseActivity {
	private static final String[] CONTENT = new String[] { "提醒", "我的地盘" };
	private static final int[] ICONS = new int[] { R.drawable.ic_launcher,
			R.drawable.ic_launcher, };

	private RemindListFragment remindList;
	private MySiteListFragment mysiteList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSlidingMenu().setMode(SlidingMenu.RIGHT);
		getSlidingMenu().setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setContentView(R.layout.activity_main);

		initUI();
		FragmentPagerAdapter adapter = new MainAdapter(
				getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	@Override
	public void onDestroy() {
		GuoKeApp.setMainHandler(null);
		super.onDestroy();
	}

	private void initUI() {
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
}
