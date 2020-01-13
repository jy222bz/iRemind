package se.umu.jayo0002.iremind.view;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;


/**
 * This Class is meant to display either snack or toast.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2020-01-04
 */
public class Toaster {


    /**
     * It displays a toast.
     *
     * @param context
     * @param message
     * @param isLong
     */
    public static void displayToast(Context context, String message, int isLong){
        Toast.makeText(context, message, isLong).show();
    }

    /**
     * It displays a snack.
     *
     * @param view
     * @param message
     * @param isLong
     */
    public static void displaySnack(View view, String message, int isLong){
        Snackbar snackbar = Snackbar.make(view, message, isLong);
        snackbar.show();
    }

}
