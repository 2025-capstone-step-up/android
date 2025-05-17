package com.example.capston3;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView titleText = findViewById(R.id.detailTitle);
        TextView descriptionText = findViewById(R.id.detailDescription);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        titleText.setText(title);
        descriptionText.setText(description);
    }
}
