package com.lbs.guoke;

import java.util.Timer;
import java.util.TimerTask;

import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.fragment.AddSiteFragment;
import com.lbs.guoke.fragment.SiteTypeFragment;
import com.lbs.guoke.fragment.AddSiteFragment.AddSiteListFragmentListener;
import com.lbs.guoke.fragment.SiteTypeFragment.SiteTypeFragmentListener;
import com.neo.tools.CameraUtil;
import com.neo.tools.GetPhotoFromAlbum;
import com.neo.tools.PhotoCallBack;
import com.pad_go.loka.R;

import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

public class AddSiteActivity extends BaseActivity implements
		AddSiteListFragmentListener, SiteTypeFragmentListener,
		OnBackStackChangedListener {
	private AddSiteFragment asFragment;
	private SiteTypeFragment stFragment;

	private String imagePath;
	private static Timer mTimer;

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
			asFragment = new AddSiteFragment();
			asFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_frame, asFragment).commit();

			getFragmentManager().addOnBackStackChangedListener(this);
		}
	}

	@Override
	public void LoadSiteTypeFragmentListener(int type) {
		// TODO Auto-generated method stub
		stFragment = new SiteTypeFragment();
		Bundle arguments = new Bundle();
		arguments.putInt("type", type);
		stFragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction()
				.add(stFragment, "dialog").addToBackStack(null).commit();
	}

	@Override
	public void SetSiteTypeFragmentListener(int type) {
		// TODO Auto-generated method stub
		asFragment.setSiteType(type);
	}

	@Override
	public void onBackStackChanged() {
		// TODO Auto-generated method stub
	}

	protected void onActivityResult(final int requestCode, int resultCode,
			final Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CameraUtil.PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
				GetPhotoFromAlbum.GetPhoto(this, requestCode, data, true,
						new PhotoCallBack() {
							@Override
							public void Success(String path) {
								imagePath = path;
							}

							@Override
							public void Failed(String errormsg) {
							}

							@Override
							public void Crop(Bitmap bitmap) {
							}
						});
				break;
			}
			case CameraUtil.CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
				GetPhotoFromAlbum.GetPhoto(this, requestCode, data, true,
						new PhotoCallBack() {
							@Override
							public void Success(String path) {
								imagePath = path;
							}

							@Override
							public void Failed(String errormsg) {
							}

							@Override
							public void Crop(Bitmap bitmap) {
							}
						});
				break;
			}
			case CameraUtil.CAMERA_COMPLETE:// 裁剪完成
				asFragment.setImageLink(imagePath);
				break;
			default:
				break;
			}
		}
	}

	public static void setTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
			CellModuleManager.bAddToArray = false;
		}
		mTimer = new Timer();
		CellModuleManager.bAddToArray = true;
		CellModuleManager.instance().getBugCellInfos().clear();
		for (int i = 0; i < CellModuleManager.instance().getCellInfos().size(); i++) {
			CellModuleManager.instance().getBugCellInfos()
					.add(CellModuleManager.instance().getCellInfos().get(i));
		}
		setTimerTask();
	}

	private static void setTimerTask() {
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				sHandler.sendEmptyMessage(1);
			}
		}, 300000);
	}

	public static void stopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		CellModuleManager.bAddToArray = false;
	}

	private static Handler sHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (mTimer != null) {
					mTimer.cancel();
					mTimer = null;
				}
				CellModuleManager.bAddToArray = false;
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
}
