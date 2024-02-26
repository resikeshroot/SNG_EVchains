//package com.example.mdevchain;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class Pview extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pview);
//    }
//}


package com.example.mdevchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Pview extends ArrayAdapter<String> {

    private Activity context;

    private String[] first_name,last_name ,phone, email;

    public Pview(Activity context, String[] first_name,String[] last_name, String[] phone, String[] email) {
        super(context, R.layout.activity_pview,first_name);
        this.context = context;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_pview, null, true);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t1 = listViewItem.findViewById(R.id.first_name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t2 = listViewItem.findViewById(R.id.last_name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t3 = listViewItem.findViewById(R.id.phone);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t4= listViewItem.findViewById(R.id.email);

        String capitalizedfName = capitalizeFirstLetter(first_name[position]);
        t1.setText("\t\t"+"First Name: " + capitalizedfName);
        String capitalizedlName = capitalizeFirstLetter(last_name[position]);
        t2.setText("\t\t"+"Last Name: " + capitalizedlName);

        t3.setText("\t\t"+"Phone: " + phone[position]);


        String capitalizedEmail = capitalizeFirstLetter(email[position]);
        t4.setText("\t\t"+"Email: " + capitalizedEmail);

        return listViewItem;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
}
