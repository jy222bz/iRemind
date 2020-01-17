package se.umu.jayo0002.iremind;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.service.AlarmHandler;
import se.umu.jayo0002.iremind.view.Toaster;
import se.umu.jayo0002.iremind.view_models.TaskViewModel;

import static android.app.Activity.RESULT_OK;


/**
 * This Fragment is for showing the current active Tasks.
 */
public class FragmentTask extends BaseFragment {
    private SearchView mSearchView;
    private FloatingActionButton mFAB;
    private Task mTask;
    private TaskAdapter mAdapter;
    private TaskViewModel mTaskViewModel;
    private RecyclerView mRV;
    private String mSearchQuery;
    private boolean mDoesMenuNeedUpdate;
    private InputMethodManager mInputMethodManager;
    private MenuItem mMenuItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTaskViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TaskViewModel.class);
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        mInputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        mRV = view.findViewById(R.id.recycler_view);
        mAdapter = new TaskAdapter(getContext());
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setHasFixedSize(true);
        setHasOptionsMenu(true);
        mFAB = view.findViewById(R.id.fab);
        mRV.setAdapter(mAdapter);
        mTaskViewModel.getActiveTasks().observe(Objects.requireNonNull(getActivity()), tasks -> mAdapter.setTasks(tasks));
        onClickFAButton();
        onSwipe();
        mAdapter.setOnItemClickListener(task -> {
            mTask = task;
            edit();
        });
        reinitializeValues(savedInstanceState);
        return view;
    }

    private void onClickFAButton() {
        mFAB.setOnClickListener((View v) -> {
            collapseMenu(mSearchView, mMenuItem);
            Intent intent = new Intent(getContext(), CreateTaskActivity.class);
            startActivityForResult(intent, Tags.REQUEST_CODE_CREATE_EVENT);
        });
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        if (mSearchView != null ){
            mSearchView.setIconified(true);
            UIUtil.hideKeyboard(Objects.requireNonNull(getActivity()));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        MenuInflater in = Objects.requireNonNull(getActivity()).getMenuInflater();
        in.inflate(R.menu.menu, menu);
        mMenuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) mMenuItem.getActionView();
        mSearchView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mSearchView.setIconifiedByDefault(false);
        updateSearchView(mDoesMenuNeedUpdate);
        setSearchViewOnQueryTextListener(mSearchView, mMenuItem);
        setMenuItemOnActionExpandListener(mSearchView, mMenuItem, mInputMethodManager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Tags.REQUEST_CODE_CREATE_EVENT && resultCode == RESULT_OK) {
            mTask = Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).getParcelable(Tags.TASK);
            mTaskViewModel.add(mTask);
            AlarmHandler.scheduleAlarm(Objects.requireNonNull(getActivity()), mTask);
            Toaster.displaySnack(getView(), Tags.ALARM_SET, Tags.SHORT_SNACK);
        } else if (requestCode == Tags.REQUEST_CODE_EDIT_EVENT && resultCode == RESULT_OK) {
            mTask = Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).getParcelable(Tags.TASK);
            mTaskViewModel.update(mTask);
            AlarmHandler.cancelAlarm(Objects.requireNonNull(getActivity()), mTask);
            AlarmHandler.scheduleAlarm(Objects.requireNonNull(getActivity()), mTask);
            Toaster.displaySnack(getView(), Tags.ALARM_IS_UPDATED, Tags.SHORT_SNACK);
        }
    }

    private void edit() {
        collapseMenu(mSearchView, mMenuItem);
        Intent intent = new Intent(getContext(), CreateTaskActivity.class);
        intent.putExtra(Tags.TASK, mTask);
        startActivityForResult(intent, Tags.REQUEST_CODE_EDIT_EVENT);
    }

    private void onSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                collapseMenu(mSearchView, mMenuItem);
                Task task = mAdapter.getTaskAt(viewHolder.getAdapterPosition());
                if (direction == ItemTouchHelper.LEFT) {
                    AlarmHandler.cancelAlarm(Objects.requireNonNull(getActivity()), task);
                    mTaskViewModel.delete(task);
                    Toaster.displaySnack(getView(), Tags.EVENT_DELETED, Tags.SHORT_SNACK);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    AlarmHandler.cancelAlarm(Objects.requireNonNull(getActivity()), task);
                    task.setInactive();
                    mTaskViewModel.update(task);
                    Toaster.displaySnack(getView(), Tags.EVENT_DEACTIVATED, Tags.SHORT_SNACK);
                }
            }
        }).attachToRecyclerView(mRV);
    }

    private void updateSearchView(boolean isTheStateOut) {
        if (isTheStateOut) {
            mTaskViewModel.getActiveTasks().observe(Objects.requireNonNull(getActivity()),
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
}