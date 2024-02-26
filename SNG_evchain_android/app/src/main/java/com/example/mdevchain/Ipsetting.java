//package com.example.mdevchain;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.preference.PreferenceManager;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//
//public class Ipsetting extends Activity {
//
//    EditText e1;
//    Button b1;
//    public static String text;
//    SharedPreferences sh;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ipsetting);
//        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//
//        e1 = findViewById(R.id.et);
//        e1.setText(sh.getString("ip:", "192.168"));
//        b1 = findViewById(R.id.btn);
//
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                text = e1.getText().toString();
//
//                if (text.equals("")) {
//                    e1.setText("Enter ip address");
//                    e1.setFocusable(true);
//                } else {
//                    // Show progress bar
//                    showProgressBar();
//
//                    // Simulate a delay (replace this with your actual network operation)
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            hideProgressBar();
//                            // Save IP and navigate to login page
//                            SharedPreferences.Editor e = sh.edit();
//                            e.putString("ip", text);
//                            e.apply();
//                            startActivity(new Intent(getApplicationContext(), Login.class));
//                        }
//                    }, 2000); // Simulating a 2-second delay
//                }
//            }
//        });
//    }
//
//    private void showProgressBar() {
//        // Show the progress bar page
//        setContentView(R.layout.activity_progress_bar);
//    }
//
//    private void hideProgressBar() {
//        // Restore the original layout
//        setContentView(R.layout.activity_ipsetting);
//    }
//
//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Exit:")
//                .setMessage("Are you sure you want to exit?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        Intent i = new Intent(Intent.ACTION_MAIN);
//                        i.addCategory(Intent.CATEGORY_HOME);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
//                        finish();
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }
//}

package com.example.mdevchain;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class Ipsetting extends Activity {

    EditText e1;
    Button b1;
    ProgressBar progressBar;
    TextView loadingText;
    public static String text;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipsetting);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        e1 = findViewById(R.id.et);
        e1.setText(sh.getString("ip:", "192.168"));
        b1 = findViewById(R.id.btn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = e1.getText().toString();

                if (text.equals("")) {
                    e1.setText("Enter ip address");
                    e1.setFocusable(true);
                } else {
                    // Show progress bar
                    showProgressBar();

                    // Simulate a delay (replace this with your actual network operation)
                    simulateLoading();
                }
            }
        });
    }

    private void showProgressBar() {
        setContentView(R.layout.activity_progress_bar);
        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);
    }

    private void simulateLoading() {
        final int totalProgressTime = 100;
        final Handler handler = new Handler();
        final int delay = 5; // Milliseconds between each progress update

        new Thread(new Runnable() {
            int progress = 0;

            @Override
            public void run() {
                while (progress <= totalProgressTime) {
                    try {
                        Thread.sleep(delay);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progress);
                                loadingText.setText("Loading... " + progress + "%");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress++;
                }

                // Hide progress bar and navigate to login page
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressBar();
                        // Save IP and navigate to login page
                        SharedPreferences.Editor e = sh.edit();
                        e.putString("ip", text);
                        e.apply();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    }
                });
            }
        }).start();
    }

    private void hideProgressBar() {
        setContentView(R.layout.activity_ipsetting);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit:")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}

