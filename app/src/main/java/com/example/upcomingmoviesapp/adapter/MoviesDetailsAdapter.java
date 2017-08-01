package com.example.upcomingmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.upcomingmoviesapp.Model.MoviesDetailsModel;
import com.example.upcomingmoviesapp.R;
import com.example.upcomingmoviesapp.utils.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Vikas.Dasade on 29/07/2017.
 */

public class MoviesDetailsAdapter extends RecyclerView.Adapter<MoviesDetailsAdapter.MyViewHolder>  {

    ArrayList<MoviesDetailsModel> listOfMoviesDetailsItems;
    Context mContext;
    private boolean mExpanded;
    private View loaderView;

    public MoviesDetailsAdapter(Context mContext, ArrayList<MoviesDetailsModel> listOfMoviesDetailsItems) {

        this.mContext = mContext;
        this.listOfMoviesDetailsItems = listOfMoviesDetailsItems;

    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
        notifyDataSetChanged();
    }
    @Override
    public MoviesDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_poster_item, parent, false);
        MoviesDetailsAdapter.MyViewHolder vhItem = new MoviesDetailsAdapter.MyViewHolder(v);

        return vhItem;

    }

    @Override
    public void onBindViewHolder(final MoviesDetailsAdapter.MyViewHolder holder, int position) {

        if(listOfMoviesDetailsItems.get(position).getIv_poster().equalsIgnoreCase(""))
        {
            holder.iv_poster.setImageResource(R.color.color_app_text);
        }else {

           // Picasso.with(mContext).load("http://image.tmdb.org/t/p/w500/"+ Util.checkString(listOfMoviesDetailsItems.get(position).getIv_poster())).into(holder.iv_poster);
            Picasso.with(mContext)
                    .load("http://image.tmdb.org/t/p/w500/"+ Util.checkString(listOfMoviesDetailsItems.get(position).getIv_poster()))
//                    .resize(500,300)
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



    }

    @Override
    public int getItemCount() {

        return mExpanded ? listOfMoviesDetailsItems.size() : 5;
       // return listOfMoviesDetailsItems.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_poster;
         private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
            iv_poster = (ImageView) view.findViewById(R.id.iv_moviePoster);

        }
    }
}
