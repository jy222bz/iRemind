package se.umu.jayo0002.iremind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.view_models.TaskViewModel;

public class OpenTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private Task mTask;
    private Button mCLose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_task);
        TaskViewModel mNoteViewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(TaskViewModel.class);
        if (savedInstanceState  == null) {
            Bundle bundle = getIntent().getBundleExtra(Tags.BUNDLE);
            mTask = Objects.requireNonNull(bundle).getParcelable(Tags.TASK);
            assert mTask != null;
            mTask.setStatus(0);
            mTask.setStatus(false);
            mNoteViewModel.update(mTask);
        } else
            updateUI(savedInstanceState);
        prepareUI();
        mCLose.setOnClickListener(this);
    }

    private void prepareUI(){
        mCLose= findViewById(R.id.close_buttons);
        TextView mTextViewTitle = findViewById(R.id.tvTitles);
        TextView mTextViewInfo = findViewById(R.id.tvMoreInfos);
        Button mButtonDate = findViewById(R.id.btDates);
        Button mButtonTime = findViewById(R.id.btStartingTimes);
        Button mLocation = findViewById(R.id.btLocations);
        mTextViewTitle.setText(mTask.getTitle());
        mButtonDate.setText(mTask.getDate());
        mButtonTime.setText(mTask.getTime());
        mLocation.setText(mTask.getAddress());
        if (!mTask.getNote().isEmpty())
            mTextViewInfo.setText(mTask.getNote());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Tags.TASK,mTask);
    }

    private void updateUI(Bundle outState) {
        mTask = outState.getParcelable(Tags.TASK);
    }

    @Override
    public void onClick(View view) {
        this.finish();
    }
}
