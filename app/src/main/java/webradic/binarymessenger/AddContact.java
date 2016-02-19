package webradic.binarymessenger;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.wr.dellpc.binary.backend.initApi.InitApi;
import com.wr.dellpc.binary.backend.initApi.model.User;

import java.io.IOException;

import binary.datahandlers.DataProvider;

public class AddContact extends AppCompatActivity {


    Button b;
    EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        b = (Button)findViewById(R.id.btn_add_ok);
        ed = (EditText)findViewById(R.id.input_new_contact_id_ed);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inp_email= ed.getText().toString();
               inp_email= inp_email.trim();
                inp_email=inp_email.replaceAll("\\s","");
               inp_email= inp_email.toLowerCase();

                User user = new User();
                user.setGcmid("null");
                user.setEmail(inp_email);
                user.setDispname("null");

                new AsyncTask<User,Void,User>(){
                    private InitApi iapi = null;
                    @Override
                    protected User doInBackground(User... params) {
                        if(iapi==null){
                            InitApi.Builder builder = new InitApi.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(),null)
                                    .setRootUrl("https://binary-messenger.appspot.com/_ah/api");

                            iapi = builder.build();

                        }
                        User u1 = params[0];
                        User u2 = new User();

                        try {
                            u2 = iapi.checkUser(u1).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("IOException in chckuser", "MainActivity" + e.toString());
                            u2.setGcmid("null");
                            u2.setEmail("null");
                            u2.setDispname("null");
                        }finally {
                            return u2;
                        }

                    }

                    @Override
                    protected void onPostExecute(User user) {
                        super.onPostExecute(user);
                        if(user.getEmail().equals("null")&&user.getGcmid().equals("null")&&user.getDispname().equals("null")){
                            Log.e("onPost exec adduser","Cannot retrieve data");
                            Toast.makeText(getApplicationContext(), "Cannot retrieve data", Toast.LENGTH_LONG).show();
                        }else if(!user.getEmail().equals("null")&&(user.getGcmid().equals("null"))) {
                            Toast.makeText(getApplicationContext(), "No such user", Toast.LENGTH_LONG).show();
                        }
                        else{
                            /**
                             Add user to database
                             */

                            Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();
                            ContentValues cv = new ContentValues(3);
                            cv.put(DataProvider.COL_NAME,user.getDispname());
                            cv.put(DataProvider.COL_EMAIL,user.getEmail());
                            cv.put(DataProvider.COL_ISGROUP,0);
                            cv.put(DataProvider.COL_GCm_ID,user.getGcmid());
                            getApplicationContext().getContentResolver().insert(DataProvider.CONTENT_URI_PROFILE,cv);



                        }
                    }
                }.execute(user);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
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
}
