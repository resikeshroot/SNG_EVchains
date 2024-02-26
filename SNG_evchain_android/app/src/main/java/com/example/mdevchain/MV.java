//package com.example.mdevchain;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class MV extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mv);
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

public class MV extends ArrayAdapter<String> {

    private Activity context;

    private String[] brd, eng, lno,clr;

    public MV(Activity context, String[] brd, String[] eng, String[] lno,String[] clr) {
        super(context, R.layout.activity_mv, brd);
        this.context = context;
        this.brd = brd;
        this.eng = eng;
        this.lno = lno;
        this.clr = clr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_mv, null, true);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t1 = listViewItem.findViewById(R.id.brd);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t2 = listViewItem.findViewById(R.id.eng);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t3= listViewItem.findViewById(R.id.lno);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t4= listViewItem.findViewById(R.id.clr);

        String capitalizedName = capitalizeFirstLetter(brd[position]);
        t1.setText("\t\t"+"NAME : " + capitalizedName);

        t2.setText("\t\t"+"ENGINE : " + eng[position]);
        t3.setText("\t\t"+"LICENSE NO : " + lno[position]);


        String capitalizedEmail = capitalizeFirstLetter(clr[position]);
        t4.setText("\t\t"+"COLOR : " + capitalizedEmail);

        return listViewItem;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
}
