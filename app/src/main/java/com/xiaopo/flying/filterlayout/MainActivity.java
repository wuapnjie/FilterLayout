package com.xiaopo.flying.filterlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        final List<String> selectedStrings = new ArrayList<>();
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
        selectedStrings.add("Item12");
        selectedStrings.add("Item13");
        selectedStrings.add("Item14");
        selectedStrings.add("Item15");
        selectedStrings.add("Item16");
        selectedStrings.add("Item17");
        selectedStrings.add("Item18");
        selectedStrings.add("Item19");
        selectedStrings.add("Item20");
        selectedStrings.add("Item21");
        selectedStrings.add("Item22");
        selectedStrings.add("Item23");


        mFilterLayout.setAdapter(new MyFilterAdapter(selectedStrings), 12);
        mFilterLayout.setColumnCount(6);

        Button button = (Button) findViewById(R.id.btn_click);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Integer> positions = mFilterLayout.getSelectedPositions();
                StringBuilder stringBuilder = new StringBuilder("你选中了" + positions.size() + "项" + ",分别为");
                for (Integer integer : positions) {
                    stringBuilder.append(String.valueOf(integer)).append(" ");
                }

                Toast.makeText(MainActivity.this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
