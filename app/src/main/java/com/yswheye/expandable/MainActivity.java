package com.yswheye.expandable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yswheye.expandable.view.ExpandableLayout;
import com.yswheye.expandable.view.SettingView;

public class MainActivity extends AppCompatActivity {
    ExpandableLayout expandableLayout1;
    ExpandableLayout expandableLayout2;
    ExpandableLayout expandableLayout5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableLayout1 = (ExpandableLayout) findViewById(R.id.expandableview1);
        expandableLayout2 = (ExpandableLayout) findViewById(R.id.expandableview2);
        expandableLayout5 = (ExpandableLayout) findViewById(R.id.expandableview5);
        expandableLayout1.setOnExpandStateChangeListener(new ExpandableLayout.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(boolean isExpanded) {
                if (isExpanded) {
                    Toast.makeText(MainActivity.this, "展开", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "折叠", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView textView = new TextView(this);
        textView.setTextSize(28);
        textView.setText("O(∩_∩)O哈哈~");
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        expandableLayout2.addView(textView);

        expandableLayout5.getTitleView().setOnStateChangeListener(new SettingView.OnStateChangeListener() {
            @Override
            public void onStateChanged(SettingView view, boolean state) {
                expandableLayout5.toggle();
            }
        });
    }
}
