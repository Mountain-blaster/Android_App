package com.project.webserviceapp.app.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.webserviceapp.app.activities.MainActivity;
import com.project.webserviceapp.app.config.RetrofitConfig;
import com.project.webserviceapp.app.config.Server;
import com.project.webserviceapp.app.models.Personne;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class PersonnesController {

    private static ArrayList<Personne> personneList = new ArrayList<>();
    private static Personne personne;

    public static ArrayList<Personne> getPersonneList() {
        return personneList;
    }
    public static ArrayList<Personne> getPersonne() {
        ArrayList<Personne> arrayList = new ArrayList<>();
        arrayList.add(personne);
        return arrayList;
    }

    public static void getAllPersonnesVolley(final Context mainContext, final MainActivity current, final Method methodAfterFinished) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.BASE_URL + "personnes", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Personne[] array = gson.fromJson(response,Personne[].class);

                personneList.clear();
                Collections.addAll(personneList, array);
                try {
                    methodAfterFinished.invoke(current);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0013)", Toast.LENGTH_SHORT).show();
                    System.exit(0013);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainContext, "Error communicating with server. Try again later.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(mainContext);
        requestQueue.add(stringRequest);
    }

    public static void getPersonne(String email, final Context mainContext, final MainActivity current, final Method methodAfterFinished) {

        Call<Personne> call = new RetrofitConfig().getService().getByEmail(email);

        call.enqueue(new Callback<Personne>() {
            @Override
            public void onResponse(retrofit.Response<Personne> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    personne = response.body();
                    try {
                        methodAfterFinished.invoke(current);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        Toast.makeText(mainContext, "Unexpected error. (status: 0013)", Toast.LENGTH_SHORT).show();
                        System.exit(0013);
                    }
                }
                else{
                    Toast.makeText(mainContext, "Error communicating with server. Try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getAllPersonnesRetrofit(final Context mainContext, final MainActivity current, final Method methodAfterFinished) {

        Call<List<Personne>> call = new RetrofitConfig().getService().getAll();

        call.enqueue(new Callback<List<Personne>>() {
            @Override
            public void onResponse(retrofit.Response<List<Personne>> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    personneList.clear();
                    for(int i = 0; i < response.body().size(); i++) {
                        personneList.add((Personne) response.body().get(i));
                    }
                    try {
                        methodAfterFinished.invoke(current);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        Toast.makeText(mainContext, "Unexpected error. (status: 0013)", Toast.LENGTH_SHORT).show();
                        System.exit(0013);
                    }
                }
                else{
                    Toast.makeText(mainContext, "Error communicating with server. Try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void insertPersonne(final Context mainContext, String prenom, String nom, String email, Date naissance){

        Personne personne = null;

        try {
            personne = new Personne(prenom, nom, email, naissance);
        }
        catch (Exception e) {
            Toast.makeText(mainContext, e + "Personne data was entered incorrectly.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Personne> call = new RetrofitConfig().getService().insert(personne);
        call.enqueue(new Callback<Personne>() {
            @Override
            public void onResponse(retrofit.Response<Personne> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Toast.makeText(mainContext, "Personne was included successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mainContext, "Error inserting personne.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server." + t, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public static void updatePersonne(final Context mainContext, String prenom, String nom, String email, Date naissance){

        Personne personne = null;

        if (naissance == null) { naissance = new Date(); }
        try {
            personne = new Personne(prenom, nom, email, naissance);
        }
        catch (Exception e) {
            Toast.makeText(mainContext, "Personne data was entered incorrectly.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Personne> call = new RetrofitConfig().getService().update(email, personne);
        call.enqueue(new Callback<Personne>() {
            @Override
            public void onResponse(retrofit.Response<Personne> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Toast.makeText(mainContext, "Personne was updated successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mainContext, "Error updating personne.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public static void deletePersonne(final Context mainContext, String email) {

        Call<Personne> call = new RetrofitConfig().getService().delete(email);
        call.enqueue(new Callback<Personne>(){
            @Override
            public void onResponse(retrofit.Response<Personne> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Toast.makeText(mainContext, "Personne was deleted successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mainContext, "Error deleting personne.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public static void deleteAllPersonnes(final Context mainContext) {

        Call<Personne> call = new RetrofitConfig().getService().deleteAll();
        call.enqueue(new Callback<Personne>(){
            @Override
            public void onResponse(retrofit.Response<Personne> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Toast.makeText(mainContext, "All personnes were deleted successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mainContext, "Error deleting personnes.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mainContext, "Error communicating with server.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
