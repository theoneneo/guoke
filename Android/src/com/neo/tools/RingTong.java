package com.neo.tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.Settings;

public class RingTong {
	public static String musicUrl = null;
	public static void systemNotificationRing(Context context) {
		if (musicUrl == null) {
			musicUrl = Settings.System.getString(context.getContentResolver(),
					Settings.System.NOTIFICATION_SOUND);
		}
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(musicUrl);
			player.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		player.start();
	}
}
