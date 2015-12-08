package barqsoft.footballscores.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.service.FetchDataService;

/**
 * Created by andermaco on 12/1/2015.
 */
public class FootballScoreWidget extends AppWidgetProvider {

    public static final String WIDGET_CLICKED = "barqsoft.footballscores.service.APPWIDGET_CLICKED";
    public static final String EXTRA_LIST_VIEW_ROW_NUMBER = "row_number";

    @Override
    public final void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int i=0; i<appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            // Invoke the service
            Intent it = new Intent(context, FootballScoreService.class);
            it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            it.setData(Uri.parse(it.toUri(Intent.URI_INTENT_SCHEME)));
            // Add the remoteView
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.scores_list_widget);
            rv.setRemoteAdapter(appWidgetId, R.id.scores_list_widget, it);
            rv.setEmptyView(R.id.scores_list_widget, R.id.empty_view);
            // Start intent for managing widget clicking
            Intent startActivityIntent = new Intent(context, FootballScoreWidget.class);
            startActivityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            startActivityIntent.setAction(WIDGET_CLICKED);
            PendingIntent startActivityPendingIntent = PendingIntent.getBroadcast(context,
                    appWidgetId, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.scores_list_widget, startActivityPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, rv);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public final void onReceive(@NonNull Context context, @NonNull Intent intent) {
        if (FetchDataService.BROADCAST_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.scores_list_widget);
        } else if (WIDGET_CLICKED.equals(intent.getAction())) {
            // Manage widget click
            Intent it = new Intent(context, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            it.putExtras(intent.getExtras());
            Bundle bundle = intent.getExtras();
            context.startActivity(it);
        }
        super.onReceive(context, intent);
    }


    @Override
    public final void onEnabled(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.setAction("barqsoft.footballscores.widget.WAKE_UP");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Trigger each 15 minutes
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, 900*1000, 900000, pendingIntent);
        super.onEnabled(context);
    }

    @Override
    public final void onDisabled(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }
}

