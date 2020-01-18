package se.umu.jayo0002.iremind;

import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;
import java.util.Objects;

/**
 *  This a base fragment for the other fragments.
 *  The purpose of this fragment is to allow other fragments to inherit mutual methods from this fragment.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * It calls the adapter in order to call its filter.
     * @param filter
     */
    abstract void callAdapter(String filter);

    /**
     * It passes the left swiped ViewHolder, to carry out the desired function.
     * @param viewHolder
     */
    abstract void onLeftSwipe(@NonNull RecyclerView.ViewHolder viewHolder);

    /**
     * It passes the right swiped ViewHolder, to carry out the desired function.
     * @param viewHolder
     */
    abstract void onRightSwipe(@NonNull RecyclerView.ViewHolder viewHolder);

    /**
     * It collapses the menu and it iconify the search view.
     * @param searchView
     * @param menuItem
     */
    public void collapseMenu(SearchView searchView, MenuItem menuItem) {
        if (searchView != null && menuItem != null && menuItem.isActionViewExpanded()){
                menuItem.collapseActionView();
                searchView.setIconified(true);
                UIUtil.hideKeyboard(Objects.requireNonNull(getActivity()));
        }
    }

    /**
     * It sets a listen to the menu item in order to either show or hide the keyboard.
     * @param searchView
     * @param menuItem
     * @param inputMethodManager
     */
    public void setMenuItemOnActionExpandListener(SearchView searchView, MenuItem menuItem, InputMethodManager inputMethodManager){
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                UIUtil.hideKeyboard(Objects.requireNonNull(getActivity()));
                searchView.setQuery("", false);
                return true;
            }
        });
    }

    /**
     * It iconify the search view and closes the keyboard.
     * @param searchView
     */
    public void destroyMenu(SearchView searchView){
        if (searchView != null ){
            searchView.setIconified(true);
            UIUtil.hideKeyboard(Objects.requireNonNull(getActivity()));
        }
    }

    /**
     * It sets a listener to the search view in order to detect query test.
     * @param searchView
     * @param menuItem
     */
    public void setSearchViewOnQueryTextListener(SearchView searchView, MenuItem menuItem){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                menuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                callAdapter(s);
                return false;
            }
        });
    }

    /**
     * It updates the menu.
     * @param menuItem
     * @param searchView
     * @param searchQuery
     * @param doesNeedUpdate
     * @return boolean
     */
    public boolean onUpdateMenu(MenuItem menuItem, SearchView searchView, String searchQuery, boolean doesNeedUpdate){
        menuItem.expandActionView();
        searchView.onActionViewExpanded();
        searchView.setQuery(searchQuery, false);
        searchView.setFocusable(true);
        return !doesNeedUpdate;
    }

    /**
     * It attaches Item Touch Helper to the target Recycle View.
     * @param recyclerView
     */
    public void registerRecyclerViewOnItemTouchHelper(RecyclerView recyclerView) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    onLeftSwipe(viewHolder);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    onRightSwipe(viewHolder);
                }
            }
        }).attachToRecyclerView(recyclerView);
    }
}
