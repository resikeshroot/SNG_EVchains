package com.example.mdevchain;

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

import com.squareup.picasso.Picasso;

public class CarListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] brand, price, mileage, engine, safety, fuelType, transmission, seatingCapacity;

    SharedPreferences sh;

    public CarListAdapter(Activity context, String[] brand, String[] price, String[] mileage,
                          String[] engine, String[] safety, String[] fuelType, String[] transmission, String[] seatingCapacity) {
        super(context, R.layout.cus_car_view);
        this.context = context;
        this.brand = brand;
        this.price = price;
        this.mileage = mileage;
        this.engine = engine;
        this.safety = safety;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.seatingCapacity = seatingCapacity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.cus_car_view, null, true);

//        ImageView carImageView = listViewItem.findViewById(R.id.imageView1);
        TextView carName = listViewItem.findViewById(R.id.brd);
        TextView carPrice = listViewItem.findViewById(R.id.prc);
        TextView mil = listViewItem.findViewById(R.id.mil);
        TextView eng = listViewItem.findViewById(R.id.eng);
        TextView sft = listViewItem.findViewById(R.id.sft);
        TextView ft = listViewItem.findViewById(R.id.ft);
        TextView trns = listViewItem.findViewById(R.id.trns);
        TextView sc = listViewItem.findViewById(R.id.sc);

        carName.setText("Brand: " + brand[position]);
        carPrice.setText("Price: " + price[position]);
        mil.setText("Mileage: " + mileage[position]);
        eng.setText("Engine: " + engine[position]);
        sft.setText("Saftey: " + safety[position]);
        ft.setText("Fuel Type: " + fuelType[position]);
        trns.setText("Transmission: " + transmission[position]);
        sc.setText("Seat Capacity: " + seatingCapacity[position]);


//        sh= PreferenceManager.getDefaultSharedPreferences(getContext());
//
//        String pth = "http://"+sh.getString("ip", "")+"/";
//        pth = pth.replace("~", "");
//	       Toast.makeText(context, pth, Toast.LENGTH_LONG).show();

//        Log.d("-------------", pth);
//        Picasso.with(context)
//                .load(pth)
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background).into(im);



        // You can set other TextViews for other car details in a similar way

        return listViewItem;
    }
    private TextView setText(String string) {
        // TODO Auto-generated method stub
        return null;
    }
}
