package com.example.mdevchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Sh_view extends ArrayAdapter<String> {

    private Activity context;

    private String[] name, phone, email;

    public Sh_view(Activity context, String[] name, String[] phone, String[] email) {
        super(context, R.layout.activity_sh_view,name);
        this.context = context;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_sh_view, null, true);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView name1 = listViewItem.findViewById(R.id.name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView phone1 = listViewItem.findViewById(R.id.phone);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView email1= listViewItem.findViewById(R.id.email);

        String capitalizedName = capitalizeFirstLetter(name[position]);
        name1.setText("\t\t"+"Name: " + capitalizedName);

        phone1.setText("\t\t"+"Phone: " + phone[position]);


        String capitalizedEmail = capitalizeFirstLetter(email[position]);
        email1.setText("\t\t"+"Email: " + capitalizedEmail);

        return listViewItem;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
}
