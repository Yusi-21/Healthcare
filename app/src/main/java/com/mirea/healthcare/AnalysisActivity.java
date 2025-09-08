package com.mirea.healthcare;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

public class AnalysisActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analysis);

    }
}