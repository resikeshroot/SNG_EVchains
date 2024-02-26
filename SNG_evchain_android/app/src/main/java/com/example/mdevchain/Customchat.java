package com.example.mdevchain;



import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class Customchat extends ArrayAdapter<String>
{
    //needs to extend ArrayAdapter

    private String[] message;         //for custom view name item


    private String[] time;
    private String[] sid;	//for custom view photo items
    private Activity context;       //for to get current activity context
    SharedPreferences sh;
    public Customchat(Activity context, String[] message, String[] sid, String[] time)
    {
        //constructor of this class to get the values from main_activity_class

        super(context, R.layout.activity_customchat,message);
        this.context = context;
        this.message = message;


        this.time= time;

        this.sid= sid;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sh = PreferenceManager.getDefaultSharedPreferences(getContext());
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_customchat, null, true);


        TextView t1 = listViewItem.findViewById(R.id.rm);

        TextView t2 = listViewItem.findViewById(R.id.sm);

        if (sid[position].equalsIgnoreCase(Login.logid)) {
           t2.setText(""+"\t\t"+message[position]+ ""+"\n  \t \t \t \t \t \t \t \t \t \t \t \t \t \t "+time[position]+"");
           t2.setBackgroundResource(R.drawable.send_mssg);

            // Align sent messages to the right
        } else {
            t1.setText(""+"\t\t\t"+message[position]+""+"\n \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t  "+time[position]+"");
            t1.setBackgroundResource(R.drawable.received);
        }
        sh = PreferenceManager.getDefaultSharedPreferences(getContext());
        return listViewItem;
    }








}