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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;
import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;
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
        mTaskViewModel.getInactiveTasks().observe(Objects.requireNonNull(getActivity()), tasks -> mAdapter.setAll(tasks));
        onSwipe();
        mAdapter.setOnItemClickListener(task -> {
            mTask = task;
            Intent intent = new Intent(getActivity(), OpenTaskActivity.class);
            intent.putExtra(Tags.NEW_LAUNCH, task);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        });
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
                 ItemTouchHelper.LEFT) {
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
                    mTaskViewModel.delete(task);
                    snackbar = Snackbar.make(Objects.requireNonNull(getView()), Tags.EVENT_DELETED, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }).attachToRecyclerView(mRV);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete){
            if (Objects.requireNonNull(mTaskViewModel.getInactiveTasks().getValue()).size() == 0)
                Toast.makeText(getActivity(), Tags.NO_ARCHIVE, Toast.LENGTH_LONG).show();
            else
                mTaskViewModel.deleteAllInactiveTasks();
        }

        return super.onOptionsItemSelected(item);
    }

}