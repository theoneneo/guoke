package com.neo.tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.Settings;

public class RingTong {
	public void systemNotificationRing(Context context) {
		String curMusic = Settings.System.getString(
				context.getContentResolver(),
				Settings.System.NOTIFICATION_SOUND);
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(curMusic);
			player.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		player.start();
	}
}
