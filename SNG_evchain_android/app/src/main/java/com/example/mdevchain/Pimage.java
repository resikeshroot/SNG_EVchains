package com.example.mdevchain;

import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Pimage extends ArrayAdapter<String>  {

    private Activity context;       //for to get current activity context
    SharedPreferences sh;
    private String[] fname,lname,desc,phone,image,amount;


    public Pimage(Activity context,String[]fname, String[] lname,String[] phone,String[] desc,String[] image,String[] amount) {
        //constructor of this class to get the values from main_activity_class

        super(context, R.layout.activity_pimage,image );
        this.context = context;

        this.fname=fname;
        this.lname=lname;
        this.desc=desc;
        this.phone=phone;
        this.image = image;
        this.amount = amount;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //override getView() method

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_pimage, null, true);
        //cust_list_view is xml file of layout created in step no.2

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView im = (ImageView) listViewItem.findViewById(R.id.imageView1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t=(TextView)listViewItem.findViewById(R.id.textView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t1=(TextView)listViewItem.findViewById(R.id.textView1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t2=(TextView)listViewItem.findViewById(R.id.textView2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t3=(TextView)listViewItem.findViewById(R.id.textView3);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t4=(TextView)listViewItem.findViewById(R.id.textView4);



        t.setText("First Name : "+ fname[position]);
        t1.setText("Last Name : "+ lname[position]);
        t2.setText("Description : "+ desc[position]);
        t4.setText("Phone : "+ phone[position]);
        t3.setText("Amount : "+ amount[position]);


        Log.d("Debug", "First Name: " + fname[position]);
        Log.d("Debug", "Amount: " + amount[position]);





        sh=PreferenceManager.getDefaultSharedPreferences(getContext());

        String pth = "http://"+sh.getString("ip", "")+"/"+image[position];
        pth = pth.replace("~", "");
//	       Toast.makeText(context, pth, Toast.LENGTH_LONG).show();

        Log.d("-------------", pth);
        Picasso.with(context)
                .load(pth)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background).into(im);

        return  listViewItem;
    }

    private TextView setText(String string) {
        // TODO Auto-generated method stub
        return null;
    }
}