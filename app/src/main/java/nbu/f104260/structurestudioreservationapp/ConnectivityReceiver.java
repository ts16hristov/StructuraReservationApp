package nbu.f104260.structurestudioreservationapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetworkUtil.isNetworkConnected(context)) {
            Toast.makeText(context, "Lost internet connection!", Toast.LENGTH_SHORT).show();
        }
    }
}