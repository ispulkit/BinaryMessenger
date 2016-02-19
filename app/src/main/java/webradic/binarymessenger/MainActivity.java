package webradic.binarymessenger;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.wr.dellpc.binary.backend.initApi.InitApi;
import com.wr.dellpc.binary.backend.initApi.model.User;

import java.io.IOException;
import java.sql.SQLException;

import binary.datahandlers.DataProvider;
import binary.gcmhandlers.CCSManager;
import binary.handlers.ChatFragment;
import binary.handlers.ContactsFragment;
import binary.handlers.MainViewPagerAdapter;



public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tablayout;
    private ViewPager viewpager;
    public static CCSManager ccs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        viewpager = (ViewPager)findViewById(R.id.viewpager);
        setupviewpager(viewpager);
        tablayout = (TabLayout)findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewpager);
        ccs = new CCSManager();


    }


    public void showhome(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void hidehome(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void setupviewpager(ViewPager viewpager){
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getFragmentManager());
        adapter.addfragment(new ChatFragment(),"Chats");
        adapter.addfragment(new ContactsFragment(),"Contacts");
        viewpager.setAdapter(adapter);
        setuptabicons();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void setuptabicons(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.add_new_contact) {
            Intent i = new Intent();
            i.setClass(this, AddContact.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ccs.bye();
    }
}
