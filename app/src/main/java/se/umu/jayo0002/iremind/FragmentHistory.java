package se.umu.jayo0002.iremind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.service.AlarmHandler;
import se.umu.jayo0002.iremind.view.Toaster;
import se.umu.jayo0002.iremind.view_models.TaskViewModel;

/**
 * This Fragment is for showing the old and inactive Tasks.
 */
public class FragmentHistory extends BaseFragment {
    private SearchView mSearchView;
    private TaskAdapter mAdapter;
    private TaskViewModel mTaskViewModel;
    private String mSearchQuery;
    private boolean mDoesMenuNeedUpdate;
    private InputMethodManager mInputMethodManager;
    private MenuItem mMenuItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTaskViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TaskViewModel.class);
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mInputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        RecyclerView mRV = view.findViewById(R.id.recycler_view1);
        mAdapter = new TaskAdapter(getContext());
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setHasFixedSize(true);
        setHasOptionsMenu(true);
        mRV.setAdapter(mAdapter);
        mTaskViewModel.getInactiveTasks().observe(Objects.requireNonNull(getActivity()), tasks -> mAdapter.setTasks(tasks));
        registerRecyclerViewOnItemTouchHelper(mRV);
        mAdapter.setOnItemClickListener(task -> {
            UIUtil.hideKeyboard(Objects.requireNonNull(getActivity()));
            Intent intent = new Intent(getActivity(), OpenTaskActivity.class);
            intent.putExtra(Tags.TASK_LAUNCHED_FROM_AN_ACTIVITY, task);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        });
        reinitializeValues(savedInstanceState);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        MenuInflater in = Objects.requireNonNull(getActivity()).getMenuInflater();
        in.inflate(R.menu.menu2, menu);
        mMenuItem = menu.findItem(R.id.search2);
        mSearchView = (SearchView) mMenuItem.getActionView();
        mSearchView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mSearchView.setIconifiedByDefault(false);
        updateSearchView(mDoesMenuNeedUpdate);
        setSearchViewOnQueryTextListener(mSearchView, mMenuItem);
        setMenuItemOnActionExpandListener(mSearchView, mMenuItem, mInputMethodManager);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        destroyMenu(mSearchView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            collapseMenu(mSearchView, mMenuItem);
            if (Objects.requireNonNull(mTaskViewModel.getInactiveTasks().getValue()).isEmpty())
                Toaster.displayToast(getActivity(), Tags.NO_ARCHIVE, Tags.LONG_TOAST);
            else
                mTaskViewModel.deleteAllInactiveTasks();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSearchView(boolean isTheStateOut) {
        if (isTheStateOut) {
            mTaskViewModel.getInactiveTasks().observe(Objects.requireNonNull(getActivity()),
                    tasks -> mAdapter.setTasks(tasks));
            mAdapter.getFilter().filter(mSearchQuery);
            mDoesMenuNeedUpdate = onUpdateMenu(mMenuItem,mSearchView,mSearchQuery, mDoesMenuNeedUpdate);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mMenuItem != null && mMenuItem.isActionViewExpanded()) {
            outState.putString(Tags.SEARCH_QUERY, mSearchQuery);
            outState.putBoolean(Tags.STATE_OF_THE_SEARCH_VIEW, true);
            mSearchView.setQuery("", false);
        }
        super.onSaveInstanceState(outState);
    }

    private void reinitializeValues(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSearchQuery = savedInstanceState.getString(Tags.SEARCH_QUERY);
            mDoesMenuNeedUpdate = savedInstanceState.getBoolean(Tags.STATE_OF_THE_SEARCH_VIEW);
        }
    }

    @Override
    void callAdapter(String filter) {
        mSearchQuery = filter;
        mAdapter.getFilter().filter(filter);
    }

    @Override
    void onLeftSwipe(@NonNull RecyclerView.ViewHolder viewHolder) {
        collapseMenu(mSearchView,mMenuItem);
        Task task = mAdapter.getTaskAt(viewHolder.getAdapterPosition());
        mTaskViewModel.delete(task);
        Toaster.displaySnack(getView(), Tags.EVENT_DELETED, Tags.SHORT_SNACK);
    }

    @Override
    void onRightSwipe(@NonNull RecyclerView.ViewHolder viewHolder) {
        collapseMenu(mSearchView,mMenuItem);
        Task task = mAdapter.getTaskAt(viewHolder.getAdapterPosition());
        if (task.setActive()) {
            mTaskViewModel.update(task);
            AlarmHandler.scheduleAlarm(Objects.requireNonNull(getContext()), task);
            Toaster.displaySnack(getView(), Tags.EVENT_ACTIVATED, Tags.SHORT_SNACK);
        } else {
            mAdapter.notifyDataSetChanged();
            Toaster.displaySnack(getView(), Tags.EVENT_INVALID, Tags.SHORT_SNACK);
        }
    }
}