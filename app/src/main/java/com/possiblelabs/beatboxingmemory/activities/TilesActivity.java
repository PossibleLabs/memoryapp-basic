package com.possiblelabs.beatboxingmemory.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.possiblelabs.beatboxingmemory.R;

import ru.katso.livebutton.LiveButton;

/**
 * Created by Brian on 04/06/2016.
 */
public class TilesActivity extends BaseActivity {
    TextView tittle;
    TextView textLevel;
    TextView textLevelCurrent;

    LiveButton buttonDo;
    LiveButton buttonRe;
    LiveButton buttonMi;
    LiveButton buttonFa;
    LiveButton buttonSol;
    LiveButton buttonLa;
    LiveButton buttonSi;
    LiveButton buttonSolb;
    LiveButton buttonReb;
    LiveButton buttonRepeat;
    LiveButton buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        loadReferences();

        Typeface TF = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tittle.setTypeface(TF);
        textLevel.setTypeface(TF);
        textLevelCurrent.setTypeface(TF);

        buttonDo.setTypeface(TF);
        buttonRe.setTypeface(TF);
        buttonMi.setTypeface(TF);
        buttonFa.setTypeface(TF);
        buttonSol.setTypeface(TF);
        buttonLa.setTypeface(TF);
        buttonSi.setTypeface(TF);
        buttonSolb.setTypeface(TF);
        buttonReb.setTypeface(TF);
        buttonRepeat.setTypeface(TF);
        buttonExit.setTypeface(TF);
    }

    private void loadReferences() {
        buttonDo = (LiveButton) findViewById(R.id.button_do);
        buttonRe = (LiveButton) findViewById(R.id.button_re);
        buttonMi = (LiveButton) findViewById(R.id.button_mi);
        buttonFa = (LiveButton) findViewById(R.id.button_fa);
        buttonSol = (LiveButton) findViewById(R.id.button_sol);
        buttonLa = (LiveButton) findViewById(R.id.button_la);
        buttonSi = (LiveButton) findViewById(R.id.button_si);
        buttonSolb = (LiveButton) findViewById(R.id.button_slob);
        buttonReb = (LiveButton) findViewById(R.id.button_Reb);
        buttonRepeat = (LiveButton) findViewById(R.id.button_repeat);
        buttonExit = (LiveButton) findViewById(R.id.button_menu);

        tittle = (TextView) findViewById(R.id.tview_game_tittle);
        textLevel = (TextView) findViewById(R.id.text_level);
        textLevelCurrent = (TextView) findViewById(R.id.tview_level);
        tittle.setText(Html.fromHtml(TITTLE_APP));
        textLevel.setText(Html.fromHtml(LEVEL_TEXT));
    }

}
