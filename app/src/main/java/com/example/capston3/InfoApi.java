package com.example.capston3;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InfoApi {
    @GET("competitions")
    Call<List<CompetitionResponse>> getCompetitions();

    @GET("schedules")
    Call<List<ScheduleResponse>> getSchedules();
}
