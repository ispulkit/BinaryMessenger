package binary.handlers;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import binary.datahandlers.DataProvider;
import binary.datahandlers.SessionManager;
import webradic.binarymessenger.BinaryChatActivity;
import webradic.binarymessenger.Decoder;
import webradic.binarymessenger.R;

/**
 * Created by Pulkit on 10/7/2015.
 */
public class BinaryChatMessagesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter mad;

    public BinaryChatMessagesFragment() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String profilechatid = SessionManager.chat_profile_id;
        Log.e("BCMsgsFG", "SessnMngr.Pname :" + SessionManager.chat_profile_email + "id:" + SessionManager.chat_profile_id);
        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(),
                DataProvider.CONTENT_URI_MESSAGES,
                null,
                DataProvider.COL_TO + " = ? or " + DataProvider.COL_FROM + " = ?",
                new String[]{SessionManager.chat_profile_email, SessionManager.chat_profile_email},
               // null,
               // null,
                DataProvider.COL_AT + " DESC");

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mad.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mad.swapCursor(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mad = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                R.layout.row_bin_chat,
                null,
                new String[]{DataProvider.COL_MSG, DataProvider.COL_FROM},
                new int[]{R.id.bin_chat_msg_tv1, R.id.bin_chat_msg_tv2},
                0);

        mad.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                String from = cursor.getString(cursor.getColumnIndex(DataProvider.COL_FROM));
                String to = cursor.getString(cursor.getColumnIndex(DataProvider.COL_TO));

                switch (view.getId()) {
                    case R.id.bin_chat_msg_tv1:
                        LinearLayout parent = (LinearLayout) view.getParent();
                        LinearLayout root = (LinearLayout) parent.getParent();
                        if (from == null || from.equals("null")) {
                            root.setGravity(Gravity.RIGHT);
                            root.setPadding(50, 10, 10, 10);
                        } else {
                            root.setGravity(Gravity.LEFT);
                            root.setPadding(10, 10, 50, 10);

                        }
                        break;
                }
                return false;
            }
        });

        setListAdapter(mad);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(null);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contactsfragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        String data = ((TextView)v.findViewById(R.id.bin_chat_msg_tv1)).getText().toString();
        Intent i = new Intent(getActivity(), Decoder.class);
        i.putExtra("data",data);
        startActivity(i);
    }
}
