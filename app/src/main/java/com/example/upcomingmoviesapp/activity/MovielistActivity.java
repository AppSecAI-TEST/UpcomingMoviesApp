package com.example.upcomingmoviesapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.upcomingmoviesapp.ClickListener.RecyclerTouchListener;
import com.example.upcomingmoviesapp.Model.MoviesListItem;
import com.example.upcomingmoviesapp.R;
import com.example.upcomingmoviesapp.adapter.MovieListAdapter;
import com.example.upcomingmoviesapp.interfaces.ClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovielistActivity extends AppCompatActivity {

    ArrayList<MoviesListItem> modelArrayList;
    private MovieListAdapter adapter;
    CoordinatorLayout cdMovieList;
    private RecyclerView rvMovieList;
    private Toolbar tb_movieList;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        init();

        callToWebservice();

        handleRecyclerViewClick();

    }

    private void handleRecyclerViewClick() {
        rvMovieList.addOnItemTouchListener(new RecyclerTouchListener(MovielistActivity.this,
                rvMovieList,
                new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Intent intentMovieListActivity = new Intent(MovielistActivity.this, MoviesDetailsActivity.class);
                        intentMovieListActivity.putExtra("id",modelArrayList.get(position).getId());
                        intentMovieListActivity.putExtra("title",modelArrayList.get(position).getTitle());
                        startActivity(intentMovieListActivity);



                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }


                }));


    }

    private void callToWebservice() {
        makeRequest("https://api.themoviedb.org/3/movie/upcoming?api_key=b7cd3340a794e5a2f35e3abb820b497f");

    }

    private void makeRequest(String url) {
        String url_validate = "@#&=*+-_.,:!?()/~'%";

        final String final_url = Uri.encode(url, url_validate);
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();
        final RequestQueue queue = Volley.newRequestQueue(this.mContext);

        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,
                final_url,
                null,
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            dialog.dismiss();
                            JSONObject obj = new JSONObject(response.toString());
                            Log.e("VolleyWebServices====", "RESPONSE==" + response.toString());
                            JSONArray resultsArray = obj.getJSONArray("results");
                            modelArrayList.clear();
                            for (int i = 0; i < resultsArray.length(); i++) {

                                JSONObject jsonObject = resultsArray.getJSONObject(i);

                                MoviesListItem moviesListItem = new MoviesListItem();
                                moviesListItem.setIv_poster(jsonObject.getString("poster_path"));
                                moviesListItem.setTitle(jsonObject.getString("title"));
                                moviesListItem.setReleaseDate(jsonObject.getString("release_date"));
                                moviesListItem.setAdult(jsonObject.getString("adult"));
                                moviesListItem.setId(jsonObject.getString("id"));
                                modelArrayList.add(moviesListItem);

                            }
                            if (modelArrayList.size() > 0) {
                                adapter = new MovieListAdapter(mContext, modelArrayList);
                                rvMovieList.setAdapter(adapter);
                            } else {
                                modelArrayList.clear();
                                rvMovieList.setAdapter(null);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Log.e("VolleyWebServices====", "REQUEST===" + final_url);
                        Log.e("VolleyWebServices===", "inside Response.ErrorListener()===" + error.toString());


                    }
                }
        );

        queue.add(obreq);
    }


    private void init() {
        mContext = MovielistActivity.this;

        cdMovieList = (CoordinatorLayout) findViewById(R.id.cdMovieList);
        rvMovieList = (RecyclerView) findViewById(R.id.rvMovieList);
        tb_movieList = (Toolbar) findViewById(R.id.tb_movieList);
        TextView mTitle = (TextView) tb_movieList.findViewById(R.id.tb_title);
        setSupportActionBar(tb_movieList);
        mTitle.setText("Movies List");
        modelArrayList = new ArrayList<>();

        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvMovieList.setLayoutManager(llm);

    }


}
