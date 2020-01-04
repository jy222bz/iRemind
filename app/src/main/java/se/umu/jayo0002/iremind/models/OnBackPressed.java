package se.umu.jayo0002.iremind.models;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import java.util.Objects;

public class OnBackPressed implements IBackUp {

    private Activity mActivity;
    private SearchView mSearchView;

    public OnBackPressed(Activity activity, SearchView searchView){
        mActivity = activity;
        mSearchView = searchView;
    }

    @Override
    public boolean onBackPressed() {
        View view = Objects.requireNonNull(mActivity).getCurrentFocus();
        boolean isKeyboardShowing = false;
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (Objects.requireNonNull(imm).isAcceptingText()){
                Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
                isKeyboardShowing = true;
                if (mSearchView != null &&
                        !mSearchView.getQuery().toString().isEmpty())
                    mSearchView.setIconified(true);
            }
        }
        return isKeyboardShowing;
    }
}
