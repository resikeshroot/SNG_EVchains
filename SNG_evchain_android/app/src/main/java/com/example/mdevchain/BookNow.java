package com.example.mdevchain;

import static com.example.mdevchain.Login.logid;
import static com.example.mdevchain.View_charging_center.centerid;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.prefs.Preferences;

public class BookNow extends AppCompatActivity implements JsonResponse, AdapterView.OnItemSelectedListener {

    SharedPreferences sh;

    Spinner s1;
EditText e1,e2,e3,e4,e5;
TextView t1;
Button b1;
String dt,tm,clevel,dlevel,esttime,bcapacity;
public static String slid;

    private int backPressCount = 0;

String[] slot,slot_id;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
//        e1=(EditText)findViewById(R.id.date);
        e2=(EditText)findViewById(R.id.time);
        e3=(EditText)findViewById(R.id.clevel);
        e4=(EditText)findViewById(R.id.dlevel);
        e5=(EditText)findViewById(R.id.bc);
        t1=(TextView) findViewById(R.id.est);
        b1=(Button)findViewById(R.id.button);



        s1=findViewById(R.id.spinner20);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        s1.setOnItemSelectedListener(this);
        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) BookNow.this;
        String q="/slotid?log_id=" + sh.getString("log_id","")+"&center_id="+ centerid;
        q=q.replace(" ","%20");
        JR.execute(q);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dt=e1.getText().toString();
                tm=e2.getText().toString();
                clevel=e3.getText().toString();
                dlevel=e4.getText().toString();
                bcapacity=e5.getText().toString();
                if (tm.equalsIgnoreCase("") || !tm.matches("^([01][0-9]|2[0-3]):[0-5][0-9]$"))
                {
                    e2.setError("Enter time in HH:MM format");
                    e2.setFocusable(true);
                }
                else if (clevel.isEmpty() || !clevel.matches("[0-9]+") || Integer.parseInt(clevel) < 1 || Integer.parseInt(clevel) > 100) {
                    e3.setError("Enter a number between 1 and 100");
                    e3.setFocusable(true);
                }

                else if (dlevel.isEmpty() || !dlevel.matches("[0-9]+") || Integer.parseInt(dlevel) < 1 || Integer.parseInt(dlevel) > 100) {
                    e4.setError("Enter a number between 1 and 100");
                    e4.setFocusable(true);
                }

                else {
                    JsonReq jr = new JsonReq();
                    jr.json_response = (JsonResponse) BookNow.this;
                    String q = "/booknow?time=" + tm + "&center=" + centerid + "&logid=" + logid + "&clevel=" + clevel + "&dlevel=" + dlevel + "&bcapacity=" + bcapacity+"&slid="+slid;
                    q.replace(" ", "%20");
                    jr.execute(q);





                }
            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub

        try
        {
            String method=jo.getString("method");
            String status=jo.getString("status");


            if (method.equalsIgnoreCase("slot")) {
                if (status.equalsIgnoreCase("pass")) {
                    JSONArray ja1 = jo.getJSONArray("data");

                    slot = new String[ja1.length() + 1];  // Add 1 for the dummy item
                    slot_id = new String[ja1.length() + 1];  // Add 1 for the dummy item

                    // Add dummy item at the first position
                    slot[0] = "Select Slot";
                    slot_id[0] = "";  // You can set it to an empty string or any default value

                    for (int i = 0; i < ja1.length(); i++) {
                        slot[i + 1] = ja1.getJSONObject(i).getString("slots");
                        slot_id[i + 1] = ja1.getJSONObject(i).getString("slot_id");
                    }

                    ArrayAdapter<String> ar = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner_dropdown_item, slot);
                    s1.setAdapter(ar);
                } else {
                    // Optionally, you can remove or customize this toast message
                    Toast.makeText(this, "Slot information retrieval failed.", Toast.LENGTH_SHORT).show();
                }
            }

            if (method.equalsIgnoreCase("book")) {
                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "Booked Successfully", Toast.LENGTH_LONG).show();
                    String esttym = jo.getString("estimated_time");
                    t1.setText("Estimated Time:" + esttym + "Hrs");

                    Toast.makeText(getApplicationContext(), "Booked", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),User_home.class));


//                startActivity(new Intent(getApplicationContext(),userhome.class));
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Not Successfull", Toast.LENGTH_LONG).show();

                }
            }




        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hai"+e, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Check which Spinner triggered the event
        if (parent.getId() == R.id.spinner20) {
            // Spinner 1 selected
            slid = slot_id[position];
            // Handle selectedValue1 as needed

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onBackPressed(){

        if (backPressCount == 0) {
            // First back press: Navigate to View_charging_center
            startActivity(new Intent(getApplicationContext(), View_charging_center.class));
            backPressCount++;
        } else if (backPressCount == 1) {
            // Second back press: Navigate to User_home
            startActivity(new Intent(getApplicationContext(), User_home.class));
            backPressCount++;
        }
    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


}