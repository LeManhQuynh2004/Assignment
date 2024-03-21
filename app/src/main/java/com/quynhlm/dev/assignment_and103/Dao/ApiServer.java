package com.quynhlm.dev.assignment_and103.Dao;

import com.quynhlm.dev.assignment_and103.Model.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiServer {
    @GET("/students")
    Call<List<Student>> getAllStudent();
    @POST("/students/mobile")
    Call<Void> createStudent(@Body Student student);

    @PUT("students/mobile/{id}")
    Call<Void> putStudent(@Path("id") String student_id, @Body Student student);
    @DELETE("students/{id}")
    Call<Void> deleteStudent(@Path("id") String student_id);
}
