package com.example.mdevchain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class User_profile extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    String[] first_name, last_name, hname, place, phone, email, sid, lid,idcard, value;
    public static String student_id;

    Button b1;
    SharedPreferences sh;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        l1 = findViewById(R.id.studentid);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1=findViewById(R.id.button400);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), User_home.class));
            }
        });

            JsonReq JR = new JsonReq();
            JR.json_response = this;
            String q = "/view_profile?log_id=" + Login.logid;
            q = q.replace(" ", "%20");
            JR.execute(q);
        }


    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            String method = jo.getString("method");

            if (method.equalsIgnoreCase("viewprofile")) {
                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = jo.getJSONArray("data");
                    int length = ja1.length();
                    first_name = new String[length];

                    last_name = new String[length];


                    phone = new String[length];
                    email = new String[length];

                    value = new String[length];

                    for (int i = 0; i < length; i++) {
                        first_name[i] = ja1.getJSONObject(i).getString("first_name");

                        last_name[i] = ja1.getJSONObject(i).getString("last_name");

                        email[i] = ja1.getJSONObject(i).getString("email");
                        phone[i] = ja1.getJSONObject(i).getString("phone");


                        value[i] = "First Name : " + first_name[i] + "\nLast Name : " + last_name[i]  + "\nEmail : " + email[i] + "\nPhone : " + phone[i];
                    }

//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
//                    l1.setAdapter(adapter);


                    Pview pv=new Pview(User_profile.this,first_name,last_name,phone,email);

                    l1.setAdapter(pv);





                } else {
                    Toast.makeText(getApplicationContext(), "No messages", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Student_profile", "Exception: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), User_home.class);
        startActivity(intent);
    }
}
