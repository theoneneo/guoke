package com.lbs.guoke.listener;

import java.lang.ref.WeakReference;
import java.util.Vector;

public class ListenerAbility {
    public Vector<WeakReference<Listener>> myListener;
    public boolean myIsInvalid = false;
    public Object mLock = new Object();

    public ListenerAbility() {
	myListener = new Vector<WeakReference<Listener>>(2);
    }

    public void addListener(Listener listener) {
	if (myListener == null) {
	    myListener = new Vector<WeakReference<Listener>>(2);
	}
	synchronized (mLock) {
	    WeakReference<Listener> wr = null;
	    Listener tempListener = null;
	    for (int i = myListener.size() - 1; i >= 0; i--) {
		wr = (WeakReference<Listener>) myListener.elementAt(i);
		tempListener = (Listener) wr.get();

		if (tempListener == null) {
		    myListener.removeElementAt(i);
		    continue;
		} else if (tempListener == listener) {
		    return;
		}
	    }
	    myListener.add(new WeakReference<Listener>(listener));
	}
    }

    public void clearListener() {
	if (myListener == null) {
	    return;
	}
	synchronized (myListener) {
	    myListener.removeAllElements();
	}
    }
}
