package com.example.mdevchain;

import android.view.View;
import android.widget.AdapterView;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonResponse {
    public void response(JSONObject jo) throws JSONException;

    void onItemClick(AdapterView<?> adapterView, View view, int i, long l);

    void onItemSelected(AdapterView<?> parent, View view, int position, long id);

    void onNothingSelected(AdapterView<?> parent);
}
