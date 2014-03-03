package com.lbs.guoke.fragment;

import com.pad_go.loka.R;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.fragment.AddSiteFragment.AddSiteListFragmentListener;
import com.lbs.guoke.fragment.MySiteListFragment.SiteViewHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SiteTypeFragment extends DialogFragment implements OnTouchListener {
	private Integer[] mImageIds = { R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher };
	
	private SiteTypeFragmentListener fListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.fragment_grid, null);
		
		GridView gridview = (GridView) view.findViewById(R.id.grid);
		gridview.setAdapter(new SiteTypeAdapter(getActivity()));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				fListener.SetSiteTypeFragmentListener(position);
				getFragmentManager().popBackStack();
			}
		});
		
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.site_type).setView(view).create();
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	fListener = (SiteTypeFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentListener");
        }
    }
	
    public interface SiteTypeFragmentListener{
        public void SetSiteTypeFragmentListener(int type);
    }

	public class SiteTypeAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context mContext;

		public SiteTypeAdapter(Context context) {
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setImageResource(mImageIds[position]);
			return imageView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageIds.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO 拦截触摸事件防止泄露下去
		view.setOnTouchListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
}
