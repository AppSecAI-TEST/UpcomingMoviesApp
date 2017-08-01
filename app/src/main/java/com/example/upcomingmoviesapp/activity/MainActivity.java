package com.example.upcomingmoviesapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upcomingmoviesapp.R;

public class MainActivity extends AppCompatActivity {
    private Menu menu;
    private Toolbar tb_main;
    private ImageView iv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
      callWebservice();


    }

    private void callWebservice() {




        //webservice = "https://api.themoviedb.org/3/movie/upcoming?api_key=b7cd3340a794e5a2f35e3abb820b497f"

    }

    private void setClickListener() {



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.menu_movieList:

                Intent MovieListIntent = new Intent(MainActivity.this,MovielistActivity.class);
                startActivity(MovieListIntent);

                break;

            case R.id.menu_movieDetails:
                Intent MovieListIntent2 = new Intent(MainActivity.this,MovielistActivity.class);
                Toast.makeText(this, "Please select one Movie", Toast.LENGTH_SHORT).show();
                startActivity(MovieListIntent2);
                break;

            case R.id.menu_info:

                Intent MovieListIntent3 = new Intent(MainActivity.this,InformationActivity.class);
               // Toast.makeText(this, "Please select one Movie", Toast.LENGTH_SHORT).show();
                startActivity(MovieListIntent3);

                break;



        }
        return true;

    }




    private void init() {
        tb_main = (Toolbar) findViewById(R.id.tb_main);
        TextView mTitle = (TextView) tb_main.findViewById(R.id.tb_title);
        setSupportActionBar(tb_main);
        mTitle.setText("Upcoming Movies App");

    }
}
