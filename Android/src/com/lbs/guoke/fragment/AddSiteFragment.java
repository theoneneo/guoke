package com.lbs.guoke.fragment;

import com.lbs.guoke.R;
import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager.SiteInfo;
import com.lbs.guoke.db.DBTools;
import com.lbs.guoke.fragment.MySiteListFragment.MySiteListFragmentListener;
import com.lbs.guoke.structure.CellInfo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AddSiteFragment extends Fragment {
	private AddSiteListFragmentListener fListener;

	public final static int ADD_DATA_STATUS = 0;
	public final static int INFO_DATA_STATUS = 1;
	public final static int MODIFY_DATA_STATUS = 2;
	private int mStatus;
	private int type;
	private String key, name, address, mark, typeText, img_link;

	private TextView titleText;
	private EditText edit_name, edit_address, edit_mark;
	private Button btn_type, btn_bottom_left, btn_bottom_right;
	private ImageView img_photo;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_site, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mStatus = getArguments().getInt("status", 0);
		key = getArguments().getString("key");
		if (INFO_DATA_STATUS == mStatus) {
			for (int i = 0; i < MySiteModuleManager.instance().getSiteInfos()
					.size(); i++) {
				SiteInfo siteInfo = MySiteModuleManager.instance()
						.getSiteInfos().get(i);
				if (siteInfo.key.equals(key)) {
					name = siteInfo.siteName;
					address = siteInfo.siteAddress;
					type = siteInfo.siteType;
					img_link = siteInfo.siteImageLink;
					mark = siteInfo.siteMark;
					break;
				}
			}
		}
		initUI();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			fListener = (AddSiteListFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentListener");
		}
	}

	public interface AddSiteListFragmentListener {
		public void LoadSiteTypeFragmentListener(int type);
	}

	private void initUI() {
		titleText = (TextView) getActivity().findViewById(R.id.title)
				.findViewById(R.id.text_title);
		ImageButton btn_back = (ImageButton) titleText
				.findViewById(R.id.btn_left);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		edit_name = (EditText) getActivity().findViewById(R.id.edit_name);
		edit_address = (EditText) getActivity().findViewById(R.id.edit_address);
		edit_mark = (EditText) getActivity().findViewById(R.id.edit_mark);
		btn_type = (Button) getActivity().findViewById(R.id.btn_type);
		btn_type.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fListener.LoadSiteTypeFragmentListener(type);
			}
		});
		img_photo = (ImageView) getActivity().findViewById(R.id.img_photo);
		img_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeSitePhoto();
			}
		});
		btn_bottom_left = (Button) getActivity().findViewById(R.id.bottom)
				.findViewById(R.id.btn_bottom_left);
		btn_bottom_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		btn_bottom_right = (Button) getActivity().findViewById(R.id.bottom)
				.findViewById(R.id.btn_bottom_right);
		btn_bottom_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mStatus == INFO_DATA_STATUS) {
					setStatus(MODIFY_DATA_STATUS);
				} else if (mStatus == ADD_DATA_STATUS) {
					saveSiteInfo();
					getActivity().setResult(getActivity().RESULT_OK, null);
					getActivity().finish();
				} else if (mStatus == MODIFY_DATA_STATUS) {
					modifySiteInfo();
					setStatus(INFO_DATA_STATUS);
				}
			}
		});
		changeSiteType();
		setStatus(mStatus);
	}

	public void setStatus(int status) {
		mStatus = status;
		switch (mStatus) {
		case ADD_DATA_STATUS: {
			titleText.setText(R.string.add_site);
			edit_name.setEnabled(true);
			edit_address.setEnabled(true);
			edit_mark.setEnabled(true);
			btn_type.setEnabled(true);
			img_photo.setEnabled(true);
			btn_bottom_left.setVisibility(View.VISIBLE);
			btn_bottom_right.setText(R.string.save);
		}
			break;
		case INFO_DATA_STATUS: {
			titleText.setText(R.string.info_site);
			edit_name.setEnabled(false);
			edit_address.setEnabled(false);
			edit_mark.setEnabled(false);
			btn_type.setEnabled(false);
			img_photo.setEnabled(false);
			edit_name.setText(name);
			edit_address.setText(address);
			edit_mark.setText(mark);
			btn_type.setText(typeText);
			Bitmap bm = BitmapFactory.decodeFile(img_link);
			img_photo.setImageBitmap(bm);
			btn_bottom_left.setVisibility(View.GONE);
			btn_bottom_right.setText(R.string.modify);
		}
			break;
		case MODIFY_DATA_STATUS: {
			titleText.setText(R.string.modify_site);
			edit_name.setEnabled(true);
			edit_address.setEnabled(true);
			edit_mark.setEnabled(true);
			btn_type.setEnabled(true);
			img_photo.setEnabled(true);
			edit_name.setText(name);
			edit_address.setText(address);
			edit_mark.setText(mark);
			btn_type.setText(typeText);
			Bitmap bm = BitmapFactory.decodeFile(img_link);
			img_photo.setImageBitmap(bm);
			btn_bottom_left.setVisibility(View.VISIBLE);
			btn_bottom_right.setText(R.string.save);
		}
			break;
		default:
			break;
		}
	}

	public void setSiteType(int type) {
		this.type = type;
		changeSiteType();
	}

	private void changeSiteType() {
		switch (type) {
		case 0:
			typeText = "超市";
			break;
		case 1:
			typeText = "商场";
			break;
		case 2:
			typeText = "大厦";
			break;
		case 3:
			typeText = "餐饮";
			break;
		case 4:
			typeText = "门店";
			break;
		case 5:
			typeText = "其他";
			break;
		default:
			break;
		}
		btn_type.setText(typeText);
	}

	private void changeSitePhoto() {

	}

	private void saveSiteInfo() {
		name = edit_name.getText().toString();
		address = edit_address.getText().toString();
		mark = edit_mark.getText().toString();
		MySiteModuleManager.instance().addSiteInfo(name, address, type,
				img_link, mark);
	}

	private void modifySiteInfo() {
		name = edit_name.getText().toString();
		address = edit_address.getText().toString();
		mark = edit_mark.getText().toString();
		MySiteModuleManager.instance().modifySite(key, name, address, type,
				img_link, mark);
	}
}
