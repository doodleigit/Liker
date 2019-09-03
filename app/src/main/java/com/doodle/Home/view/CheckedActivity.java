package com.doodle.Home.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.doodle.Home.adapter.MyCategory;
import com.doodle.R;
import com.doodle.Tool.ConstantManager;

public class CheckedActivity extends AppCompatActivity {
    private TextView tvParent, tvChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked);

        tvParent = findViewById(R.id.parent);
        tvChild = findViewById(R.id.child);

        for (int i = 0; i < MyCategory.parentItems.size(); i++ ){

            String isChecked = MyCategory.parentItems.get(i).get(ConstantManager.Parameter.IS_CHECKED);

            if (isChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE))
            {
                tvParent.setText(tvParent.getText() + MyCategory.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME));
            }

            for (int j = 0; j < MyCategory.childItems.get(i).size(); j++ ){

                String isChildChecked = MyCategory.childItems.get(i).get(j).get(ConstantManager.Parameter.IS_CHECKED);

                if (isChildChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE))
                {
                    tvChild.setText(tvChild.getText() +" , " + MyCategory.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME) + " "+(j+1));
                }

            }

        }
    }
}