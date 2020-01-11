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

import se.umu.jayo0002.iremind.controllers.OverdueTasksNotifier;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.service.AlarmHandler;
import se.umu.jayo0002.iremind.view.Toaster;
import se.umu.jayo0002.iremind.view_models.TaskViewModel;
import static android.app.Activity.RESULT_OK;


/**
 * This Fragment is for showing the current active Tasks.
 */
public class FragmentTask extends Fragment {
    private SearchView mSearchView;
    private FloatingActionButton mFAB;
    private Task mTask;
    private TaskAdapter mAdapter;
    private TaskViewModel mTaskViewModel;
    private RecyclerView mRV;
    private String mSearchQuery;
    private boolean mIsTheSearchViewUp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTaskViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TaskViewModel.class);
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        mRV = view.findViewById(R.id.recycler_view);
        OverdueTasksNotifier mOverdueTasksNotifier = new OverdueTasksNotifier();
        mAdapter = new TaskAdapter(getContext());
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setHasFixedSize(true);
        setHasOptionsMenu(true);
        mFAB = view.findViewById(R.id.fab);
        mRV.setAdapter(mAdapter);
        boolean mIsThereOverdue = mOverdueTasksNotifier.doControlOverdueTasks(Objects.requireNonNull(getActivity()));
        mTaskViewModel.getActiveTasks().observe(Objects.requireNonNull(getActivity()), tasks -> mAdapter.setAll(tasks));
        if (mIsThereOverdue) Toaster.displayToast(getContext(), Tags.OVERDUE_NOTIFICATION_DETAILS, Tags.LONG_TOAST);
        onClickFAButton();
        onSwipe();
        mAdapter.setOnItemClickListener(task -> {
            mTask = task;
            edit();
        });

        restateSearchView(savedInstanceState);
        return view;
    }

    private void onClickFAButton() {
        mFAB.setOnClickListener((View v) -> {
            Intent intent = new Intent(getContext(), CreateTaskActivity.class);
            startActivityForResult(intent, Tags.REQUEST_CODE_CREATE_EVENT);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        MenuInflater in = Objects.requireNonNull(getActivity()).getMenuInflater();
        in.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.search);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Tags.REQUEST_CODE_CREATE_EVENT && resultCode == RESULT_OK) {
            mTask = Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).getParcelable(Tags.TASK);
            mTaskViewModel.insert(mTask);
            AlarmHandler.scheduleAlarm(Objects.requireNonNull(getActivity()), mTask);
        } else if (requestCode == Tags.REQUEST_CODE_EDIT_EVENT && resultCode == RESULT_OK) {
            mTask = Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).getParcelable(Tags.TASK);
            mTaskViewModel.update(mTask);
            AlarmHandler.cancelAlarm(Objects.requireNonNull(getActivity()), mTask);
            AlarmHandler.scheduleAlarm(Objects.requireNonNull(getActivity()), mTask);
        }
    }

    private void edit() {
        Intent intent = new Intent(getContext(), CreateTaskActivity.class);
        intent.putExtra(Tags.TASK, mTask);
        startActivityForResult(intent, Tags.REQUEST_CODE_EDIT_EVENT);
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
                if (direction == ItemTouchHelper.LEFT) {
                    AlarmHandler.cancelAlarm(Objects.requireNonNull(getActivity()), task);
                    mTaskViewModel.delete(task);
                    Toaster.displaySnack(getView(),Tags.EVENT_DELETED, Tags.LONG_SNACK);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    AlarmHandler.cancelAlarm(Objects.requireNonNull(getActivity()), task);
                    task.setInactive();
                    mTaskViewModel.update(task);
                    Toaster.displaySnack(getView(),Tags.EVENT_DEACTIVATED, Tags.LONG_SNACK);
                }
            }
        }).attachToRecyclerView(mRV);
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
            mTaskViewModel.getActiveTasks().observe(Objects.requireNonNull(getActivity()), tasks -> mAdapter.setAll(tasks));
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

    private void restateSearchView(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            mSearchQuery = savedInstanceState.getString(Tags.SEARCH_QUERY);
            mIsTheSearchViewUp = savedInstanceState.getBoolean(Tags.STATE_OF_THE_SEARCH_VIEW);
        }
    }
}