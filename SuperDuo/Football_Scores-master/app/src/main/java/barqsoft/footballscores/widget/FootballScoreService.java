package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.FootballScoreContract.ScoreTable;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by andermaco on 23/11/15.
 */
@TargetApi(VERSION_CODES.HONEYCOMB)
public class FootballScoreService extends RemoteViewsService {

    @Override
    public final RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballScoreRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}


class FootballScoreRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
        , OnLoadCompleteListener<Cursor> {

    private static final String TAG = "RemoteViewsFactory";
    Context mContext = null;
    private Cursor mCursor = null;

    public FootballScoreRemoteViewsFactory(Context applicationContext, Intent intent) {
        this.mContext = applicationContext;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {}

    @Override
    public final void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        final long token = Binder.clearCallingIdentity();
        try {
            Uri uri = ScoreTable.buildScoreWithDate();
            String formatString = mContext.getString(R.string.date_format);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
            mCursor = mContext.getContentResolver().query(uri, null, null
                    , new String[]{simpleDateFormat.format(new Date())}
                    , mContext.getString(R.string.order_by));
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    @Override
    public final RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }
        // Get data
        String home = mCursor.getString(mCursor.getColumnIndex(ScoreTable.HOME_COL));
        String away = mCursor.getString(mCursor.getColumnIndex(ScoreTable.AWAY_COL));
        int homeScore = mCursor.getInt(mCursor.getColumnIndex(ScoreTable.HOME_GOALS_COL));
        int awayScore = mCursor.getInt(mCursor.getColumnIndex(ScoreTable.AWAY_GOALS_COL));
        String score = homeScore + "-" + awayScore;
        String time = mCursor.getString(mCursor.getColumnIndex(ScoreTable.TIME_COL));

        // Build remote object
        RemoteViews rView = new RemoteViews(mContext.getPackageName(), R.layout.scores_list_item);
        rView.setTextViewText(R.id.home_name, home);
        rView.setTextViewText(R.id.away_name, away);
        rView.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(home));
        rView.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(away));
        rView.setTextViewText(R.id.score_textview, score);
        rView.setTextViewText(R.id.data_textview, time);

        // Listview item position
        Bundle extras = new Bundle();
        extras.putInt(FootballScoreWidget.EXTRA_LIST_VIEW_ROW_NUMBER, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rView.setOnClickFillInIntent(R.id.list_view_row, fillInIntent);

        return  rView;
    }

    @Override
    public final RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.scores_list_item);
    }

    @Override
    public final int getViewTypeCount() {
        return 1;
    }

    @Override
    public final long getItemId(int position) {
        if (mCursor.moveToPosition(position))
            return mCursor.getLong(mCursor.getColumnIndex(ScoreTable.MATCH_ID));
        return position;
    }

    @Override
    public final int getCount() {
        return mCursor != null?mCursor.getCount():0;
    }

    @Override
    public final boolean hasStableIds() {
        return true;
    }

    @Override
    public final void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }
}