package com.timecat.plugin.colorreference.mdcolor;

import android.app.Activity;
import android.os.Bundle;

import com.timecat.plugin.colorreference.R;
import com.timecat.plugin.colorreference.mdcolor.util.C;
import com.timecat.plugin.colorreference.mdcolor.util.SP;

public class BaseActivity extends Activity {
    protected SP sp = null;

    public static final int[] DEFAULT_THEME_ID = {R.style.PluginAppThemeDark,
                                                  R.style.PluginAppThemeLight, R.style.PluginAppThemeLightD};
    public static final int[][] THEMES_ID = {
            {R.style.PluginAppThemeDark_Red, R.style.PluginAppThemeLight_Red, R.style.PluginAppThemeLightD_Red},
            {R.style.PluginAppThemeDark_Pink, R.style.PluginAppThemeLight_Pink, R.style.PluginAppThemeLightD_Pink},
            {R.style.PluginAppThemeDark_Purple, R.style.PluginAppThemeLight_Purple, R.style.PluginAppThemeLightD_Purple},
            {R.style.PluginAppThemeDark_DeepPurple, R.style.PluginAppThemeLight_DeepPurple, R.style.PluginAppThemeLightD_DeepPurple},
            {R.style.PluginAppThemeDark_Indigo, R.style.PluginAppThemeLight_Indigo, R.style.PluginAppThemeLightD_Indigo},
            {R.style.PluginAppThemeDark_Blue, R.style.PluginAppThemeLight_Blue, R.style.PluginAppThemeLightD_Blue},
            {R.style.PluginAppThemeDark_LightBlue, R.style.PluginAppThemeLight_LightBlue, R.style.PluginAppThemeLightD_LightBlue},
            {R.style.PluginAppThemeDark_Cyan, R.style.PluginAppThemeLight_Cyan, R.style.PluginAppThemeLightD_Cyan},
            {R.style.PluginAppThemeDark_Teal, R.style.PluginAppThemeLight_Teal, R.style.PluginAppThemeLightD_Teal},
            {R.style.PluginAppThemeDark_Green, R.style.PluginAppThemeLight_Green, R.style.PluginAppThemeLightD_Green},
            {R.style.PluginAppThemeDark_LightGreen, R.style.PluginAppThemeLight_LightGreen, R.style.PluginAppThemeLightD_LightGreen},
            {R.style.PluginAppThemeDark_Lime, R.style.PluginAppThemeLight_Lime, R.style.PluginAppThemeLightD_Lime},
            {R.style.PluginAppThemeDark_Yellow, R.style.PluginAppThemeLight_Yellow, R.style.PluginAppThemeLightD_Yellow},
            {R.style.PluginAppThemeDark_Amber, R.style.PluginAppThemeLight_Amber, R.style.PluginAppThemeLightD_Amber},
            {R.style.PluginAppThemeDark_Orange, R.style.PluginAppThemeLight_Orange, R.style.PluginAppThemeLightD_Orange},
            {R.style.PluginAppThemeDark_DeepOrange, R.style.PluginAppThemeLight_DeepOrange, R.style.PluginAppThemeLightD_DeepOrange},
            {R.style.PluginAppThemeDark_Brown, R.style.PluginAppThemeLight_Brown, R.style.PluginAppThemeLightD_Brown},
            {R.style.PluginAppThemeDark_Grey, R.style.PluginAppThemeLight_Grey, R.style.PluginAppThemeLightD_Grey},
            {R.style.PluginAppThemeDark_BlueGrey, R.style.PluginAppThemeLight_BlueGrey, R.style.PluginAppThemeLightD_BlueGrey},
    };
    public static final int[] DIALOG_THEME_ID = {R.style.DialogThemeDark,
            R.style.DialogThemeLight, R.style.DialogThemeLight};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = new SP(this, false);

        int themeColor = sp.getInt(C.SP_THEME_COLOR, -1);
        int themeStyle = sp.getInt(C.SP_THEME_STYLE);
        boolean withDarkAb = sp.getBoolean(C.SP_WITH_DARK_AB);

        if (themeColor >= 0 && themeColor < THEMES_ID.length) {
            if (themeStyle == 0) {
                setTheme(THEMES_ID[themeColor][0]);
            } else {
                setTheme(THEMES_ID[themeColor][withDarkAb ? 2 : 1]);
            }
        } else {
            setTheme(DEFAULT_THEME_ID[themeStyle]);
        }

        super.onCreate(savedInstanceState);
    }
}
