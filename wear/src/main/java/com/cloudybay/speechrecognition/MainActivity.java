package com.cloudybay.speechrecognition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class MainActivity extends Activity {

    private TextView mTextView;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE = 0;
    private float tipPercent = .15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }


    public void onClickMe(View view){
        startSpeechRecognition();
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,      RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_RECOGNIZER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_RECOGNIZER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                String recognizedText = results.get(0);
                //Try to extract the total bill amount in a whole number

                try {
                    float billAmount = Float.parseFloat(recognizedText);
                    // calculate tip and total
                    float tipAmount = billAmount * tipPercent;
                    float totalAmount = billAmount + tipAmount;

                    // Display the tip on the watch
                    NumberFormat currency = NumberFormat.getCurrencyInstance();
                    mTextView.setText(currency.format(totalAmount));
                }
                catch(NumberFormatException ex) {
                    mTextView.setText("Enter a whole number!");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
