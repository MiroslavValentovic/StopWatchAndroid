package sk.valentovic.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private EditText mEtLaps;
    private ImageButton buttonStart;
    private ImageButton buttonStop;
    private ImageButton buttonLap;
    private ScrollView mSvLaps;
    private TextView txtLap;
    private AlphaAnimation buttonClick;

    private int mLaps = 1;

    private Context mContext;
    private Chronometer mChronometer;
    private Thread mThreadChrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mTextView = (TextView) findViewById(R.id.textView);
        txtLap = (TextView) findViewById(R.id.textView4);
        mEtLaps = (EditText) findViewById(R.id.et_laps);
        mSvLaps = (ScrollView) findViewById(R.id.sv_laps);
        buttonStart = (ImageButton) findViewById(R.id.buttonStart);
        buttonStop = (ImageButton) findViewById(R.id.buttonStop);
        buttonLap = (ImageButton) findViewById(R.id.buttonLap);

        mEtLaps.setGravity(Gravity.CENTER_HORIZONTAL);
        mEtLaps.setEnabled(false);

        buttonClick = new AlphaAnimation(1F, 0.5F);


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStart.startAnimation(buttonClick);

                if(mChronometer == null) {
                    txtLap.setText("Lap");
                    mChronometer = new Chronometer(mContext);
                    mThreadChrono = new Thread(mChronometer);
                    mThreadChrono.start();
                    mChronometer.start();

                    mLaps = 1;
                    mEtLaps.setText("");

                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStop.startAnimation(buttonClick);
                if(mChronometer != null) {
                    txtLap.setText("Reset");
                    mChronometer.stop();
                    mThreadChrono.interrupt();
                    mThreadChrono = null;
                    mChronometer = null;
                }
            }
        });

        buttonLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLap.startAnimation(buttonClick);
                if (mChronometer == null) {
                    mEtLaps.setText("");
                    mTextView.setText("00:00:00:000");

                } else {

                    mEtLaps.append("LAP " + String.valueOf(mLaps) + "       " + " "
                            + String.valueOf(mTextView.getText()) + "\n");

                mLaps++;

                mSvLaps.post(new Runnable() {
                    @Override
                    public void run() {
                        mEtLaps.setScroller(new Scroller(mContext));
                        mEtLaps.setMaxLines(100);
                        mEtLaps.setVerticalScrollBarEnabled(true);
                        mEtLaps.setMovementMethod(new ScrollingMovementMethod());
                        mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
                    }
                });
            }
            }


        });

    }

    public void updateTimerText(final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(time);
            }
        });
    }

}