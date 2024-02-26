package com.example.mdevchain;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class View_charging_center extends AppCompatActivity implements JsonResponse {

    ListView l1;
    Button b1,b2;
    EditText e1;

    String[] center_id,slot_id,login_id, name, place,email,phone,latitude,longitude, value;
    public static String centerid,slid,search_center,lat,longit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_charging_center);

        l1 = findViewById(R.id.view_cc);

        e1=(EditText) findViewById(R.id.search);
        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) View_charging_center.this;
                String q = "/view_charging_center";
                q=q.replace(" ","%20");
                JR.execute(q);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                search_center=e1.getText().toString();

                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) View_charging_center.this;
                String q = "/Search_center?search_center="+search_center;
                q=q.replace(" ","%20");
                JR.execute(q);

            }
        });


        b1=(Button)findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) View_charging_center.this;
                String q = "/User_view_bunk_review";
                q=q.replace(" ","%20");
                JR.execute(q);
            }
        });
        b2=findViewById(R.id.button23);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) View_charging_center.this;
                String q = "/view_cc?lati="+LocationService.lati+"&longi="+LocationService.logi;
                q = q.replace(" ", "%20");
                JR.execute(q);
            }
        });

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                centerid=center_id[position];
                lat = latitude[position];
                longit = longitude[position];

//                final CharSequence[] items = {"Book Now","Todays Bookings","View Faculties","Cancel"};
                final CharSequence[] items = {"Book Now","Todays Bookings","Track Location","Send Feedback"};


                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(View_charging_center.this);
                builder.setTitle("Select Option!");
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        // TODO Auto-generated method stub
                        if (items[item].equals("Book Now"))
                        {
                            Intent il=new Intent(getApplicationContext(),BookNow.class);
                            startActivity(il);
                        }
                        else if (items[item].equals("Todays Bookings"))
                        {
                            Intent il=new Intent(getApplicationContext(),TodaysBooking.class);
                            startActivity(il);
                        }
                        else if (items[item].equals("Track Location"))
                        {

                            String uri = "http://maps.google.com/maps?q=loc:" + lat + "," + longit;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps"); // Specify package for Google Maps
                            startActivity(intent);


                        }
                        else if (items[item].equals("Send Feedback"))
                        {
                            Intent il=new Intent(getApplicationContext(),Send_feedback.class);
                            startActivity(il);
                        }
                        else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }

                    }
                });
                builder.show();
            }
        });

        String q = "/view_charging_center?log_id=" + Login.logid;
        q=q.replace(" ","%20");
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) View_charging_center.this;
        JR.execute(q);


    }


    @Override
    public void response(JSONObject jo) throws JSONException {

        try {

            String method = jo.getString("method");

            String status = jo.getString("status");

            if(method.equalsIgnoreCase("cc_view")){
            if(status.equalsIgnoreCase("success")){
                JSONArray ja=jo.getJSONArray("data");

                center_id=new String[ja.length()];

                name=new String[ja.length()];
                place=new String[ja.length()];
                email=new String[ja.length()];
                phone=new String[ja.length()];
                latitude=new String[ja.length()];
                longitude=new String[ja.length()];
                login_id=new String[ja.length()];

                value=new String[ja.length()];


                for(int i=0;i<ja.length();i++){

                    center_id[i]=ja.getJSONObject(i).getString("center_id");

                    name[i]=ja.getJSONObject(i).getString("name");
                    place[i]=ja.getJSONObject(i).getString("place");
                    email[i]=ja.getJSONObject(i).getString("email");
                    phone[i]=ja.getJSONObject(i).getString("phone");
                    latitude[i]=ja.getJSONObject(i).getString("latitude");
                    longitude[i]=ja.getJSONObject(i).getString("longitude");
                    login_id[i]=ja.getJSONObject(i).getString("login_id");

                    value[i]="Name : "+ name[i] + "\nPlace : "+ place[i]+"\nEmail : "+email[i]+"\nPhone : "+phone[i]+"\nLatitude : "+latitude[i]+"\nLongitude : "+longitude[i]+"\n";

                }
//                ArrayAdapter<String> ar=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,value);
//                l1.setAdapter(ar);

                Cc_view cv=new Cc_view(View_charging_center.this,name,place,phone,email);

                l1.setAdapter(cv);






            }

        }
            else if(method.equalsIgnoreCase("search")){
                if(status.equalsIgnoreCase("success")){
                    JSONArray ja=jo.getJSONArray("data");

                    center_id=new String[ja.length()];

                    name=new String[ja.length()];
                    place=new String[ja.length()];
                    email=new String[ja.length()];
                    phone=new String[ja.length()];
                    latitude=new String[ja.length()];
                    longitude=new String[ja.length()];
                    login_id=new String[ja.length()];

                    value=new String[ja.length()];


                    for(int i=0;i<ja.length();i++){

                        center_id[i]=ja.getJSONObject(i).getString("center_id");

                        name[i]=ja.getJSONObject(i).getString("name");
                        place[i]=ja.getJSONObject(i).getString("place");
                        email[i]=ja.getJSONObject(i).getString("email");
                        phone[i]=ja.getJSONObject(i).getString("phone");
                        latitude[i]=ja.getJSONObject(i).getString("latitude");
                        longitude[i]=ja.getJSONObject(i).getString("longitude");
                        login_id[i]=ja.getJSONObject(i).getString("login_id");

                        value[i]="Name : "+ name[i] + "\nPlace : "+ place[i]+"\nEmail : "+email[i]+"\nPhone : "+phone[i]+"\nLatitude : "+latitude[i]+"\nLongitude : "+longitude[i]+"\n";

                    }
//                ArrayAdapter<String> ar=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,value);
//                l1.setAdapter(ar);

                    Cc_view cv=new Cc_view(View_charging_center.this,name,place,phone,email);

                    l1.setAdapter(cv);






                }

            } else if(method.equalsIgnoreCase("nearme")){
                if(status.equalsIgnoreCase("success")){
                    JSONArray ja=jo.getJSONArray("data");

                    center_id=new String[ja.length()];

                    name=new String[ja.length()];
                    place=new String[ja.length()];
                    email=new String[ja.length()];
                    phone=new String[ja.length()];
                    latitude=new String[ja.length()];
                    longitude=new String[ja.length()];
                    login_id=new String[ja.length()];

                    value=new String[ja.length()];


                    for(int i=0;i<ja.length();i++){

                        center_id[i]=ja.getJSONObject(i).getString("center_id");

                        name[i]=ja.getJSONObject(i).getString("name");
                        place[i]=ja.getJSONObject(i).getString("place");
                        email[i]=ja.getJSONObject(i).getString("email");
                        phone[i]=ja.getJSONObject(i).getString("phone");
                        latitude[i]=ja.getJSONObject(i).getString("latitude");
                        longitude[i]=ja.getJSONObject(i).getString("longitude");
                        login_id[i]=ja.getJSONObject(i).getString("login_id");

                        value[i]="Name : "+ name[i] + "\nPlace : "+ place[i]+"\nEmail : "+email[i]+"\nPhone : "+phone[i]+"\nLatitude : "+latitude[i]+"\nLongitude : "+longitude[i]+"\n";

                    }
//                ArrayAdapter<String> ar=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,value);
//                l1.setAdapter(ar);

                    Cc_view cv=new Cc_view(View_charging_center.this,name,place,phone,email);

                    l1.setAdapter(cv);






                }

            }else if(method.equalsIgnoreCase("User_view_bank")){
                if(status.equalsIgnoreCase("success")){
                    JSONArray ja=jo.getJSONArray("check");

                    center_id=new String[ja.length()];

                    name=new String[ja.length()];
                    place=new String[ja.length()];
                    email=new String[ja.length()];
                    phone=new String[ja.length()];
                    latitude=new String[ja.length()];
                    longitude=new String[ja.length()];
                    login_id=new String[ja.length()];

                    value=new String[ja.length()];


                    for(int i=0;i<ja.length();i++){

                        center_id[i]=ja.getJSONObject(i).getString("center_id");

                        name[i]=ja.getJSONObject(i).getString("name");
                        place[i]=ja.getJSONObject(i).getString("place");
                        email[i]=ja.getJSONObject(i).getString("email");
                        phone[i]=ja.getJSONObject(i).getString("phone");
                        latitude[i]=ja.getJSONObject(i).getString("latitude");
                        longitude[i]=ja.getJSONObject(i).getString("longitude");
                        login_id[i]=ja.getJSONObject(i).getString("login_id");

                        value[i]="Name : "+ name[i] + "\nPlace : "+ place[i]+"\nEmail : "+email[i]+"\nPhone : "+phone[i]+"\nLatitude : "+latitude[i]+"\nLongitude : "+longitude[i]+"\n";

                    }
//                ArrayAdapter<String> ar=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,value);
//                l1.setAdapter(ar);

                    Cc_view cv=new Cc_view(View_charging_center.this,name,place,phone,email);

                    l1.setAdapter(cv);






                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

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
        // Your custom behavior here
        // For example, you might want to navigate to a different activity, show a dialog, or finish the current activity
        startActivity(new Intent(getApplicationContext(),User_home.class));
    }

}