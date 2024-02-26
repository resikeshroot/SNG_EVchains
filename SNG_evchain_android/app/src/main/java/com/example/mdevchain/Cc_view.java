package com.example.mdevchain;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class Cc_view extends ArrayAdapter<String> {

    private Activity context;

    SharedPreferences sh;

    private String[] name,place,phone,email;

    public Cc_view(Activity context, String[] name,String[] place,String[] phone,String[] email) {
        //constructor of this class to get the values from main_activity_class

        super(context, R.layout.activity_cc_view,place );
        this.context = context;

        this.name = name;
        this.place = place;
        this.phone = phone;
        this.email = email;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //override getView() method

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_cc_view, null, true);
        //cust_list_view is xml file of layout created in step no.2


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t=(TextView)listViewItem.findViewById(R.id.name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t1=(TextView)listViewItem.findViewById(R.id.place);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t2=(TextView)listViewItem.findViewById(R.id.phone);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t3=(TextView)listViewItem.findViewById(R.id.email);

        String capitalizedName = capitalizeFirstLetter(name[position]);
        t.setText("Name : " + capitalizedName);

        // Convert the first letter of 'place' to uppercase
        String capitalizedPlace = capitalizeFirstLetter(place[position]);
        t1.setText("Place : " + capitalizedPlace);

        t2.setText("Phone : " + phone[position]);

        // Convert the first letter of 'email' to uppercase
        String capitalizedEmail = capitalizeFirstLetter(email[position]);
        t3.setText("Email : " + capitalizedEmail);



//        t.setText("Name : "+ name[position]);
//        t1.setText("Place : "+ place[position]);
//        t2.setText("Phone : "+ phone[position]);
//        t3.setText("Email : "+ email[position]);














        return  listViewItem;
    }


    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
    private TextView setText(String string) {
        // TODO Auto-generated method stub
        return null;
    }


}