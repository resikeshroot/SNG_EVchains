package com.example.mdevchain;

import static com.example.mdevchain.Login.logid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class TodaysBooking extends AppCompatActivity implements JsonResponse {
    ListView l1;
    String[] center_id,name,place,val,date,time,stat,amnt,booking_id,slot;
    //    public static String bank_id;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_booking);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=(ListView) findViewById(R.id.lvstaff);


        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) TodaysBooking.this;
        String q = "/User_view_todaysbooking?logid="+logid+"&center="+View_charging_center.centerid;
        q=q.replace(" ","%20");
        JR.execute(q);

    }
    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try{
            String method=jo.getString("method");

            if(method.equalsIgnoreCase("User_view_bank")){
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("check");
                    center_id=new String[ja1.length()];
                    booking_id=new String[ja1.length()];
                    slot=new String[ja1.length()];

                    date=new String[ja1.length()];
                    time=new String[ja1.length()];

                    val=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        center_id[i]=ja1.getJSONObject(i).getString("center_id");
                        booking_id[i]=ja1.getJSONObject(i).getString("book_id");
                        slot[i]=ja1.getJSONObject(i).getString("slots");

                        date[i]=ja1.getJSONObject(i).getString("date");
                        time[i]=ja1.getJSONObject(i).getString("time")+" - "+ja1.getJSONObject(i).getString("end_time");

                        val[i]="Time:  "+time[i] +"\n Slot\t"+slot[i];


                    }
                    ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,val);
                    l1.setAdapter(ar);
                    //startActivity(new Intent(getApplicationContext(),User_Post_Disease.class));
                }

                else

                {
                    Toast.makeText(getApplicationContext(), "No Data!!", Toast.LENGTH_LONG).show();

                }
            }

        }catch(Exception e)
        {
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
        Intent b=new Intent(getApplicationContext(), View_charging_center.class);
        startActivity(b);
    }

}