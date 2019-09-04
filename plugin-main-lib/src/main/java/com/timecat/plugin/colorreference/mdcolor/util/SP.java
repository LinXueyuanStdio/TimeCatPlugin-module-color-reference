package com.timecat.plugin.colorreference.mdcolor.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author zby
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-08-16
 * @discription null
 * @usage null
 */
public class SP {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String TAG_LAUNCH = "times_launch";
    public static final String TAG_LAUNCH_CUR_VER = "times_launch_cur_ver";

    public SP(Context context, SharedPreferences sharedPreferences, boolean enable_stats) {
        this.sharedPreferences = null;
        this.editor = null;
        this.sharedPreferences = sharedPreferences;
        this.recordLaunchTimes(context, enable_stats);
    }

    public SP(Context context, SharedPreferences sharedPreferences) {
        this(context, sharedPreferences, true);
    }

    public SP(Context context, boolean enable_stats) {
        this(context, context.getSharedPreferences(context.getPackageName(), 0), enable_stats);
    }

    public SP(Context context) {
        this(context, true);
    }

    private void recordLaunchTimes(Context context, boolean enable_stats) {
        if (enable_stats) {
            String curVerCode = String.valueOf(ExtraUtil.getVersionCode(context));
            this.put("times_launch", this.getInt("times_launch") + 1);
            this.put(curVerCode, this.getInt(curVerCode) + 1);
            this.put("times_launch_cur_ver", curVerCode);
            this.save();
        }

    }

    public int getLaunchTimes() {
        return this.getInt("times_launch");
    }

    public int getCurVerLaunchTimes() {
        return this.getInt(this.getString("times_launch_cur_ver"));
    }

    public boolean save(String tag, boolean value) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putBoolean(tag, value);
            return this.save();
        } else {
            return false;
        }
    }

    public boolean reverseAndSave(String tag, boolean def) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putBoolean(tag, !this.getBoolean(tag, def));
            return this.save();
        } else {
            return false;
        }
    }

    public boolean reverseAndSave(String tag) {
        return this.reverseAndSave(tag, false);
    }

    public SP put(String tag, boolean value) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putBoolean(tag, value);
            return this;
        } else {
            return this;
        }
    }

    public boolean save(String tag, int value) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putInt(tag, value);
            return this.save();
        } else {
            return false;
        }
    }

    public SP put(String tag, int value) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putInt(tag, value);
            return this;
        } else {
            return this;
        }
    }

    public boolean save(String tag, long value) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putLong(tag, value);
            return this.save();
        } else {
            return false;
        }
    }

    public SP put(String tag, long value) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putLong(tag, value);
            return this;
        } else {
            return this;
        }
    }

    public boolean save(String tag, float value) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putFloat(tag, value);
            return this.save();
        } else {
            return false;
        }
    }

    public SP put(String tag, float value) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putFloat(tag, value);
            return this;
        } else {
            return this;
        }
    }

    public boolean save(String tag, String value) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putString(tag, value);
            return this.save();
        } else {
            return false;
        }
    }

    public SP put(String tag, String value) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.putString(tag, value);
            return this;
        } else {
            return this;
        }
    }

    @TargetApi(9)
    public boolean save() {
        if (this.editor == null) {
            return false;
        } else if (C.SDK >= 9) {
            this.editor.apply();
            return true;
        } else {
            return this.editor.commit();
        }
    }

    public boolean getBoolean(String tag, boolean def) {
        return this.sharedPreferences != null && tag != null && this.sharedPreferences.getBoolean(tag, def);
    }

    public boolean getBoolean(String tag) {
        return this.getBoolean(tag, false);
    }

    public int getInt(String tag, int def) {
        return this.sharedPreferences != null && tag != null ? this.sharedPreferences.getInt(tag, def) : 0;
    }

    public int getInt(String tag) {
        return this.getInt(tag, 0);
    }

    public long getLong(String tag, long def) {
        return this.sharedPreferences != null && tag != null ? this.sharedPreferences.getLong(tag, def) : 0L;
    }

    public long getLong(String tag) {
        return this.getLong(tag, 0L);
    }

    public float getFloat(String tag, float def) {
        return this.sharedPreferences != null && tag != null ? this.sharedPreferences.getFloat(tag, def) : 0.0F;
    }

    public float getFloat(String tag) {
        return this.getFloat(tag, 0.0F);
    }

    public String getString(String tag, String def) {
        return this.sharedPreferences != null && tag != null ? this.sharedPreferences.getString(tag, def) : "";
    }

    public String getString(String tag) {
        return this.getString(tag, "");
    }

    public boolean contains(String tag) {
        return this.sharedPreferences != null && tag != null && this.sharedPreferences.contains(tag);
    }

    public boolean delete(String tag) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.remove(tag);
            return this.save();
        } else {
            return false;
        }
    }

    public SP remove(String tag) {
        if (this.sharedPreferences != null && tag != null) {
            if (this.editor == null) {
                this.editor = this.sharedPreferences.edit();
            }

            this.editor.remove(tag);
            return this;
        } else {
            return this;
        }
    }
}
