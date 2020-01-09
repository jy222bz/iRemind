package se.umu.jayo0002.iremind.view;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;



public class Toaster {


    public static void displayToast(Context context, String message, int isLong){
        Toast.makeText(context, message, isLong).show();
    }

    public static void displaySnack(View view, String message, int isLong){
        Snackbar snackbar = Snackbar.make(view, message, isLong);
        snackbar.show();
    }

}
