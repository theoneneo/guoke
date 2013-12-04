package com.lbs.guoke;

import com.lbs.guoke.cell.CellModule;
import com.lbs.guoke.db.DBTools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	GuoKeApp.setMainHandler(mainHandler);
	initUI();
    }

    @Override
    protected void onDestroy() {
	GuoKeApp.setMainHandler(null);
	super.onDestroy();
    }

    private void initUI() {
	DBTools.getInstance();
	DBTools.initDBContext(getApplicationContext());
	for (int i = 0; i < CellModule.mCellids.size(); i++) {
	    DBTools.getInstance().insertCellData(CellModule.mCellids.get(i),
		    "first", "firstdetail", "www.");
	}
    }

    private Handler mainHandler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    default:
		break;
	    }
	    super.handleMessage(msg);
	}
    };
}
