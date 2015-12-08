package barqsoft.footballscores.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import barqsoft.footballscores.MainScreenFragment;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.service.FetchDataService;

/**
 * Created by andermaco on 3/12/15.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmManagerBroadcastReceiver.class.getSimpleName();
    private static final String WAKE_UP = "wake_up";

    @Override
    public final void onReceive(Context context, Intent intent) {
        Log.v(TAG, "AlarmManagerBroadcastReceiver onReceive");
        if (!Utilies.isMyServiceRunning(context)) {
            Intent service = new Intent(context, FetchDataService.class);
            service.putExtra(MainScreenFragment.FILTER, MainScreenFragment.FILTER_TODAY);
            context.startService(service);
        }
    }
}
