package barqsoft.footballscores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import barqsoft.footballscores.FootballScoreContract.ScoreTable;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class ScoresDBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 2;
    public ScoresDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public final void onCreate(SQLiteDatabase db)
    {
        final String CreateScoresTable = "CREATE TABLE " + FootballScoreContract.SCORES_TABLE + " ("
                + ScoreTable._ID + " INTEGER PRIMARY KEY,"
                + ScoreTable.DATE_COL + " TEXT NOT NULL,"
                + ScoreTable.TIME_COL + " INTEGER NOT NULL,"
                + ScoreTable.HOME_COL + " TEXT NOT NULL,"
                + ScoreTable.AWAY_COL + " TEXT NOT NULL,"
                + ScoreTable.LEAGUE_COL + " INTEGER NOT NULL,"
                + ScoreTable.HOME_GOALS_COL + " TEXT NOT NULL,"
                + ScoreTable.AWAY_GOALS_COL + " TEXT NOT NULL,"
                + ScoreTable.MATCH_ID + " INTEGER NOT NULL,"
                + ScoreTable.MATCH_DAY + " INTEGER NOT NULL,"
                + " UNIQUE ("+ ScoreTable.MATCH_ID+") ON CONFLICT REPLACE"
                + " );";
        db.execSQL(CreateScoresTable);
    }

    @Override
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + FootballScoreContract.SCORES_TABLE);
    }
}
