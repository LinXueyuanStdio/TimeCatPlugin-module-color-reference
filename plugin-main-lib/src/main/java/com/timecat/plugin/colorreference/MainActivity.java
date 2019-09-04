package com.timecat.plugin.colorreference;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.timecat.plugin.colorreference.mdcolor.BaseActivity;
import com.timecat.plugin.colorreference.mdcolor.util.C;
import com.timecat.plugin.colorreference.mdcolor.util.Palette;
import com.timecat.plugin.colorreference.mdcolor.util.TransitionHelper;
import com.timecat.plugin.colorreference.mdcolor.util.adapter.MainAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseActivity {
    private ListView lvColors;
    private Switch switchBoard;
    private View viewControlBar;
    private View viewUIBoard;
    private ImageButton fabLucky;

    private MainAdapter mainAdapter = null;

    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        (new LoadColorsTask()).execute();

        TransitionHelper.getInstance().onActivityCreate(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabLucky.setVisibility(View.VISIBLE);
                fabLucky.setAnimation(AnimationUtils
                        .loadAnimation(MainActivity.this, R.anim.fab_bottom_in));
            }
        }, 134);
    }

    private void init() {
        setTitle("配色参考");

        viewControlBar = findViewById(R.id.view_control_bar);
        switchBoard = (Switch) findViewById(R.id.switch_board);
        fabLucky = (ImageButton) findViewById(R.id.fab_lucky);

        // Sets the data behind the ListView.
        mainAdapter = new MainAdapter(this, sp.getInt(C.SP_THEME_COLOR, -1));
        lvColors = (ListView) findViewById(R.id.lv_colors);
        lvColors.setAdapter(mainAdapter);

        lvColors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == sp.getInt(C.SP_THEME_COLOR, -1)) {
                    gotoDetails(position);
                    return;
                }

                // Tell users how to view details.
                // Just once.
                viewDetailsToast();

                sp.put(C.SP_THEME_COLOR, position).put(C.SP_WITH_DARK_AB,
                        mainAdapter.getItem(position).isLightThemeWithDarkABSuggested())
                        .save();

                changeTheme();
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_themes);
        radioGroup.check(switchRadioButtonOrderAndId(sp.getInt(C.SP_THEME_STYLE)));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int theme_style = switchRadioButtonOrderAndId(checkedId);
                if (theme_style == -1) {
                    return;
                }

                sp.save(C.SP_THEME_STYLE, theme_style);

                changeTheme();
            }
        });

        initControlAndUIBoard();

        initFab();
    }

    private void initControlAndUIBoard() {
        // As default, off if in portrait, and on if in landscape.
        if (sp.getBoolean(C.SP_UI_BOARD, getResources().getBoolean(R.bool.is_land))) {
            viewUIBoard = ((ViewStub) findViewById(R.id.vs_ui_board)).inflate();
            switchBoard.setChecked(true);
        }

        switchBoard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sp.save(C.SP_UI_BOARD, isChecked);

                if (isChecked) { // Show the UI board.
                    if (viewUIBoard == null) {
                        viewUIBoard = ((ViewStub) findViewById(R.id.vs_ui_board)).inflate();
                    } else {
                        viewUIBoard.setVisibility(View.VISIBLE);
                    }

                    if (!getResources().getBoolean(R.bool.is_land)) {
                        viewControlBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                                R.anim.translate));
                    }
                    viewUIBoard.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                            R.anim.fade_in));
                } else { // Hide the UI board.
                    viewUIBoard.setVisibility(View.GONE);
                }
            }
        });

        viewControlBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchBoard.clearFocus();

                switchBoard.performClick();
            }
        });
    }

    private void initFab() {

        fabLucky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Choose a random number except for current one.
                Random random = new Random();
                int lucky_one;
                do {
                    lucky_one = random.nextInt(mainAdapter.getCount());
                } while (lucky_one == mainAdapter.getChecked());

                sp.put(C.SP_THEME_COLOR, lucky_one)
                        .put(C.SP_WITH_DARK_AB, mainAdapter.getItem(lucky_one)
                                .isLightThemeWithDarkABSuggested())
                        .save();

                Toast.makeText(MainActivity.this, getString(R.string.toast_lucky_color,
                        mainAdapter.getItem(lucky_one).getName()), Toast.LENGTH_LONG).show();

                changeTheme();
            }
        });
        fabLucky.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.menu_lucky, Toast.LENGTH_LONG).show();

                return true;
            }
        });

    }

    private void gotoDetails(int which) {
        // Do not show toast again.
        sp.save(C.SP_TOAST_DETAILS, false);

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("palette", mainAdapter.getItem(which));

        startActivity(intent);
    }

    private void viewDetailsToast() {
        if (sp.getBoolean(C.SP_TOAST_DETAILS, true)) {
            Toast.makeText(this, R.string.toast_tap_again_details, Toast.LENGTH_LONG).show();

            //sp.save(C.SP_TOAST_DETAILS, false);
        }
    }

    private int switchRadioButtonOrderAndId(int i) {
        if (i == 0) {
            return R.id.rb_dark_theme;
        } else if (i == 1) {
            return R.id.rb_light_theme;
        } else if (i == R.id.rb_dark_theme) {
            return 0;
        } else if (i == R.id.rb_light_theme) {
            return 1;
            //case R.id.rb_light_with_dark_theme:
            //    return 2;
        }
        return -1;
    }

    private class LoadColorsTask extends AsyncTask<String, Integer, List<Palette>> {
        @Override
        protected List<Palette> doInBackground(String... params) {
            return loadColors();
        }

        @Override
        protected void onPostExecute(List<Palette> dataList) {
            super.onPostExecute(dataList);

            mainAdapter.notifyRefresh(dataList);
            //mainAdapter.notifyDataSetChanged();

            //lvColors.smoothScrollToPosition(sharedPreferences.getInt(C.SP_THEME_COLOR, 0));
            lvColors.setSelection(sp.getInt(C.SP_THEME_COLOR));
        }
    }

    private List<Palette> loadColors() {
        List<Palette> dataList = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResources()
                .openRawResource(R.raw.palette)));

        Palette palette;
        String tempStr;
        try {
            while ((tempStr = bufferedReader.readLine()) != null) {
                if (tempStr.startsWith("# ")) {
                    continue;
                }

                palette = new Palette();
                boolean is_ok = palette.setAll(tempStr);
                if (!is_ok) {
                    continue;
                }

                dataList.add(palette);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private void changeTheme() {
        // Shine!!!
        //recreate();

        TransitionHelper.getInstance().onRestartActivity(this);
        startActivity(new Intent(this, MainActivity.class));
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("onCreateOptionsMenu", "R.menu.menu_main");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Log.e("onOptionsItemSelected", "R.menu.menu_main : " + itemId);
        if (itemId == R.id.menu_reset) {
            sp.delete(C.SP_THEME_COLOR);
            sp.delete(C.SP_THEME_STYLE);

            Toast.makeText(this, R.string.toast_reset, Toast.LENGTH_LONG).show();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                        /*// Instead of this:
                        // ((RadioGroup) findViewById(R.id.rg_themes)).check(R.id.rb_dark_theme);

                        ((RadioGroup) findViewById(R.id.rg_themes)).clearCheck();
                        ((RadioButton) findViewById(R.id.rb_dark_theme)).setChecked(true);*/

                    changeTheme();
                }
            }, 400);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
