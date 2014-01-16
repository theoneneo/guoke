package com.lbs.guoke;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.fragment.MySiteListFragment;
import com.lbs.guoke.fragment.MySiteListFragment.MySiteListFragmentListener;
import com.lbs.guoke.fragment.RemindListFragment;
import com.lbs.guoke.fragment.RemindListFragment.RemindListFragmentListener;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends SlidingBaseActivity implements
		MySiteListFragmentListener, RemindListFragmentListener {
	private static final String[] CONTENT = new String[] { "提醒", "我的地盘" };
	private static final int[] ICONS = new int[] { R.drawable.ic_launcher,
			R.drawable.ic_launcher, };

	private static RemindListFragment remindList;
	private static MySiteListFragment mysiteList;

	private int REQUEST_ADD_SITE = 0;
	private int REQUEST_ADD_REMIND = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSlidingMenu().setMode(SlidingMenu.RIGHT);
		getSlidingMenu().setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		initUI();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		RemindModuleManager.instance().saveRemindData();
	}

	@Override
	public void onBackPressed(){
		GuoKeApp.setMainHandler(null);
		GuoKeApp.getApplication().exit();
		super.onBackPressed();
	}

	@Override
	// 当结果返回后判断并执行操作
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQUEST_ADD_SITE) {
			mysiteList.updateAdapter();
			remindList.updateAdapter();
		} else if (requestCode == REQUEST_ADD_REMIND) {
			remindList.updateAdapter();
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
	public void LoadAddRemindFragmentListener(int status, String key) {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, AddRemindActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("status", status);
		bundle.putString("key", key);
		i.putExtras(bundle);
		startActivityForResult(i, REQUEST_ADD_REMIND);
	}
	
	@Override
	public void LoadAddSiteFragmentListener(int status, String key) {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, AddSiteActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("status", status);
		bundle.putString("key", key);
		i.putExtras(bundle);
		startActivityForResult(i, REQUEST_ADD_SITE);
	}
}
