package uk.lgl.RainbowAnimator;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public final class RainbowMath {

  private RainbowMath() {
    // Utility class
  }

  /**
   * Converts dp to pixels.
   */
  public static int dp2px(float dp) {
    return Math.round(dp * getDisplayMetrics().density);
  }

  /**
   * Returns device display metrics.
   */
  public static DisplayMetrics getDisplayMetrics() {
    return Resources.getSystem().getDisplayMetrics();
  }

  /**
   * Returns screen height in pixels.
   */
  public static int getScreenHeight() {
    return getDisplayMetrics().heightPixels;
  }

  /**
   * Returns screen width in pixels.
   */
  public static int getScreenWidth() {
    return getDisplayMetrics().widthPixels;
  }
}
