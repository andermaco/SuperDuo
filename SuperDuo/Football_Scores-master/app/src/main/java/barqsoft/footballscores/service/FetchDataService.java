package barqsoft.footballscores.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;

import barqsoft.footballscores.FootballScoreContract;
import barqsoft.footballscores.FootballScoreContract.ScoreTable;
import barqsoft.footballscores.MainScreenFragment;
import barqsoft.footballscores.R;

/**
 * Created by Andermaco 30/11/2015
 */
public class FetchDataService extends IntentService
{
    private static final String TAG = FetchDataService.class.getSimpleName();
    public static final String BROADCAST_DATA_UPDATED = "barqsoft.footballscores.service.BROADCAST_DATA_UPDATED";

    public FetchDataService() {
        super("FetchDataService");
    }

    @Override
    protected final void onHandleIntent(Intent intent)
    {
        Log.v(TAG, "onHandleIntent()");
        String filter = intent.getStringExtra(MainScreenFragment.FILTER);
        Bundle bundle = intent.getExtras();
        if (filter.equals(MainScreenFragment.FILTER_ALL)) {
            getData("n2");
            getData("p3");
        } else if (filter.equals(MainScreenFragment.FILTER_TODAY)) {
            getData("n1");
        }
    }

    private void getData (String timeFrame)
    {
        Log.v(TAG, "GetData " + timeFrame);
        //Creating fetch URL
        final String BASE_URL = getString(R.string.base_url);
        final String QUERY_TIME_FRAME = "timeFrame"; //Time Frame parameter to determine days
        final String QUERY_LEAGUE = "league";
        Uri fetch_build = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_TIME_FRAME, timeFrame)
                .appendQueryParameter(QUERY_LEAGUE, getString(R.string.query_league))
                .build();
        HttpURLConnection m_connection = null;
        BufferedReader reader = null;
        String JSON_data = null;
        try {
            // Request a json string request
            JsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Method.GET, fetch_build.toString(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray matches = null;
                    try {
                        matches = response.getJSONArray("fixtures");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    processData(response);
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error getting data");
                }
            });
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        } catch (Exception e)
        {
            Log.i(TAG,  e.getMessage());
        }
    }

    private class CustomJsonObjectRequest extends JsonObjectRequest
    {
        public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest, Listener listener, ErrorListener errorListener)
        {
            super(method, url, jsonRequest, listener, errorListener);
        }

        @Override
        public final Map getHeaders() throws AuthFailureError {
            Map headers = new HashMap();
            headers.put("X-Auth-Token",getString(R.string.api_key));
            return headers;
        }
    }


    private void processData(JSONObject jsonObject) {
        final String SEASON_LINK = "http://api.football-data.org/v1/soccerseasons/";
        final String MATCH_LINK = "http://api.football-data.org/v1/fixtures/";
        final String FIXTURES = "fixtures";
        final String LINKS = "_links";
        final String SOCCER_SEASON = "soccerseason";
        final String SELF = "self";
        final String MATCH_DATE = "date";
        final String HOME_TEAM = "homeTeamName";
        final String AWAY_TEAM = "awayTeamName";
        final String RESULT = "result";
        final String HOME_GOALS = "goalsHomeTeam";
        final String AWAY_GOALS = "goalsAwayTeam";
        final String MATCH_DAY = "matchday";

        //Match data
        String League = null;
        String mDate = null;
        String mTime = null;
        String Home = null;
        String Away = null;
        String Home_goals = null;
        String Away_goals = null;
        String match_id = null;
        String match_day = null;

        try {
            JSONArray matches = jsonObject.getJSONArray(FIXTURES);

            //ContentValues to be inserted
            List<ContentValues> values = new Vector <ContentValues> (matches.length());
            for(int i = 0;i < matches.length();i++)
            {
                JSONObject match_data = matches.getJSONObject(i);
                League = match_data.getJSONObject(LINKS).getJSONObject(SOCCER_SEASON).
                        getString("href");
                League = League.replace(SEASON_LINK,"");
                match_id = match_data.getJSONObject(LINKS).getJSONObject(SELF).
                        getString("href");
                match_id = match_id.replace(MATCH_LINK, "");
                mDate = match_data.getString(MATCH_DATE);
                mTime = mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z"));
                mDate = mDate.substring(0,mDate.indexOf("T"));
                SimpleDateFormat match_date = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                match_date.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    Date parseddate = match_date.parse(mDate+mTime);
                    SimpleDateFormat new_date = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                    new_date.setTimeZone(TimeZone.getDefault());
                    mDate = new_date.format(parseddate);
                    mTime = mDate.substring(mDate.indexOf(":") + 1);
                    mDate = mDate.substring(0,mDate.indexOf(":"));
                }
                catch (Exception e)
                {
                    Log.d(TAG, "error here!");
                    Log.e(TAG,e.getMessage());
                }
                Home = match_data.getString(HOME_TEAM);
                Away = match_data.getString(AWAY_TEAM);
                Home_goals = match_data.getJSONObject(RESULT).getString(HOME_GOALS);
                Away_goals = match_data.getJSONObject(RESULT).getString(AWAY_GOALS);
                match_day = match_data.getString(MATCH_DAY);
                ContentValues match_values = new ContentValues();
                match_values.put(ScoreTable.MATCH_ID,match_id);
                match_values.put(ScoreTable.DATE_COL,mDate);
                match_values.put(ScoreTable.TIME_COL,mTime);
                match_values.put(ScoreTable.HOME_COL,Home);
                match_values.put(ScoreTable.AWAY_COL,Away);
                match_values.put(ScoreTable.HOME_GOALS_COL,Home_goals);
                match_values.put(ScoreTable.AWAY_GOALS_COL,Away_goals);
                match_values.put(ScoreTable.LEAGUE_COL,League);
                match_values.put(ScoreTable.MATCH_DAY,match_day);
                values.add(match_values);
            }
            int inserted_data = 0;
            ContentValues[] insert_data = new ContentValues[values.size()];
            values.toArray(insert_data);
            inserted_data = getApplicationContext().getContentResolver().bulkInsert(
                    FootballScoreContract.BASE_CONTENT_URI,insert_data);
            if (inserted_data > 0) {
                sendBroadcast(new Intent(BROADCAST_DATA_UPDATED).setPackage(getPackageName()));
//                sendBroadcast(new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).setPackage(getPackageName()));
            }

            Log.v(TAG,"Succesfully Inserted : " + String.valueOf(inserted_data));
        }
        catch (JSONException e)
        {
            Log.e(TAG,e.getMessage());
        }
    }
}

