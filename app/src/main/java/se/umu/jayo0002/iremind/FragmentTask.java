package se.umu.jayo0002.iremind;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.notifications.AlarmHandler;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTaskViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TaskViewModel.class);
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        mRV = view.findViewById(R.id.recycler_view);
        mAdapter = new TaskAdapter(getContext());
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setHasFixedSize(true);
        setHasOptionsMenu(true);
        mFAB = view.findViewById(R.id.fab);
        mRV.setAdapter(mAdapter);
        mTaskViewModel.getActiveTasks().observe(Objects.requireNonNull(getActivity()), tasks -> mAdapter.setAll(tasks));
        onClickFAButton();
        onSwipe();
        mAdapter.setOnItemClickListener(task -> {
            mTask = task;
            edit();
        });


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
        in.inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.search);
        mSearchView= (SearchView)item.getActionView();
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchView.setQuery("", false);
                mSearchView.setIconified(true);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
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
        if (requestCode == Tags.REQUEST_CODE_CREATE_EVENT && resultCode == RESULT_OK){
            mTask = Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).getParcelable(Tags.TASK);
            mTaskViewModel.insert(mTask);
            AlarmHandler.scheduleAlarm(Objects.requireNonNull(getActivity()), mTask);
        } else if (requestCode == Tags.REQUEST_CODE_EDIT_EVENT && resultCode == RESULT_OK){
            mTask = Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).getParcelable(Tags.TASK);
            mTaskViewModel.update(mTask);
            AlarmHandler.cancelAlarm(Objects.requireNonNull(getActivity()),mTask);
            AlarmHandler.scheduleAlarm(Objects.requireNonNull(getActivity()), mTask);
        }
    }

    private void edit(){
        Intent intent = new Intent(getContext(), CreateTaskActivity.class);
        intent.putExtra(Tags.TASK, mTask);
        startActivityForResult(intent, Tags.REQUEST_CODE_EDIT_EVENT);
    }

    private void onSwipe(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                 ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Snackbar snackbar;
                Task task = mAdapter.getTaskAt(viewHolder.getAdapterPosition());
                if (direction == ItemTouchHelper.LEFT){
                    AlarmHandler.cancelAlarm(Objects.requireNonNull(getActivity()), task);
                    mTaskViewModel.delete(task);
                    snackbar = Snackbar.make(Objects.requireNonNull(getView()), Tags.EVENT_DELETED, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }).attachToRecyclerView(mRV);
    }

    public boolean isKeyboardShowing(Context context, View view) {
        try {
            InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(keyboard).hideSoftInputFromWindow(view.getWindowToken(), 0);
            return keyboard.isActive();
        } catch (Exception ex) {
            return false;
        }
    }

}