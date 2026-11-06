package uk.lgl.modmenu;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import uk.lgl.RainbowAnimator.RainbowActivity;

public class FloatingModMenuService extends Service {

  public static final String TAG = "Mod_Menu";
  int BTN_COLOR;
  int BtnOFF;
  int BtnON;
  int CI;
  int CategoryBG;
  int CheckBoxColor;
  ScrollView H;
  float ICON_ALPHA;
  int ICON_SIZE;
  int LST_MAB;
  int MENU_BG_COLOR;
  float MENU_CORNER;
  int MENU_FEATURE_BG_COLOR;
  int MENU_HEIGHT;
  int MENU_WIDTH;
  String NumberTxtColor;
  int RadioColor;
  HorizontalScrollView Scroll;
  int SeekBarColor;
  int SeekBarProgressColor;
  int TAB_STROKE_COLOR;
  int TAB_TEXT_COLOR;
  float TAB_TEXT_SIZE;
  int TEXT_COLOR;
  int TEXT_COLOR_2;
  int ToggleOFF;
  int ToggleON;
  BitmapDrawable blackgamer;
  Button closeBtn;
  LinearLayout h;
  LinearLayout h2;
  private Handler handler;
  Button hideBtn;
  LinearLayout l;
  LinearLayout mCollapse;
  RelativeLayout mCollapsed;
  LinearLayout mExpanded;
  RelativeLayout mRootContainer;
  LinearLayout mSettings;
  WindowManager mWindowManager;
  WindowManager.LayoutParams params;
  LinearLayout patches;
  LinearLayout patches2;
  LinearLayout patches3;
  LinearLayout patches4;
  LinearLayout patches5;
  LinearLayout patches6;
  LinearLayout patches7;
  LinearLayout patches8;
  FrameLayout rootFrame;
  LinearLayout.LayoutParams scrlLL;
  LinearLayout.LayoutParams scrlLLExpanded;
  ScrollView scrollView;
  ImageView startimage;
  boolean stopChecking;
  LinearLayout subLayout;
  TextView t1;
  TextView t2;
  TextView t3;
  TextView t4;
  TextView t5;
  TextView t6;
  TextView t7;
  TextView t8;
  private TextView textView;
  LinearLayout tl;
  private Runnable updateTimeRunnable;
  LinearLayout v1;
  LinearLayout v10;
  LinearLayout v2;
  LinearLayout v3;
  LinearLayout v4;
  LinearLayout v5;
  LinearLayout v6;
  LinearLayout v7;
  LinearLayout v8;
  LinearLayout v9;
  int vxy;
  int yen;

  public native String Background();

  public native String EYE();

  public native String EYECLOSE();

  public native String Icon();

  public native String IconTele();

  public native String IconTeleVip();

  public native String IconWebViewData();

  public native String IconYoutube();

  public native String Sonic();

  public native String[] getFeatureList();

  public native String[] getFeatureList2();

  public native String[] getFeatureList3();

  public native String[] getFeatureList4();

  public native String[] getFeatureList5();

  public native String[] getFeatureList6();

  public native String[] getFeatureList7();

  public native String[] getFeatureList8();

  public native boolean isGameLibLoaded();

  public native void setHeadingText(TextView textView);

  public native void setTitleText(TextView textView);

  public native String[] settingsList();

  public void AddColor(View view, int i, float f, int i2, int i3) {
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(i);
    gradientDrawable.setCornerRadius(f);
    gradientDrawable.setStroke(i2, i3);
    view.setBackgroundDrawable(gradientDrawable);
  }

  public void AddColor(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8,
      int i9, int i10, int i11, int i12, int i13) {
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(i);
    gradientDrawable.setCornerRadii(new float[] { i6, i7, i8, i9, i10, i11, i12, i13 });
    gradientDrawable.setStroke(i2, i5, i3, i4);
    view.setBackgroundDrawable(gradientDrawable);
  }

  @Override // android.app.Service
  public void onCreate() {
    super.onCreate();
    Preferences.context = this;
    initFloating();
    Handler handler = new Handler();
    handler.post(new AnonymousClass100000000(this, handler));
  }

  public static class AnonymousClass100000000 implements Runnable {
    private final FloatingModMenuService this$0;
    final Handler val$handler;

    public AnonymousClass100000000(FloatingModMenuService floatingModMenuService, Handler handler) {
      this.this$0 = floatingModMenuService;
      this.val$handler = handler;
    }

    @Override // java.lang.Runnable
    public void run() {
      this.this$0.Thread();
      this.val$handler.postDelayed(this, 1000);
    }
  }

  public static class AnonymousClass100000001 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000001(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.this$0.mCollapsed.setVisibility(8);
      this.this$0.mExpanded.setVisibility(0);
    }
  }

  public static class AnonymousClass100000002 implements View.OnClickListener {
    boolean settingsOpen;
    private final FloatingModMenuService this$0;

    public AnonymousClass100000002(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      Intent intent = new Intent("android.intent.action.VIEW");
      intent.setFlags(268435456);
      intent.setData(Uri.parse("https://t.me/sbgamingytgroup"));
      this.this$0.startActivity(intent);
    }
  }

  public static class AnonymousClass100000003 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000003(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.this$0.scrollView.removeAllViews();
      this.this$0.scrollView.addView(this.this$0.patches);
      this.this$0.AddColor(this.this$0.t1, this.this$0.LST_MAB, 0, 3, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t2, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t3, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t4, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t5, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t6, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t7, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t8, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
    }
  }

  public static class AnonymousClass100000004 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000004(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.this$0.scrollView.removeAllViews();
      this.this$0.scrollView.addView(this.this$0.patches2);
      this.this$0.AddColor(this.this$0.t1, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t2, this.this$0.LST_MAB, 0, 3, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t3, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t4, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t5, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t6, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t7, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t8, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
    }
  }

  public static class AnonymousClass100000005 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000005(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.this$0.scrollView.removeAllViews();
      this.this$0.scrollView.addView(this.this$0.patches8);
      this.this$0.AddColor(this.this$0.t1, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t2, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t3, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t4, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t5, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t6, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t7, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t8, this.this$0.LST_MAB, 0, 3, this.this$0.TAB_STROKE_COLOR);
    }
  }

  public static class AnonymousClass100000006 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000006(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.this$0.scrollView.removeAllViews();
      this.this$0.scrollView.addView(this.this$0.patches3);
      this.this$0.AddColor(this.this$0.t1, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t2, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t3, this.this$0.LST_MAB, 0, 3, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t4, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t5, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t6, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t7, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t8, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
    }
  }

  public static class AnonymousClass100000007 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000007(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.this$0.scrollView.removeAllViews();
      this.this$0.scrollView.addView(this.this$0.patches4);
      this.this$0.AddColor(this.this$0.t1, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t2, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t3, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t4, this.this$0.LST_MAB, 0, 3, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t5, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t6, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t7, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t8, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
    }
  }

  public static class AnonymousClass100000008 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000008(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.this$0.scrollView.removeAllViews();
      this.this$0.scrollView.addView(this.this$0.patches5);
      this.this$0.AddColor(this.this$0.t1, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t2, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t3, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t4, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t5, this.this$0.LST_MAB, 0, 3, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t6, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t7, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t8, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
    }
  }

  public static class AnonymousClass100000009 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000009(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.this$0.scrollView.removeAllViews();
      this.this$0.scrollView.addView(this.this$0.patches6);
      this.this$0.AddColor(this.this$0.t1, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t2, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t3, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t4, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t5, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t6, this.this$0.LST_MAB, 0, 3, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t7, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t8, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
    }
  }

  public static class AnonymousClass100000010 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000010(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.this$0.scrollView.removeAllViews();
      this.this$0.scrollView.addView(this.this$0.patches7);
      this.this$0.AddColor(this.this$0.t1, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t2, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t3, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t4, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t5, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t6, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t7, this.this$0.LST_MAB, 0, 3, this.this$0.TAB_STROKE_COLOR);
      this.this$0.AddColor(this.this$0.t8, 0, 0, 0, this.this$0.TAB_STROKE_COLOR);
    }
  }

  public static class AnonymousClass100000011 implements View.OnClickListener {
    boolean settingsOpen;
    private final FloatingModMenuService this$0;

    public AnonymousClass100000011(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      try {
        this.settingsOpen = !this.settingsOpen;
        if (this.settingsOpen) {
          this.this$0.hideBtn.setText("CLOSE");
          this.this$0.H.setVisibility(0);
          this.this$0.l.setVisibility(8);
          this.this$0.scrollView.setVisibility(8);
        } else {
          this.this$0.hideBtn.setText("SETTINGS");
          this.this$0.H.setVisibility(8);
          this.this$0.l.setVisibility(0);
          this.this$0.scrollView.setVisibility(0);
        }
      } catch (IllegalStateException e) {
      }
    }
  }

  public static class AnonymousClass100000012 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000012(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.this$0.mCollapsed.setVisibility(0);
      this.this$0.mCollapsed.setAlpha(this.this$0.ICON_ALPHA);
      this.this$0.mExpanded.setVisibility(8);
    }
  }

  public void initFloating() {
    Typeface.createFromAsset(getAssets(), "NoName1.ttf");
    Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "NoName2.ttf");
    Typeface createFromAsset2 = Typeface.createFromAsset(getAssets(), "NoName3.ttf");
    this.rootFrame = new FrameLayout(this);
    this.rootFrame.setOnTouchListener(onTouchListener());
    this.mRootContainer = new RelativeLayout(this);
    this.mCollapsed = new RelativeLayout(this);
    this.mCollapsed.setVisibility(0);
    this.mCollapsed.setAlpha(this.ICON_ALPHA);
    this.mExpanded = new LinearLayout(this);
    this.mExpanded.setVisibility(8);
    this.mExpanded.setBackgroundColor(this.MENU_BG_COLOR);
    this.mExpanded.setOrientation(1);
    this.mExpanded.setLayoutParams(new LinearLayout.LayoutParams(dp(this.MENU_WIDTH), -2));
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setCornerRadius(this.MENU_CORNER);
    gradientDrawable.setColor(this.MENU_BG_COLOR);
    gradientDrawable.setStroke(4, -1);
    byte[] decode = Base64.decode(Background(), 0);
    this.blackgamer = new BitmapDrawable(BitmapFactory.decodeByteArray(decode, 0, decode.length));
    this.mExpanded.setBackground(this.blackgamer);
    this.startimage = new ImageView(this);
    this.startimage.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
    int applyDimension = (int) TypedValue.applyDimension(1, this.ICON_SIZE,
        getResources().getDisplayMetrics());
    this.startimage.getLayoutParams().height = applyDimension;
    this.startimage.getLayoutParams().width = applyDimension;
    this.startimage.setScaleType(ImageView.ScaleType.FIT_XY);
    byte[] decode2 = Base64.decode(Icon(), 0);
    this.startimage.setImageBitmap(BitmapFactory.decodeByteArray(decode2, 0, decode2.length));
    ((ViewGroup.MarginLayoutParams) this.startimage
        .getLayoutParams()).topMargin = convertDipToPixels(10);
    this.startimage.setOnTouchListener(onTouchListener());
    this.startimage.setOnClickListener(new AnonymousClass100000001(this));
    WebView webView = new WebView(this);
    webView.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
    int applyDimension2 = (int) TypedValue.applyDimension(1, this.ICON_SIZE,
        getResources().getDisplayMetrics());
    webView.getLayoutParams().height = applyDimension2;
    webView.getLayoutParams().width = applyDimension2;
    webView.loadData(
        new StringBuffer()
            .append(new StringBuffer().append(new StringBuffer().append(new StringBuffer()
                .append(new StringBuffer().append(new StringBuffer()
                    .append(new StringBuffer()
                        .append(new StringBuffer()
                            .append(new StringBuffer()
                                .append(new StringBuffer()
                                    .append(new StringBuffer().append("<html>")
                                        .append("<head></head>").toString())
                                    .append("<body style=\"margin: 0; padding: 0\">").toString())
                                .append("<img src=\"").toString())
                            .append(IconWebViewData()).toString())
                        .append("\" width=\"").toString())
                    .append(this.ICON_SIZE).toString()).append("\" height=\"").toString())
                .append(this.ICON_SIZE).toString()).append("\" >").toString()).append("</body>")
                .toString())
            .append("</html>").toString(),
        "text/html", "utf-8");
    webView.setBackgroundColor(0);
    webView.setAlpha(this.ICON_ALPHA);
    webView.setOnTouchListener(onTouchListener());
    this.subLayout = new LinearLayout(this);
    this.subLayout.setOrientation(1);
    this.subLayout.setPadding(5, 5, 5, 5);
    TextView textView = new TextView(this);
    textView.setText(Build.VERSION.SDK_INT >= 23 ? "☹︎" : "☻︎︎");
    textView.setTextColor(this.TEXT_COLOR);
    textView.setTypeface(Typeface.DEFAULT_BOLD);
    textView.setTextSize(25.0f);
    textView.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
    layoutParams.addRule(11);
    textView.setLayoutParams(layoutParams);
    textView.setOnClickListener(new AnonymousClass100000002(this));
    this.H = new ScrollView(this);
    this.H.setVisibility(8);
    this.H.setLayoutParams(new LinearLayout.LayoutParams(-1, dp(220)));
    this.mSettings = new LinearLayout(this);
    this.mSettings.setOrientation(1);
    featureList(settingsList(), this.mSettings);
    RelativeLayout relativeLayout = new RelativeLayout(this);
    relativeLayout.setPadding(10, 5, 10, 5);
    relativeLayout.setVerticalGravity(16);
    RainbowActivity rainbowActivity = new RainbowActivity(this);
    new TextView(this);
    rainbowActivity.setTextColor(this.TEXT_COLOR);
    rainbowActivity.setTextSize(22.0f);
    rainbowActivity.setTypeface(createFromAsset);
    rainbowActivity.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    rainbowActivity.setGravity(17);
    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
    layoutParams2.addRule(14);
    layoutParams2.addRule(15);
    rainbowActivity.setLayoutParams(layoutParams2);
    setTitleText(rainbowActivity);
    RainbowActivity rainbowActivity2 = new RainbowActivity(this);
    rainbowActivity2.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    rainbowActivity2.setMarqueeRepeatLimit(-1);
    rainbowActivity2.setSingleLine(true);
    rainbowActivity2.setSelected(true);
    rainbowActivity2.setTextColor(this.TEXT_COLOR);
    rainbowActivity2.setBackgroundColor(this.LST_MAB);
    rainbowActivity2.setTextSize(10.0f);
    rainbowActivity2.setGravity(17);
    rainbowActivity2.setPadding(0, 0, 0, 5);
    setHeadingText(rainbowActivity2);
    TableRow.LayoutParams layoutParams3 = new TableRow.LayoutParams(-2, -1);
    layoutParams3.setMargins(5, 2, 5, 2);
    this.h = new LinearLayout(this);
    this.h.setBackgroundColor(this.TAB_STROKE_COLOR);
    this.h.setPadding(-1, 2, -1, 1);
    this.h2 = new LinearLayout(this);
    this.h2.setBackgroundColor(this.TAB_STROKE_COLOR);
    this.h2.setPadding(-1, 2, -1, 1);
    this.v1 = new LinearLayout(this);
    this.v1.setBackgroundColor(this.TAB_STROKE_COLOR);
    this.v1.setPadding(2, 5, 1, 5);
    this.v1.setLayoutParams(layoutParams3);
    this.v2 = new LinearLayout(this);
    this.v2.setBackgroundColor(this.TAB_STROKE_COLOR);
    this.v2.setPadding(2, 5, 1, 5);
    this.v2.setLayoutParams(layoutParams3);
    this.v3 = new LinearLayout(this);
    this.v3.setBackgroundColor(this.TAB_STROKE_COLOR);
    this.v3.setPadding(2, 5, 1, 5);
    this.v3.setLayoutParams(layoutParams3);
    this.v4 = new LinearLayout(this);
    this.v4.setBackgroundColor(this.TAB_STROKE_COLOR);
    this.v4.setPadding(2, 5, 1, 5);
    this.v4.setLayoutParams(layoutParams3);
    this.v5 = new LinearLayout(this);
    this.v5.setBackgroundColor(-1);
    this.v5.setPadding(2, 5, 1, 5);
    this.v5.setLayoutParams(layoutParams3);
    this.v6 = new LinearLayout(this);
    this.v6.setBackgroundColor(-1);
    this.v6.setPadding(2, 5, 1, 5);
    this.v6.setLayoutParams(layoutParams3);
    this.v7 = new LinearLayout(this);
    this.v7.setBackgroundColor(-1);
    this.v7.setPadding(2, 5, 1, 5);
    this.v7.setLayoutParams(layoutParams3);
    this.v8 = new LinearLayout(this);
    this.v8.setBackgroundColor(-1);
    this.v8.setPadding(2, 5, 1, 5);
    this.v8.setLayoutParams(layoutParams3);
    this.v9 = new LinearLayout(this);
    this.v9.setBackgroundColor(this.TAB_STROKE_COLOR);
    this.v9.setPadding(2, 5, 1, 5);
    this.v9.setLayoutParams(layoutParams3);
    this.v10 = new LinearLayout(this);
    this.v10.setBackgroundColor(-1);
    this.v10.setPadding(2, 5, 1, 5);
    this.v10.setLayoutParams(layoutParams3);
    this.l = new LinearLayout(this);
    this.l.setLayoutParams(new TableRow.LayoutParams(-2, -2));
    this.l.setOrientation(1);
    this.l.setPadding(3, 0, 3, 0);
    this.Scroll = new HorizontalScrollView(this);
    this.Scroll.setEnabled(true);
    this.Scroll.setHorizontalScrollBarEnabled(false);
    this.tl = new LinearLayout(this);
    this.tl.setOrientation(0);
    TableRow.LayoutParams layoutParams4 = new TableRow.LayoutParams(-1, -2);
    layoutParams4.setMargins(-2, 5, -2, 5);
    this.t1 = new TextView(this);
    this.t1.setLayoutParams(layoutParams4);
    this.t1.setText("Libray\nSelect");
    this.t1.setGravity(17);
    this.t1.setTextColor(this.TAB_TEXT_COLOR);
    this.t1.setTextSize(this.TAB_TEXT_SIZE);
    this.t1.setPadding(30, 15, 30, 15);
    this.t1.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    AddColor(this.t1, this.LST_MAB, 0, 3, this.TAB_STROKE_COLOR);
    this.t1.setTypeface(createFromAsset2, 1);
    this.t1.setOnClickListener(new AnonymousClass100000003(this));
    this.t2 = new TextView(this);
    this.t2.setLayoutParams(layoutParams4);
    this.t2.setText("Patch\nOffset");
    this.t2.setGravity(17);
    this.t2.setTextColor(this.TAB_TEXT_COLOR);
    this.t2.setTextSize(this.TAB_TEXT_SIZE);
    this.t2.setPadding(30, 15, 30, 15);
    this.t2.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    AddColor(this.t2, 0, 0, 0, this.TAB_STROKE_COLOR);
    this.t2.setTypeface(createFromAsset2, 1);
    this.t2.setOnClickListener(new AnonymousClass100000004(this));
    this.t8 = new TextView(this);
    this.t8.setLayoutParams(layoutParams4);
    this.t8.setText("Eazy\nPatch");
    this.t8.setGravity(17);
    this.t8.setTextColor(this.TAB_TEXT_COLOR);
    this.t8.setTextSize(this.TAB_TEXT_SIZE);
    this.t8.setPadding(30, 15, 30, 15);
    this.t8.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    AddColor(this.t8, 0, 0, 0, this.TAB_STROKE_COLOR);
    this.t8.setTypeface(createFromAsset2, 1);
    this.t8.setOnClickListener(new AnonymousClass100000005(this));
    this.t3 = new TextView(this);
    this.t3.setLayoutParams(layoutParams4);
    this.t3.setText("Hook\nOffset");
    this.t3.setGravity(17);
    this.t3.setTextColor(this.TAB_TEXT_COLOR);
    this.t3.setTextSize(this.TAB_TEXT_SIZE);
    this.t3.setPadding(30, 15, 30, 15);
    this.t3.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    AddColor(this.t3, 0, 0, 0, this.TAB_STROKE_COLOR);
    this.t3.setTypeface(createFromAsset2, 1);
    this.t3.setOnClickListener(new AnonymousClass100000006(this));
    this.t4 = new TextView(this);
    this.t4.setLayoutParams(layoutParams4);
    this.t4.setText("Feild\nOffset");
    this.t4.setGravity(17);
    this.t4.setTextColor(this.TAB_TEXT_COLOR);
    this.t4.setTextSize(this.TAB_TEXT_SIZE);
    this.t4.setPadding(30, 15, 30, 15);
    this.t4.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    AddColor(this.t4, 0, 0, 0, this.TAB_STROKE_COLOR);
    this.t4.setTypeface(createFromAsset2, 1);
    this.t4.setOnClickListener(new AnonymousClass100000007(this));
    this.t5 = new TextView(this);
    this.t5.setLayoutParams(layoutParams4);
    this.t5.setText("Hook\nVoid");
    this.t5.setGravity(17);
    this.t5.setTextColor(this.TAB_TEXT_COLOR);
    this.t5.setTextSize(this.TAB_TEXT_SIZE);
    this.t5.setPadding(30, 15, 30, 15);
    this.t5.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    AddColor(this.t5, 0, 0, 0, this.TAB_STROKE_COLOR);
    this.t5.setTypeface(createFromAsset2, 1);
    this.t5.setOnClickListener(new AnonymousClass100000008(this));
    this.t6 = new TextView(this);
    this.t6.setLayoutParams(layoutParams4);
    this.t6.setText("Disable\nVoid");
    this.t6.setGravity(17);
    this.t6.setTextColor(this.TAB_TEXT_COLOR);
    this.t6.setTextSize(this.TAB_TEXT_SIZE);
    this.t6.setPadding(30, 15, 30, 15);
    this.t6.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    AddColor(this.t6, 0, 0, 0, this.TAB_STROKE_COLOR);
    this.t6.setTypeface(createFromAsset2, 1);
    this.t6.setOnClickListener(new AnonymousClass100000009(this));
    this.t7 = new TextView(this);
    this.t7.setLayoutParams(layoutParams4);
    this.t7.setText("Call\nVoid");
    this.t7.setGravity(17);
    this.t7.setTextColor(this.TAB_TEXT_COLOR);
    this.t7.setTextSize(this.TAB_TEXT_SIZE);
    this.t7.setPadding(30, 15, 30, 15);
    this.t7.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    AddColor(this.t7, 0, 0, 0, this.TAB_STROKE_COLOR);
    this.t7.setTypeface(createFromAsset2, 1);
    this.t7.setOnClickListener(new AnonymousClass100000010(this));
    this.scrollView = new ScrollView(this);
    this.scrlLL = new LinearLayout.LayoutParams(-1, dp(this.MENU_HEIGHT));
    this.scrlLLExpanded = new LinearLayout.LayoutParams(this.mExpanded.getLayoutParams());
    this.scrlLLExpanded.weight = 1.0f;
    this.scrollView.setLayoutParams(Preferences.isExpanded ? this.scrlLLExpanded : this.scrlLL);
    this.scrollView.setBackgroundColor(this.MENU_FEATURE_BG_COLOR);
    this.patches = new LinearLayout(this);
    this.patches.setOrientation(1);
    this.patches.setPadding(3, 0, 3, 0);
    this.patches2 = new LinearLayout(this);
    this.patches2.setOrientation(1);
    this.patches2.setPadding(3, 0, 3, 0);
    featureList(getFeatureList2(), this.patches2);
    this.patches3 = new LinearLayout(this);
    this.patches3.setOrientation(1);
    this.patches3.setPadding(3, 0, 3, 0);
    featureList(getFeatureList3(), this.patches3);
    this.patches4 = new LinearLayout(this);
    this.patches4.setOrientation(1);
    this.patches4.setPadding(3, 0, 3, 0);
    featureList(getFeatureList4(), this.patches4);
    this.patches5 = new LinearLayout(this);
    this.patches5.setOrientation(1);
    this.patches5.setPadding(3, 0, 3, 0);
    featureList(getFeatureList5(), this.patches5);
    this.patches6 = new LinearLayout(this);
    this.patches6.setOrientation(1);
    this.patches6.setPadding(3, 0, 3, 0);
    featureList(getFeatureList6(), this.patches6);
    this.patches7 = new LinearLayout(this);
    this.patches7.setOrientation(1);
    this.patches7.setPadding(3, 0, 3, 0);
    featureList(getFeatureList7(), this.patches7);
    this.patches8 = new LinearLayout(this);
    this.patches8.setOrientation(1);
    this.patches8.setPadding(3, 0, 3, 0);
    featureList(getFeatureList8(), this.patches8);
    RelativeLayout relativeLayout2 = new RelativeLayout(this);
    relativeLayout2.setPadding(0, 0, 0, 0);
    relativeLayout2.setVerticalGravity(17);
    RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(-2, -2);
    layoutParams5.addRule(9);
    this.hideBtn = new Button(this);
    this.hideBtn.setLayoutParams(layoutParams5);
    this.hideBtn.setText("SETTINGS");
    GradientDrawable gradientDrawable2 = new GradientDrawable();
    gradientDrawable2.setColor(this.BTN_COLOR);
    gradientDrawable2.setStroke(3, -1);
    gradientDrawable2.setCornerRadii(new float[] { 0, 0, 30, 30, 0, 0, 0, 0 });
    this.hideBtn.setBackground(gradientDrawable2);
    this.hideBtn.setTextColor(this.TEXT_COLOR);
    this.hideBtn.setOnClickListener(new AnonymousClass100000011(this));
    RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams(-2, -2);
    layoutParams6.addRule(11);
    this.closeBtn = new Button(this);
    this.closeBtn.setLayoutParams(layoutParams6);
    GradientDrawable gradientDrawable3 = new GradientDrawable();
    gradientDrawable3.setColor(this.BTN_COLOR);
    gradientDrawable3.setStroke(3, -1);
    gradientDrawable3.setCornerRadii(new float[] { 30, 30, 0, 0, 0, 0, 0, 0 });
    this.closeBtn.setBackground(gradientDrawable3);
    this.closeBtn.setText("MINIMIZE");
    this.closeBtn.setTextColor(this.TEXT_COLOR);
    this.closeBtn.setOnClickListener(new AnonymousClass100000012(this));
    this.params = new WindowManager.LayoutParams(-2, -2, Build.VERSION.SDK_INT >= 26 ? 2038 : 2002,
        8, -3);
    this.params.gravity = 51;
    this.params.x = 0;
    this.params.y = 100;
    this.rootFrame.addView(this.mRootContainer);
    this.mRootContainer.addView(this.mCollapsed);
    this.mRootContainer.addView(this.mExpanded);
    this.mCollapsed.addView(this.startimage);
    relativeLayout.addView(rainbowActivity);
    relativeLayout.addView(textView);
    this.mExpanded.addView(this.subLayout);
    this.subLayout.addView(relativeLayout);
    this.subLayout.addView(rainbowActivity2);
    this.subLayout.addView(this.l);
    this.l.addView(this.h);
    this.l.addView(this.Scroll);
    this.Scroll.addView(this.tl);
    this.tl.addView(this.v1);
    this.tl.addView(this.t1);
    this.tl.addView(this.v2);
    this.tl.addView(this.t2);
    this.tl.addView(this.v3);
    this.tl.addView(this.t3);
    this.tl.addView(this.v4);
    this.tl.addView(this.t4);
    this.tl.addView(this.v5);
    this.tl.addView(this.t5);
    this.tl.addView(this.v6);
    this.tl.addView(this.t6);
    this.tl.addView(this.v7);
    this.tl.addView(this.t7);
    this.tl.addView(this.v9);
    this.tl.addView(this.t8);
    this.tl.addView(this.v8);
    this.l.addView(this.h2);
    this.H.addView(this.mSettings);
    this.mExpanded.addView(this.H);
    this.scrollView.addView(this.patches);
    this.mExpanded.addView(this.scrollView);
    relativeLayout2.addView(this.hideBtn);
    relativeLayout2.addView(this.closeBtn);
    this.mExpanded.addView(relativeLayout2);
    this.mWindowManager = (WindowManager) getSystemService("window");
    this.mWindowManager.addView(this.rootFrame, this.params);
    Handler handler = new Handler();
    handler.postDelayed(new AnonymousClass100000013(this, handler), 500);
  }

  public static class AnonymousClass100000013 implements Runnable {
    private final FloatingModMenuService this$0;
    final Handler val$handler;
    boolean viewLoaded = false;

    public AnonymousClass100000013(FloatingModMenuService floatingModMenuService, Handler handler) {
      this.this$0 = floatingModMenuService;
      this.val$handler = handler;
    }

    @Override // java.lang.Runnable
    public void run() {
      if (Preferences.loadPref && !this.this$0.isGameLibLoaded() && !this.this$0.stopChecking) {
        if (!this.viewLoaded) {
          this.this$0.featureList(this.this$0.getFeatureList2(), this.this$0.patches2);
          this.this$0.featureList(this.this$0.getFeatureList3(), this.this$0.patches3);
          this.this$0.featureList(this.this$0.getFeatureList4(), this.this$0.patches4);
          this.this$0.featureList(this.this$0.getFeatureList5(), this.this$0.patches5);
          this.this$0.featureList(this.this$0.getFeatureList6(), this.this$0.patches6);
          this.this$0.featureList(this.this$0.getFeatureList7(), this.this$0.patches7);
          this.this$0.featureList(this.this$0.getFeatureList8(), this.this$0.patches8);
          this.this$0.patches
              .addView(this.this$0.ColorPicker(-1, "Color Picker", 255, 255, 255, 255));
          this.this$0.patches.addView(this.this$0.Category(
              "Save preferences was been enabled. Waiting for game lib to be loaded...\n\nForce load menu may not apply mods instantly. You would need to reactivate them again"));
          this.this$0.patches.addView(this.this$0.Button(-100, "Force load menu"));
          this.viewLoaded = true;
        }
        this.val$handler.postDelayed(this, 600);
        return;
      }
      this.this$0.patches.removeAllViews();
      this.this$0.featureList(this.this$0.getFeatureList(), this.this$0.patches);
    }
  }

  public View.OnTouchListener onTouchListener() {
    return new AnonymousClass100000014(this);
  }

  public static class AnonymousClass100000014 implements View.OnTouchListener {
    final View collapsedView;
    final View expandedView;
    private float initialTouchX;
    private float initialTouchY;
    private int initialX;
    private int initialY;
    private final FloatingModMenuService this$0;

    public AnonymousClass100000014(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
      this.collapsedView = this.this$0.mCollapsed;
      this.expandedView = this.this$0.mExpanded;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
      switch (motionEvent.getAction()) {
        case 0:
          this.initialX = this.this$0.params.x;
          this.initialY = this.this$0.params.y;
          this.initialTouchX = motionEvent.getRawX();
          this.initialTouchY = motionEvent.getRawY();
          return true;
        case 1:
          int rawX = (int) (motionEvent.getRawX() - this.initialTouchX);
          int rawY = (int) (motionEvent.getRawY() - this.initialTouchY);
          this.this$0.mExpanded.setAlpha(1.0f);
          this.this$0.mCollapsed.setAlpha(1.0f);
          if (rawX < 10 && rawY < 10 && this.this$0.isViewCollapsed()) {
            try {
              this.collapsedView.setVisibility(8);
              this.expandedView.setVisibility(0);
            } catch (NullPointerException e) {
            }
          }
          return true;
        case 2:
          this.this$0.mExpanded.setAlpha(0.5f);
          this.this$0.mCollapsed.setAlpha(0.5f);
          this.this$0.params.x = this.initialX
              + ((int) (motionEvent.getRawX() - this.initialTouchX));
          this.this$0.params.y = this.initialY
              + ((int) (motionEvent.getRawY() - this.initialTouchY));
          this.this$0.mWindowManager.updateViewLayout(this.this$0.rootFrame, this.this$0.params);
          return true;
        default:
          return false;
      }
    }
  }

  public void featureList(String[] strArr, LinearLayout linearLayout) {
    int parseInt;
    int i = 0;
    for (int i2 = 0; i2 < strArr.length; i2++) {
      boolean z = false;
      String str = strArr[i2];
      if (str.contains("True_")) {
        z = true;
        str = str.replaceFirst("True_", "");
      }
      LinearLayout linearLayout2 = linearLayout;
      if (str.contains("CollapseAdd_")) {
        linearLayout2 = this.mCollapse;
        str = str.replaceFirst("CollapseAdd_", "");
      }
      String[] split = str.split("_");
      if (TextUtils.isDigitsOnly(split[0]) || split[0].matches("-[0-9]*")) {
        parseInt = Integer.parseInt(split[0]);
        str = str.replaceFirst(new StringBuffer().append(split[0]).append("_").toString(), "");
        i++;
      } else {
        parseInt = i2 - i;
      }
      String[] split2 = str.split("_");
      String str2 = split2[0];
      if (str2.equals("Toggle")) {
        linearLayout2.addView(Switch(parseInt, split2[1], z));
      } else if (str2.equals("SeekBar")) {
        linearLayout2.addView(
            SeekBar(parseInt, split2[1], Integer.parseInt(split2[2]), Integer.parseInt(split2[3])));
      } else if (str2.equals("Button")) {
        linearLayout2.addView(Button(parseInt, split2[1]));
      } else if (str2.equals("OffsetFind")) {
        linearLayout2.addView(OffsetMethod(parseInt, split2[1]));
      } else if (str2.equals("ButtonOnOff")) {
        linearLayout2.addView(ButtonOnOff(parseInt, split2[1], z));
      } else if (str2.equals("Spinner")) {
        linearLayout2.addView(RichTextView(split2[1]));
        linearLayout2.addView(Spinner(parseInt, split2[1], split2[2]));
      } else if (str2.equals("InputText")) {
        InputText(linearLayout2, parseInt, split2[1]);
      } else if (str2.equals("InputValue")) {
        if (split2.length == 3) {
          InputNum(linearLayout2, parseInt, split2[2], Integer.parseInt(split2[1]));
        }
        if (split2.length == 2) {
          InputNum(linearLayout2, parseInt, split2[1], 0);
        }
      } else if (str2.equals("CheckBox")) {
        linearLayout2.addView(CheckBox(parseInt, split2[1], z));
      } else if (str2.equals("RadioButton")) {
        linearLayout2.addView(RadioButton(parseInt, split2[1], split2[2]));
      } else if (str2.equals("Collapse")) {
        Collapse(linearLayout2, split2[1]);
        i++;
      } else if (str2.equals("ButtonLink")) {
        i++;
        linearLayout2.addView(ButtonLink(split2[1], split2[2]));
      } else if (str2.equals("Category")) {
        i++;
        linearLayout2.addView(Category(split2[1]));
      } else if (str2.equals("RichTextView")) {
        i++;
        linearLayout2.addView(RichTextView(split2[1]));
      } else if (str2.equals("RichWebView")) {
        i++;
        linearLayout2.addView(RichWebView(split2[1]));
      } else if (str2.equals("RichTimeDate")) {
        linearLayout2.addView(RichTimeDate(split2[1]));
      } else if (str2.equals("ColorPicker")) {
        linearLayout2.addView(ColorPicker(parseInt, split2[1], Integer.parseInt(split2[2]),
            Integer.parseInt(split2[3]), Integer.parseInt(split2[4]), Integer.parseInt(split2[5])));
      } else if (str2.equals("YoutubeLink")) {
        i++;
        YoutubeLink(linearLayout2, parseInt, split2[1]);
      } else if (str2.equals("TeleGroupLink")) {
        i++;
        TeleGroupLink(linearLayout2, parseInt, split2[1]);
      } else if (str2.equals("TeleChannelLink")) {
        i++;
        TeleChannelLink(linearLayout2, parseInt, split2[1]);
      } else if (str2.equals("HrzBttn")) {
        i++;
        HorizontalButton(linearLayout2, parseInt, split2[1]);
      }
    }
  }

  public static class AnonymousClass100000015 implements Runnable {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000015(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // java.lang.Runnable
    public void run() {
      this.this$0.textView
          .setText(
              new StringBuffer()
                  .append(
                      new StringBuffer()
                          .append(
                              new StringBuffer()
                                  .append(
                                      new StringBuffer().append("🗓 DATE : ")
                                          .append(new SimpleDateFormat("yyyy-MM-dd",
                                              Locale.getDefault()).format(new Date()))
                                          .toString())
                                  .append(" 🗓\n🕒 TIME : ").toString())
                          .append(new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                              .format(new Date()))
                          .toString())
                  .append(" 🕒").toString());
      this.this$0.handler.postDelayed(this, 1000);
    }
  }

  public View RichTimeDate(String str) {
    this.textView = new TextView(this);
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(0);
    gradientDrawable.setStroke(3, Color.parseColor("#FFFFFFFF"));
    gradientDrawable.setCornerRadii(new float[] { 77, 77, 89, 89, 80, 80, 76, 76 });
    this.textView.setGravity(17);
    this.textView.setTextSize(13);
    this.textView.setTextColor(-1);
    this.textView.setBackground(gradientDrawable);
    this.handler = new Handler();
    this.updateTimeRunnable = new AnonymousClass100000015(this);
    this.handler.post(this.updateTimeRunnable);
    return this.textView;
  }

  public static class AnonymousClass100000019 implements View.OnClickListener {
    private final FloatingModMenuService this$0;
    final TextView val$colorTextView;
    final View val$colorView;
    final String val$featName;
    final int[] val$initialProgress;

    public AnonymousClass100000019(FloatingModMenuService floatingModMenuService, String str,
        int[] iArr, View view, TextView textView) {
      this.this$0 = floatingModMenuService;
      this.val$featName = str;
      this.val$initialProgress = iArr;
      this.val$colorView = view;
      this.val$colorTextView = textView;
    }

    public static class AnonymousClass100000016 implements DialogInterface.OnCancelListener {
      private final AnonymousClass100000019 this$0;

      public AnonymousClass100000016(AnonymousClass100000019 anonymousClass100000019) {
        this.this$0 = anonymousClass100000019;
      }

      @Override // android.content.DialogInterface.OnCancelListener
      public void onCancel(DialogInterface dialogInterface) {
        FloatingModMenuService floatingModMenuService = this.this$0.this$0;
        this.this$0.this$0.getApplicationContext();
        ((InputMethodManager) floatingModMenuService.getSystemService(Context.INPUT_METHOD_SERVICE))
            .toggleSoftInput(1, 0);
      }
    }

    public static class AnonymousClass100000017 implements SeekBar.OnSeekBarChangeListener {
      private final AnonymousClass100000019 this$0;
      final TextView val$colorTextView2;
      final View val$colorview2;
      final SeekBar[] val$seekBarArr;
      final String[] val$strArr;
      final TextView val$textView3;
      final int val$xg;

      public AnonymousClass100000017(AnonymousClass100000019 anonymousClass100000019,
          SeekBar[] seekBarArr, View view, TextView textView, TextView textView2, String[] strArr,
          int i) {
        this.this$0 = anonymousClass100000019;
        this.val$seekBarArr = seekBarArr;
        this.val$colorview2 = view;
        this.val$colorTextView2 = textView;
        this.val$textView3 = textView2;
        this.val$strArr = strArr;
        this.val$xg = i;
      }

      @Override // android.widget.SeekBar.OnSeekBarChangeListener
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override // android.widget.SeekBar.OnSeekBarChangeListener
      public void onStopTrackingTouch(SeekBar seekBar) {
      }

      @Override // android.widget.SeekBar.OnSeekBarChangeListener
      public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int progress = this.val$seekBarArr[0].getProgress();
        int progress2 = this.val$seekBarArr[1].getProgress();
        int progress3 = this.val$seekBarArr[2].getProgress();
        int progress4 = this.val$seekBarArr[3].getProgress();
        this.val$colorview2
            .setBackgroundColor(Color.argb(progress, progress2, progress3, progress4));
        this.val$colorTextView2.setText(new StringBuffer()
            .append(new StringBuffer()
                .append(new StringBuffer()
                    .append(new StringBuffer().append("#")
                        .append(String.format("%02x", new Integer(progress))).toString())
                    .append(String.format("%02x", new Integer(progress2))).toString())
                .append(String.format("%02x", new Integer(progress3))).toString())
            .append(String.format("%02x", new Integer(progress4))).toString().toUpperCase());
        this.val$textView3.setText(new StringBuffer().append(new StringBuffer()
            .append(new StringBuffer().append(this.val$strArr[this.val$xg]).append(" (").toString())
            .append(i).toString()).append(") ").toString());
      }
    }

    public static class AnonymousClass100000018 implements View.OnClickListener {
      private final AnonymousClass100000019 this$0;
      final AlertDialog val$alert;
      final TextView val$colorTextView;
      final View val$colorView;
      final int[] val$initialProgress;
      final SeekBar[] val$seekBarArr;

      public AnonymousClass100000018(AnonymousClass100000019 anonymousClass100000019,
          SeekBar[] seekBarArr, int[] iArr, View view, TextView textView, AlertDialog alertDialog) {
        this.this$0 = anonymousClass100000019;
        this.val$seekBarArr = seekBarArr;
        this.val$initialProgress = iArr;
        this.val$colorView = view;
        this.val$colorTextView = textView;
        this.val$alert = alertDialog;
      }

      @Override // android.view.View.OnClickListener
      public void onClick(View view) {
        int progress = this.val$seekBarArr[0].getProgress();
        int progress2 = this.val$seekBarArr[1].getProgress();
        int progress3 = this.val$seekBarArr[2].getProgress();
        int progress4 = this.val$seekBarArr[3].getProgress();
        for (int i = 0; i < 4; i++) {
          this.val$initialProgress[i] = this.val$seekBarArr[i].getProgress();
        }
        int argb = Color.argb(progress, progress2, progress3, progress4);
        this.val$colorView.setBackgroundColor(argb);
        this.val$colorTextView.setText(new StringBuffer()
            .append(new StringBuffer()
                .append(new StringBuffer()
                    .append(new StringBuffer().append("#")
                        .append(String.format("%02x", new Integer(progress))).toString())
                    .append(String.format("%02x", new Integer(progress2))).toString())
                .append(String.format("%02x", new Integer(progress3))).toString())
            .append(String.format("%02x", new Integer(progress4))).toString().toUpperCase());
        this.val$alert.dismiss();
        this.this$0.this$0.mExpanded.setBackgroundColor(argb);
        this.this$0.this$0.closeBtn.setBackgroundColor(argb);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(3, -1);
        gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 0, 0, 0, 0 });
        this.this$0.this$0.closeBtn.setBackground(gradientDrawable);
        this.this$0.this$0.hideBtn.setBackgroundColor(argb);
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setStroke(3, -1);
        gradientDrawable2.setCornerRadii(new float[] { 0, 0, 30, 30, 0, 0, 0, 0 });
        this.this$0.this$0.hideBtn.setBackground(gradientDrawable2);
      }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      AlertDialog create = new AlertDialog.Builder(this.this$0.getApplicationContext(), 2).create();
      if (Build.VERSION.SDK_INT >= 19) {
        ((Window) Objects.requireNonNull(create.getWindow()))
            .setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
      }
      create.setOnCancelListener(new AnonymousClass100000016(this));
      LinearLayout linearLayout = new LinearLayout(this.this$0.getApplicationContext());
      linearLayout.setPadding(5, 5, 5, 5);
      linearLayout.setOrientation(1);
      GradientDrawable gradientDrawable = new GradientDrawable();
      gradientDrawable.setColor(-16777216);
      gradientDrawable.setStroke(3, -1);
      linearLayout.setBackground(gradientDrawable);
      TextView textView = new TextView(this.this$0.getApplicationContext());
      textView.setText(Html.fromHtml(new StringBuffer()
          .append(new StringBuffer().append("<u>").append(this.val$featName).toString())
          .append("</u>").toString()));
      textView.setGravity(17);
      textView.setTextColor(-1);
      textView.setTextSize(22.0f);
      textView.setTypeface(Typeface.DEFAULT_BOLD);
      LinearLayout linearLayout2 = new LinearLayout(this.this$0.getApplicationContext());
      linearLayout2.setLayoutParams(new TableRow.LayoutParams(-1, -2));
      linearLayout2.setGravity(16);
      linearLayout2.setPadding(15, 10, 15, 10);
      linearLayout2.setOrientation(0);
      View view2 = new View(this.this$0.getApplicationContext());
      view2.setLayoutParams(new TableRow.LayoutParams(this.this$0.dp(20), this.this$0.dp(20)));
      view2.setBackgroundColor(Color.argb(this.val$initialProgress[0], this.val$initialProgress[1],
          this.val$initialProgress[2], this.val$initialProgress[3]));
      TextView textView2 = new TextView(this.this$0.getApplicationContext());
      textView2
          .setText(
              new StringBuffer()
                  .append(
                      new StringBuffer()
                          .append(new StringBuffer()
                              .append(new StringBuffer().append("#")
                                  .append(String.format("%02x",
                                      new Integer(this.val$initialProgress[0])))
                                  .toString())
                              .append(
                                  String.format("%02x", new Integer(this.val$initialProgress[1])))
                              .toString())
                          .append(String.format("%02x", new Integer(this.val$initialProgress[2])))
                          .toString())
                  .append(String.format("%02x", new Integer(this.val$initialProgress[3])))
                  .toString().toUpperCase());
      textView2.setTextColor(-1);
      textView2.setPadding(5, 0, 0, 0);
      textView2.setTextSize(20.0f);
      LinearLayout linearLayout3 = new LinearLayout(this.this$0.getApplicationContext());
      linearLayout3.setLayoutParams(new TableRow.LayoutParams(-1, -2));
      linearLayout3.setPadding(10, 0, 0, 0);
      linearLayout3.setOrientation(1);
      String[] strArr = { "Transparency", "Red", "Green", "Blue" };
      SeekBar[] seekBarArr = new SeekBar[4];
      int[] iArr = { -1, -65536, -16711936, -16776961 };
      for (int i = 0; i < 4; i++) {
        LinearLayout linearLayout4 = new LinearLayout(this.this$0.getApplicationContext());
        linearLayout4.setOrientation(1);
        linearLayout4.setGravity(17);
        TextView textView3 = new TextView(this.this$0.getApplicationContext());
        textView3.setText(new StringBuffer().append(
            new StringBuffer().append(new StringBuffer().append(strArr[i]).append(" (").toString())
                .append(this.val$initialProgress[i]).toString())
            .append(") ").toString());
        textView3.setTextColor(iArr[i]);
        textView3.setGravity(8388611);
        textView3.setPadding(10, 0, 0, 0);
        SeekBar seekBar = new SeekBar(this.this$0.getApplicationContext());
        seekBar.setLayoutParams(new TableRow.LayoutParams(-1, -2));
        seekBar.setMax(255);
        seekBar.setProgress(this.val$initialProgress[i]);
        seekBar.getProgressDrawable()
            .setColorFilter(new PorterDuffColorFilter(iArr[i], PorterDuff.Mode.MULTIPLY));
        seekBar.getThumb().setColorFilter(iArr[i], PorterDuff.Mode.SRC_IN);
        seekBar.setOnSeekBarChangeListener(
            new AnonymousClass100000017(this, seekBarArr, view2, textView2, textView3, strArr, i));
        linearLayout4.addView(textView3);
        linearLayout4.addView(seekBar);
        linearLayout3.addView(linearLayout4);
        seekBarArr[i] = seekBar;
      }
      TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(-1, -2);
      layoutParams.setMargins(20, 10, 20, 15);
      Button button = new Button(this.this$0.getApplicationContext());
      button.setLayoutParams(layoutParams);
      GradientDrawable gradientDrawable2 = new GradientDrawable();
      gradientDrawable2.setColor(Color.argb(40, 0, 0, 0));
      gradientDrawable2.setCornerRadii(new float[] { 60, 60, 0, 0, 60, 60, 0, 0 });
      gradientDrawable2.setStroke(3, -1, 0, 0);
      button.setBackground(gradientDrawable2);
      button.setTextColor(-1);
      button.setPadding(15, 10, 15, 10);
      button.setText("SET VALUE");
      button.setOnClickListener(new AnonymousClass100000018(this, seekBarArr,
          this.val$initialProgress, this.val$colorView, this.val$colorTextView, create));
      linearLayout.addView(textView);
      linearLayout.addView(linearLayout2);
      linearLayout2.addView(view2);
      linearLayout2.addView(textView2);
      linearLayout.addView(linearLayout3);
      linearLayout.addView(button);
      create.setView(linearLayout);
      create.show();
    }
  }

  public View ColorPicker(int i, String str, int i2, int i3, int i4, int i5) {
    LinearLayout linearLayout = new LinearLayout(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(20, 10, 20, 10);
    linearLayout.setOrientation(0);
    linearLayout.setGravity(16);
    linearLayout.setPadding(10, 8, 8, 8);
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(Color.argb(0, 0, 0, 0));
    gradientDrawable.setCornerRadii(new float[] { 60, 60, 0, 0, 60, 60, 0, 0 });
    gradientDrawable.setStroke(4, -1, 5, 5);
    linearLayout.setBackground(gradientDrawable);
    linearLayout.setLayoutParams(layoutParams);
    TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams) layoutParams2).weight = 1.0f;
    LinearLayout linearLayout2 = new LinearLayout(this);
    linearLayout2.setLayoutParams(layoutParams2);
    linearLayout2.setGravity(8388611);
    LinearLayout linearLayout3 = new LinearLayout(this);
    linearLayout3.setOrientation(1);
    TextView textView = new TextView(this);
    textView.setTextColor(-1);
    textView.setText(str);
    textView.setTextSize(14.0f);
    textView.setGravity(3);
    textView.setPadding(20, 0, 10, 0);
    LinearLayout linearLayout4 = new LinearLayout(this);
    linearLayout4.setOrientation(0);
    linearLayout4.setPadding(20, 5, 0, 5);
    linearLayout4.setGravity(16);
    View view = new View(this);
    view.setLayoutParams(new TableRow.LayoutParams(dp(20), dp(20)));
    view.setBackgroundColor(Color.argb(i2, i3, i4, i5));
    TextView textView2 = new TextView(this);
    textView2.setText(new StringBuffer()
        .append(new StringBuffer()
            .append(new StringBuffer()
                .append(new StringBuffer().append("#")
                    .append(String.format("%02x", new Integer(i2))).toString())
                .append(String.format("%02x", new Integer(i3))).toString())
            .append(String.format("%02x", new Integer(i4))).toString())
        .append(String.format("%02x", new Integer(i5))).toString().toUpperCase());
    textView2.setTextColor(-1);
    textView2.setTextSize(14.0f);
    textView2.setGravity(3);
    textView2.setPadding(5, 0, 0, 0);
    Button button = new Button(this);
    button.setText("Pick");
    GradientDrawable gradientDrawable2 = new GradientDrawable();
    gradientDrawable2.setColor(-16777216);
    gradientDrawable2.setCornerRadii(new float[] { 60, 60, 0, 0, 60, 60, 0, 0 });
    gradientDrawable2.setStroke(3, -1, 0, 0);
    button.setBackground(gradientDrawable2);
    button.setTextColor(-1);
    button.setOnClickListener(
        new AnonymousClass100000019(this, str, new int[] { i2, i3, i4, i5 }, view, textView2));
    linearLayout.addView(linearLayout2);
    linearLayout2.addView(linearLayout3);
    linearLayout3.addView(textView);
    linearLayout3.addView(linearLayout4);
    linearLayout4.addView(view);
    linearLayout4.addView(textView2);
    linearLayout.addView(button);
    return linearLayout;
  }

  public static class AnonymousClass100000020 implements CompoundButton.OnCheckedChangeListener {
    private final FloatingModMenuService this$0;
    final GradientDrawable val$GD_THUMB;
    final GradientDrawable val$GD_TRACK;
    final String val$featName;
    final int val$featNum;
    final Switch val$switchR;

    public AnonymousClass100000020(FloatingModMenuService floatingModMenuService, String str, int i,
        GradientDrawable gradientDrawable, GradientDrawable gradientDrawable2, Switch r16) {
      this.this$0 = floatingModMenuService;
      this.val$featName = str;
      this.val$featNum = i;
      this.val$GD_TRACK = gradientDrawable;
      this.val$GD_THUMB = gradientDrawable2;
      this.val$switchR = r16;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
      Preferences.changeFeatureBool(this.val$featName, this.val$featNum, z);
      if (z) {
        this.val$GD_TRACK.setStroke(2, -1);
        this.val$GD_TRACK.setColor(this.this$0.LST_MAB);
        this.val$GD_THUMB.setStroke(2, -1);
        this.val$GD_THUMB.setColor(-16776961);
      } else {
        this.val$GD_TRACK.setStroke(2, -16776961);
        this.val$GD_TRACK.setColor(Color.argb(200, 255, 255, 255));
        this.val$GD_THUMB.setStroke(2, -16776961);
        this.val$GD_THUMB.setColor(-1);
      }
      switch (this.val$featNum) {
        case -7:
          if (z) {
            this.this$0.mExpanded.setBackground(null);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.parseColor("#AB000000"));
            gradientDrawable.setStroke(4, -1);
            this.this$0.mExpanded.setBackground(gradientDrawable);
            return;
          }
          this.this$0.mExpanded.setBackground(this.this$0.blackgamer);
          return;
        case -6:
          if (z) {
            this.this$0.mExpanded.setBackground(null);
            GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setColor(this.this$0.LST_MAB);
            gradientDrawable2.setStroke(4, -1);
            this.this$0.mExpanded.setBackground(gradientDrawable2);
            return;
          }
          this.this$0.mExpanded.setBackground(this.this$0.blackgamer);
          return;
        case -5:
        case -4:
        case -2:
        default:
          return;
        case -3:
          Preferences.isExpanded = z;
          this.this$0.scrollView
              .setLayoutParams(z ? this.this$0.scrlLLExpanded : this.this$0.scrlLL);
          return;
        case -1:
          Preferences.with(this.val$switchR.getContext()).writeBoolean(-1, z);
          if (!z) {
            Preferences.with(this.val$switchR.getContext()).clear();
            return;
          }
          return;
      }
    }
  }

  public View Switch(int i, String str, boolean z) {
    Switch r10 = new Switch(this);
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setSize(20, 20);
    gradientDrawable.setCornerRadius(100);
    GradientDrawable gradientDrawable2 = new GradientDrawable();
    gradientDrawable2.setSize(40, 40);
    gradientDrawable2.setShape(1);
    if (Preferences.loadPrefBool(str, i, z)) {
      gradientDrawable.setStroke(2, -1);
      gradientDrawable.setColor(this.LST_MAB);
      gradientDrawable2.setStroke(2, -1);
      gradientDrawable2.setColor(-16776961);
    } else {
      gradientDrawable.setStroke(2, -16776961);
      gradientDrawable.setColor(Color.argb(200, 255, 255, 255));
      gradientDrawable2.setStroke(2, -16776961);
      gradientDrawable2.setColor(-1);
    }
    r10.setText(str);
    r10.setTextColor(this.TEXT_COLOR_2);
    r10.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    r10.setPadding(10, 5, 10, 5);
    r10.setThumbDrawable(gradientDrawable2);
    r10.setTrackDrawable(gradientDrawable);
    r10.setChecked(Preferences.loadPrefBool(str, i, z));
    r10.setOnCheckedChangeListener(
        new AnonymousClass100000020(this, str, i, gradientDrawable, gradientDrawable2, r10));
    return r10;
  }

  public static class AnonymousClass100000021 implements SeekBar.OnSeekBarChangeListener {
    private final FloatingModMenuService this$0;
    final String val$featName;
    final int val$featNum;
    final int val$min;
    final TextView val$textView;

    public AnonymousClass100000021(FloatingModMenuService floatingModMenuService, int i, String str,
        int i2, TextView textView) {
      this.this$0 = floatingModMenuService;
      this.val$min = i;
      this.val$featName = str;
      this.val$featNum = i2;
      this.val$textView = textView;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
      if (i > this.val$min) {
        seekBar.setProgress(i < this.val$min ? this.val$min : i);
        Preferences.changeFeatureInt(this.val$featName, this.val$featNum,
            i < this.val$min ? this.val$min : i);
        this.val$textView
            .setText(
                Html.fromHtml(
                    new StringBuffer()
                        .append(
                            new StringBuffer()
                                .append(new StringBuffer()
                                    .append(new StringBuffer().append(this.val$featName)
                                        .append(" -> <font color='").toString())
                                    .append(this.this$0.NumberTxtColor).toString())
                                .append("'>").toString())
                        .append(i < this.val$min ? this.val$min : i).toString()));
      } else {
        seekBar.setProgress(i < this.val$min ? this.val$min : i);
        Preferences.changeFeatureInt(this.val$featName, this.val$featNum,
            i < this.val$min ? this.val$min : i);
        this.val$textView
            .setText(
                Html.fromHtml(
                    new StringBuffer()
                        .append(
                            new StringBuffer()
                                .append(new StringBuffer()
                                    .append(new StringBuffer().append(this.val$featName)
                                        .append(" -> <font color='").toString())
                                    .append(this.this$0.NumberTxtColor).toString())
                                .append("'>").toString())
                        .append(" [DEFAULT]").toString()));
      }
      switch (this.val$featNum) {
        case -10:
          this.val$textView.setText(Html.fromHtml(new StringBuffer().append(new StringBuffer()
              .append(new StringBuffer().append(this.val$featName).append(" -> ").toString())
              .append(i + 0).toString()).append("X").toString()));
          if (i >= 1) {
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360, 1, 0.5f, 1, 0.5f);
            rotateAnimation.setDuration(360000 / (i > 10 ? (i - 10) * 1000 : i * 100));
            rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            rotateAnimation.setRepeatCount(-1);
            this.this$0.startimage.startAnimation(rotateAnimation);
          } else {
            this.this$0.startimage.clearAnimation();
          }
          switch (i) {
            case 0:
              this.val$textView
                  .setText(Html.fromHtml(new StringBuffer()
                      .append(
                          new StringBuffer()
                              .append(new StringBuffer()
                                  .append(new StringBuffer().append(this.val$featName)
                                      .append(" -> <font color='").toString())
                                  .append(this.this$0.NumberTxtColor).toString())
                              .append("'>").toString())
                      .append(" [DEFAULT]").toString()));
              return;
            case 20:
              this.val$textView
                  .setText(Html.fromHtml(new StringBuffer()
                      .append(
                          new StringBuffer()
                              .append(new StringBuffer()
                                  .append(new StringBuffer().append(this.val$featName)
                                      .append("-> <font color='").toString())
                                  .append(this.this$0.NumberTxtColor).toString())
                              .append("'>").toString())
                      .append("[INFINITY]").toString()));
              return;
            default:
              return;
          }
        case -9:
          this.val$textView.setText(Html.fromHtml(new StringBuffer().append(new StringBuffer()
              .append(new StringBuffer().append(this.val$featName).append(" -> ").toString())
              .append(i + 0).toString()).append("X").toString()));
          this.this$0.startimage.getLayoutParams().height = (i + 13) * 5;
          this.this$0.startimage.getLayoutParams().width = (i + 13) * 5;
          if (i == 13) {
            this.val$textView
                .setText(
                    Html.fromHtml(new StringBuffer()
                        .append(
                            new StringBuffer()
                                .append(new StringBuffer()
                                    .append(new StringBuffer().append(this.val$featName)
                                        .append("-> <font color='").toString())
                                    .append(this.this$0.NumberTxtColor).toString())
                                .append("'>").toString())
                        .append("[DEFAULT]").toString()));
          }
          switch (i) {
            case 1:
              this.val$textView
                  .setText(Html.fromHtml(new StringBuffer()
                      .append(
                          new StringBuffer()
                              .append(new StringBuffer()
                                  .append(new StringBuffer().append(this.val$featName)
                                      .append(" -> <font color='").toString())
                                  .append(this.this$0.NumberTxtColor).toString())
                              .append("'>").toString())
                      .append(" [DEFAULT]").toString()));
              return;
            case 99:
              this.val$textView
                  .setText(Html.fromHtml(new StringBuffer()
                      .append(
                          new StringBuffer()
                              .append(new StringBuffer()
                                  .append(new StringBuffer().append(this.val$featName)
                                      .append("-> <font color='").toString())
                                  .append(this.this$0.NumberTxtColor).toString())
                              .append("'>").toString())
                      .append("[INFINITY]").toString()));
              return;
            default:
              return;
          }
        case -8:
          this.val$textView.setText(Html.fromHtml(new StringBuffer().append(new StringBuffer()
              .append(new StringBuffer().append(this.val$featName).append(" -> ").toString())
              .append(i + 0).toString()).append("X").toString()));
          this.this$0.ICON_ALPHA = (i + 1) / 10.0f;
          if (i == 10) {
            this.val$textView
                .setText(
                    Html.fromHtml(new StringBuffer()
                        .append(
                            new StringBuffer()
                                .append(new StringBuffer()
                                    .append(new StringBuffer().append(this.val$featName)
                                        .append("-> <font color='").toString())
                                    .append(this.this$0.NumberTxtColor).toString())
                                .append("'>").toString())
                        .append("[DEFAULT]").toString()));
          }
          if (i == this.val$min) {
            this.this$0.ICON_ALPHA = 1.0f;
          }
          switch (i) {
            case 1:
              this.val$textView
                  .setText(Html.fromHtml(new StringBuffer()
                      .append(
                          new StringBuffer()
                              .append(new StringBuffer()
                                  .append(new StringBuffer().append(this.val$featName)
                                      .append(" -> <font color='").toString())
                                  .append(this.this$0.NumberTxtColor).toString())
                              .append("'>").toString())
                      .append(" [DEFAULT]").toString()));
              return;
            case 9:
              this.val$textView
                  .setText(Html.fromHtml(new StringBuffer()
                      .append(
                          new StringBuffer()
                              .append(new StringBuffer()
                                  .append(new StringBuffer().append(this.val$featName)
                                      .append("-> <font color='").toString())
                                  .append(this.this$0.NumberTxtColor).toString())
                              .append("'>").toString())
                      .append("[INFINITY]").toString()));
              return;
            default:
              return;
          }
        default:
          return;
      }
    }
  }

  public View SeekBar(int i, String str, int i2, int i3) {
    int loadPrefInt = Preferences.loadPrefInt(str, i);
    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setPadding(10, 5, 0, 5);
    linearLayout.setOrientation(1);
    linearLayout.setGravity(17);
    TextView textView = new TextView(this);
    if (Preferences.loadPref) {
      textView
          .setText(
              Html.fromHtml(
                  new StringBuffer()
                      .append(
                          new StringBuffer()
                              .append(
                                  new StringBuffer()
                                      .append(
                                          new StringBuffer()
                                              .append(new StringBuffer()
                                                  .append(new StringBuffer().append(str)
                                                      .append(" -> ").toString())
                                                  .append("<font color='").toString())
                                              .append(-1).toString())
                                      .append("'>").toString())
                              .append(loadPrefInt).toString())
                      .append(" (SAVED VALUE)").toString()));
    } else {
      textView.setText(Html.fromHtml(new StringBuffer().append(new StringBuffer()
          .append(new StringBuffer().append(
              new StringBuffer().append(new StringBuffer().append(str).append(" -> ").toString())
                  .append("<font color='").toString())
              .append(-1).toString())
          .append("'>").toString()).append(" [DEFAULT]").toString()));
    }
    textView.setTextColor(this.TEXT_COLOR_2);
    textView.setShadowLayer(7.0f, 0.0f, 0.0f, -16777216);
    textView.setTextSize(13.0f);
    SeekBar seekBar = new SeekBar(this);
    seekBar.setPadding(25, 10, 35, 10);
    seekBar.setMax(i3);
    if (Build.VERSION.SDK_INT >= 26) {
      seekBar.setMin(i2);
    }
    seekBar.setProgress(loadPrefInt == 0 ? i2 : loadPrefInt);
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setShape(1);
    gradientDrawable.setColor(this.SeekBarColor);
    gradientDrawable.setStroke(dp(2), -1);
    gradientDrawable.setCornerRadius(15.0f);
    gradientDrawable.setSize(dp(18), dp(18));
    seekBar.setThumb(gradientDrawable);
    seekBar.getProgressDrawable().setColorFilter(this.SeekBarProgressColor,
        PorterDuff.Mode.SRC_ATOP);
    seekBar.setOnSeekBarChangeListener(new AnonymousClass100000021(this, i2, str, i, textView));
    linearLayout.addView(textView);
    linearLayout.addView(seekBar);
    return linearLayout;
  }

  public static class AnonymousClass100000022 implements View.OnClickListener {
    private final FloatingModMenuService this$0;
    final String val$featName;
    final int val$featNum;

    public AnonymousClass100000022(FloatingModMenuService floatingModMenuService, int i,
        String str) {
      this.this$0 = floatingModMenuService;
      this.val$featNum = i;
      this.val$featName = str;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      switch (this.val$featNum) {
        case -561:
          Logcat.Clear(this.this$0.getApplicationContext());
          break;
        case -452:
          Logcat.Save(this.this$0.getApplicationContext());
          break;
      }
      Preferences.changeFeatureInt(this.val$featName, this.val$featNum, 0);
    }
  }

  public void HorizontalButton(LinearLayout linearLayout, int i, String str) {
    LinearLayout linearLayout2 = new LinearLayout(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(5, 7, 5, 7);
    linearLayout2.setOrientation(0);
    linearLayout2.setLayoutParams(layoutParams);
    Button button = new Button(this);
    AddColor(button, Color.parseColor("#AFFF3434"), 3, 0, 0, this.LST_MAB, 30, 30, 0, 0, 30, 30, 0,
        0);
    button.setTextColor(this.TEXT_COLOR_2);
    button.setText(Html.fromHtml(str));
    button.setOnClickListener(new AnonymousClass100000022(this, i, str));
    linearLayout2.addView(button);
    linearLayout.addView(linearLayout2);
  }

  public static class AnonymousClass100000023 implements View.OnClickListener {
    private final FloatingModMenuService this$0;
    final String val$featName;
    final int val$featNum;

    public AnonymousClass100000023(FloatingModMenuService floatingModMenuService, int i,
        String str) {
      this.this$0 = floatingModMenuService;
      this.val$featNum = i;
      this.val$featName = str;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      switch (this.val$featNum) {
        case -100:
          this.this$0.stopChecking = true;
          break;
        case -12:
          this.this$0.stopSelf();
          break;
        case -11:
          this.this$0.mCollapsed.setVisibility(0);
          this.this$0.mCollapsed.setAlpha(0);
          this.this$0.mExpanded.setVisibility(8);
          break;
        case -5:
          Logcat.Clear(this.this$0.getApplicationContext());
          break;
        case -4:
          Logcat.Save(this.this$0.getApplicationContext());
          break;
      }
      Preferences.changeFeatureInt(this.val$featName, this.val$featNum, 0);
    }
  }

  public View Button(int i, String str) {
    Button button = new Button(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(7, 5, 7, 5);
    button.setLayoutParams(layoutParams);
    button.setTextColor(this.TEXT_COLOR_2);
    button.setAllCaps(false);
    button.setText(Html.fromHtml(str));
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(this.BTN_COLOR);
    gradientDrawable.setStroke(3, -1);
    gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    button.setBackground(gradientDrawable);
    button.setOnClickListener(new AnonymousClass100000023(this, i, str));
    return button;
  }

  public static class AnonymousClass100000024 implements View.OnClickListener {
    private final FloatingModMenuService this$0;
    final String val$url;

    public AnonymousClass100000024(FloatingModMenuService floatingModMenuService, String str) {
      this.this$0 = floatingModMenuService;
      this.val$url = str;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      Intent intent = new Intent("android.intent.action.VIEW");
      intent.setFlags(268435456);
      intent.setData(Uri.parse(this.val$url));
      this.this$0.startActivity(intent);
    }
  }

  public View ButtonLink(String str, String str2) {
    Button button = new Button(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(7, 5, 7, 5);
    button.setLayoutParams(layoutParams);
    button.setAllCaps(false);
    button.setTextColor(this.TEXT_COLOR_2);
    button.setText(Html.fromHtml(str));
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(this.BTN_COLOR);
    gradientDrawable.setStroke(3, -1);
    gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    button.setBackground(gradientDrawable);
    button.setOnClickListener(new AnonymousClass100000024(this, str2));
    return button;
  }

  public static class AnonymousClass100000025 implements DialogInterface.OnCancelListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000025(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
      FloatingModMenuService floatingModMenuService = this.this$0;
      this.this$0.getApplicationContext();
      ((InputMethodManager) floatingModMenuService.getSystemService(Context.INPUT_METHOD_SERVICE))
          .toggleSoftInput(1, 0);
    }
  }

  public static class AnonymousClass100000026 implements View.OnClickListener {
    private final FloatingModMenuService this$0;
    final AlertDialog val$alert;

    public AnonymousClass100000026(FloatingModMenuService floatingModMenuService,
        AlertDialog alertDialog) {
      this.this$0 = floatingModMenuService;
      this.val$alert = alertDialog;
    }

    class AnonymousClass100000025 implements DialogInterface.OnClickListener {
      private final AnonymousClass100000026 this$0;

      AnonymousClass100000025(AnonymousClass100000026 anonymousClass100000026) {
        this.this$0 = anonymousClass100000026;
      }

      @Override // android.content.DialogInterface.OnClickListener
      public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();

      }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.val$alert.show();

    }
  }

  public View OffsetMethod(int i, String str) {
    Button button = new Button(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(7, 5, 7, 5);
    button.setLayoutParams(layoutParams);
    button.setTextColor(this.TEXT_COLOR_2);
    button.setAllCaps(false);
    button.setText(Html.fromHtml(str));
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(this.BTN_COLOR);
    gradientDrawable.setStroke(3, -1);
    gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    button.setBackground(gradientDrawable);
    AlertDialog create = new AlertDialog.Builder(this).create();
    if (Build.VERSION.SDK_INT >= 19) {
      ((Window) Objects.requireNonNull(create.getWindow()))
          .setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
    }
    create.setOnCancelListener(new AnonymousClass100000025(this));
    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setPadding(5, 5, 5, 5);
    linearLayout.setOrientation(1);
    byte[] decode = Base64.decode(Sonic(), 0);
    linearLayout
        .setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(decode, 0, decode.length)));
    TextView textView = new TextView(this);
    textView.setText(Html
        .fromHtml(new StringBuffer().append(new StringBuffer().append("<u>").append(str).toString())
            .append("</u>").toString()));
    textView.setGravity(17);
    textView.setTypeface(Typeface.DEFAULT_BOLD);
    textView.setTextColor(this.TEXT_COLOR);
    textView.setTextSize(22.0f);
    TextView textView2 = new TextView(this);
    textView2.setGravity(17);
    textView2.setTextSize(14.0f);
    textView2.setText("Click \"Set Value\" button to apply changes || outside to cancel");
    textView2.setPadding(10, 5, 10, 5);
    textView2.setTextColor(this.TEXT_COLOR);
    LinearLayout linearLayout2 = new LinearLayout(this);
    linearLayout2.setOrientation(1);
    for (Map.Entry<String, String> entry : searchMethods(
        new String[] { "get_transform", "get_position_Injected", "WorldToScreenPoint_Injected",
            "get_main", "SetResolution", "set_position_Injected" })
        .entrySet()) {
      TextView textView3 = new TextView(this);
      textView3.setText(new StringBuffer()
          .append(new StringBuffer().append(entry.getKey()).append(": ").toString())
          .append(entry.getValue()).toString());
      textView3.setTextColor(this.TEXT_COLOR);
      linearLayout2.addView(textView3);
    }
    linearLayout.addView(textView);
    linearLayout.addView(textView2);
    linearLayout.addView(linearLayout2);
    create.setView(linearLayout);
    button.setOnClickListener(new AnonymousClass100000026(this, create));
    WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
    layoutParams2.copyFrom(create.getWindow().getAttributes());
    ((ViewGroup.LayoutParams) layoutParams2).height = 1000;
    create.getWindow().setAttributes(layoutParams2);
    return button;
  }

  public static Map<String, String> searchMethods(String[] strArr) {
    HashMap hashMap = new HashMap();
    try {
      FileInputStream fileInputStream = new FileInputStream("/storage/emulated/0/MT2/libil2cpp.so");
      DataInputStream dataInputStream = new DataInputStream(fileInputStream);
      byte[] bArr = new byte[1024];
      while (true) {
        int read = dataInputStream.read(bArr);
        if (read == -1) {
          break;
        }
        String str = new String(bArr, 0, read);
        for (String str2 : strArr) {
          int indexOf = str.indexOf(str2);
          if (indexOf != -1) {
            hashMap.put(str2, str.substring(indexOf + str2.length(), indexOf + str2.length() + 8));
          }
        }
      }
      dataInputStream.close();
      fileInputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (hashMap.isEmpty()) {
      hashMap.put("Method and offset not found", "0x0");
    }
    return hashMap;
  }

  public static class AnonymousClass100000033 implements View.OnClickListener {
    private final FloatingModMenuService this$0;
    final EditTextNum val$edittextnum;
    final String val$featName;
    final int val$featNum;
    final int val$maxValue;
    final TextView val$textView2;

    AnonymousClass100000033(FloatingModMenuService floatingModMenuService, String str, int i,
        EditTextNum editTextNum, TextView textView, int i2) {
      this.this$0 = floatingModMenuService;
      this.val$featName = str;
      this.val$maxValue = i;
      this.val$edittextnum = editTextNum;
      this.val$textView2 = textView;
      this.val$featNum = i2;
    }

    public static class AnonymousClass100000029 implements DialogInterface.OnCancelListener {
      private final AnonymousClass100000033 this$0;

      public AnonymousClass100000029(AnonymousClass100000033 anonymousClass100000033) {
        this.this$0 = anonymousClass100000033;
      }

      @Override // android.content.DialogInterface.OnCancelListener
      public void onCancel(DialogInterface dialogInterface) {
        FloatingModMenuService floatingModMenuService = this.this$0.this$0;
        this.this$0.this$0.getApplicationContext();
        ((InputMethodManager) floatingModMenuService.getSystemService(Context.INPUT_METHOD_SERVICE))
            .toggleSoftInput(1, 0);
      }
    }

    public static class AnonymousClass100000030 implements View.OnTouchListener {
      private final AnonymousClass100000033 this$0;
      final EditText val$edittext;

      public AnonymousClass100000030(AnonymousClass100000033 anonymousClass100000033,
          EditText editText) {
        this.this$0 = anonymousClass100000033;
        this.val$edittext = editText;
      }

      @Override // android.view.View.OnTouchListener
      public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
          case 0:
            this.val$edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
            return true;
          case 1:
            this.val$edittext
                .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            return true;
          default:
            return false;
        }
      }
    }

    public static class AnonymousClass100000031 implements View.OnFocusChangeListener {
      private final AnonymousClass100000033 this$0;

      public AnonymousClass100000031(AnonymousClass100000033 anonymousClass100000033) {
        this.this$0 = anonymousClass100000033;
      }

      @Override // android.view.View.OnFocusChangeListener
      public void onFocusChange(View view, boolean z) {
        FloatingModMenuService floatingModMenuService = this.this$0.this$0;
        this.this$0.this$0.getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) floatingModMenuService
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (z) {
          inputMethodManager.toggleSoftInput(2, 1);
          return;
        } else {
          inputMethodManager.toggleSoftInput(1, 0);
          return;
        }
      }
    }

    public static class AnonymousClass100000032 implements View.OnClickListener {
      private final AnonymousClass100000033 this$0;
      final AlertDialog val$alert;
      final EditText val$edittext;
      final EditTextNum val$edittextnum;
      final String val$featName;
      final int val$featNum;
      final int val$maxValue;
      final TextView val$textView2;

      public AnonymousClass100000032(AnonymousClass100000033 anonymousClass100000033,
          EditText editText, int i, EditTextNum editTextNum, TextView textView,
          AlertDialog alertDialog, String str, int i2) {
        this.this$0 = anonymousClass100000033;
        this.val$edittext = editText;
        this.val$maxValue = i;
        this.val$edittextnum = editTextNum;
        this.val$textView2 = textView;
        this.val$alert = alertDialog;
        this.val$featName = str;
        this.val$featNum = i2;
      }

      @Override // android.view.View.OnClickListener
      public void onClick(View view) {
        int i;
        try {
          i = Integer.parseInt(TextUtils.isEmpty(this.val$edittext.getText().toString()) ? "0"
              : this.val$edittext.getText().toString());
          if (this.val$maxValue != 0 && i >= this.val$maxValue) {
            i = this.val$maxValue;
          }
        } catch (NumberFormatException e) {
          i = 2147483640;
        }
        this.val$edittextnum.setNum(i);
        this.val$textView2
            .setText(
                Html.fromHtml(
                    new StringBuffer()
                        .append(
                            new StringBuffer().append(new StringBuffer()
                                .append(new StringBuffer()
                                    .append(new StringBuffer().append("═▶︎ ")
                                        .append("<font color='").toString())
                                    .append(this.this$0.this$0.NumberTxtColor).toString())
                                .append("'>").toString()).append(i).toString())
                        .append("</font>").toString()));
        this.val$alert.dismiss();
        Preferences.changeFeatureInt(this.val$featName, this.val$featNum, i);
        this.val$edittext.setFocusable(false);
      }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      AlertDialog create = new AlertDialog.Builder(this.this$0.getApplicationContext(), 2).create();
      if (Build.VERSION.SDK_INT >= 19) {
        ((Window) Objects.requireNonNull(create.getWindow()))
            .setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
      }
      create.setOnCancelListener(new AnonymousClass100000029(this));
      LinearLayout linearLayout = new LinearLayout(this.this$0.getApplicationContext());
      linearLayout.setPadding(5, 5, 5, 5);
      linearLayout.setOrientation(1);
      byte[] decode = Base64.decode(this.this$0.Sonic(), 0);
      this.this$0.blackgamer = new BitmapDrawable(
          BitmapFactory.decodeByteArray(decode, 0, decode.length));
      linearLayout.setBackground(this.this$0.blackgamer);
      linearLayout.setElevation(5.0f);
      RainbowActivity rainbowActivity = new RainbowActivity(this.this$0.getApplicationContext());
      rainbowActivity.setText(Html.fromHtml(new StringBuffer()
          .append(new StringBuffer().append("<u>").append(this.val$featName).toString())
          .append("</u>").toString()));
      rainbowActivity.setGravity(17);
      rainbowActivity.setTypeface(Typeface.DEFAULT_BOLD);
      rainbowActivity.setTextColor(this.this$0.TEXT_COLOR);
      rainbowActivity.setTextSize(22.0f);
      TextView textView = new TextView(this.this$0.getApplicationContext());
      textView.setGravity(17);
      textView.setTextSize(14.0f);
      textView.setText("Click \"Set Value\" button to apply changes || outside to cancel");
      textView.setPadding(10, 5, 10, 5);
      textView.setTextColor(this.this$0.TEXT_COLOR);
      TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(-1, -2);
      ((LinearLayout.LayoutParams) layoutParams).weight = 1;
      EditText editText = new EditText(this.this$0.getApplicationContext());
      editText.setLayoutParams(layoutParams);
      editText.setMaxLines(1);
      editText.setHint("Write Value");
      editText.setWidth(this.this$0.dp(300));
      editText.setTextColor(this.this$0.TEXT_COLOR);
      editText.setInputType(2);
      editText.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
      editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
      editText.setOnTouchListener(new AnonymousClass100000030(this, editText));
      editText.setOnFocusChangeListener(new AnonymousClass100000031(this));
      editText.requestFocus();
      TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(-1, -2);
      layoutParams2.setMargins(20, 10, 20, 15);
      Button button = new Button(this.this$0.getApplicationContext());
      GradientDrawable gradientDrawable = new GradientDrawable();
      gradientDrawable.setColor(this.this$0.BTN_COLOR);
      gradientDrawable.setStroke(2, -1);
      gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
      button.setBackground(gradientDrawable);
      button.setLayoutParams(layoutParams2);
      button.setTextColor(this.this$0.TEXT_COLOR_2);
      button.setPadding(15, 10, 15, 10);
      button.setText("SET VALUE");
      button.setOnClickListener(new AnonymousClass100000032(this, editText, this.val$maxValue,
          this.val$edittextnum, this.val$textView2, create, this.val$featName, this.val$featNum));
      linearLayout.addView(rainbowActivity);
      linearLayout.addView(textView);
      linearLayout.addView(editText);
      linearLayout.addView(button);
      create.setView(linearLayout);
      create.show();
    }
  }

  public static class AnonymousClass100000027 implements View.OnClickListener {
    boolean isOn;
    private final FloatingModMenuService this$0;
    final Button val$button;
    final int val$featNum;
    final boolean val$finalIsOn;
    final String val$finalfeatName;

    public AnonymousClass100000027(FloatingModMenuService floatingModMenuService, boolean z,
        String str, int i, Button button) {
      this.this$0 = floatingModMenuService;
      this.val$finalIsOn = z;
      this.val$finalfeatName = str;
      this.val$featNum = i;
      this.val$button = button;
      this.isOn = this.val$finalIsOn;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      Preferences.changeFeatureBool(this.val$finalfeatName, this.val$featNum, this.isOn);
      if (this.isOn) {
        this.val$button.setText(Html
            .fromHtml(new StringBuffer().append(this.val$finalfeatName).append(": ON").toString()));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(this.this$0.LST_MAB);
        gradientDrawable.setStroke(3, -1, 4, 4);
        gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
        this.val$button.setBackground(gradientDrawable);
        this.isOn = false;
        return;
      }
      this.val$button.setText(Html
          .fromHtml(new StringBuffer().append(this.val$finalfeatName).append(": OFF").toString()));
      GradientDrawable gradientDrawable2 = new GradientDrawable();
      gradientDrawable2.setColor(this.this$0.BTN_COLOR);
      gradientDrawable2.setStroke(3, -1, 4, 4);
      gradientDrawable2.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
      this.val$button.setBackground(gradientDrawable2);
      this.isOn = true;
    }
  }

  public View ButtonOnOff(int i, String str, boolean z) {
    boolean z2;
    Button button = new Button(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(7, 5, 7, 5);
    button.setLayoutParams(layoutParams);
    button.setTextColor(this.TEXT_COLOR_2);
    button.setAllCaps(false);
    String replace = str.replace("OnOff_", "");
    if (Preferences.loadPrefBool(str, i, z)) {
      button.setText(Html.fromHtml(new StringBuffer().append(replace).append(": ON").toString()));
      GradientDrawable gradientDrawable = new GradientDrawable();
      gradientDrawable.setColor(this.LST_MAB);
      gradientDrawable.setStroke(3, -1, 4, 4);
      gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
      button.setBackground(gradientDrawable);
      z2 = false;
    } else {
      button.setText(Html.fromHtml(new StringBuffer().append(replace).append(": OFF").toString()));
      GradientDrawable gradientDrawable2 = new GradientDrawable();
      gradientDrawable2.setColor(this.BTN_COLOR);
      gradientDrawable2.setStroke(3, -1, 4, 4);
      gradientDrawable2.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
      button.setBackground(gradientDrawable2);
      z2 = true;
    }
    button.setOnClickListener(new AnonymousClass100000027(this, z2, replace, i, button));
    return button;
  }

  public static class AnonymousClass100000028 implements AdapterView.OnItemSelectedListener {
    private final FloatingModMenuService this$0;
    final int val$featNum;
    final Spinner val$spinner;

    public AnonymousClass100000028(FloatingModMenuService floatingModMenuService, Spinner spinner,
        int i) {
      this.this$0 = floatingModMenuService;
      this.val$spinner = spinner;
      this.val$featNum = i;
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
      Preferences.changeFeatureInt(this.val$spinner.getSelectedItem().toString(), this.val$featNum,
          i);
      ((TextView) adapterView.getChildAt(0)).setTextColor(this.this$0.TEXT_COLOR_2);
    }
  }

  public View Spinner(int i, String str, String str2) {
    Log.d(TAG, new StringBuffer()
        .append(new StringBuffer().append(new StringBuffer().append(
            new StringBuffer().append(new StringBuffer().append("spinner ").append(i).toString())
                .append(" ").toString())
            .append(str).toString()).append(" ").toString())
        .append(str2).toString());
    LinkedList linkedList = new LinkedList(Arrays.asList(str2.split(",")));
    LinearLayout linearLayout = new LinearLayout(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
    layoutParams.setMargins(7, 2, 7, 5);
    linearLayout.setOrientation(1);
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(this.LST_MAB);
    gradientDrawable.setStroke(3, -1, 4, 4);
    gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    linearLayout.setBackground(gradientDrawable);
    linearLayout.setLayoutParams(layoutParams);
    Spinner spinner = new Spinner(this, 1);
    spinner.setLayoutParams(layoutParams);
    spinner.getBackground().setColorFilter(1, PorterDuff.Mode.SRC_ATOP);
    ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367049, linkedList);
    arrayAdapter.setDropDownViewResource(17367049);
    spinner.setAdapter((SpinnerAdapter) arrayAdapter);
    spinner.setSelection(Preferences.loadPrefInt(str, i));
    spinner.setOnItemSelectedListener(new AnonymousClass100000028(this, spinner, i));
    linearLayout.addView(spinner);
    return linearLayout;
  }

  public static class AnonymousClass100000029 implements View.OnClickListener {
    boolean settingsOpen;
    private final FloatingModMenuService this$0;
    final TextView val$eye;
    final BitmapDrawable val$sbmods;
    final BitmapDrawable val$sbmods2;
    final TextView val$textView2;

    public AnonymousClass100000029(FloatingModMenuService floatingModMenuService, TextView textView,
        BitmapDrawable bitmapDrawable, TextView textView2, BitmapDrawable bitmapDrawable2) {
      this.this$0 = floatingModMenuService;
      this.val$eye = textView;
      this.val$sbmods2 = bitmapDrawable;
      this.val$textView2 = textView2;
      this.val$sbmods = bitmapDrawable2;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      try {
        this.settingsOpen = !this.settingsOpen;
        if (this.settingsOpen) {
          this.val$eye.setBackground(this.val$sbmods2);
          this.val$textView2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
          this.val$eye.setBackground(this.val$sbmods);
          this.val$textView2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
      } catch (IllegalStateException e) {
      }
    }
  }

  public static class AnonymousClass100000034 implements View.OnClickListener {
    private final FloatingModMenuService this$0;
    final EditTextNum val$edittextnum;
    final String val$featName;
    final int val$featNum;
    final int val$maxValue;
    final TextView val$textView2;

    public AnonymousClass100000034(FloatingModMenuService floatingModMenuService, String str, int i,
        EditTextNum editTextNum, TextView textView, int i2) {
      this.this$0 = floatingModMenuService;
      this.val$featName = str;
      this.val$maxValue = i;
      this.val$edittextnum = editTextNum;
      this.val$textView2 = textView;
      this.val$featNum = i2;
    }

    public static class AnonymousClass100000030 implements DialogInterface.OnCancelListener {
      private final AnonymousClass100000034 this$0;

      public AnonymousClass100000030(AnonymousClass100000034 anonymousClass100000034) {
        this.this$0 = anonymousClass100000034;
      }

      @Override // android.content.DialogInterface.OnCancelListener
      public void onCancel(DialogInterface dialogInterface) {
        FloatingModMenuService floatingModMenuService = this.this$0.this$0;
        this.this$0.this$0.getApplicationContext();
        ((InputMethodManager) floatingModMenuService.getSystemService(Context.INPUT_METHOD_SERVICE))
            .toggleSoftInput(1, 0);
      }
    }

    public static class AnonymousClass100000031 implements View.OnTouchListener {
      private final AnonymousClass100000034 this$0;
      final EditText val$edittext;

      public AnonymousClass100000031(AnonymousClass100000034 anonymousClass100000034,
          EditText editText) {
        this.this$0 = anonymousClass100000034;
        this.val$edittext = editText;
      }

      @Override // android.view.View.OnTouchListener
      public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
          case 0:
            this.val$edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
            return true;
          case 1:
            this.val$edittext
                .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            return true;
          default:
            return false;
        }
      }
    }

    public static class AnonymousClass100000032 implements View.OnFocusChangeListener {
      private final AnonymousClass100000034 this$0;

      public AnonymousClass100000032(AnonymousClass100000034 anonymousClass100000034) {
        this.this$0 = anonymousClass100000034;
      }

      @Override // android.view.View.OnFocusChangeListener
      public void onFocusChange(View view, boolean z) {
        FloatingModMenuService floatingModMenuService = this.this$0.this$0;
        this.this$0.this$0.getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) floatingModMenuService
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (z) {
          inputMethodManager.toggleSoftInput(2, 1);
          return;
        } else {
          inputMethodManager.toggleSoftInput(1, 0);
          return;
        }
      }
    }

    public static class AnonymousClass100000033 implements View.OnClickListener {
      private final AnonymousClass100000034 this$0;
      final AlertDialog val$alert;
      final EditText val$edittext;
      final EditTextNum val$edittextnum;
      final String val$featName;
      final int val$featNum;
      final int val$maxValue;
      final TextView val$textView2;

      public AnonymousClass100000033(AnonymousClass100000034 anonymousClass100000034,
          EditText editText, int i, EditTextNum editTextNum, TextView textView,
          AlertDialog alertDialog, String str, int i2) {
        this.this$0 = anonymousClass100000034;
        this.val$edittext = editText;
        this.val$maxValue = i;
        this.val$edittextnum = editTextNum;
        this.val$textView2 = textView;
        this.val$alert = alertDialog;
        this.val$featName = str;
        this.val$featNum = i2;
      }

      @Override // android.view.View.OnClickListener
      public void onClick(View view) {
        int i;
        try {
          i = Integer.parseInt(TextUtils.isEmpty(this.val$edittext.getText().toString()) ? "0"
              : this.val$edittext.getText().toString());
          if (this.val$maxValue != 0 && i >= this.val$maxValue) {
            i = this.val$maxValue;
          }
        } catch (NumberFormatException e) {
          i = 2147483640;
        }
        this.val$edittextnum.setNum(i);
        this.val$textView2
            .setText(
                Html.fromHtml(
                    new StringBuffer()
                        .append(
                            new StringBuffer().append(new StringBuffer()
                                .append(new StringBuffer()
                                    .append(new StringBuffer().append("═▶︎ ")
                                        .append("<font color='").toString())
                                    .append(this.this$0.this$0.NumberTxtColor).toString())
                                .append("'>").toString()).append(i).toString())
                        .append("</font>").toString()));
        this.val$alert.dismiss();
        Preferences.changeFeatureInt(this.val$featName, this.val$featNum, i);
        this.val$edittext.setFocusable(false);
      }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      AlertDialog create = new AlertDialog.Builder(this.this$0.getApplicationContext(), 2).create();
      if (Build.VERSION.SDK_INT >= 19) {
        ((Window) Objects.requireNonNull(create.getWindow()))
            .setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
      }
      create.setOnCancelListener(new AnonymousClass100000030(this));
      LinearLayout linearLayout = new LinearLayout(this.this$0.getApplicationContext());
      linearLayout.setPadding(5, 5, 5, 5);
      linearLayout.setOrientation(1);
      byte[] decode = Base64.decode(this.this$0.Sonic(), 0);
      this.this$0.blackgamer = new BitmapDrawable(
          BitmapFactory.decodeByteArray(decode, 0, decode.length));
      linearLayout.setBackground(this.this$0.blackgamer);
      linearLayout.setElevation(5.0f);
      RainbowActivity rainbowActivity = new RainbowActivity(this.this$0.getApplicationContext());
      rainbowActivity.setText(Html.fromHtml(new StringBuffer()
          .append(new StringBuffer().append("<u>").append(this.val$featName).toString())
          .append("</u>").toString()));
      rainbowActivity.setGravity(17);
      rainbowActivity.setTypeface(Typeface.DEFAULT_BOLD);
      rainbowActivity.setTextColor(this.this$0.TEXT_COLOR);
      rainbowActivity.setTextSize(22.0f);
      TextView textView = new TextView(this.this$0.getApplicationContext());
      textView.setGravity(17);
      textView.setTextSize(14.0f);
      textView.setText("Click \"Set Value\" button to apply changes || outside to cancel");
      textView.setPadding(10, 5, 10, 5);
      textView.setTextColor(this.this$0.TEXT_COLOR);
      TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(-1, -2);
      ((LinearLayout.LayoutParams) layoutParams).weight = 1;
      EditText editText = new EditText(this.this$0.getApplicationContext());
      editText.setLayoutParams(layoutParams);
      editText.setMaxLines(1);
      editText.setHint("Write Value");
      editText.setWidth(this.this$0.convertDipToPixels(300));
      editText.setTextColor(this.this$0.TEXT_COLOR);
      editText.setInputType(2);
      editText.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
      editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
      editText.setOnTouchListener(new AnonymousClass100000031(this, editText));
      editText.setOnFocusChangeListener(new AnonymousClass100000032(this));
      editText.requestFocus();
      TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(-1, -2);
      layoutParams2.setMargins(20, 10, 20, 15);
      Button button = new Button(this.this$0.getApplicationContext());
      GradientDrawable gradientDrawable = new GradientDrawable();
      gradientDrawable.setColor(this.this$0.BTN_COLOR);
      gradientDrawable.setStroke(2, -1);
      gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
      button.setBackground(gradientDrawable);
      button.setLayoutParams(layoutParams2);
      button.setTextColor(this.this$0.TEXT_COLOR_2);
      button.setPadding(15, 10, 15, 10);
      button.setText("SET VALUE");
      button.setOnClickListener(new AnonymousClass100000033(this, editText, this.val$maxValue,
          this.val$edittextnum, this.val$textView2, create, this.val$featName, this.val$featNum));
      linearLayout.addView(rainbowActivity);
      linearLayout.addView(textView);
      linearLayout.addView(editText);
      linearLayout.addView(button);
      create.setView(linearLayout);
      create.show();
    }
  }

  public static class AnonymousClass100000038 implements View.OnClickListener {
    private final FloatingModMenuService this$0;
    final EditTextString val$edittextstring;
    final String val$featName;
    final int val$featNum;
    final TextView val$textView2;

    AnonymousClass100000038(FloatingModMenuService floatingModMenuService, String str,
        EditTextString editTextString, TextView textView, int i) {
      this.this$0 = floatingModMenuService;
      this.val$featName = str;
      this.val$edittextstring = editTextString;
      this.val$textView2 = textView;
      this.val$featNum = i;
    }

    public static class AnonymousClass100000035 implements DialogInterface.OnCancelListener {
      private final AnonymousClass100000038 this$0;

      public AnonymousClass100000035(AnonymousClass100000038 anonymousClass100000038) {
        this.this$0 = anonymousClass100000038;
      }

      @Override // android.content.DialogInterface.OnCancelListener
      public void onCancel(DialogInterface dialogInterface) {
        FloatingModMenuService floatingModMenuService = this.this$0.this$0;
        this.this$0.this$0.getApplicationContext();
        ((InputMethodManager) floatingModMenuService.getSystemService(Context.INPUT_METHOD_SERVICE))
            .toggleSoftInput(1, 0);
      }
    }

    public static class AnonymousClass100000036 implements View.OnFocusChangeListener {
      private final AnonymousClass100000038 this$0;

      public AnonymousClass100000036(AnonymousClass100000038 anonymousClass100000038) {
        this.this$0 = anonymousClass100000038;
      }

      @Override // android.view.View.OnFocusChangeListener
      public void onFocusChange(View view, boolean z) {
        FloatingModMenuService floatingModMenuService = this.this$0.this$0;
        this.this$0.this$0.getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) floatingModMenuService
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (z) {
          inputMethodManager.toggleSoftInput(2, 1);
          return;
        } else {
          inputMethodManager.toggleSoftInput(1, 0);
          return;
        }
      }
    }

    public static class AnonymousClass100000037 implements View.OnClickListener {
      private final AnonymousClass100000038 this$0;
      final AlertDialog val$alert;
      final EditText val$edittext;
      final EditTextString val$edittextstring;
      final String val$featName;
      final int val$featNum;
      final TextView val$textView2;

      public AnonymousClass100000037(AnonymousClass100000038 anonymousClass100000038,
          EditText editText, EditTextString editTextString, TextView textView,
          AlertDialog alertDialog, String str, int i) {
        this.this$0 = anonymousClass100000038;
        this.val$edittext = editText;
        this.val$edittextstring = editTextString;
        this.val$textView2 = textView;
        this.val$alert = alertDialog;
        this.val$featName = str;
        this.val$featNum = i;
      }

      @Override // android.view.View.OnClickListener
      public void onClick(View view) {
        String editable = this.val$edittext.getText().toString();
        this.val$edittextstring.setString(this.val$edittext.getText().toString());
        this.val$textView2
            .setText(
                Html.fromHtml(
                    new StringBuffer()
                        .append(
                            new StringBuffer()
                                .append(new StringBuffer()
                                    .append(new StringBuffer()
                                        .append(new StringBuffer().append("═▶︎")
                                            .append("<font color='").toString())
                                        .append(this.this$0.this$0.NumberTxtColor).toString())
                                    .append("'>").toString())
                                .append(editable).toString())
                        .append("</font>").toString()));
        this.val$alert.dismiss();
        Preferences.changeFeatureString(this.val$featName, this.val$featNum, editable);
        this.val$edittext.setFocusable(false);
      }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      AlertDialog create = new AlertDialog.Builder(this.this$0.getApplicationContext(), 2).create();
      if (Build.VERSION.SDK_INT >= 19) {
        ((Window) Objects.requireNonNull(create.getWindow()))
            .setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
      }
      create.setOnCancelListener(new AnonymousClass100000035(this));
      LinearLayout linearLayout = new LinearLayout(this.this$0.getApplicationContext());
      linearLayout.setPadding(5, 5, 5, 5);
      linearLayout.setOrientation(1);
      byte[] decode = Base64.decode(this.this$0.Sonic(), 0);
      this.this$0.blackgamer = new BitmapDrawable(
          BitmapFactory.decodeByteArray(decode, 0, decode.length));
      linearLayout.setBackground(this.this$0.blackgamer);
      RainbowActivity rainbowActivity = new RainbowActivity(this.this$0.getApplicationContext());
      rainbowActivity.setText(Html.fromHtml(new StringBuffer()
          .append(new StringBuffer().append("<u>").append(this.val$featName).toString())
          .append("</u>").toString()));
      rainbowActivity.setGravity(17);
      rainbowActivity.setTypeface(Typeface.DEFAULT_BOLD);
      rainbowActivity.setTextColor(this.this$0.TEXT_COLOR);
      rainbowActivity.setTextSize(22.0f);
      TextView textView = new TextView(this.this$0.getApplicationContext());
      textView.setGravity(17);
      textView.setTextSize(14.0f);
      textView.setText("Click \"Set Value\" button to apply changes || outside to cancel");
      textView.setPadding(10, 5, 10, 5);
      textView.setTextColor(this.this$0.TEXT_COLOR);
      TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(-1, -2);
      ((LinearLayout.LayoutParams) layoutParams).weight = 1;
      EditText editText = new EditText(this.this$0.getApplicationContext());
      editText.setLayoutParams(layoutParams);
      editText.setMaxLines(1);
      editText.setHint("Write Text");
      editText.setWidth(this.this$0.dp(300));
      editText.setTextColor(this.this$0.TEXT_COLOR_2);
      editText.setText(this.val$edittextstring.getString());
      editText.setOnFocusChangeListener(new AnonymousClass100000036(this));
      editText.requestFocus();
      TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(-1, -2);
      layoutParams2.setMargins(20, 10, 20, 15);
      Button button = new Button(this.this$0.getApplicationContext());
      GradientDrawable gradientDrawable = new GradientDrawable();
      gradientDrawable.setColor(this.this$0.BTN_COLOR);
      gradientDrawable.setStroke(2, -1);
      gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
      button.setBackground(gradientDrawable);
      button.setLayoutParams(layoutParams2);
      button.setTextColor(this.this$0.TEXT_COLOR_2);
      button.setPadding(15, 10, 15, 10);
      button.setText("SET VALUE");
      button.setOnClickListener(new AnonymousClass100000037(this, editText, this.val$edittextstring,
          this.val$textView2, create, this.val$featName, this.val$featNum));
      linearLayout.addView(rainbowActivity);
      linearLayout.addView(textView);
      linearLayout.addView(editText);
      linearLayout.addView(button);
      create.setView(linearLayout);
      create.show();
    }
  }

  public void InputNum(LinearLayout linearLayout, int i, String str, int i2) {
    EditTextNum editTextNum = new EditTextNum(this);
    LinearLayout linearLayout2 = new LinearLayout(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(15, 10, 15, 10);
    linearLayout2.setLayoutParams(layoutParams);
    linearLayout2.setOrientation(0);
    linearLayout2.setPadding(10, 8, 8, 8);
    linearLayout2.setGravity(16);
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(0);
    gradientDrawable.setStroke(3, -1);
    gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    linearLayout2.setBackground(gradientDrawable);
    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
    layoutParams2.weight = 1.0f;
    LinearLayout linearLayout3 = new LinearLayout(this);
    linearLayout3.setLayoutParams(layoutParams2);
    linearLayout3.setOrientation(1);
    linearLayout3.setGravity(17);
    TextView textView = new TextView(this);
    textView.setText(str);
    textView.setTextColor(this.TEXT_COLOR_2);
    textView.setTextSize(14.0f);
    textView.setSingleLine(true);
    textView.setGravity(3);
    textView.setPadding(5, 0, 0, 0);
    TextView textView2 = new TextView(this);
    int loadPrefInt = Preferences.loadPrefInt(str, i);
    textView2
        .setText(
            Html.fromHtml(
                new StringBuffer()
                    .append(
                        new StringBuffer()
                            .append(
                                new StringBuffer()
                                    .append(new StringBuffer()
                                        .append(new StringBuffer().append("═▶︎")
                                            .append("<font color='").toString())
                                        .append(this.NumberTxtColor).toString())
                                    .append("'>").toString())
                            .append(loadPrefInt == 0 ? 1 : loadPrefInt).toString())
                    .append("</font>").toString()));
    textView2.setTextColor(this.TEXT_COLOR_2);
    textView2.setTextSize(14.0f);
    textView2.setGravity(3);
    textView2.setPadding(5, 0, 0, 0);
    GradientDrawable gradientDrawable2 = new GradientDrawable();
    gradientDrawable2.setColor(this.BTN_COLOR);
    gradientDrawable2.setStroke(2, -1);
    gradientDrawable2.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    TableRow.LayoutParams layoutParams3 = new TableRow.LayoutParams(dp(30), dp(30));
    ((LinearLayout.LayoutParams) layoutParams3).gravity = 80;
    layoutParams3.setMargins(0, 0, 20, 0);
    TextView textView3 = new TextView(this);
    textView3.setLayoutParams(layoutParams3);
    byte[] decode = Base64.decode(EYE(), 0);
    BitmapDrawable bitmapDrawable = new BitmapDrawable(
        BitmapFactory.decodeByteArray(decode, 0, decode.length));
    textView3.setBackground(bitmapDrawable);
    byte[] decode2 = Base64.decode(EYECLOSE(), 0);
    textView3.setOnClickListener(new AnonymousClass100000029(this, textView3,
        new BitmapDrawable(BitmapFactory.decodeByteArray(decode2, 0, decode2.length)), textView2,
        bitmapDrawable));
    Button button = new Button(this);
    button.setText("ENTER");
    button.setAllCaps(false);
    button.setBackground(gradientDrawable2);
    button.setTextColor(this.TEXT_COLOR_2);
    button
        .setOnClickListener(new AnonymousClass100000034(this, str, i2, editTextNum, textView2, i));
    linearLayout2.addView(linearLayout3);
    linearLayout3.addView(textView);
    linearLayout3.addView(textView2);
    linearLayout2.addView(textView3);
    linearLayout2.addView(button);
    linearLayout.addView(linearLayout2);
  }

  public static class AnonymousClass100000035 implements View.OnClickListener {
    boolean settingsOpen;
    private final FloatingModMenuService this$0;
    final TextView val$eye;
    final BitmapDrawable val$sbmods;
    final BitmapDrawable val$sbmods2;
    final TextView val$textView2;

    public AnonymousClass100000035(FloatingModMenuService floatingModMenuService, TextView textView,
        BitmapDrawable bitmapDrawable, TextView textView2, BitmapDrawable bitmapDrawable2) {
      this.this$0 = floatingModMenuService;
      this.val$eye = textView;
      this.val$sbmods2 = bitmapDrawable;
      this.val$textView2 = textView2;
      this.val$sbmods = bitmapDrawable2;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      try {
        this.settingsOpen = !this.settingsOpen;
        if (this.settingsOpen) {
          this.val$eye.setBackground(this.val$sbmods2);
          this.val$textView2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
          this.val$eye.setBackground(this.val$sbmods);
          this.val$textView2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
      } catch (IllegalStateException e) {
      }
    }
  }

  public static class AnonymousClass100000039 implements View.OnClickListener {
    private final FloatingModMenuService this$0;
    final EditTextString val$edittextstring;
    final String val$featName;
    final int val$featNum;
    final TextView val$textView2;

    public AnonymousClass100000039(FloatingModMenuService floatingModMenuService, String str,
        EditTextString editTextString, TextView textView, int i) {
      this.this$0 = floatingModMenuService;
      this.val$featName = str;
      this.val$edittextstring = editTextString;
      this.val$textView2 = textView;
      this.val$featNum = i;
    }

    public static class AnonymousClass100000036 implements DialogInterface.OnCancelListener {
      private final AnonymousClass100000039 this$0;

      public AnonymousClass100000036(AnonymousClass100000039 anonymousClass100000039) {
        this.this$0 = anonymousClass100000039;
      }

      @Override // android.content.DialogInterface.OnCancelListener
      public void onCancel(DialogInterface dialogInterface) {
        FloatingModMenuService floatingModMenuService = this.this$0.this$0;
        this.this$0.this$0.getApplicationContext();
        ((InputMethodManager) floatingModMenuService.getSystemService(Context.INPUT_METHOD_SERVICE))
            .toggleSoftInput(1, 0);
      }
    }

    public static class AnonymousClass100000037 implements View.OnFocusChangeListener {
      private final AnonymousClass100000039 this$0;

      public AnonymousClass100000037(AnonymousClass100000039 anonymousClass100000039) {
        this.this$0 = anonymousClass100000039;
      }

      @Override // android.view.View.OnFocusChangeListener
      public void onFocusChange(View view, boolean z) {
        FloatingModMenuService floatingModMenuService = this.this$0.this$0;
        this.this$0.this$0.getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) floatingModMenuService
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (z) {
          inputMethodManager.toggleSoftInput(2, 1);
          return;
        } else {
          inputMethodManager.toggleSoftInput(1, 0);
          return;
        }
      }
    }

    public static class AnonymousClass100000038 implements View.OnClickListener {
      private final AnonymousClass100000039 this$0;
      final AlertDialog val$alert;
      final EditText val$edittext;
      final EditTextString val$edittextstring;
      final String val$featName;
      final int val$featNum;
      final TextView val$textView2;

      public AnonymousClass100000038(AnonymousClass100000039 anonymousClass100000039,
          EditText editText, EditTextString editTextString, TextView textView,
          AlertDialog alertDialog, String str, int i) {
        this.this$0 = anonymousClass100000039;
        this.val$edittext = editText;
        this.val$edittextstring = editTextString;
        this.val$textView2 = textView;
        this.val$alert = alertDialog;
        this.val$featName = str;
        this.val$featNum = i;
      }

      @Override // android.view.View.OnClickListener
      public void onClick(View view) {
        String editable = this.val$edittext.getText().toString();
        this.val$edittextstring.setString(this.val$edittext.getText().toString());
        this.val$textView2
            .setText(
                Html.fromHtml(
                    new StringBuffer()
                        .append(
                            new StringBuffer()
                                .append(new StringBuffer()
                                    .append(new StringBuffer()
                                        .append(new StringBuffer().append("═▶︎")
                                            .append("<font color='").toString())
                                        .append(this.this$0.this$0.NumberTxtColor).toString())
                                    .append("'>").toString())
                                .append(editable).toString())
                        .append("</font>").toString()));
        this.val$alert.dismiss();
        Preferences.changeFeatureString(this.val$featName, this.val$featNum, editable);
        this.val$edittext.setFocusable(false);
      }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      AlertDialog create = new AlertDialog.Builder(this.this$0.getApplicationContext(), 2).create();
      if (Build.VERSION.SDK_INT >= 19) {
        ((Window) Objects.requireNonNull(create.getWindow()))
            .setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
      }
      create.setOnCancelListener(new AnonymousClass100000036(this));
      LinearLayout linearLayout = new LinearLayout(this.this$0.getApplicationContext());
      linearLayout.setPadding(5, 5, 5, 5);
      linearLayout.setOrientation(1);
      byte[] decode = Base64.decode(this.this$0.Sonic(), 0);
      this.this$0.blackgamer = new BitmapDrawable(
          BitmapFactory.decodeByteArray(decode, 0, decode.length));
      linearLayout.setBackground(this.this$0.blackgamer);
      RainbowActivity rainbowActivity = new RainbowActivity(this.this$0.getApplicationContext());
      rainbowActivity.setText(Html.fromHtml(new StringBuffer()
          .append(new StringBuffer().append("<u>").append(this.val$featName).toString())
          .append("</u>").toString()));
      rainbowActivity.setGravity(17);
      rainbowActivity.setTypeface(Typeface.DEFAULT_BOLD);
      rainbowActivity.setTextColor(this.this$0.TEXT_COLOR);
      rainbowActivity.setTextSize(22.0f);
      TextView textView = new TextView(this.this$0.getApplicationContext());
      textView.setGravity(17);
      textView.setTextSize(14.0f);
      textView.setText("Click \"Set Value\" button to apply changes || outside to cancel");
      textView.setPadding(10, 5, 10, 5);
      textView.setTextColor(this.this$0.TEXT_COLOR);
      TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(-1, -2);
      ((LinearLayout.LayoutParams) layoutParams).weight = 1;
      EditText editText = new EditText(this.this$0.getApplicationContext());
      editText.setLayoutParams(layoutParams);
      editText.setMaxLines(1);
      editText.setHint("Write Text");
      editText.setWidth(this.this$0.convertDipToPixels(300));
      editText.setTextColor(this.this$0.TEXT_COLOR_2);
      editText.setText(this.val$edittextstring.getString());
      editText.setOnFocusChangeListener(new AnonymousClass100000037(this));
      editText.requestFocus();
      TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(-1, -2);
      layoutParams2.setMargins(20, 10, 20, 15);
      Button button = new Button(this.this$0.getApplicationContext());
      GradientDrawable gradientDrawable = new GradientDrawable();
      gradientDrawable.setColor(this.this$0.BTN_COLOR);
      gradientDrawable.setStroke(2, -1);
      gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
      button.setBackground(gradientDrawable);
      button.setLayoutParams(layoutParams2);
      button.setTextColor(this.this$0.TEXT_COLOR_2);
      button.setPadding(15, 10, 15, 10);
      button.setText("SET VALUE");
      button.setOnClickListener(new AnonymousClass100000038(this, editText, this.val$edittextstring,
          this.val$textView2, create, this.val$featName, this.val$featNum));
      linearLayout.addView(rainbowActivity);
      linearLayout.addView(textView);
      linearLayout.addView(editText);
      linearLayout.addView(button);
      create.setView(linearLayout);
      create.show();
    }
  }

  public void InputText(LinearLayout linearLayout, int i, String str) {
    EditTextString editTextString = new EditTextString(this);
    LinearLayout linearLayout2 = new LinearLayout(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(15, 10, 15, 10);
    linearLayout2.setLayoutParams(layoutParams);
    linearLayout2.setOrientation(0);
    linearLayout2.setPadding(10, 8, 8, 8);
    linearLayout2.setGravity(16);
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(0);
    gradientDrawable.setStroke(3, -1);
    gradientDrawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    linearLayout2.setBackground(gradientDrawable);
    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
    layoutParams2.weight = 1.0f;
    LinearLayout linearLayout3 = new LinearLayout(this);
    linearLayout3.setLayoutParams(layoutParams2);
    linearLayout3.setOrientation(1);
    linearLayout3.setGravity(17);
    TextView textView = new TextView(this);
    textView.setText(str);
    textView.setTextColor(this.TEXT_COLOR_2);
    textView.setGravity(3);
    textView.setTextSize(14.0f);
    textView.setPadding(5, 0, 0, 0);
    textView.setSingleLine(true);
    TextView textView2 = new TextView(this);
    String loadPrefString = Preferences.loadPrefString(str, i);
    editTextString.setString(loadPrefString == "" ? "" : loadPrefString);
    textView2
        .setText(
            Html.fromHtml(
                new StringBuffer()
                    .append(
                        new StringBuffer()
                            .append(
                                new StringBuffer()
                                    .append(new StringBuffer()
                                        .append(new StringBuffer().append("═▶︎")
                                            .append("<font color='").toString())
                                        .append(this.NumberTxtColor).toString())
                                    .append("'>").toString())
                            .append(loadPrefString).toString())
                    .append("</font>").toString()));
    textView2.setTextColor(this.TEXT_COLOR_2);
    textView2.setGravity(3);
    textView2.setTextSize(14.0f);
    textView2.setPadding(5, 0, 0, 0);
    GradientDrawable gradientDrawable2 = new GradientDrawable();
    gradientDrawable2.setColor(this.BTN_COLOR);
    gradientDrawable2.setStroke(2, -1);
    gradientDrawable2.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    TableRow.LayoutParams layoutParams3 = new TableRow.LayoutParams(dp(30), dp(30));
    ((LinearLayout.LayoutParams) layoutParams3).gravity = 80;
    layoutParams3.setMargins(0, 0, 20, 0);
    TextView textView3 = new TextView(this);
    textView3.setLayoutParams(layoutParams3);
    byte[] decode = Base64.decode(EYE(), 0);
    BitmapDrawable bitmapDrawable = new BitmapDrawable(
        BitmapFactory.decodeByteArray(decode, 0, decode.length));
    textView3.setBackground(bitmapDrawable);
    byte[] decode2 = Base64.decode(EYECLOSE(), 0);
    textView3.setOnClickListener(new AnonymousClass100000035(this, textView3,
        new BitmapDrawable(BitmapFactory.decodeByteArray(decode2, 0, decode2.length)), textView2,
        bitmapDrawable));
    Button button = new Button(this);
    button.setText("ENTER");
    button.setBackground(gradientDrawable2);
    button.setAllCaps(false);
    button.setTextColor(this.TEXT_COLOR_2);
    button.setOnClickListener(new AnonymousClass100000039(this, str, editTextString, textView2, i));
    linearLayout2.addView(linearLayout3);
    linearLayout3.addView(textView);
    linearLayout3.addView(textView2);
    linearLayout2.addView(textView3);
    linearLayout2.addView(button);
    linearLayout.addView(linearLayout2);
  }

  public static class AnonymousClass100000040 implements CompoundButton.OnCheckedChangeListener {
    private final FloatingModMenuService this$0;
    final CheckBox val$checkBox;
    final String val$featName;
    final int val$featNum;

    public AnonymousClass100000040(FloatingModMenuService floatingModMenuService, CheckBox checkBox,
        String str, int i) {
      this.this$0 = floatingModMenuService;
      this.val$checkBox = checkBox;
      this.val$featName = str;
      this.val$featNum = i;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
      if (this.val$checkBox.isChecked()) {
        Preferences.changeFeatureBool(this.val$featName, this.val$featNum, z);
        return;
      } else {
        Preferences.changeFeatureBool(this.val$featName, this.val$featNum, z);
        return;
      }
    }
  }

  public View CheckBox(int i, String str, boolean z) {
    CheckBox checkBox = new CheckBox(this);
    checkBox.setText(str);
    checkBox.setTextColor(this.TEXT_COLOR_2);
    if (Build.VERSION.SDK_INT >= 21) {
      checkBox.setButtonTintList(ColorStateList.valueOf(this.CheckBoxColor));
    }
    checkBox.setChecked(Preferences.loadPrefBool(str, i, z));
    checkBox.setOnCheckedChangeListener(new AnonymousClass100000040(this, checkBox, str, i));
    return checkBox;
  }

  public static class AnonymousClass100000041 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000041(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      Intent intent = new Intent("android.intent.action.VIEW");
      intent.setFlags(268435456);
      intent.setData(Uri.parse("https://youtube.com/@donigbryt_official?si=jixWVg2EdsvFTeRs"));
      this.this$0.startActivity(intent);
    }
  }

  public void YoutubeLink(LinearLayout linearLayout, int i, String str) {
    LinearLayout linearLayout2 = new LinearLayout(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(5, 7, 5, 7);
    linearLayout2.setOrientation(0);
    linearLayout2.setGravity(16);
    linearLayout2.setPadding(5, 8, 8, 8);
    linearLayout2.setLayoutParams(layoutParams);
    TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams) layoutParams2).weight = 1.0f;
    LinearLayout linearLayout3 = new LinearLayout(this);
    linearLayout3.setLayoutParams(layoutParams2);
    linearLayout3.setOrientation(1);
    linearLayout3.setGravity(17);
    RainbowActivity rainbowActivity = new RainbowActivity(this);
    rainbowActivity.setTextColor(this.TEXT_COLOR_2);
    rainbowActivity.setText(str);
    rainbowActivity.setTextSize(12.0f);
    rainbowActivity.setGravity(3);
    rainbowActivity.setSingleLine(true);
    rainbowActivity.setPadding(5, 0, 0, 0);
    Button button = new Button(this);
    AddColor(button, Color.parseColor("#AFFF3434"), 3, 0, 0, this.LST_MAB, 30, 30, 0, 0, 30, 30, 0,
        0);
    button.setTextColor(this.TEXT_COLOR_2);
    byte[] decode = Base64.decode(IconYoutube(), 0);
    button
        .setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(decode, 0, decode.length)));
    RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(75, 75);
    layoutParams3.addRule(11);
    button.setLayoutParams(layoutParams3);
    button.setOnClickListener(new AnonymousClass100000041(this));
    linearLayout2.addView(linearLayout3);
    linearLayout3.addView(rainbowActivity);
    linearLayout2.addView(button);
    linearLayout.addView(linearLayout2);
  }

  public static class AnonymousClass100000042 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000042(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      Intent intent = new Intent("android.intent.action.VIEW");
      intent.setFlags(268435456);
      intent.setData(Uri.parse("https://t.me/DONIfunntgamer"));
      this.this$0.startActivity(intent);
    }
  }

  public void TeleGroupLink(LinearLayout linearLayout, int i, String str) {
    LinearLayout linearLayout2 = new LinearLayout(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(5, 7, 5, 7);
    linearLayout2.setOrientation(0);
    linearLayout2.setGravity(16);
    linearLayout2.setPadding(5, 8, 8, 8);
    linearLayout2.setLayoutParams(layoutParams);
    TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams) layoutParams2).weight = 1.0f;
    LinearLayout linearLayout3 = new LinearLayout(this);
    linearLayout3.setLayoutParams(layoutParams2);
    linearLayout3.setOrientation(1);
    linearLayout3.setGravity(17);
    RainbowActivity rainbowActivity = new RainbowActivity(this);
    rainbowActivity.setTextColor(this.TEXT_COLOR_2);
    rainbowActivity.setText(str);
    rainbowActivity.setTextSize(12.0f);
    rainbowActivity.setGravity(3);
    rainbowActivity.setSingleLine(true);
    rainbowActivity.setPadding(5, 0, 0, 0);
    Button button = new Button(this);
    AddColor(button, Color.parseColor("#AFFF3434"), 3, 0, 0, this.LST_MAB, 30, 30, 0, 0, 30, 30, 0,
        0);
    button.setTextColor(this.TEXT_COLOR_2);
    byte[] decode = Base64.decode(IconTeleVip(), 0);
    button
        .setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(decode, 0, decode.length)));
    RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(75, 75);
    layoutParams3.addRule(11);
    button.setLayoutParams(layoutParams3);
    button.setOnClickListener(new AnonymousClass100000042(this));
    linearLayout2.addView(linearLayout3);
    linearLayout3.addView(rainbowActivity);
    linearLayout2.addView(button);
    linearLayout.addView(linearLayout2);
  }

  public static class AnonymousClass100000043 implements View.OnClickListener {
    private final FloatingModMenuService this$0;

    public AnonymousClass100000043(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      Intent intent = new Intent("android.intent.action.VIEW");
      intent.setFlags(268435456);
      intent.setData(Uri.parse("https://t.me/DONIMODZ"));
      this.this$0.startActivity(intent);
    }
  }

  public void TeleChannelLink(LinearLayout linearLayout, int i, String str) {
    LinearLayout linearLayout2 = new LinearLayout(this);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(5, 7, 5, 7);
    linearLayout2.setOrientation(0);
    linearLayout2.setGravity(16);
    linearLayout2.setPadding(5, 8, 8, 8);
    linearLayout2.setLayoutParams(layoutParams);
    TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams) layoutParams2).weight = 1.0f;
    LinearLayout linearLayout3 = new LinearLayout(this);
    linearLayout3.setLayoutParams(layoutParams2);
    linearLayout3.setOrientation(1);
    linearLayout3.setGravity(17);
    RainbowActivity rainbowActivity = new RainbowActivity(this);
    rainbowActivity.setTextColor(this.TEXT_COLOR_2);
    rainbowActivity.setText(str);
    rainbowActivity.setTextSize(12.0f);
    rainbowActivity.setGravity(3);
    rainbowActivity.setSingleLine(true);
    rainbowActivity.setPadding(5, 0, 0, 0);
    Button button = new Button(this);
    AddColor(button, Color.parseColor("#AFFF3434"), 3, 0, 0, this.LST_MAB, 30, 30, 0, 0, 30, 30, 0,
        0);
    button.setTextColor(this.TEXT_COLOR_2);
    byte[] decode = Base64.decode(IconTele(), 0);
    button
        .setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(decode, 0, decode.length)));
    RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(75, 75);
    layoutParams3.addRule(11);
    button.setLayoutParams(layoutParams3);
    button.setOnClickListener(new AnonymousClass100000043(this));
    linearLayout2.addView(linearLayout3);
    linearLayout3.addView(rainbowActivity);
    linearLayout2.addView(button);
    linearLayout.addView(linearLayout2);
  }

  public static class AnonymousClass100000044 implements View.OnClickListener {
    private final FloatingModMenuService this$0;
    final RadioButton val$Radioo;
    final int val$featNum;
    final String val$finalfeatName;
    final RadioGroup val$radioGroup;
    final String val$radioName;
    final RainbowActivity val$textView;

    public AnonymousClass100000044(FloatingModMenuService floatingModMenuService,
        RainbowActivity rainbowActivity, String str, String str2, int i, RadioGroup radioGroup,
        RadioButton radioButton) {
      this.this$0 = floatingModMenuService;
      this.val$textView = rainbowActivity;
      this.val$finalfeatName = str;
      this.val$radioName = str2;
      this.val$featNum = i;
      this.val$radioGroup = radioGroup;
      this.val$Radioo = radioButton;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      this.val$textView
          .setText(
              Html.fromHtml(
                  new StringBuffer()
                      .append(new StringBuffer()
                          .append(new StringBuffer()
                              .append(new StringBuffer().append(this.val$finalfeatName)
                                  .append(": <font color='").toString())
                              .append(this.this$0.NumberTxtColor).toString())
                          .append("'>").toString())
                      .append(this.val$radioName).toString()));
      Preferences.changeFeatureInt(this.val$finalfeatName, this.val$featNum,
          this.val$radioGroup.indexOfChild(this.val$Radioo));
    }
  }

  public View RadioButton(int i, String str, String str2) {
    LinkedList linkedList = new LinkedList(Arrays.asList(str2.split(",")));
    RainbowActivity rainbowActivity = new RainbowActivity(this);
    rainbowActivity.setText(new StringBuffer().append(str).append(":").toString());
    rainbowActivity.setTextColor(this.TEXT_COLOR_2);
    RadioGroup radioGroup = new RadioGroup(this);
    radioGroup.setPadding(10, 5, 10, 5);
    radioGroup.setOrientation(1);
    radioGroup.addView(rainbowActivity);
    for (int i2 = 0; i2 < linkedList.size(); i2++) {
      RadioButton radioButton = new RadioButton(this);
      AnonymousClass100000044 anonymousClass100000044 = new AnonymousClass100000044(this,
          rainbowActivity, str, (String) linkedList.get(i2), i, radioGroup, radioButton);
      System.out.println((String) linkedList.get(i2));
      radioButton.setText((String) linkedList.get(i2));
      radioButton.setTextColor(-3355444);
      if (Build.VERSION.SDK_INT >= 21) {
        radioButton.setButtonTintList(ColorStateList.valueOf(this.RadioColor));
      }
      radioButton.setOnClickListener(anonymousClass100000044);
      radioGroup.addView(radioButton);
    }
    int loadPrefInt = Preferences.loadPrefInt(str, i);
    if (loadPrefInt > 0) {
      rainbowActivity
          .setText(
              Html.fromHtml(
                  new StringBuffer()
                      .append(
                          new StringBuffer()
                              .append(
                                  new StringBuffer()
                                      .append(new StringBuffer().append(str)
                                          .append(": <font color='").toString())
                                      .append(this.NumberTxtColor).toString())
                              .append("'>").toString())
                      .append((String) linkedList.get(loadPrefInt - 1)).toString()));
      ((RadioButton) radioGroup.getChildAt(loadPrefInt)).setChecked(true);
    }
    return radioGroup;
  }

  public static class AnonymousClass100000045 implements View.OnClickListener {
    boolean isChecked;
    private final FloatingModMenuService this$0;
    final GradientDrawable val$DAJHEFD;
    final LinearLayout val$collapseSub;
    final String val$text;
    final TextView val$textView;

    public AnonymousClass100000045(FloatingModMenuService floatingModMenuService,
        LinearLayout linearLayout, TextView textView, String str,
        GradientDrawable gradientDrawable) {
      this.this$0 = floatingModMenuService;
      this.val$collapseSub = linearLayout;
      this.val$textView = textView;
      this.val$text = str;
      this.val$DAJHEFD = gradientDrawable;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
      boolean z = !this.isChecked;
      this.isChecked = z;
      if (z) {
        this.val$collapseSub.setVisibility(0);
        this.val$textView.setText(new StringBuffer()
            .append(new StringBuffer().append("△ ").append(this.val$text).toString()).append(" △")
            .toString());
        this.val$DAJHEFD.setStroke(3, Color.parseColor("#3c5afe"));
        return;
      }
      this.val$collapseSub.setVisibility(8);
      this.val$textView.setText(new StringBuffer()
          .append(new StringBuffer().append("▽ ").append(this.val$text).toString()).append(" ▽")
          .toString());
      this.val$DAJHEFD.setStroke(3, Color.parseColor("#3c5afe"));
    }
  }

  public void Collapse(LinearLayout linearLayout, String str) {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(0, 5, 0, 0);
    LinearLayout linearLayout2 = new LinearLayout(this);
    linearLayout2.setLayoutParams(layoutParams);
    linearLayout2.setVerticalGravity(16);
    linearLayout2.setOrientation(1);
    LinearLayout linearLayout3 = new LinearLayout(this);
    linearLayout3.setVerticalGravity(16);
    linearLayout3.setPadding(0, 5, 0, 5);
    linearLayout3.setOrientation(1);
    linearLayout3.setBackgroundColor(0);
    linearLayout3.setVisibility(8);
    GradientDrawable gradientDrawable = new GradientDrawable();
    this.mCollapse = linearLayout3;
    TextView textView = new TextView(this);
    textView.setBackgroundColor(0);
    gradientDrawable.setStroke(3, Color.parseColor("BLUE"));
    gradientDrawable.setCornerRadii(new float[] { 50, 50, 30, 30, 30, 30, 30, 30 });
    linearLayout2.setBackground(gradientDrawable);
    textView.setText(new StringBuffer()
        .append(new StringBuffer().append("▽ ").append(str).toString()).append(" ▽").toString());
    textView.setGravity(17);
    textView.setTextColor(this.TEXT_COLOR_2);
    textView.setTypeface(null, 1);
    textView.setPadding(0, 20, 0, 20);
    textView.setOnClickListener(
        new AnonymousClass100000045(this, linearLayout3, textView, str, gradientDrawable));
    linearLayout2.addView(textView);
    linearLayout2.addView(linearLayout3);
    linearLayout.addView(linearLayout2);
  }

  public View Category(String str) {
    TextView textView = new TextView(this);
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(this.CategoryBG);
    gradientDrawable.setCornerRadii(new float[] { 30, 30, 30, 30, 30, 30, 30, 30 });
    gradientDrawable.setStroke(3, -1);
    textView.setBackground(gradientDrawable);
    textView.setText(Html.fromHtml(str));
    textView.setTypeface(Typeface.createFromAsset(getAssets(), "NoName1.ttf"));
    textView.setGravity(17);
    textView.setTextColor(this.TEXT_COLOR_2);
    textView.setPadding(0, 5, 0, 5);
    return textView;
  }

  public View RichTextView(String str) {
    RainbowActivity rainbowActivity = new RainbowActivity(this);
    rainbowActivity.setText(Html.fromHtml(str));
    rainbowActivity.setTextColor(this.TEXT_COLOR_2);
    rainbowActivity.setPadding(10, 5, 10, 5);
    return rainbowActivity;
  }

  public View RichWebView(String str) {
    WebView webView = new WebView(this);
    webView.loadData(str, "text/html", "utf-8");
    webView.setBackgroundColor(0);
    webView.setPadding(0, 5, 0, 5);
    return webView;
  }

  @Override // android.app.Service
  public int onStartCommand(Intent intent, int i, int i2) {
    return 2;
  }

  public boolean isViewCollapsed() {
    return this.rootFrame == null || this.mCollapsed.getVisibility() == 0;
  }

  public int convertDipToPixels(int i) {
    return (int) ((i * getResources().getDisplayMetrics().density) + 0.5f);
  }

  public int dp(int i) {
    return (int) TypedValue.applyDimension(1, i, getResources().getDisplayMetrics());
  }

  public boolean isNotInGame() {
    ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
    ActivityManager.getMyMemoryState(runningAppProcessInfo);
    return runningAppProcessInfo.importance != 100;
  }

  @Override // android.app.Service
  public void onDestroy() {
    super.onDestroy();
    if (this.rootFrame != null) {
      this.mWindowManager.removeView(this.rootFrame);
      return;
    }
  }

  @Override // android.app.Service
  public void onTaskRemoved(Intent intent) {
    super.onTaskRemoved(intent);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopSelf();
  }

  public void Thread() {
    if (this.rootFrame != null) {
      if (isNotInGame()) {
        this.rootFrame.setVisibility(0);
        return;
      } else {
        this.rootFrame.setVisibility(0);
        return;
      }
    }
  }

  public static class EditTextString {
    private String text;
    private final FloatingModMenuService this$0;

    public void setString(String str) {
      this.text = str;

    }

    public String getString() {
      return this.text;
    }

    public EditTextString(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }
  }

  public static class EditTextNum {
    private final FloatingModMenuService this$0;
    private int val;

    public void setNum(int i) {
      this.val = i;

    }

    public int getNum() {
      return this.val;
    }

    public EditTextNum(FloatingModMenuService floatingModMenuService) {
      this.this$0 = floatingModMenuService;
    }
  }

  @Override // android.app.Service
  public IBinder onBind(Intent intent) {
    return null;
  }

  public FloatingModMenuService() {
    this.TEXT_COLOR = Color.parseColor("#FFFFFF");
    this.TEXT_COLOR_2 = Color.parseColor("#FFFFFF");
    this.BTN_COLOR = Color.parseColor("BLUE");
    this.MENU_BG_COLOR = Color.parseColor("#4AFF0000");
    this.MENU_FEATURE_BG_COLOR = Color.parseColor("#00141C22");
    this.MENU_WIDTH = 255;
    this.MENU_HEIGHT = 170;
    this.MENU_CORNER = 4.0f;
    this.ICON_SIZE = 65;
    this.ICON_ALPHA = 0.7f;
    this.ToggleON = -16711936;
    this.ToggleOFF = -65536;
    this.BtnON = Color.parseColor("#1b5e20");
    this.BtnOFF = Color.parseColor("#7f0000");
    this.CategoryBG = Color.parseColor("BLUE");
    this.SeekBarColor = Color.parseColor("BLUE");
    this.SeekBarProgressColor = Color.parseColor("BLUE");
    this.CheckBoxColor = Color.parseColor("BLUE");
    this.RadioColor = Color.parseColor("BLUE");
    this.NumberTxtColor = "#FFFFFF";
    this.LST_MAB = Color.parseColor("#770000FF");
    this.TAB_STROKE_COLOR = Color.parseColor("#FFFFFF");
    this.TAB_TEXT_COLOR = Color.parseColor("#FFFFFF");
    this.TAB_TEXT_SIZE = 14.0f;
    this.CI = 1;
    this.yen = 4;
    this.vxy = 3;
  }
}