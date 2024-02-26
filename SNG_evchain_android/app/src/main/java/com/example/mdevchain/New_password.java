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

public class New_password extends AppCompatActivity implements JsonResponse {

    EditText e1,e2;

    Button b1;

    String nw,confirm;


    SharedPreferences sh;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        e1=findViewById(R.id.np);
        e2=findViewById(R.id.cp);
        b1=findViewById(R.id.button4);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nw=e1.getText().toString();
                confirm=e2.getText().toString();
                if (nw.equalsIgnoreCase("")) {
                    e1.setError("Enter password");
                    e1.setFocusable(true);
                } else if (confirm.equalsIgnoreCase("")) {
                    e2.setError("Confirm Password");
                    e2.setFocusable(true);
                }  else {

                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) New_password.this;
                    String q = "/new_password?np=" +nw+"&cp="+confirm+"&lid="+sh.getString("log_id","") ;
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

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Login.class));


            }
            else{

                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

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