package com.possiblelabs.beatboxingmemory.activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.possiblelabs.beatboxingmemory.R;

import java.util.HashMap;
import java.util.Map;

import ru.katso.livebutton.LiveButton;


/**
 * Created by Brian on 04/06/2016.
 */
public class PianoActivity extends BaseActivity {


    private static final int NUM_KEYS = 17;

    private final int[] mKeyIds = {R.id.key_c4, R.id.key_csharp4, R.id.key_d4, R.id.key_eflat4,
            R.id.key_e4, R.id.key_f4, R.id.key_fsharp4, R.id.key_g4, R.id.key_gsharp4,
            R.id.key_a4, R.id.key_bflat4, R.id.key_b4, R.id.key_c5, R.id.key_csharp5,
            R.id.key_d5, R.id.key_eflat5, R.id.key_e5
    };

    private int[] mNoteResourceIds;
    TypedArray mArr;

    private Map<Integer, Integer> mKeyMidiMap = null;
    private SoundPool mSoundPool;

    private Rect mRect;
    private Rect mNextBlackKeyRect;
    private Rect mPrevBlackKeyRect;

    private RelativeLayout mParent;

    TextView tittle;
    TextView textLevel;
    TextView textLevelCurrent;

    LiveButton buttonExit;
    LiveButton buttonRepeat;

    private final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, "Action down at (" + event.getX() + "," + event.getY() + ")");
                    mSoundPool.play(mKeyMidiMap.get(v.getId()).intValue(), 1.0f, 1.0f, 1, 0, 1.0f);
                    if (isWhiteKey(v)) {
                        ((ImageView) v).setImageDrawable(getResources().getDrawable(R.drawable.piano_white_key_pressed));
                    } else {
                        ((ImageView) v).setImageDrawable(getResources().getDrawable(R.drawable.piano_black_key_pressed));
                    }

                    mRect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

                    if (isWhiteKey(v)) {
                        int idBlackKeyHigh = getOverlappingBlackKeyHigher(v);
                        if (idBlackKeyHigh != 0) {
                            View tmp = findViewById(idBlackKeyHigh);
                            mNextBlackKeyRect = new Rect(tmp.getLeft(), tmp.getTop(), tmp.getRight(), tmp.getBottom());
                        } else {
                            mNextBlackKeyRect = null;
                        }

                        int idBlackKeyLow = getOverlappingBlackKeyLower(v);
                        if (idBlackKeyLow != 0) {
                            View tmp = findViewById(idBlackKeyLow);
                            mPrevBlackKeyRect = new Rect(tmp.getLeft(), tmp.getTop(), tmp.getRight(), tmp.getBottom());
                        } else {
                            mPrevBlackKeyRect = null;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isWhiteKey(v)) {
                        ((ImageView) v).setImageDrawable(getResources().getDrawable(R.drawable.piano_white_key_unpressed));
                    } else {
                        ((ImageView) v).setImageDrawable(getResources().getDrawable(R.drawable.piano_black_key_unpressed));
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if ((!mRect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) ||
                            (isWhiteKey(v) && (mNextBlackKeyRect != null) && (mNextBlackKeyRect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))) ||
                            (isWhiteKey(v) && (mPrevBlackKeyRect != null) && (mPrevBlackKeyRect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())))) {
                        MotionEvent upEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                                MotionEvent.ACTION_CANCEL, event.getX(), event.getY(), 0);
                        v.dispatchTouchEvent(upEvent);
                        upEvent.recycle();

                        MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                                MotionEvent.ACTION_DOWN, v.getLeft() + event.getX(), v.getTop() + event.getY(), 0);
                        mParent.dispatchTouchEvent(downEvent);
                        downEvent.recycle();
                    }
            }

            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piano);

        mParent = (RelativeLayout) findViewById(R.id.parent_view);

        mNoteResourceIds = new int[NUM_KEYS];
        mArr = getResources().obtainTypedArray(R.array.piano_notes);
        loadReferences();

        Typeface TF = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tittle.setTypeface(TF);
        textLevel.setTypeface(TF);
        textLevelCurrent.setTypeface(TF);
        buttonRepeat.setTypeface(TF);
        buttonExit.setTypeface(TF);

        getNoteRawResources();

        ImageView imageView = null;
        for (int i = 0; i < NUM_KEYS; i++) {
            imageView = (ImageView) findViewById(mKeyIds[i]);
            imageView.setOnTouchListener(mOnTouchListener);
        }
        if (mKeyMidiMap == null) {
            loadMIDISounds();
        }
    }

    private void loadReferences() {
        tittle = (TextView) findViewById(R.id.textView3);
        textLevel = (TextView) findViewById(R.id.textView_level);
        textLevelCurrent = (TextView) findViewById(R.id.textView_level_current);
        tittle.setText(Html.fromHtml(TITTLE_APP));
        textLevel.setText(Html.fromHtml(LEVEL_TEXT));
        buttonRepeat = (LiveButton) findViewById(R.id.button_listen);
        buttonExit = (LiveButton) findViewById(R.id.button_menu);
    }

    private void getNoteRawResources() {
        mArr = getResources().obtainTypedArray(R.array.piano_notes);
        for (int i = 0; i < NUM_KEYS; i++) {
            mNoteResourceIds[i] = mArr.getResourceId(i, 0);
        }
        mArr.recycle();
    }

    @SuppressLint("UseSparseArrays")
    private void loadMIDISounds() {
        mSoundPool = new SoundPool(NUM_KEYS, AudioManager.STREAM_MUSIC, 0);

        mKeyMidiMap = new HashMap<Integer, Integer>(NUM_KEYS);
        for (int i = 0; i < NUM_KEYS; i++) {
            mKeyMidiMap.put(mKeyIds[i], mSoundPool.load(this, mNoteResourceIds[i], 0));
        }
    }

    private boolean isWhiteKey(View key) {
        switch (key.getId()) {
            case R.id.key_c4:
            case R.id.key_d4:
            case R.id.key_e4:
            case R.id.key_f4:
            case R.id.key_g4:
            case R.id.key_a4:
            case R.id.key_b4:
            case R.id.key_c5:
            case R.id.key_d5:
            case R.id.key_e5:
                return true;
            default:
                return false;

        }
    }

    private int getOverlappingBlackKeyHigher(View key) {
        switch (key.getId()) {
            case R.id.key_c4:
                return R.id.key_csharp4;
            case R.id.key_d4:
                return R.id.key_eflat4;
            case R.id.key_f4:
                return R.id.key_fsharp4;
            case R.id.key_g4:
                return R.id.key_gsharp4;
            case R.id.key_a4:
                return R.id.key_bflat4;
            case R.id.key_c5:
                return R.id.key_csharp5;
            case R.id.key_d5:
                return R.id.key_eflat5;
            default:
                return 0;
        }
    }

    private int getOverlappingBlackKeyLower(View key) {
        switch (key.getId()) {
            case R.id.key_d4:
                return R.id.key_csharp4;
            case R.id.key_e4:
                return R.id.key_eflat4;
            case R.id.key_g4:
                return R.id.key_fsharp4;
            case R.id.key_a4:
                return R.id.key_gsharp4;
            case R.id.key_b4:
                return R.id.key_bflat4;
            case R.id.key_d5:
                return R.id.key_csharp5;
            case R.id.key_e5:
                return R.id.key_eflat5;
            default:
                return 0;
        }
    }

}
