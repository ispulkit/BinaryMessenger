package binary.gcmhandlers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver{

	@Override
	public void onReceive(Context c, Intent i) {
		// TODO Auto-generated method stub
		ComponentName cmp = new ComponentName(c.getPackageName(),GcmIntentService.class.getName());
		startWakefulService(c, i.setComponent(cmp));
		setResultCode(Activity.RESULT_OK);

	}

	
	
}
