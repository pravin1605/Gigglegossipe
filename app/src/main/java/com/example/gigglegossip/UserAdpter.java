package com.example.gigglegossip;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdpter extends RecyclerView.Adapter<UserAdpter.ViewHolder> {
    MainActivity mainActivity;
    ArrayList<Users> usersArrayList;

    public UserAdpter(MainActivity mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public UserAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdpter.ViewHolder holder, int position) {

        Users users = usersArrayList.get(position);

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUserId())){
            holder.itemView.setVisibility(View.GONE);
        }

        holder.username.setText(users.userName);
        holder.userstatus.setText(users.status);

        // Check if profilepic is not empty or null
        if (users.profilepic != null && !users.profilepic.isEmpty()) {
            Picasso.get().load(users.profilepic).into(holder.userimg);
        } else {
            // Load a placeholder image or set a default image
            holder.userimg.setImageResource(R.drawable.photocamera);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, chatWin.class);
                intent.putExtra("nameee", users.getUserName());
                intent.putExtra("reciverImg", users.getProfilepic());
                intent.putExtra("uid", users.getUserId());
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView username;
        TextView userstatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            username = itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);
        }
    }
}
