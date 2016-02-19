package binary.splash;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import webradic.binarymessenger.R;
import webradic.binarymessenger.RegisterActivity;

public class Splash1 extends AppCompatActivity {

    TextView t1, t2, t3, t4, t5, t6;
    FloatingActionButton b1;
    SharedPreferences spf;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash1);
        spf = getSharedPreferences("session", MODE_PRIVATE);
        boolean islo = spf.getBoolean("isloggedin", false);
        if (islo) {
            Intent i = new Intent(Splash1.this, RegisterActivity.class);
            startActivity(i);
        } else {


            t1 = (TextView) findViewById(R.id.tv1_splash);
            t6 = (TextView) findViewById(R.id.tv6_splash);
            t2 = (TextView) findViewById(R.id.tv2_splash);
            t3 = (TextView) findViewById(R.id.tv3_splash);
            t4 = (TextView) findViewById(R.id.tv4_splash);
            t5 = (TextView) findViewById(R.id.tv5_splash);
            b1 = (FloatingActionButton) findViewById(R.id.floating_b1);
            iv = (ImageView) findViewById(R.id.imageView_splash_logo);
            Splash1.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Animation a = new AlphaAnimation(0.00f, 1.00f);
                    a.setDuration(4000);
                    a.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            iv.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    iv.startAnimation(a);
                    t1.animate().alpha(1.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            t1.setVisibility(View.VISIBLE);
                            t2.animate().alpha(1.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    t2.setVisibility(View.VISIBLE);

                                    t3.animate().alpha(1.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            t3.setVisibility(View.VISIBLE);
                                            t4.animate().alpha(1.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    t4.setVisibility(View.VISIBLE);

                                                    t5.animate().alpha(1.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                                        @Override
                                                        public void onAnimationStart(Animator animation) {

                                                        }

                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            t5.setVisibility(View.VISIBLE);

                                                            t6.animate().alpha(1.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                                                @Override
                                                                public void onAnimationStart(Animator animation) {

                                                                }

                                                                @Override
                                                                public void onAnimationEnd(Animator animation) {
                                                                    t6.setVisibility(View.VISIBLE);

                                                                    b1.animate().alpha(1.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                                                        @Override
                                                                        public void onAnimationStart(Animator animation) {

                                                                        }

                                                                        @Override
                                                                        public void onAnimationEnd(Animator animation) {
                                                                            b1.setVisibility(View.VISIBLE);
                                                                        }

                                                                        @Override
                                                                        public void onAnimationCancel(Animator animation) {

                                                                        }

                                                                        @Override
                                                                        public void onAnimationRepeat(Animator animation) {

                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onAnimationCancel(Animator animation) {

                                                                }

                                                                @Override
                                                                public void onAnimationRepeat(Animator animation) {

                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onAnimationCancel(Animator animation) {

                                                        }

                                                        @Override
                                                        public void onAnimationRepeat(Animator animation) {

                                                        }
                                                    });


                                                }

                                                @Override
                                                public void onAnimationCancel(Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationRepeat(Animator animation) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Splash1.this, RegisterActivity.class);
                            startActivity(i);

                        }
                    });


                }


            });


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash1, menu);
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
