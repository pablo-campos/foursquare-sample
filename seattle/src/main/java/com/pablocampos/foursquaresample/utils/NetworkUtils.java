package com.pablocampos.foursquaresample.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.pablocampos.foursquaresample.R;

public class NetworkUtils {



	public static boolean checkInternet(final Context context){

		// Test internet connection
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;	// We have internet
		} else {

			// No internet, display error dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(context)
					.setTitle(context.getResources().getString(R.string.no_internet_error_title))
					.setMessage(context.getResources().getString(R.string.no_internet_error_message))
					.setPositiveButton(context.getResources().getString(R.string.no_internet_error_button), null)
					.setIconAttribute(android.R.attr.alertDialogIcon);
			builder.show();

			return false;
		}
	}

}
