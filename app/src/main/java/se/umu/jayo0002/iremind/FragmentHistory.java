package se.umu.jayo0002.iremind;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Objects;
import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.service.AlarmHandler;
import se.umu.jayo0002.iremind.view.Toaster;
import se.umu.jayo0002.iremind.view_models.TaskViewModel;

/**
 * This Fragment is for showing the old and inactive Tasks.
 */
public class FragmentHistory extends Fragment {
    private SearchView mSearchView;
    private FloatingActionButton mFAB;
    private TaskRepo mTaskRepo;
    private Task mTask;
    private TaskAdapter mAdapter;
    private RecyclerView mRV;
    private TaskViewModel mTaskViewModel;
    private String mSearchQuery;
    private boolean mIsTheSearchViewUp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTaskViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TaskViewModel.class);
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mRV = view.findViewById(R.id.recycler_view1);
        mAdapter = new TaskAdapter(getContext());
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setHasFixedSize(true);
        setHasOptionsMenu(true);
        mRV.setAdapter(mAdapter);
        mTaskViewModel.getInactiveTasks().observe(Objects.requireNonNull(getActivity()), tasks -> mAdapter.setTasks(tasks));
        onSwipe();
        mAdapter.setOnItemClickListener(task -> {
            mTask = task;
            Intent intent = new Intent(getActivity(), OpenTaskActivity.class);
            intent.putExtra(Tags.NEW_LAUNCH, task);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        });

        if (savedInstanceState != null) {
            mSearchQuery = savedInstanceState.getString(Tags.SEARCH_QUERY);
            mIsTheSearchViewUp = savedInstanceState.getBoolean(Tags.STATE_OF_THE_SEARCH_VIEW);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        MenuInflater in = Objects.requireNonNull(getActivity()).getMenuInflater();
        in.inflate(R.menu.menu2, menu);
        MenuItem item = menu.findItem(R.id.search2);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        updateSearchView(mIsTheSearchViewUp);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchView.setQuery("", false);
                mSearchView.setIconified(true);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                mSearchQuery = s;
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        if (mSearchView != null &&
                !mSearchView.getQuery().toString().isEmpty())
            mSearchView.setIconified(true);
    }

    private void onSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                 ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Task task = mAdapter.getTaskAt(viewHolder.getAdapterPosition());
                if (direction == ItemTouchHelper.LEFT){
                    mTaskViewModel.delete(task);
                    Toaster.displaySnack(getView(),Tags.EVENT_DELETED, Tags.LONG_SNACK);
                }else if (direction == ItemTouchHelper.RIGHT) {
                    if (task.setActive()){
                        mTaskViewModel.update(task);
                        AlarmHandler.scheduleAlarm(Objects.requireNonNull(getContext()),task);
                        Toaster.displaySnack(getView(),Tags.EVENT_ACTIVATED, Tags.LONG_SNACK);
                    } else{
                        mAdapter.notifyDataSetChanged();
                        Toaster.displaySnack(getView(),Tags.EVENT_INVALID, Tags.LONG_SNACK);
                    }
                }
            }
        }).attachToRecyclerView(mRV);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete){
            if (Objects.requireNonNull(mTaskViewModel.getInactiveTasks().getValue()).size() == 0)
                Toaster.displayToast(getActivity(),Tags.NO_ARCHIVE, Tags.LONG_TOAST);
            else
                mTaskViewModel.deleteAllInactiveTasks();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSearchView(boolean isTheStateOut) {
        if (isTheStateOut) {
            SearchManager searchManager =
                    (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
            mSearchView.setSearchableInfo(
                    Objects.requireNonNull(searchManager).getSearchableInfo(getActivity().getComponentName()));
            mSearchView.onActionViewExpanded();
            mSearchView.setQuery(mSearchQuery, true);
            mSearchView.clearFocus();
            mTaskViewModel.getInactiveTasks().observe(Objects.requireNonNull(getActivity()), tasks -> mAdapter.setTasks(tasks));
            mAdapter.getFilter().filter(mSearchQuery);
            mIsTheSearchViewUp = false;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mSearchQuery != null && !mSearchView.getQuery().toString().isEmpty()) {
            outState.putString(Tags.SEARCH_QUERY, mSearchQuery);
            outState.putBoolean(Tags.STATE_OF_THE_SEARCH_VIEW, true);
            mSearchView.setQuery("", false);
        }
        super.onSaveInstanceState(outState);
    }
}