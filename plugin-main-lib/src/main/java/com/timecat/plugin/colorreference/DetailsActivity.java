package com.timecat.plugin.colorreference;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.timecat.plugin.colorreference.mdcolor.BaseActivity;
import com.timecat.plugin.colorreference.mdcolor.fragment.HelpDialog;
import com.timecat.plugin.colorreference.mdcolor.util.C;
import com.timecat.plugin.colorreference.mdcolor.util.Palette;
import com.timecat.plugin.colorreference.mdcolor.util.adapter.GradesAdapter;

public class DetailsActivity extends BaseActivity {
    private ListView lvGrades;

    private GradesAdapter gradesAdapter = null;
    private Palette palette = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        init();

        // Tell users how to copy color values.
        // Just once.
        copyToast();
    }

    private void init() {
        Intent intent = getIntent();
        palette = (Palette) intent.getSerializableExtra("palette");

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(palette.getName());
        }

        lvGrades = (ListView) findViewById(R.id.lv_grades);

        gradesAdapter = new GradesAdapter(this, palette);
        lvGrades.setAdapter(gradesAdapter);

        lvGrades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                copy2Clipboard(palette.getColorStr(position));

                // Do not show again.
                sp.save(C.SP_TOAST_COPY, false);
            }
        });
    }

    private void copyToast() {
        if (sp.getBoolean(C.SP_TOAST_COPY, true)) {
            Toast.makeText(this, R.string.toast_tap_card_to_copy, Toast.LENGTH_LONG).show();
        }
    }

    private void copy2Clipboard(String text) {
        if (text == null) {
            return;
        }

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));

        Toast.makeText(this, getString(R.string.toast_copied, text), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("onCreateOptionsMenu", "R.menu.menu_details");
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.menu_help) {
            (new HelpDialog()).show(getFragmentManager(), "helpDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
