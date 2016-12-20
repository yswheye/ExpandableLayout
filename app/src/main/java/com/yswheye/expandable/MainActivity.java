package com.yswheye.expandable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yswheye.expandable.view.ExpandableLayout;

public class MainActivity extends AppCompatActivity {
    ExpandableLayout expandableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableLayout = (ExpandableLayout) findViewById(R.id.expandableview1);
        expandableLayout.setOnExpandStateChangeListener(new ExpandableLayout.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(boolean isExpanded) {
                if (isExpanded) {
                    Toast.makeText(MainActivity.this, "展开", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "折叠", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
