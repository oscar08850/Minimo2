package dsa.eetac.upc.edu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowerActivity extends MainActivity{

    private APIRest myapirest;
    private Recycler recycler;
    private RecyclerView recyclerView;

    private String message;
    ImageView ivImageFromUrl;
    private String token;

    TextView textViewFollowing;
    TextView textViewRepos;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recycler = new Recycler(this);
        recyclerView.setAdapter(recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TextViews where we show the number of repositories and the number of following
        textViewFollowing = findViewById(R.id.followingView);
        textViewRepos = findViewById(R.id.repositoriesView);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        ivImageFromUrl = (ImageView)findViewById(R.id.iv_image_from_url);

        //Progress loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Waiting for the server");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        myapirest = APIRest.createAPIRest();

        getProfile();

        getFollowers();

    }

    private void getProfile() {
        Call<User> userCall = myapirest.getProfile(message);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();

                    Log.i("name: " + user.name, response.message());
                    Log.i("id: " + user.id, response.message());
                    Log.i("public_repos: " + user.public_repos, response.message());
                    Log.i("followers: " + user.followers, response.message());
                    Log.i("following: " + user.following, response.message());
                    Log.i("avatar_url: " + user.avatar_url, response.message());

                    Picasso.with(getApplicationContext()).load(user.avatar_url).into(ivImageFromUrl);

                    textViewRepos.setText(String.valueOf(user.public_repos));

                    textViewFollowing.setText(String.valueOf(user.following));

                    progressDialog.hide();

                } else {
                    Log.e("Response failure", String.valueOf(response.errorBody()));

                    progressDialog.hide();

                    //Show the alert dialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FollowerActivity.this);

                    alertDialogBuilder
                            .setTitle("Error")
                            .setMessage(response.message())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, which) -> finish());

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("No api connection", t.getMessage());

                progressDialog.hide();

                //Show the alert dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FollowerActivity.this);

                alertDialogBuilder
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> finish());

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }


    /*
    private void getFollowers(){
        Call<List<User>> userCall = myapirest.getFollowers(message);


        userCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    List<User> followersList = response.body();

                    if(followersList.size() != 0) {
                        recycler.addFollowers(followersList);
                    }

                    progressDialog.hide();

                    for(int i = 0; i < followersList.size(); i++) {
                        Log.i("name: " + followersList.get(i).name, response.message());
                        Log.i("Size of the list: " +followersList.size(), response.message());
                    }
                }
                else{
                    Log.e("No api connection", response.message());

                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("No api connection: ", t.getMessage());

                progressDialog.hide();
            }
        });
    }
    */


    //el que hay que modificar
    private void getFollowers(){
        Call<List<User>> userCall = myapirest.getFollowers(message);


        userCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    List<User> followersList = response.body();

                    if(followersList.size() != 0) {
                        recycler.addFollowers(followersList);
                    }

                    progressDialog.hide();

                    for(int i = 0; i < followersList.size(); i++) {
                        Log.i("name: " + followersList.get(i).name, response.message());
                        Log.i("Size of the list: " +followersList.size(), response.message());
                    }
                }
                else{
                    Log.e("No api connection", response.message());

                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("No api connection: ", t.getMessage());

                progressDialog.hide();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (token != null) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            token = data.getStringExtra("token");
        }
    }

}