package se.umu.jayo0002.iremind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.view_models.SharedViewModel;
import se.umu.jayo0002.iremind.view_models.TaskViewModel;

public class OpenTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private Task mTask;
    private Button mCLose;
    private boolean mGoMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_task);
        TaskViewModel mNoteViewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(TaskViewModel.class);
        if (savedInstanceState  == null && getIntent().hasExtra(Tags.BUNDLE)) {
            mGoMain = false;
            Bundle bundle = getIntent().getBundleExtra(Tags.BUNDLE);
            mTask = Objects.requireNonNull(bundle).getParcelable(Tags.TASK);
            assert mTask != null;
            mTask.setStatus(false);
            SharedViewModel mViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
            mViewModel.sendTaskToBeUpdated(mTask);
            mNoteViewModel.update(mTask);
        } else if (savedInstanceState  == null && getIntent().hasExtra(Tags.TASK)){
           mGoMain = true;
           mTask = Objects.requireNonNull(getIntent().getExtras()).getParcelable(Tags.NEW_LAUNCH);
        } else if (savedInstanceState  != null){
            updateUI(savedInstanceState);
        }
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
        mTextViewInfo.setText(mTask.getNote());
        mLocation.setText(mTask.getAddress());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Tags.TASK,mTask);
        outState.putBoolean(Tags.BOOLEAN, mGoMain);
    }

    private void updateUI(Bundle outState) {
        mTask = outState.getParcelable(Tags.TASK);
        mGoMain = outState.getBoolean(Tags.BOOLEAN);
    }

    @Override
    public void onClick(View view) {
        if (!mGoMain)
            this.finish();
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
