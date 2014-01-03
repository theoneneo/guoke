package com.lbs.guoke;

import com.lbs.guoke.fragment.AddSiteFragment;
import com.lbs.guoke.fragment.SiteTypeFragment;
import com.lbs.guoke.fragment.AddSiteFragment.AddSiteListFragmentListener;
import com.lbs.guoke.fragment.SiteTypeFragment.SiteTypeFragmentListener;
import com.neo.tools.CameraUtil;
import com.neo.tools.GetPhotoFromAlbum;
import com.neo.tools.PhotoCallBack;

import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class AddSiteActivity extends FragmentActivity implements
		AddSiteListFragmentListener, SiteTypeFragmentListener,
		OnBackStackChangedListener {
	private AddSiteFragment asFragment;
	private SiteTypeFragment stFragment;

	private String imagePath;

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
}
