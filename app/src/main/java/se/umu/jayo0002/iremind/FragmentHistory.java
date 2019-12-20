package se.umu.jayo0002.iremind;

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
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.SpecialListener;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.view_models.SharedViewModel;
import se.umu.jayo0002.iremind.view_models.TaskViewModel;

public class FragmentHistory extends Fragment {
    private SearchView mSearchView;
    private FloatingActionButton mFAB;
    private TaskViewModel mTaskViewModel;
    private Task mTask;
    private TaskAdapter mAdapter;
    private RecyclerView mRV;
    private SharedViewModel mViewModel;
    private SpecialListener mListener;
    public static FragmentHistory newInstance() {
        return new FragmentHistory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mRV = view.findViewById(R.id.recycler_view1);
        mAdapter = new TaskAdapter(getContext());
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRV.setHasFixedSize(true);
        setHasOptionsMenu(true);
        mRV.setAdapter(mAdapter);
        registerForContextMenu(mRV);
        mTaskViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TaskViewModel.class);
        mTaskViewModel.getInactiveTasks().observe(getActivity(), tasks -> mAdapter.setAll(tasks));
        onSwipe();
        mAdapter.setOnItemClickListener(task -> {
            mTask = task;
            Intent intent = new Intent(getActivity(), OpenTaskActivity.class);
            intent.putExtra(Tags.NEW_LAUNCH, task);
            startActivity(intent);
            getActivity().finish();
        });
        return view;
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
                Snackbar snackbar;
                Task task = mAdapter.getTaskAt(viewHolder.getAdapterPosition());
                if (direction < 0) {
                    task.setStatus(0);
                    mTaskViewModel.update(task);
                    snackbar = Snackbar.make(Objects.requireNonNull(getView()), Tags.EVENT_ARCHIVED, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    mTaskViewModel.delete(task);
                    snackbar = Snackbar.make(Objects.requireNonNull(getView()), Tags.EVENT_DELETED, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }).attachToRecyclerView(mRV);
    }
}