package se.umu.jayo0002.iremind;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import se.umu.jayo0002.iremind.models.date.Date;
import se.umu.jayo0002.iremind.system_controllers.MapServiceController;
import se.umu.jayo0002.iremind.ui.main.SectionsPagerAdapter;
import se.umu.jayo0002.iremind.view.Toaster;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(new Date().getDate());
        setSupportActionBar(toolbar);
        if (MapServiceController.isServiceNOTSupported(this))
            Toaster.displayToast(this,Tags.NOT_SUPPORTED, Tags.LONG_TOAST);
    }
}