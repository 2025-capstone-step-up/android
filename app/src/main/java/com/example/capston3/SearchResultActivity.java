package com.example.capston3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InfoAdapter adapter;
    TextView noResultText;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        recyclerView = findViewById(R.id.resultRecyclerView);
        noResultText = findViewById(R.id.noResultText);
        backButton = findViewById(R.id.backButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 🔥 전달받은 검색어와 전체 리스트 받기
        Intent intent = getIntent();
        String query = intent.getStringExtra("query");
        ArrayList<String> fullList = intent.getStringArrayListExtra("fullList");

        // 🔥 검색어가 포함된 항목만 필터링
        ArrayList<InfoItem> filteredList = new ArrayList<>();

        if (fullList != null) {
            for (String title : fullList) {
                if (title.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(new InfoItem(title, "설명은 준비 중입니다."));
                }
            }
        }

        // 🔥 결과에 따라 화면 분기
        if (filteredList.isEmpty()) {
            recyclerView.setVisibility(RecyclerView.GONE);
            noResultText.setVisibility(TextView.VISIBLE);
        } else {
            recyclerView.setVisibility(RecyclerView.VISIBLE);
            noResultText.setVisibility(TextView.GONE);

            adapter = new InfoAdapter(this, filteredList);
            recyclerView.setAdapter(adapter);
        }

        // 🔥 뒤로가기 버튼
        backButton.setOnClickListener(v -> finish());
    }
}
