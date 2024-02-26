package com.example.mdevchain;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class enter_email extends AppCompatActivity implements JsonResponse{


    EditText e1,e2;

    Button b1;

    String em,ph;

    SharedPreferences sh;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        e1=findViewById(R.id.mail);
        e2=findViewById(R.id.hone);
        b1=findViewById(R.id.submit);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                em=e1.getText().toString();
                ph=e2.getText().toString();


                if (em.equalsIgnoreCase("")) {
                    e1.setError("Enter email");
                    e1.setFocusable(true);
                } else if (ph.equalsIgnoreCase("")) {
                    e2.setError("Enter phone number");
                    e2.setFocusable(true);
                }  else {

                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) enter_email.this;
                    String q = "/forgot_password?email=" +em+"&phone="+ph+"&lid="+sh.getString("log_id","") ;
                    q = q.replace(" ", "%20");
                    JR.execute(q);
                }

            }
        });


    }

    @Override
    public void response(JSONObject jo) throws JSONException {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {

                Toast.makeText(getApplicationContext(), "Confirm", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), New_password.class));


            }

            else{
                Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
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
}