package com.pukka.ydepg.moudule.multviewforstb.multiview.module.view.focus;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public interface FocusBorder {

    void setVisible(boolean visible);

    void setVisible(boolean visible, boolean anim);

    boolean isVisible();

    View getView();

    void onFocus(@NonNull View focusView, @Nullable Options options);

    void boundGlobalFocusListener(@NonNull OnFocusCallback callback);

    void unBoundGlobalFocusListener();

    interface OnFocusCallback {
        @Nullable Options onFocus(View oldFocus, View newFocus);
    }

    abstract class Options {}

    class Builder {
        public final ColorFocusBorder.Builder asColor() {
            return new ColorFocusBorder.Builder();
        }

        public final DrawableFocusBorder.Builder asDrawable() {
            return new DrawableFocusBorder.Builder();
        }
    }

    class OptionsFactory {
        public static Options get() {
            return get(1f, 1f);
        }

        public static Options get(float scaleX, float scaleY) {
            return get(scaleX, scaleY, null);
        }

        public static Options get(float scaleX, float scaleY, String title) {
            return AbsFocusBorder.Options.get(scaleX, scaleY, title);
        }

        public static Options get(float scaleX, float scaleY, float roundRadius) {
            return get(scaleX, scaleY, roundRadius, null);
        }

        public static Options get(float scaleX, float scaleY, float roundRadius, String title) {
            return ColorFocusBorder.Options.get(scaleX, scaleY, roundRadius, title);
        }
    }
}

