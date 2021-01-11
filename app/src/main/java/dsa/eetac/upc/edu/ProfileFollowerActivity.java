package dsa.eetac.upc.edu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFollowerActivity extends AppCompatActivity {

    private APIRest myapirest;
    private String token;
    private String message;
    ProgressDialog progressDialog;
    ImageView ivImageFromUrl;
    TextView textViewFollowing;
    TextView textViewRepos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_follower);

        //TextViews where we show the number of repositories and the number of following
        textViewFollowing = findViewById(R.id.followingView);
        textViewRepos = findViewById(R.id.repositoriesView);
        ivImageFromUrl = findViewById(R.id.iv_image_from_url);

        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //Progress loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Waiting for the server");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        myapirest = APIRest.createAPIRest();

        getProfile();
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileFollowerActivity.this);

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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileFollowerActivity.this);

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
