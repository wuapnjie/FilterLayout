package com.xiaopo.flying.filterlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xiaopo.flying.filterlayout.filterlayout.FilterLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private FilterLayout mFilterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFilterLayout = (FilterLayout) findViewById(R.id.filter_layout);
        List<String> selectedStrings = new ArrayList<>();
        selectedStrings.add("Item0");
        selectedStrings.add("Item1");
        selectedStrings.add("Item2");
        selectedStrings.add("Item3");
        selectedStrings.add("Item4");
        selectedStrings.add("Item5");
        selectedStrings.add("Item6");
        selectedStrings.add("Item7");
        selectedStrings.add("Item8");
        selectedStrings.add("Item9");
        selectedStrings.add("Item10");
        selectedStrings.add("Item11");


        mFilterLayout.setAdapter(new MyFilterAdapter(selectedStrings));
        mFilterLayout.setColumnCount(6);

        Button button = (Button) findViewById(R.id.btn_click);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Integer> positions = mFilterLayout.getSelectedPositions();
                System.out.println("this size is "+positions.size());
                for (Integer integer : positions) {
                    System.out.println("selected " + integer);
                }
            }
        });
    }
}
