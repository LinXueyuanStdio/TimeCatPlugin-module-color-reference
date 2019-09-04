package com.timecat.plugin.colorreference.mdcolor.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.timecat.plugin.colorreference.R;
import com.timecat.plugin.colorreference.mdcolor.BaseActivity;
import com.timecat.plugin.colorreference.mdcolor.util.C;
import com.timecat.plugin.colorreference.mdcolor.util.SP;


public class HelpDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return getDialogBuilder()
                .setTitle(R.string.dlg_title_help)
                .setMessage(R.string.help_desc)
                .setPositiveButton(R.string.dlg_bt_got_it, null)
                .create();
    }

    private AlertDialog.Builder getDialogBuilder() {
        if (/*C.SDK >= 21 && */C.SDK < 23) {
            SP sp = new SP(getActivity(), false);
            if (sp.getInt(C.SP_THEME_COLOR, -1) >= 0) {
                return new AlertDialog.Builder(getActivity(),
                        BaseActivity.DIALOG_THEME_ID[sp.getInt(C.SP_THEME_STYLE)]);
            }
        }

        return new AlertDialog.Builder(getActivity());
    }
}
