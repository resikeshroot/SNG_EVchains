package com.example.mdevchain;

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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class user_pay_amount extends AppCompatActivity implements JsonResponse{
    EditText e1,e2,e3,e4,e5;
    Button b1;
    String holder,cvv,expdate,amount,no;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pay_amount);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.et1);
        e2=(EditText)findViewById(R.id.et2);
        e3=(EditText)findViewById(R.id.et3);
        e4=(EditText)findViewById(R.id.et4);
        e5=(EditText)findViewById(R.id.et5);
        e4.setText(View_booked_slots.amnt);
        e4.setEnabled(false);
        b1=(Button)findViewById(R.id.btn1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder=e1.getText().toString();
                no=e5.getText().toString();
                cvv=e2.getText().toString();
                expdate=e3.getText().toString();
                amount=e4.getText().toString();

                if(holder.equalsIgnoreCase("")||!holder.matches("[A-Za-z]+"))
                {
                    e1.setError("Enter your first name");
                    e1.setFocusable(true);
                }
                else if(no.equalsIgnoreCase("")||!no.matches("[0-9]+")||no.length()!=16)
                {
                    e5.setError("Enter a valid Account");
                    e5.setFocusable(true);
                }

                else if(cvv.equalsIgnoreCase("")||!cvv.matches("[0-9]+")||cvv.length()!=3)
                {
                    e2.setError("Enter a valid cvv");
                    e2.setFocusable(true);
                }
                else if(expdate.equalsIgnoreCase(""))
//                ||!expdate.matches("[0-9\\-0-9\\-0-9]+")
                {
                    e3.setError("Enter a valid date ");
                    e3.setFocusable(true);
                }
                else if(amount.equalsIgnoreCase(""))
                {
                    e4.setError("Enter a valid cvv");
                    e4.setFocusable(true);
                }
                else
                {
                    JsonReq JR=new JsonReq();
                    JR.json_response=(JsonResponse) user_pay_amount.this;
                    String q="/user_pay_amount?bookid="+View_booked_slots.bookid+"&amount="+View_booked_slots.amnt;
                    q=q.replace(" ","%20");
                    JR.execute(q);
                }}
        });
    }
    @Override
    public void response(JSONObject jo) {
        try {
            String status=jo.getString("status");
            Log.d("pearl",status);


            if(status.equalsIgnoreCase("success")){

                Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), View_booked_slots.class));

            }
            else if(status.equalsIgnoreCase("duplicate")){


                startActivity(new Intent(getApplicationContext(), User_home.class));
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

            }

            else
            {
                startActivity(new Intent(getApplicationContext(),User_home.class));

                Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // TODO: handle exception
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