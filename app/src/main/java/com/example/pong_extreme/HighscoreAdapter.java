package com.example.pong_extreme;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class HighscoreAdapter extends ArrayAdapter {
    public HighscoreAdapter(Context context, List<Highscore> highscores){
    super(context,0,highscores);
}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent){
        View view = convertview;
        if( view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_highscore_list, parent, false);
        }

        Highscore hs = (Highscore) getItem(position);
        TextView tvhighscore = view.findViewById(R.id.tv_highscore);
        TextView tvscore = view.findViewById(R.id.tv_score);

        tvhighscore.setText(hs.getName());
        tvscore.setText(Integer.toString(hs.getScore()));

        return view;
    }
}
