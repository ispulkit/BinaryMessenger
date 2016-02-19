package binary.handlers;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Random;

import binary.datahandlers.DataProvider;
import binary.datahandlers.SessionManager;
import webradic.binarymessenger.BinaryChatActivity;
import webradic.binarymessenger.R;

/**
 * Created by dell pc on 9/26/2015.
 */
public class ChatFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public SimpleCursorAdapter adapter;

    public ChatFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                R.layout.chats_list_row, null, new String[]{DataProvider.COL_NAME, DataProvider.COL_COUNT,DataProvider.COL_ID},
                new int[]{R.id.chats_name_list_tv, R.id.chats_count_list_tv,R.id.imgvw_dp_id_no_tv}, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()) {
                    case R.id.chats_count_list_tv:
                        int count = cursor.getInt(columnIndex);
                        if (count > 0) {
                          //  ((TextView) view).setText(String.format("%d new message%s", count, count == 1 ? "" : "s"));
                            ((TextView) view).setText("");


                        }
                        return true;
                    case R.id.imgvw_dp_id_no_tv:
                        int io = cursor.getInt(columnIndex);
                        ViewGroup row = (ViewGroup)view.getParent();
                        ImageView iv = (ImageView) row.findViewById(R.id.imgvw_dp);
                        int idx = new Random().nextInt(ContactsFragment.color_gradients.length);
                        String randomized_color = ContactsFragment.color_gradients[idx];
                        int color = Color.parseColor(randomized_color);
                        iv.setColorFilter(color);
                        ((TextView) view).setText(io + "");
                        return true;
                }
                return false;
            }
        });
        setListAdapter(adapter);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i =new Intent(getActivity(), BinaryChatActivity.class);
        String pid = ((TextView)v.findViewById(R.id.imgvw_dp_id_no_tv)).getText().toString();
        SessionManager.chat_profile_id=pid;
        i.putExtra(DataProvider.COL_ID, pid);
        startActivity(i);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.chat_fragment, container, false);
        return rootview;


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);

    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(),
                DataProvider.CONTENT_URI_BANNER,
                new String[]{DataProvider.COL_ID,DataProvider.COL_LATEST_AT, DataProvider.COL_NAME, DataProvider.COL_COUNT},
                null,
                null,
                DataProvider.COL_LATEST_AT + " DESC");


        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
