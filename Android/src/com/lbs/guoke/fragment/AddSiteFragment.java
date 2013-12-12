package com.lbs.guoke.fragment;

import com.lbs.guoke.R;
import com.lbs.guoke.fragment.RemindListFragment.RemindViewHolder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddSiteFragment extends ListFragment {
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_add_site, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
    }
}
