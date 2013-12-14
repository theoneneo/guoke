package com.lbs.guoke.listener;

import java.lang.ref.WeakReference;

public class CellModuleListenerAbility extends ListenerAbility {
	public void notifyCellChangeListener() {
		if (myIsInvalid) {
			return;
		}
		synchronized (mLock) {
			WeakReference<Listener> wr = null;
			CellModuleListener tempListener = null;
			if (myListener == null || myListener.size() == 0) {
				return;
			}
			for (int i = myListener.size() - 1; i >= 0; i--) {
				wr = (WeakReference<Listener>) myListener.elementAt(i);
				tempListener = (CellModuleListener) wr.get();
				if (tempListener == null) {
					myListener.removeElementAt(i);
					continue;
				} else {
					tempListener.onCellChange();
				}
			}
		}
	}

	public void setInvalid() {
		myIsInvalid = true;
	}

	public boolean isInvalid() {
		return myIsInvalid;
	}
}
