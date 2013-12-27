package com.lbs.guoke;

import com.lbs.guoke.fragment.AddSiteFragment;
import com.lbs.guoke.fragment.SiteTypeFragment;
import com.lbs.guoke.fragment.AddSiteFragment.AddSiteListFragmentListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class AddSiteActivity extends FragmentActivity implements AddSiteListFragmentListener{
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
            AddSiteFragment asFragment = new AddSiteFragment();
            asFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, asFragment).commit();
        }	
	}

	@Override
	public void LoadSiteTypeFragmentListener(int type) {
		// TODO Auto-generated method stub
		SiteTypeFragment stFragment = new SiteTypeFragment();
		Bundle arguments = new Bundle();
		arguments.putInt("type", type);
		stFragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction()
				.add(stFragment, "dialog").addToBackStack(null)
				.commit();
	}
}
