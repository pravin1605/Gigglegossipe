package com.example.gigglegossip;

import static com.example.gigglegossip.chatWin.reciverIImg;
import static com.example.gigglegossip.chatWin.senderImg;

import android.content.Context;
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

public class messagesAdpter extends RecyclerView.Adapter {

    Context context;
    ArrayList<msgModelclass> messagesAdpterarraylist;
    int ITEM_SEND = 1;
    int ITEM_RECIVE = 2;

    public messagesAdpter(Context context, ArrayList<msgModelclass> messagesAdpterarraylist) {
        this.context = context;
        this.messagesAdpterarraylist = messagesAdpterarraylist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        msgModelclass messages = messagesAdpterarraylist.get(position);
        if (holder.getClass() == senderViewHolder.class) {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            if (senderImg != null && !senderImg.isEmpty()) {
                Picasso.get().load(senderImg).into(viewHolder.circleImageView);
            } else {
                viewHolder.circleImageView.setImageResource(R.drawable.photocamera); // Use a placeholder image
            }
        } else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            if (reciverIImg != null && !reciverIImg.isEmpty()) {
                Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);
            } else {
                viewHolder.circleImageView.setImageResource(R.drawable.photocamera); // Use a placeholder image
            }
        }
    }

    @Override
    public int getItemCount() {
        return messagesAdpterarraylist.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModelclass messages = messagesAdpterarraylist.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView msgtxt;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;

        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}
