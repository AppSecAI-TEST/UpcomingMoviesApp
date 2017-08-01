package com.example.upcomingmoviesapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.upcomingmoviesapp.Model.MoviesDetailsModel;
import com.example.upcomingmoviesapp.Model.MoviesListItem;
import com.example.upcomingmoviesapp.Model.moviesPosterItem;
import com.example.upcomingmoviesapp.R;
import com.example.upcomingmoviesapp.adapter.MovieListAdapter;
import com.example.upcomingmoviesapp.adapter.MoviesDetailsAdapter;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MoviesDetailsActivity extends AppCompatActivity {
    private TextView tvData;
    ArrayList<MoviesDetailsModel> modelMoviesPosterArrayList;
    private MoviesDetailsAdapter adapter;
    private Context mContext;
    private RecyclerView rvMoviePoster;
    private String id,title, idOnResponse, author, content, urlOnResponse, idOnResponseForImage,requiredString;
    private Toolbar tb_movieDetails;
    private TextView tv_author, tv_content, tv_id,tv_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        init();
        receiveIntent();

        //https://api.themoviedb.org/3/movie/315635/reviews?api_key=b7cd3340a794e5a2f35e3abb820b497f
        callToWebserviceForReview();

        callToWebserviceForImage();


    }

    private void callToWebserviceForImage() {

        String moviesId = id;

        final String url = "https://api.themoviedb.org/3/movie/" + moviesId + "/reviews?api_key=b7cd3340a794e5a2f35e3abb820b497f";
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


                            if(resultsArray.length()>0){
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject jobj = resultsArray.getJSONObject(i);

                                    moviesPosterItem posterItem = new moviesPosterItem();

                                    idOnResponseForImage = posterItem.setId(jobj.getString("id"));
                                    author = posterItem.setAuthor(jobj.getString("author"));
                                    content = posterItem.setContent(jobj.getString("content"));


                                    requiredString =  StringUtils.substringBetween(content, "_Final rating:", "-");

                                    if(requiredString != null) {
                                        tv_rating.setText("Ratings :" + requiredString);
                                    }else {
                                        tv_rating.setText("Ratings :Ratings Not Available");
                                    }

                                    tv_content.setText("Content :"+content);
                                    tv_id.setText(id);
                                    tv_author.setText("Title :"+title);



                                }
                            }else {
                                Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show();
                                tv_content.setText("No Data Available");
                                tv_id.setVisibility(View.GONE);
                                tv_author.setVisibility(View.GONE);
                                tv_rating.setVisibility(View.GONE);
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
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

    private void findCategory() {

    }


    private void callToWebserviceForReview() {

        String moviesId = id;

        final String url = "https://api.themoviedb.org/3/movie/" + moviesId + "/images?api_key=b7cd3340a794e5a2f35e3abb820b497f";
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
                            JSONArray resultsArray = obj.getJSONArray("posters");

                            modelMoviesPosterArrayList.clear();


                            for (int i = 0; i < resultsArray.length(); i++) {

                                JSONObject jsonObject = resultsArray.getJSONObject(i);

                                MoviesDetailsModel moviesDetailsModel = new MoviesDetailsModel();

                                moviesDetailsModel.setIv_poster(jsonObject.getString("file_path"));

                                modelMoviesPosterArrayList.add(moviesDetailsModel);

                            }
                            if (modelMoviesPosterArrayList.size() > 0) {
                                adapter = new MoviesDetailsAdapter(mContext, modelMoviesPosterArrayList);
                                rvMoviePoster.setAdapter(adapter);
                            } else {
                                modelMoviesPosterArrayList.clear();
                                rvMoviePoster.setAdapter(null);
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
        mContext = MoviesDetailsActivity.this;
        tb_movieDetails = (Toolbar) findViewById(R.id.tb_movieDetails);
        SnapHelper helper = new LinearSnapHelper();
        tvData = (TextView) findViewById(R.id.tvData);
        modelMoviesPosterArrayList = new ArrayList<>();
        rvMoviePoster = (RecyclerView) findViewById(R.id.rvMoviePoster);
        tv_author = (TextView) findViewById(R.id.tv_author);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_rating = (TextView) findViewById(R.id.tv_rating);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        helper.attachToRecyclerView(rvMoviePoster);
        rvMoviePoster.setItemViewCacheSize(4);
        rvMoviePoster.setLayoutManager(llm);
    }

    private void receiveIntent() {

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        tvData.setText(id);
        TextView mTitle = (TextView) tb_movieDetails.findViewById(R.id.tb_title);
        setSupportActionBar(tb_movieDetails);
        mTitle.setText(title);

    }
}
