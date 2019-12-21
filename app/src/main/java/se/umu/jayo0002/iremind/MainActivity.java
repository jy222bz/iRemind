package se.umu.jayo0002.iremind;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;
import se.umu.jayo0002.iremind.models.Date;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.system_controllers.MapServiceController;
import se.umu.jayo0002.iremind.ui.main.SectionsPagerAdapter;
import se.umu.jayo0002.iremind.view_models.TaskViewModel;

public class MainActivity extends AppCompatActivity {
    protected static TaskViewModel mTaskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(new Date().getDate());
        setSupportActionBar(toolbar);
        if (MapServiceController.isServiceSupported(this))
            Toast.makeText(this, Tags.NOT_SUPPORTED, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public static synchronized void update(Task task){
        mTaskViewModel.update(task);
    }
}