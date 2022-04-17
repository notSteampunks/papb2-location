package com.example.walkmyandroid1;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressTask extends AsyncTask<Location, Void, String> {

    private Context mContext;
    private OnTaskCompleted mListener;

    interface OnTaskCompleted {
        void OnTaskCompleted(String result);
    }
    public FetchAddressTask(Context context, OnTaskCompleted listener) {
        mContext = context;
        mListener = listener;
    }
    @Override
    protected String doInBackground(Location... locations) {
        Location location = locations[0];
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        List<Address> addresses = null;
        String resultMessage = "";

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses == null || addresses.size() == 0) {
                resultMessage = "Address not found";
            }
            else {
                Address address = addresses.get(0);
                ArrayList<String> parts = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    parts.add(address.getAddressLine(0));
                }

                resultMessage = TextUtils.join("\n", parts);
            }
        }
        catch (IOException ex) {
            Log.d("Exception", "unable to get address");
        }
        return resultMessage;
    }

    @Override
    protected void onPostExecute(String s) {
        mListener.OnTaskCompleted(s);
        super.onPostExecute(s);
    }
}
