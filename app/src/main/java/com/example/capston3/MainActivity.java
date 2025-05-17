package com.example.capston3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InfoAdapter adapter;
    List<InfoItem> infoList;

    private void addNewInfo(String title) {
        infoList.add(new InfoItem(title, "설명은 아직 준비 중입니다."));
        adapter.notifyItemInserted(infoList.size() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.infoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        infoList = new ArrayList<>();
        adapter = new InfoAdapter(this, infoList);
        recyclerView.setAdapter(adapter);

        loadServerData(); // 서버에서 데이터 로드
    }

    private void loadServerData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.122.50.186:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InfoApi infoApi = retrofit.create(InfoApi.class);

        // 1. 공모전 정보
        infoApi.getCompetitions().enqueue(new Callback<List<CompetitionResponse>>() {
            @Override
            public void onResponse(Call<List<CompetitionResponse>> call, Response<List<CompetitionResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (CompetitionResponse item : response.body()) {
                        if (item.getName() == null || item.getName().isEmpty()) continue;
                        String title = "🎓 공모전: " + item.getName();
                        String desc = "기간: " + item.getStartDate() + " ~ " + item.getEndDate();
                        infoList.add(new InfoItem(title, desc));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CompetitionResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        // 2. 자격증 시험 정보
        infoApi.getSchedules().enqueue(new Callback<List<ScheduleResponse>>() {
            @Override
            public void onResponse(Call<List<ScheduleResponse>> call, Response<List<ScheduleResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ScheduleResponse item : response.body()) {
                        if (item.getTitle() == null || item.getTitle().isEmpty()) continue;
                        String title = "📚 자격증: " + item.getTitle();
                        String desc = "시험일: " + item.getExamDate() + " / 결과일: " + item.getResultDate();
                        infoList.add(new InfoItem(title, desc));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // 🔥 검색 화면에 제목 리스트 전달
    private ArrayList<String> getAllTitles() {
        ArrayList<String> titles = new ArrayList<>();
        for (InfoItem item : infoList) {
            titles.add(item.getTitle());
        }
        return titles;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("검색어를 입력하세요");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                intent.putExtra("query", query);
                intent.putStringArrayListExtra("fullList", getAllTitles()); // 🔥 제목 리스트 전달
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }
}
