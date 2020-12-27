package edu.iubat.vts.extensions;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import dev.alshakib.dtext.DText;

public final class ResourcesExt {
    public static boolean isRtl(@NonNull Resources resources) {
        return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public static int convertDpToPx(@NonNull Resources resources, float dp) {
        return (int) (dp * resources.getDisplayMetrics().density);
    }

    public static int convertSpToPx(@NonNull Resources resources, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
    }

    @NonNull
    public static String getString(@NonNull Context context, @StringRes int res) {
        String string = context.getString(res);
        if (string == null) {
            return "";
        }
        return string;
    }

    public static Drawable createThumbnailFromString(String s) {
        return new DText.Builder()
                .setText(s)
                .boldText()
                .randomBackgroundColor()
                .firstCharOnly()
                .alphaNumOnly()
                .toUpperCase()
                .build();
    }
}
