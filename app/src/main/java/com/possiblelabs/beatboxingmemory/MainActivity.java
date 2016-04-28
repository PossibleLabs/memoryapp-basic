package com.possiblelabs.beatboxingmemory;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView textScore;
    private int score;
    private MediaPlayer mp;
    private final Map<Integer, Item> items = new HashMap<>();
    private int currentIndex = 0;
    private Item currentItem;
    private List<Integer> sequence = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textScore = (TextView) findViewById(R.id.textScore);
        score = 0;
        loadItems();

    }

    private void loadItems() {
        items.put(1, new Item(R.id.image1, R.color.color1, R.raw.sound1, R.drawable.lipsc1, R.drawable.lipsc2));
        items.put(2, new Item(R.id.image2, R.color.color2, R.raw.sound2, R.drawable.lipsc1, R.drawable.lipsc2));
        items.put(3, new Item(R.id.image3, R.color.color3, R.raw.sound3, R.drawable.lipsc1, R.drawable.lipsc2));
        items.put(4, new Item(R.id.image4, R.color.color4, R.raw.sound4, R.drawable.lipsc1, R.drawable.lipsf2));
        items.put(5, new Item(R.id.image5, R.color.color5, R.raw.sound5, R.drawable.lipsc1, R.drawable.lipsc2));
        items.put(6, new Item(R.id.image6, R.color.color6, R.raw.sound6, R.drawable.lipsc1, R.drawable.lipsc2));
        items.put(7, new Item(R.id.image7, R.color.color7, R.raw.sound7, R.drawable.lipsc1, R.drawable.lipsc2));
    }


    public void start(View view) {
        textScore.setText("Score: " + score);
        playlevel();
    }

    public void capture(View view) {
        switch (view.getId()) {
            case R.id.image1:
                break;
            case R.id.image2:
                break;
            case R.id.image3:
                break;
            case R.id.image4:
                break;
            case R.id.image5:
                break;
            case R.id.image6:
                break;
            case R.id.image7:
                break;
        }
    }

    private int generateRandom() {
        return (int) ((Math.random() * 7) + 1);
    }

    private void playlevel() {
        int random = generateRandom();
        sequence.add(random);
        currentIndex = 0;
        playSequence();
    }

    private void playSequence() {

        if (currentIndex < sequence.size()) {
            int id = sequence.get(currentIndex);

            currentItem = items.get(id);
            ImageView iv = ((ImageView) findViewById(currentItem.getView()));
            iv.setImageDrawable(getResources().getDrawable(currentItem.getImage2()));
            iv.setBackgroundColor(getResources().getColor(currentItem.getColor()));

            mp = MediaPlayer.create(getApplicationContext(), currentItem.getSound());
            mp.start();

            Timer timer = new Timer();
            timer.schedule(new RestoreImageTask(), 1000);
            currentIndex++;
        }
    }

    private class RestoreImageTask extends TimerTask {
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ImageView iv = (ImageView) MainActivity.this.findViewById(currentItem.getView());
                    iv.setImageDrawable(getResources().getDrawable(currentItem.getImage1()));
                    iv.setBackgroundColor(Color.WHITE);
                    playSequence();
                }
            });

        }
    }

}
