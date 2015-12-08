package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import barqsoft.footballscores.FootballScoreContract.ScoreTable;
import barqsoft.footballscores.service.FetchDataService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = MainScreenFragment.class.getSimpleName();
    public static final String FILTER = "filter";
    public static final String FILTER_ALL = "filter_all";
    public static final String FILTER_TODAY = "filter_today";
    public ScoresAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private String[] fragmentdate = new String[1];
    private int last_selected_item = -1;
    View rootView;
    ListView score_list;

    public MainScreenFragment()
    {
    }

    private void update_scores()
    {
        Log.v(TAG, "update_scores()");
        if (!Utilies.isMyServiceRunning(getActivity())) {
            Intent service_start = new Intent(getActivity(), FetchDataService.class);
            service_start.putExtra(FILTER, FILTER_ALL);
            getActivity().startService(service_start);
        }
    }
    public final void setFragmentDate(String date)
    {
        fragmentdate[0] = date;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   final Bundle savedInstanceState) {
        update_scores();
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        score_list = (ListView) rootView.findViewById(R.id.scores_list);
        mAdapter = new ScoresAdapter(getActivity(), null, 0);
        score_list.setAdapter(mAdapter);
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
        mAdapter.detail_match_id = MainActivity.selected_match_id;
        score_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder selected = (ViewHolder) view.getTag();
                mAdapter.detail_match_id = selected.match_id;
                MainActivity.selected_match_id = (int) selected.match_id;
                mAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    @Override
    public final Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new CursorLoader(getActivity(), ScoreTable.buildScoreWithDate(),
                null,null,fragmentdate,null);
    }

    @Override
    public final void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        if (cursor.getCount() > 0) {
            View no_match_layout = rootView.findViewById(R.id.no_match_layout);
            no_match_layout.setVisibility(View.INVISIBLE);
            int i = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                i++;
                cursor.moveToNext();
            }
            mAdapter.swapCursor(cursor);
        } else {
            View no_match_layout = rootView.findViewById(R.id.no_match_layout);
            no_match_layout.setVisibility(View.VISIBLE);
        }

        // Move to widget selection row item
        final MainActivity mainActivity = (MainActivity) getActivity();
        int row = mainActivity.getRow();
        if (row != -1) {
            score_list.post(new Runnable() {
                @Override
                public void run() {
                    score_list.setSelection(mainActivity.getRow());
                }
            });
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public final void onLoaderReset(Loader<Cursor> cursorLoader)
    {
        mAdapter.swapCursor(null);
    }
}
