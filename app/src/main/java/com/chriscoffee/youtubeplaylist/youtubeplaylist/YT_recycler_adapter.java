package com.chriscoffee.youtubeplaylist.youtubeplaylist;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by christophercoffee on 12/17/16.
 */

public class YT_recycler_adapter extends RecyclerView.Adapter<YT_recycler_adapter.MyViewHolder> {

    private List<Videos> videoList;
    String key;
    Activity activity;
    private int REQ_PLAYER_CODE  = 1;
    int cornerRadius;
    int cardColor;
    int textColor;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView imageView;



        public MyViewHolder(View view) {
            super(view);


            imageView = (ImageView)view.findViewById(R.id.imageView) ;


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Videos video = videoList.get(getAdapterPosition());

                    Intent videoIntent = YouTubeStandalonePlayer.createVideoIntent(activity, key, video.getVideoID(), 0, true, false);

                    activity.startActivityForResult(videoIntent, REQ_PLAYER_CODE);


                }
            });


            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setCardBackgroundColor(cardColor);
            cardView.setRadius(cornerRadius);


            name = (TextView) view.findViewById(R.id.name);
            name.setTextColor(textColor);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });



        }
    }




    public YT_recycler_adapter(List<Videos> videoList, String yt_key, Activity activity, int cornerRadius, int cardColor, int textColor) {
        this.activity  = activity;
        this.key = yt_key;
        this.videoList = videoList;
        this.cornerRadius = cornerRadius;
        this.cardColor = cardColor;
        this.textColor = textColor;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cm_yt_list_items, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Videos video = videoList.get(position);
        holder.name.setText(video.getTitle());


        Glide.with(activity)
                .load(video.getThumbnailUrl() ) // Uri of the picture
                .into(holder.imageView);




    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
