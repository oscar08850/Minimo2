package dsa.eetac.upc.edu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private List<User> data;
    private Context context;

    public void addFollowers(List<User> followersList) {
        data.addAll(followersList);
        notifyDataSetChanged();
    }

    //Asign the text TextView to the text1 in the layout
    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private TextView followerNameView;
        private ImageView photoFollower;

        public ViewHolder(View v) {
            super(v);
            followerNameView = v.findViewById(R.id.followerNameView);
            photoFollower = v.findViewById(R.id.photoFollower);
            linearLayout = v.findViewById(R.id.linearLayout);
        }
    }

    //Constructor
    public Recycler(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    @Override
    public Recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Recycler.ViewHolder holder, int position) {
        User userData = data.get(position);
        holder.followerNameView.setText(userData.name);

        Picasso.with(context).load(userData.avatar_url).into(holder.photoFollower);

        holder.linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfileFollowerActivity.class);
            TextView editText = v.findViewById(R.id.followerNameView);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}


