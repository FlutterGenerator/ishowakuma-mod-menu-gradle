package uk.lgl.RainbowAnimator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public abstract class RainbowStart extends TextView {

  public RainbowStart(Context context) {
    this(context, null);
  }

  public RainbowStart(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RainbowStart(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public abstract void animateText(CharSequence text);

  public abstract void setAnimationListener(RainbowActivation listener);

  public abstract void setProgress(float progress);
}
