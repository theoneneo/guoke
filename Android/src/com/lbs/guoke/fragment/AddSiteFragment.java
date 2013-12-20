package com.lbs.guoke.fragment;

import com.lbs.guoke.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class AddSiteFragment extends Fragment implements OnTouchListener{
	public final static int ADD_DATA_STATUS = 0;
	public final static int INFO_DATA_STATUS = 1;
	public final static int MODIFY_DATA_STATUS = 2;
	private int mStatus;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_site, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
	}
	
	public void setStatus(int status){
		mStatus = status;
	}

	private void initUI() {		
		ImageButton btn_back = (ImageButton)getActivity().findViewById(R.id.title).findViewById(R.id.btn_left);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}	
		});
		
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
