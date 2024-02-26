package com.example.mdevchain;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class View_booked_slots extends AppCompatActivity implements JsonResponse {

    ListView l1;

    String[] book_id,center_id,slot,user_id,date,end_time,Status,slots,time,power,amount,login_id,name,place,email,phone,latitude,longitude, value;
    public static String cenid,bookid,st,amnt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booked_slots);

        l1 = findViewById(R.id.view_bs);

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cenid=center_id[position];
                bookid=book_id[position];
                st=Status[position];
                amnt=amount[position];
                final CharSequence[] items = {"Pay Now","Cancel Request","Feedback","Cancel"};

                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(View_booked_slots.this);
                builder.setTitle("Select Option!");
                // Inside onItemClick method
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        // TODO Auto-generated method stub
                        if (items[item].equals("Feedback")) {
                            Intent il = new Intent(getApplicationContext(), Send_feedback.class);
                            startActivity(il);
                        } else if (items[item].equals("Cancel Request")) {
                            if (st.equalsIgnoreCase("cancelled")) {
                                Toast.makeText(getApplicationContext(), "Request cancelled", Toast.LENGTH_LONG).show();
                            } else {
                                // Proceed with cancel request
                                JsonReq JR = new JsonReq();
                                JR.json_response = (JsonResponse) View_booked_slots.this;
                                String q = "/User_cancel_booking?logid=" + Login.logid + "&bookid=" + bookid;
                                q = q.replace(" ", "%20");
                                JR.execute(q);
                                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();

                                Intent il = new Intent(getApplicationContext(), View_booked_slots.class);
                                startActivity(il);
                            }
                        } else if (items[item].equals("Pay Now")) {
                            if (st.equalsIgnoreCase("cancelled")) {
                                Toast.makeText(getApplicationContext(), "Request cancelled", Toast.LENGTH_LONG).show();
                            } else if (st.equalsIgnoreCase("booked")) {
                                Intent il = new Intent(getApplicationContext(), user_pay_amount.class);
                                startActivity(il);
                            } else if (st.equalsIgnoreCase("paid")) {
                                Toast.makeText(View_booked_slots.this, "Already paid", Toast.LENGTH_SHORT).show();
                            } else if (st.equalsIgnoreCase("rejected")) {
                                Toast.makeText(View_booked_slots.this, "Your Request rejected", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(View_booked_slots.this, "Your Request is not accepted yet", Toast.LENGTH_SHORT).show();
                            }
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        String q = "/view_booked_slots?log_id=" + Login.logid;
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) View_booked_slots.this;
        JR.execute(q);


    }


    @Override
    public void response(JSONObject jo) throws JSONException {

        try {

            String status = jo.getString("status");
            if(status.equalsIgnoreCase("success")){
                JSONArray ja=jo.getJSONArray("data");

                center_id=new String[ja.length()];
                book_id=new String[ja.length()];
                slot=new String[ja.length()];
                user_id=new String[ja.length()];
                date=new String[ja.length()];
                end_time=new String[ja.length()];
                Status=new String[ja.length()];
                slots=new String[ja.length()];
                time=new String[ja.length()];
                power=new String[ja.length()];
                amount=new String[ja.length()];


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
                    book_id[i]=ja.getJSONObject(i).getString("book_id");
                    slot[i]=ja.getJSONObject(i).getString("slots");
                    user_id[i]=ja.getJSONObject(i).getString("user_id");
                    date[i]=ja.getJSONObject(i).getString("date");
                    time[i]=ja.getJSONObject(i).getString("time");
                    end_time[i]=ja.getJSONObject(i).getString("end_time");
                    power[i]=ja.getJSONObject(i).getString("power");
                    amount[i]=ja.getJSONObject(i).getString("amount");
                    Status[i]=ja.getJSONObject(i).getString("status");
                    slots[i]=ja.getJSONObject(i).getString("slots");


                    name[i]=ja.getJSONObject(i).getString("name");
                    place[i]=ja.getJSONObject(i).getString("place");
                    email[i]=ja.getJSONObject(i).getString("email");
                    phone[i]=ja.getJSONObject(i).getString("phone");
                    latitude[i]=ja.getJSONObject(i).getString("latitude");
                    longitude[i]=ja.getJSONObject(i).getString("longitude");
                    login_id[i]=ja.getJSONObject(i).getString("login_id");

                    value[i]="Slot :"+slot[i]+"\nDate :"+date[i]+"\nTime :"+time[i]+"\nEnd Time :"+end_time[i]+"\nAmount :"+amount[i]+"\nStatus :"+Status[i]+"\nName : "+ name[i] + "\nPlace : "+ place[i]+"\nEmail : "+email[i]+"\nPhone : "+phone[i]+"\nLatitude : "+latitude[i]+"\nLongitude : "+longitude[i]+"\n";

                }
                ArrayAdapter<String> ar=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,value);
                l1.setAdapter(ar);

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
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), User_home.class);
        startActivity(intent);
    }
}