package webradic.binarymessenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Secure extends AppCompatActivity {

    EditText et;
    Button bt;
    SharedPreferences spf;
    SharedPreferences.Editor ed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure);
        et = (EditText) findViewById(R.id.input_pw_set_ed);
        bt = (Button)findViewById(R.id.btn_set_pw);
        spf = getSharedPreferences("decode",MODE_PRIVATE);
        String pw = spf.getString("pw",null);
        if(pw!=null&&!pw.equals("")){
            Intent i = new Intent(Secure.this,SignupNameChoser.class);
            startActivity(i);
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = et.getText().toString();
                pw = pw.trim();
                if(pw==null||pw.equals("")){
                    Snackbar.make(findViewById(android.R.id.content),"Enter Valid Password",Snackbar.LENGTH_LONG).show();
                }else{

                    ed = spf.edit();
                    ed.putString("pw",pw);
                    ed.commit();
                    Intent i = new Intent(Secure.this,SignupNameChoser.class);
                    startActivity(i);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secure, menu);
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
