package com.example.mdevchain;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity implements JsonResponse {


    EditText e1, e2, e3, e4, e5, e6, e7, e8, e9;

    Button b1;

    RadioGroup r1;

    String fname, lname, place, phone, email, uname, password, address, gender;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        e1 = (EditText) findViewById(R.id.fname);
        e2 = (EditText) findViewById(R.id.lname);
        e3 = (EditText) findViewById(R.id.address);


        e5 = (EditText) findViewById(R.id.phone);


        e7 = (EditText) findViewById(R.id.email);
        e8 = (EditText) findViewById(R.id.uname);
        e9 = (EditText) findViewById(R.id.password);
        r1 = findViewById(R.id.gender);

        r1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                gender = radioButton.getText().toString();

            }
        });







        b1 = findViewById(R.id.register);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = e1.getText().toString();
                lname = e2.getText().toString();

                address = e3.getText().toString();

                phone = e5.getText().toString();
                email = e7.getText().toString();
                uname = e8.getText().toString();
                password = e9.getText().toString();




                if (!fname.trim().matches("[a-zA-Z]+")) {
                    e1.setError("Enter only alphabets for first name");
                    e1.setFocusable(true);
                } else if (!lname.trim().matches("[a-zA-Z ]+")) {
                    e2.setError("Enter only alphabets for first name");
                    e2.setFocusable(true);
                } else if (address.equalsIgnoreCase("")) {
                    e3.setError("Enter Your Address");
                    e3.setFocusable(true);

                } else if (!phone.matches("\\d{10}")) {
                    e5.setError("Enter a 10-digit phone number");
                    e5.setFocusable(true);

                } else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    e7.setError("Enter Your Email");
                    e7.setFocusable(true);
                } else if (uname.equalsIgnoreCase("")) {
                    e8.setError("Enter Your User Name");
                    e8.setFocusable(true);
                } else if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@]).{8,}$")){
                    e9.setError("Password must contain at least 8 characters, including at least one digit, one lowercase letter, one uppercase letter, and '@'");
                    e9.setFocusable(true);
                } else {

                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) Register.this;
                    String q = "/user_reg?fname=" + fname + "&lname=" + lname + "&address=" + address + "&phone=" + phone + "&email=" + email + "&uname=" + uname + "&password=" + password  + "&gender=" + gender ;
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

                Toast.makeText(getApplicationContext(), "ADDED", Toast.LENGTH_LONG).show();
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




