package webradic.binarymessenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.nio.charset.Charset;

public class Decoder extends AppCompatActivity {

    EditText et;
    Button bt;
    private String pw;
    String datda;
    String decoded_data;
    SharedPreferences spf;
    SharedPreferences.Editor ed;
    private static final Charset UTF_8 = Charset.forName("UTF-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
        et = (EditText) findViewById(R.id.input_pw_decode_ed);
        Intent i = getIntent();
        datda = i.getStringExtra("data");
        String[] byteValues = datda.substring(1,datda.length()-1).split(",");
        byte[] bytes = new byte[byteValues.length];

        for(int j =0,len = bytes.length;j<len;j++){
            bytes[j]=Byte.parseByte(byteValues[j].trim());

        }
        decoded_data =new String(bytes, UTF_8);
        bt = (Button) findViewById(R.id.btn_decode_ok);
        spf = getSharedPreferences("decode", MODE_PRIVATE);
        pw = spf.getString("pw", null);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw1 = et.getText().toString();
                pw1 = pw1.trim();
                if (pw1.equals(pw)) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(Decoder.this);
                    ab.setTitle("Decoded Message");
                    ab.setMessage(decoded_data);
                    ab.setCancelable(true);
                    ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                        AlertDialog al = ab.create();
                    al.show();

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid Key", Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_decoder, menu);
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
