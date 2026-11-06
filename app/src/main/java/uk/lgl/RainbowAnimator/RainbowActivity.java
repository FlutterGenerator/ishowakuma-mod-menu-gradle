package uk.lgl.RainbowAnimator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.util.AttributeSet;

public class RainbowActivity extends RainbowStart {

  private float colorSpace;
  private float colorSpeed;
  private float translate;

  private int[] colors = { 0xFFFF2A02, 0xFFFF7F02, 0xFFEDFB02, 0xFF21FE02, 0xFF21F3FF, 0xFF21E0FF,
      0xFFFF4D17 };

  private LinearGradient gradient;
  private Matrix matrix;
  private RainbowActivation animationListener;

  public RainbowActivity(Context context) {
    this(context, null);
  }

  public RainbowActivity(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RainbowActivity(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    // Значения по умолчанию (без R.styleable, поэтому attrs.xml не нужен).
    // Менять можно через setColorSpace() и setColorSpeed().
    colorSpace = RainbowMath.dp2px(100);
    colorSpeed = RainbowMath.dp2px(3);

    matrix = new Matrix();
    initPaint();
  }

  private void initPaint() {
    if (colorSpace <= 0) {
      colorSpace = RainbowMath.dp2px(100);
    }

    gradient = new LinearGradient(0, 0, colorSpace, 0, colors, null, Shader.TileMode.MIRROR);

    getPaint().setShader(gradient);
    invalidate();
  }

  @Override
  public void setProgress(float progress) {
    translate = progress * colorSpace;
    invalidate();
  }

  @Override
  public void setAnimationListener(RainbowActivation listener) {
    animationListener = listener;
  }

  public float getColorSpace() {
    return colorSpace;
  }

  public void setColorSpace(float value) {
    colorSpace = Math.max(1f, value);
    initPaint();
  }

  public float getColorSpeed() {
    return colorSpeed;
  }

  public void setColorSpeed(float value) {
    colorSpeed = value;
  }

  public void setColors(int... newColors) {
    if (newColors == null || newColors.length < 2) {
      return;
    }

    colors = newColors;
    initPaint();
  }

  @Override
  public void animateText(CharSequence text) {
    setText(text);
    translate = 0;
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (gradient == null) {
      initPaint();
    }

    translate += colorSpeed;

    if (translate > colorSpace * 2) {
      translate = 0;

      if (animationListener != null) {
        animationListener.onAnimationEnd(this);
      }
    }

    matrix.setTranslate(translate, 0);
    gradient.setLocalMatrix(matrix);

    super.onDraw(canvas);

    postInvalidateDelayed(100);
  }
}
