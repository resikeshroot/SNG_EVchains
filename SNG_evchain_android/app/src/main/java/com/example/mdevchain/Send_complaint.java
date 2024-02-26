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

public class Send_complaint extends AppCompatActivity  implements JsonResponse {
    EditText t1;
    Button b1;
    ListView l1;
    String complaints;
    SharedPreferences sh;
    String [] uid,comp,rply,date,val;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_complaint);


        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        t1 =  findViewById(R.id.complaint);
        b1 = (Button) findViewById(R.id.cbtn);
        l1 =(ListView) findViewById(R.id.view);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Send_complaint.this;
        String q = "/view_complaint?log_id=" + sh.getString("log_id", "");
        q = q.replace(" ", "%20");
        JR.execute(q);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complaints = t1.getText().toString();

                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) Send_complaint.this;
                String q = "/user_complaint?log_id=" + sh.getString("log_id", "") + "&complaints=" + complaints ;
                q = q.replace(" ", "%20");
                JR.execute(q);
            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {



            String method = jo.getString("method");
            Log.d("result", method);
//                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();

            if (method.equalsIgnoreCase("user_complaint")){
                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {


                    Toast.makeText(getApplicationContext(), "Complaint Send Successfull", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), Send_complaint.class));
                    //startService(new Intent(getApplicationContext(),Notiservise.class));
                }
//

            }
            else if (method.equalsIgnoreCase("User_feedbak")) {
                String status = jo.getString("status");
                Log.d("result", status);

                if (status.equalsIgnoreCase("success")) {

                    JSONArray ja = (JSONArray) jo.getJSONArray("data");


                    uid = new String[ja.length()];
                    comp = new String[ja.length()];
                    rply = new String[ja.length()];
                    date = new String[ja.length()];
                    val = new String[ja.length()];


                    for (int i = 0; i < ja.length(); i++) {

                        uid[i] = ja.getJSONObject(i).getString("sender_id");
                        comp[i] = ja.getJSONObject(i).getString("complaint");
                        rply[i] = ja.getJSONObject(i).getString("reply");
                        date[i] = ja.getJSONObject(i).getString("date_time");


                        val[i] = "Complaint: " + comp[i]+"\nReply :" + rply[i] + "\nDate: " + date[i]+"\n";
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
        Intent b=new Intent(getApplicationContext(),User_home.class);
        startActivity(b);
    }

}