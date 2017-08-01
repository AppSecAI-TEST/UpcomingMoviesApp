package com.example.upcomingmoviesapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.upcomingmoviesapp.Model.MoviesListItem;
import com.example.upcomingmoviesapp.R;
import com.example.upcomingmoviesapp.utils.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Vikas.Dasade on 27/07/2017.
 */

public class MovieListAdapter  extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {
    ArrayList<MoviesListItem> listOfMoviesItems;
    Context mContext;


    public MovieListAdapter(Context mContext, ArrayList<MoviesListItem> listOfMoviesItems) {

        this.mContext = mContext;
        this.listOfMoviesItems = listOfMoviesItems;

    }


    @Override
    public MovieListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_item, parent, false);
        MovieListAdapter.MyViewHolder vhItem = new MovieListAdapter.MyViewHolder(v);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final MovieListAdapter.MyViewHolder holder, int position) {


        holder.title.setText(Util.checkString(listOfMoviesItems.get(position).getTitle()));

        if(listOfMoviesItems.get(position).getAdult().equalsIgnoreCase("false"))
        {
            holder.adult.setText("A");

        }else {
            holder.adult.setText("U/A");
        }
        //holder.adult.setText(Util.checkString(listOfMoviesItems.get(position).getAdult()));
        holder.releaseDate.setText(Util.checkString(listOfMoviesItems.get(position).getReleaseDate()));
        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w500/"+Util.checkString(listOfMoviesItems.get(position).getIv_poster()))
                .resize(200, 200)
                .into(holder.iv_poster,new Callback() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub

                    }
                });

    }

    @Override
    public int getItemCount() {
        return listOfMoviesItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, adult,releaseDate,id;
        private ImageView iv_poster;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);

            // initialise all UI text views
            title = (TextView) view.findViewById(R.id.title);
            adult = (TextView) view.findViewById(R.id.adult);
            releaseDate = (TextView) view.findViewById(R.id.releaseDate);
            iv_poster = (ImageView) view.findViewById(R.id.iv_poster);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }
}
