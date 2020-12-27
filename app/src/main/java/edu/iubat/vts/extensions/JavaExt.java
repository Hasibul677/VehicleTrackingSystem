package edu.iubat.vts.extensions;

import android.text.TextUtils;
import android.util.Patterns;

public final class JavaExt {
    public static boolean isValidEmail(final String email) {
        if (email == null) {
            return false;
        } else if (email != null && email.isEmpty()) {
            return false;
        }
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email ).matches());
    }

    public static boolean isValidUrl(final String url) {
        if (url == null) {
            return false;
        } else if (url != null && url.isEmpty()) {
            return false;
        }
        return (!TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url ).matches());
    }
}
