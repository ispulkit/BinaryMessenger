package webradic.binarymessenger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import binary.datahandlers.SessionManager;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {


    SignInButton b2;
    ProgressDialog pgd;
    GoogleApiClient mgapiclient;
    private SharedPreferences spf;
    Toolbar tb;
    private boolean isloggedin;
    private SharedPreferences.Editor editor;
    static ConnectionResult mconresult;
     static boolean gplussigninclicked,gplusintentinprogress;
     final int RC_SIGN_IN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spf = getSharedPreferences("session", Context.MODE_PRIVATE);
        isloggedin = spf.getBoolean("isloggedin", false);
        if (isloggedin) {
            Intent i = new Intent();
            SessionManager.login_email=spf.getString("email",null);
            i.setClass(this,Secure.class);
            startActivity(i);
        } else {


            tb=(Toolbar)findViewById(R.id.toolbar_reg);
            setSupportActionBar(tb);
            getSupportActionBar().setTitle("Binary Messenger");
            b2 = (SignInButton) findViewById(R.id.btn_sign_in_g);


            pgd = new ProgressDialog(this);
            pgd.setCancelable(false);
            pgd.setTitle("Please Wait");

            b2.setOnClickListener(this);
            mgapiclient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API).
                            addScope(Plus.SCOPE_PLUS_LOGIN).
                            build();




        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RC_SIGN_IN){
            if(resultCode!=RESULT_OK){
                gplussigninclicked=false;
            }

            gplusintentinprogress=false;
            if(!mgapiclient.isConnecting()){
                mgapiclient.connect();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
    public void onConnected(Bundle bundle) {


        String email=null;
        gplusintentinprogress = false;
        gplussigninclicked = false;
        if(Plus.PeopleApi.getCurrentPerson(mgapiclient)!=null){
            Person CurrentPerson = Plus.PeopleApi.getCurrentPerson(mgapiclient);
             email = Plus.AccountApi.getAccountName(mgapiclient);
        }
    if(email!=null&&email!="") {
        spf = getSharedPreferences("session", Context.MODE_PRIVATE);
        editor = spf.edit();
        editor.putBoolean("isloggedin", true);
        editor.putString("via", "g");
        editor.putString("email", email);
        editor.commit();
        Intent i = new Intent();
        i.setClass(this,Secure.class);
        startActivity(i);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mgapiclient.connect();
    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.btn_sign_in_g:
            signinwithg();
            Log.e("Register Activity","in switch case signinwithg");
            break;
    }
    }

    @Override
    protected void onStart() {
        super.onStart();
        spf = getSharedPreferences("session", Context.MODE_PRIVATE);
        isloggedin = spf.getBoolean("isloggedin", false);
        if(!isloggedin)
        mgapiclient.connect();
    }

    private void signinwithg() {

        if(!mgapiclient.isConnecting()){
            gplussigninclicked=true;
            Log.e("In signinwithg","mgapiclient is not connecting..resolving error");
            resolvesigninerror();
        }

    }

    private void resolvesigninerror(){
        Log.e("In resolveswitherror: ",mconresult.hasResolution()+"");
        if(mconresult.hasResolution()){
            gplusintentinprogress=true;

            try{
                mconresult.startResolutionForResult(this,RC_SIGN_IN);
            }catch (IntentSender.SendIntentException e){
                gplusintentinprogress=false;
                mgapiclient.connect();
            }
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

            Log.e("Connection failed", connectionResult.getErrorMessage() + "hb");
        if(!connectionResult.hasResolution()){
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),this,0).show();
            Log.e("No Resolution !",connectionResult.getErrorMessage()+connectionResult.getErrorCode());
            return;

        }
        if(!gplusintentinprogress) {
            mconresult = connectionResult;
            if (gplussigninclicked) {
                resolvesigninerror();

            }
        }
    }
       public void signoutofgplus(){
           Plus.AccountApi.clearDefaultAccount(mgapiclient);
           mgapiclient.disconnect();
       }


}

