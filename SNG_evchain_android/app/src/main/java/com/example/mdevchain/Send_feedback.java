

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Send_feedback extends AppCompatActivity  implements JsonResponse {
    EditText t1;
    Button b1;
    ListView l1;
    String feedback;
    SharedPreferences sh;
    String [] uid,fb,val;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);


        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        t1 =  findViewById(R.id.feedback);
        b1 = (Button) findViewById(R.id.cbtn);
        l1 =(ListView) findViewById(R.id.view);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Send_feedback.this;
        String q = "/view_feedback?log_id=" + sh.getString("log_id", "")+"&cenid=" + View_charging_center.centerid;
        q = q.replace(" ", "%20");
        JR.execute(q);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback = t1.getText().toString();

                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) Send_feedback.this;
                String q = "/user_feedback?log_id=" + sh.getString("log_id", "") + "&feedback=" + feedback + "&cenid=" + View_charging_center.centerid;
                q = q.replace(" ", "%20");
                JR.execute(q);
                startActivity(new Intent(getApplicationContext(),Send_feedback.class));
            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {



            String method = jo.getString("method");
            Log.d("result", method);
//                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();

            if (method.equalsIgnoreCase("send_feedback")){
                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {


                    Toast.makeText(getApplicationContext(), "Feedback Send Successfull", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), View_charging_center.class));
                    //startService(new Intent(getApplicationContext(),Notiservise.class));
                }
//

            }
            else if (method.equalsIgnoreCase("view_feedback")) {
                String status = jo.getString("status");
                Log.d("result", status);
                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                if (status.equalsIgnoreCase("success")) {

                    JSONArray ja = (JSONArray) jo.getJSONArray("data");


                    uid = new String[ja.length()];
                    fb = new String[ja.length()];

                    val = new String[ja.length()];


                    for (int i = 0; i < ja.length(); i++) {

                        uid[i] = ja.getJSONObject(i).getString("sender_id");
                        fb[i] = ja.getJSONObject(i).getString("feedback");



                        val[i] = "Feedback: " + fb[i]+"\n";
                    }
                    l1.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, val));


                }
            }





        } catch (Exception e) {
            e.printStackTrace();
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

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(), View_booked_slots.class);
        startActivity(b);
    }

}