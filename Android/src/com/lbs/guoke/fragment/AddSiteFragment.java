package com.lbs.guoke.fragment;

import com.pad_go.loka.R;
import com.lbs.guoke.AddSiteActivity;
import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager.SiteInfo;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.controller.RemindModuleManager.RemindInfo;
import com.neo.tools.CameraUtil;
import com.neo.tools.GetPhotoFromAlbum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddSiteFragment extends Fragment {
	private AddSiteListFragmentListener fListener;

	public final static int ADD_DATA_STATUS = 0;
	public final static int INFO_DATA_STATUS = 1;
	public final static int MODIFY_DATA_STATUS = 2;
	private int mStatus;
	private int type;
	private String key, typeText, img_link;

	private TextView titleText, text_prompt;
	private EditText edit_name, edit_address, edit_mark;
	private Button btn_type, btn_bottom_left, btn_bottom_right;
	private ImageView img_photo;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_site, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
		initData();
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
		text_prompt = (TextView) getActivity().findViewById(R.id.text_prompt);
		ImageButton btn_back = (ImageButton) getActivity().findViewById(
				R.id.title).findViewById(R.id.btn_left);
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
				if (mStatus == INFO_DATA_STATUS) {
					deleteSite();
				} else if (mStatus == ADD_DATA_STATUS) {
					AddSiteActivity.stopTimer();
					getActivity().finish();
				} else if (mStatus == MODIFY_DATA_STATUS) {
					getActivity().finish();
				}

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
	}

	private void initData() {
		mStatus = getArguments().getInt("status", 0);
		if(mStatus == ADD_DATA_STATUS){
			text_prompt.setVisibility(View.VISIBLE);
			AddSiteActivity.setTimer();
		}
		key = getArguments().getString("key");
		if (INFO_DATA_STATUS == mStatus) {
			for (int i = 0; i < MySiteModuleManager.instance().getSiteInfos()
					.size(); i++) {
				SiteInfo siteInfo = MySiteModuleManager.instance()
						.getSiteInfos().get(i);
				if (siteInfo.key.equals(key)) {
					type = siteInfo.siteType;
					img_link = siteInfo.siteImageLink;

					edit_name.setText(siteInfo.siteName);
					edit_address.setText(siteInfo.siteAddress);
					edit_mark.setText(siteInfo.siteMark);
					btn_type.setText(typeText);
					Bitmap bm = BitmapFactory.decodeFile(img_link);
					img_photo.setImageBitmap(bm);
					break;
				}
			}
		}
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
			btn_bottom_left.setText(R.string.cancel);
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
			btn_bottom_left.setText(R.string.delete);
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
			btn_bottom_left.setText(R.string.cancel);
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

	public void setImageLink(String imagePath) {
		this.img_link = imagePath;
		Bitmap bm = BitmapFactory.decodeFile(img_link);
		img_photo.setImageBitmap(bm);
	}

	private void deleteSite() {
		new AlertDialog.Builder(getActivity()).setTitle("提醒")
				.setMessage("确认删除")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						for (int i = 0; i < RemindModuleManager.instance()
								.getRemindInfos().size(); i++) {
							RemindInfo info = RemindModuleManager.instance()
									.getRemindInfos().get(i);
							if (info.key.equals(key)) {
								Toast.makeText(getActivity(),
										R.string.delete_site_remind,
										Toast.LENGTH_SHORT).show();
								return;
							}
						}
						MySiteModuleManager.instance().deleteSiteInfo(key);
						getActivity().finish();//
					}
				}).create().show();
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
		GetPhotoFromAlbum.ChooseWay(getActivity(),
				CameraUtil.PHOTO_PICKED_WITH_DATA);
	}

	private void saveSiteInfo() {
		MySiteModuleManager.instance().addSiteInfo(
				edit_name.getText().toString(),
				edit_address.getText().toString(), type, img_link,
				edit_mark.getText().toString());
		AddSiteActivity.stopTimer();
	}

	private void modifySiteInfo() {
		MySiteModuleManager.instance().modifySiteInfo(key,
				edit_name.getText().toString(),
				edit_address.getText().toString(), type, img_link,
				edit_mark.getText().toString());
	}
}
