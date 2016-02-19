package webradic.binarymessenger;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import binary.datahandlers.DataProvider;
import binary.datahandlers.SessionManager;
import binary.gcmhandlers.CCSManager;

public class BinaryChatActivity extends AppCompatActivity implements Button.OnClickListener {
    private static String ALGORITHM = "AES";

    private String profileId;
    private String profilename, profile_email;
    private Toolbar toolbar;
    public String my_email;
    EditText msgedt;
    Button send;
    SharedPreferences spf;
    private ContentResolver cr;
    SharedPreferences.Editor e;
    private static AtomicInteger atm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar_bin_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileId = getIntent().getStringExtra(DataProvider.COL_ID);

        msgedt = (EditText) findViewById(R.id.msg_ed_binary_chat);
        send = (Button) findViewById(R.id.send_btn_binary_chat);
        Cursor c = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), null, null, null, null);
        if (c.moveToFirst()) {
            profilename = c.getString(c.getColumnIndex(DataProvider.COL_NAME));
            profile_email = c.getString(c.getColumnIndex(DataProvider.COL_EMAIL));
            CCSManager.to_gcm_id = c.getString(c.getColumnIndex(DataProvider.COL_GCm_ID));
            getSupportActionBar().setTitle(profilename);

        }
        SessionManager.chat_profile_name = profilename;
        SessionManager.chat_profile_id = profileId;
        SessionManager.chat_profile_email = profile_email;
        spf = getSharedPreferences("packets_ext", Context.MODE_PRIVATE);
        int msgid = spf.getInt("last_msg_id", 0);
        atm = new AtomicInteger(msgid);
        spf = getSharedPreferences("session", Context.MODE_PRIVATE);
        my_email = spf.getString("email", null);
        SessionManager.login_email = my_email;
        Log.e("BinChatAct", "Email stored in spf is :" + my_email);
        cr = getApplicationContext().getContentResolver();
        send.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_binary_chat, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn_binary_chat:
                String message1 = msgedt.getText().toString();
                if (message1 != null && !message1.equals("")) {
                    e = spf.edit();
                    e.putInt("last_msg_id", atm.incrementAndGet());
                    Log.e("Updated atmint", "in onclick" + atm.get() + "");
                    e.commit();
                    /*
                    byte[] bytes = message1.getBytes();
                    StringBuilder binary = new StringBuilder();
                    for(byte b : bytes){

                        int val = b;
                        for(int i = 0;i<8;i++){
                                binary.append((val&128)==0?0:1);
                                val<<=1;

                        }binary.append(' ');

                    }
                    */
                    final Charset UTF_8 = Charset.forName("UTF-8");
                    byte[] bytes = message1.getBytes(UTF_8);

                    ContentValues cv = new ContentValues(1);
                    cv.put(DataProvider.COL_MSG, Arrays.toString(bytes));
                    cv.put(DataProvider.COL_TO, SessionManager.chat_profile_email);
                    cr.insert(DataProvider.CONTENT_URI_MESSAGES,cv);
                    MainActivity.ccs.sendmessage(Arrays.toString(bytes), profilename + atm.get());
                    msgedt.setText(null);

                }
                break;
        }
    }
}
