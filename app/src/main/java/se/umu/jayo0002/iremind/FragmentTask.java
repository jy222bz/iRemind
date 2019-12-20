package se.umu.jayo0002.iremind;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.AlarmHandler;
import se.umu.jayo0002.iremind.models.SpecialListener;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.view_models.SharedViewModel;
import se.umu.jayo0002.iremind.view_models.TaskViewModel;

import static android.app.Activity.RESULT_OK;

public class FragmentTask extends Fragment {
    private SearchView mSearchView;
    private FloatingActionButton mFAB;
    private TaskViewModel mTaskViewModel;
    private Task mTask;
    private TaskAdapter mAdapter;
    private RecyclerView mRV;
    private AlarmHandler mAlarmHandler;
    private SharedViewModel mViewModel;
    private SpecialListener mListener;
    public static FragmentTask newInstance() {
        return new FragmentTask();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        mRV = view.findViewById(R.id.recycler_view);
        mAlarmHandler = new AlarmHandler(getContext());
        mAdapter = new TaskAdapter(getContext());
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setHasFixedSize(true);
        setHasOptionsMenu(true);
        mFAB = view.findViewById(R.id.fab);
        mRV.setAdapter(mAdapter);
        registerForContextMenu(mRV);
        mTaskViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TaskViewModel.class);
        mTaskViewModel.getActiveTasks().observe(getActivity(), tasks -> mAdapter.setAll(tasks));
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
            mAlarmHandler.startAlarm(Objects.requireNonNull(mTask).getAlarmDateAndTime(), mTask.getUniqueNumber(), mTask);
        } else if (requestCode == Tags.REQUEST_CODE_EDIT_EVENT && resultCode == RESULT_OK){
            mTask = Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).getParcelable(Tags.TASK);
            mTaskViewModel.update(mTask);
            mAlarmHandler.cancelAlarm(mTask.getUniqueNumber());
            mAlarmHandler.startAlarm(mTask.getAlarmDateAndTime(), mTask.getUniqueNumber(), mTask);
        }
    }

    private void edit(){
        Intent intent = new Intent(getContext(), CreateTaskActivity.class);
        intent.putExtra(Tags.TASK, mTask);
        startActivityForResult(intent, Tags.REQUEST_CODE_EDIT_EVENT);
    }

    private void onSwipe(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Snackbar snackbar;
                Task task = mAdapter.getTaskAt(viewHolder.getAdapterPosition());
                if (direction <0){
                    task.setStatus(0);
                    mTaskViewModel.update(task);
                    snackbar = Snackbar.make(Objects.requireNonNull(getView()), Tags.EVENT_ARCHIVED, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    mAlarmHandler.cancelAlarm(task.getId());
                    mTaskViewModel.delete(task);
                    snackbar = Snackbar.make(Objects.requireNonNull(getView()), Tags.EVENT_DELETED, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }).attachToRecyclerView(mRV);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        mViewModel.getTaskToBeUpdated().observe(getViewLifecycleOwner(), task -> {
            mTaskViewModel.update(task);
            mAdapter.notifyDataSetChanged();
        });
        mViewModel.getTaskToBeDeleted().observe(getViewLifecycleOwner(), task -> {
            mTaskViewModel.delete(task);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SpecialListener){
            mListener = (SpecialListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}