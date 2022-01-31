package com.project.webserviceapp.app.services;

import com.project.webserviceapp.app.models.Personne;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface Service {

    @GET("personnes/")
    Call<List<Personne>> getAll();

    @GET("personnes/{email}")
    Call<Personne> getByEmail(@Path("email") String email);

    @PUT("personnes/")
    Call<Personne> insert(@Body Personne personne);

    @PUT("personnes/{email}")
    Call<Personne> update(@Path("email") String email, @Body Personne personne);

    @DELETE("personnes/{email}")
    Call<Personne> delete(@Path("email") String email);

    @DELETE("personnes/")
    Call<Personne> deleteAll();
}
