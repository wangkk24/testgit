package com.pukka.ydepg.customui.tv.widget;

public interface FocusHighlight {
    /**
     * No zoom factor.
     */
    public static final int ZOOM_FACTOR_NONE = 0;

    /**
     * A small zoom factor, recommended for large item views.
     */
    public static final int ZOOM_FACTOR_SMALL = 1;

    /**
     * A medium zoom factor, recommended for medium sized item views.
     */
    public static final int ZOOM_FACTOR_MEDIUM = 2;

    /**
     * A large zoom factor, recommended for small item views.
     */
    public static final int ZOOM_FACTOR_LARGE = 3;

    /**
     * An extra small zoom factor.
     */
    public static final int ZOOM_FACTOR_XSMALL = 4;
}