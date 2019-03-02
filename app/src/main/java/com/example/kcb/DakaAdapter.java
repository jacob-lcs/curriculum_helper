package com.example.kcb;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ConfigurationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by liu98 on 2019/3/2
 * Email: lcs1998@vip.qq.com
 */
public class DakaAdapter extends ArrayAdapter<thing> {
    private int resourceId;

    public DakaAdapter(Context context, int textViewResourceId, List<thing> objets) {
        super(context, textViewResourceId, objets);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        thing thing = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView thing_name = (TextView) view.findViewById(R.id.thing_name);
        TextView thing_day = (TextView) view.findViewById(R.id.thing_day);
        ImageView img = (ImageView) view.findViewById(R.id.imageView3);
        String i = thing.getThing_icon();
        String icons[] = {"@drawable/alarm", "@drawable/breakfast", "@drawable/english", "@drawable/exercise", "@drawable/fruit", "@drawable/rainbow", "@drawable/reading", "@drawable/sleep", "@drawable/smile", "@drawable/snacks", "@drawable/water"};
        if (i == icons[0]) {
            img.setImageDrawable(img.getResources().getDrawable(R.drawable.alarm));
        } else if (i == icons[1]) {
            img.setImageDrawable(img.getResources().getDrawable(R.drawable.breakfast));
        } else if (i == icons[2]) {
            img.setImageDrawable(img.getResources().getDrawable(R.drawable.english));
        } else if (i == icons[3]) {
            img.setImageDrawable(img.getResources().getDrawable(R.drawable.exercise));
        } else if (i == icons[4]) {
            img.setImageDrawable(img.getResources().getDrawable(R.drawable.fruit));
        } else if (i == icons[5]) {
            img.setImageDrawable(img.getResources().getDrawable(R.drawable.rainbow));
        } else if (i == icons[6]) {
            img.setImageDrawable(img.getResources().getDrawable(R.drawable.reading));
        } else if (i == icons[7]) {
            img.setImageDrawable(img.getResources().getDrawable(R.drawable.smile));
        } else if (i == icons[8]) {
            img.setImageDrawable(img.getResources().getDrawable(R.drawable.snacks));
        } else if (i == icons[9]) {
            img.setImageDrawable(img.getResources().getDrawable(R.drawable.water));
        }

        thing_name.setText(thing.getName());
        thing_day.setText("已连续打卡"+String.valueOf(thing.getDay())+"天");
        return view;
    }
}
