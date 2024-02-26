package com.example.mdevchain;

import com.squareup.picasso.Picasso;

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

public class Custimage extends ArrayAdapter<String>  {

	 private Activity context;       //for to get current activity context
	    SharedPreferences sh;
	private String[] name,image,amount;
	
	
	 public Custimage(Activity context, String[] name,String[] image,String[] amount) {
	        //constructor of this class to get the values from main_activity_class

	        super(context, R.layout.cust_images,image );
	        this.context = context;

	        this.name=name;
	        this.image = image;
	        this.amount = amount;

	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	                 //override getView() method

	        LayoutInflater inflater = context.getLayoutInflater();
	        View listViewItem = inflater.inflate(R.layout.cust_images, null, true);
			//cust_list_view is xml file of layout created in step no.2

	        ImageView im = (ImageView) listViewItem.findViewById(R.id.imageView1);
			TextView t=(TextView)listViewItem.findViewById(R.id.textView);
	        TextView t1=(TextView)listViewItem.findViewById(R.id.textView1);



			t.setText("Name : "+ name[position]);
	        t1.setText("Amount : "+ amount[position] +"\n");


			Log.d("Debug", "Name: " + name[position]);
			Log.d("Debug", "Amount: " + amount[position] );





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