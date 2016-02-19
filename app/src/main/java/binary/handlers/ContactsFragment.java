package binary.handlers;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
public class ContactsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public SimpleCursorAdapter adapter_contacts;
    public static String[] color_gradients = {"#AE6118","#C5C1AA","#8E8E38","#4682B4","#B0171F","#770533","#770533","#770533","#76a99a","#bb7f78"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adapter_contacts = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                R.layout.chats_list_row, null, new String[]{DataProvider.COL_NAME,DataProvider.COL_ID},
                new int[]{R.id.chats_name_list_tv,R.id.imgvw_dp_id_no_tv}, 0);
                adapter_contacts.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                        switch(view.getId()){
                            case R.id.imgvw_dp_id_no_tv:
                                int io = cursor.getInt(columnIndex);
                                ViewGroup row = (ViewGroup)view.getParent();
                                ImageView iv = (ImageView) row.findViewById(R.id.imgvw_dp);
                                int idx = new Random().nextInt(color_gradients.length);
                                String randomized_color = color_gradients[idx];
                                int color = Color.parseColor(randomized_color);
                                iv.setImageResource(R.drawable.ic_brightness_1_black_48dp);
                                iv.setColorFilter(color);
                                ((TextView) view).setText(io+"");
                                return true;
                        }
                        return false;
                    }
                });

        setListAdapter(adapter_contacts);


    }

    public ContactsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contactsfragment, container, false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(),
                DataProvider.CONTENT_URI_PROFILE,
                new String[]{DataProvider.COL_ID, DataProvider.COL_NAME},
                null,
                null,
                DataProvider.COL_ID + " DESC");


        return loader;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i =new Intent(getActivity(), BinaryChatActivity.class);
        String pid = ((TextView)v.findViewById(R.id.imgvw_dp_id_no_tv)).getText().toString();
        SessionManager.chat_profile_id=pid;
        i.putExtra(DataProvider.COL_ID,pid);
        startActivity(i);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter_contacts.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter_contacts.swapCursor(null);
    }
}
