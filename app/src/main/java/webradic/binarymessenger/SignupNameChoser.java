package webradic.binarymessenger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.wr.dellpc.binary.backend.initApi.InitApi;
import com.wr.dellpc.binary.backend.initApi.model.Responser;
import com.wr.dellpc.binary.backend.initApi.model.User;
import java.io.IOException;

import binary.datahandlers.SessionManager;

public class SignupNameChoser extends AppCompatActivity {

    private static String regid;
    private  String GCMSENDERID;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    private static GoogleCloudMessaging gcm;
    Button Continue;
    EditText dispnameed;
      ProgressDialog pgd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_name_choser);
        GCMSENDERID = getResources().getString(R.string.gcm_sender_id);
        Continue = (Button)findViewById(R.id.btn_conti);
        dispnameed = (EditText)findViewById(R.id.input_register_name_ed);
        spf = getSharedPreferences("user_details",Context.MODE_PRIVATE);
        String name = spf.getString("name",null);
        if(name!=null&&!name.equals("null")){
            Intent i = new Intent();
            SessionManager.login_email=spf.getString("email",null);
            SessionManager.loginval=true;
            SessionManager.via="g";
            i.setClass(this,MainActivity.class);
            startActivity(i);

        }else {


            pgd = new ProgressDialog(SignupNameChoser.this);
            pgd.setCancelable(false);

            pgd.setTitle("Please Wait..");
            new getgcm().execute();


        }
    }
    class getgcm extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            gcm=GoogleCloudMessaging.getInstance(getApplicationContext());
            try {
                regid = gcm.register(GCMSENDERID);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Signupnamechoser", "Cannot register for gcm" + e.toString());
                finish();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pgd.dismiss();
            checkexistanceonserver();
        }
    }

    private void checkexistanceonserver(){
        spf = getSharedPreferences("session", Context.MODE_PRIVATE);
        String email = spf.getString("email",null);
        new checkforexistanceAsync().execute(email);



    }
private class checkforexistanceAsync extends AsyncTask<String,User,User>{


    private InitApi iapi = null;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pgd.setTitle("Checking your profile");
        pgd.setMessage("Please Wait");


    }

    @Override
    protected User doInBackground(String... params) {
        if(iapi==null){
            InitApi.Builder builder = new InitApi.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(),null)
                    .setRootUrl("https://binary-messenger.appspot.com/_ah/api");

            iapi = builder.build();

        }

        String email = params[0];
        User u1 = new User();
        User u2 = new User();
        u1.setEmail(email);
        u1.setGcmid("null");
        try{

            u2= iapi.checkUser(u1).execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException in chckuser", "signupnamechoser" + e.toString());
            u2.setGcmid("null");
            u2.setEmail("null");
            u2.setDispname("null");
        }finally {
            return u2;
        }


    }

    @Override
    protected void onPostExecute(User user) {
        pgd.dismiss();
        if(user.getEmail().equals("null")&&user.getGcmid().equals("null")&&user.getDispname().equals("null")){
            Toast.makeText(getApplicationContext(),"Cannot retrieve data",Toast.LENGTH_LONG).show();
        }else if(!user.getEmail().equals("null")&&(user.getGcmid().equals("null"))){

            /**
             * USER DOESN'T EXIST, CREATE A USER ON SERVER
             *
             */
            final String email = user.getEmail();

            Snackbar.make(findViewById(android.R.id.content),"Please input a Display Name to continue",Snackbar.LENGTH_LONG)
                    .show();

                            Continue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (dispnameed.getText().toString() != null) {
                                        /**
                                         * store to backend
                                         */
                                    User u = new User();
                                        u.setEmail(email);
                                        u.setGcmid(regid);
                                        u.setDispname(dispnameed.getText().toString());
                                        createserveruser(u);

                                    } else {
                                        /**
                                         * Data enterd is null
                                         */
                                        Snackbar.make(findViewById(android.R.id.content), "Enter Valid data", Snackbar.LENGTH_LONG)
                                                .show();
                                    }

                                }
                            });


        }
        else{
            Toast.makeText(getApplicationContext(),"Welcome Back",Toast.LENGTH_LONG).show();
            if(!regid.equals(user.getGcmid())) {
                /**
                 * UPDATE THE DATASTORE WITH NEW GCMID
                 */
                spf = getSharedPreferences("user_details", Context.MODE_PRIVATE);
                editor = spf.edit();
                editor.putString("name", user.getDispname());
                editor.putString("gcm", regid);
                editor.commit();
                Intent i = new Intent();
                i.setClass(SignupNameChoser.this, MainActivity.class);
                startActivity(i);
            }else{
                spf = getSharedPreferences("user_details", Context.MODE_PRIVATE);
                editor = spf.edit();
                editor.putString("name", user.getDispname());
                editor.putString("gcm", user.getGcmid());
                editor.commit();
                Intent i = new Intent();
                i.setClass(SignupNameChoser.this, MainActivity.class);
                startActivity(i);
            }

        }
    }
}

    private void createserveruser(User user){
        new AsyncTask<User,Void,User>(){
            private InitApi iapi = null;
            @Override
            protected void onPreExecute() {

            pgd.setMessage("Loading");
                pgd.setTitle("Please Wait");
                pgd.show();
            }

            @Override
            protected User doInBackground(User... params) {
                if(iapi==null) {
                    InitApi.Builder builder = new InitApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl("https://binary-messenger.appspot.com/_ah/api");

                    iapi = builder.build();
                }

                    User u = params[0];
                    Responser r = new Responser();
                   try{
                       r= iapi.createUser(u).execute();
                   } catch (IOException e) {
                       e.printStackTrace();
                       Log.e("AddtoServerAsync",e.toString());
                   }
                if(r.getResponse().equals("fail")){
                    u.setDispname("null");
                    u.setEmail("null");
                    u.setGcmid("null");
                }

        return u;



            }

            @Override
            protected void onPostExecute(User user) {
               pgd.dismiss();
                if(user.getEmail().equals("null")){
                    Snackbar.make(findViewById(android.R.id.content),"Failed to register :( ",Snackbar.LENGTH_LONG).show();
                }else{
                    spf = getSharedPreferences("user_details", Context.MODE_PRIVATE);
                    editor = spf.edit();
                    editor.putString("name", user.getDispname());
                    editor.putString("gcm", user.getGcmid());
                    editor.commit();
                    Intent i = new Intent();
                    i.setClass(SignupNameChoser.this, MainActivity.class);
                    startActivity(i);
                }

            }
        }.execute(user);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup_name_choser, menu);
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
