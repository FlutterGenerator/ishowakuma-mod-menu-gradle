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
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
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

  private static final int TAB_COUNT = 8;
  private static final String[] TAB_TITLES = { "Libray\nSelect", "Patch\nOffset", "Hook\nOffset",
      "Feild\nOffset", "Hook\nVoid", "Disable\nVoid", "Call\nVoid", "Eazy\nPatch" };

  // ───────────── Цвета и размеры ─────────────
  int TEXT_COLOR = Color.parseColor("#FFFFFF");
  int TEXT_COLOR_2 = Color.parseColor("#FFFFFF");
  int BTN_COLOR = Color.parseColor("BLUE");
  int MENU_BG_COLOR = Color.parseColor("#4AFF0000");
  int MENU_FEATURE_BG_COLOR = Color.parseColor("#00141C22");
  int MENU_WIDTH = 255;
  int MENU_HEIGHT = 170;
  float MENU_CORNER = 4.0f;
  int ICON_SIZE = 65;
  float ICON_ALPHA = 0.7f;
  int CategoryBG = Color.parseColor("BLUE");
  int SeekBarColor = Color.parseColor("BLUE");
  int SeekBarProgressColor = Color.parseColor("BLUE");
  int CheckBoxColor = Color.parseColor("BLUE");
  int RadioColor = Color.parseColor("BLUE");
  String NumberTxtColor = "#FFFFFF";
  int LST_MAB = Color.parseColor("#770000FF");
  int TAB_STROKE_COLOR = Color.parseColor("#FFFFFF");
  int TAB_TEXT_COLOR = Color.parseColor("#FFFFFF");
  float TAB_TEXT_SIZE = 14.0f;

  // ───────────── Views ─────────────
  FrameLayout rootFrame;
  RelativeLayout mRootContainer;
  RelativeLayout mCollapsed;
  LinearLayout mExpanded;
  LinearLayout subLayout;
  LinearLayout tabsPanel;
  LinearLayout mSettings;
  LinearLayout mCollapse;
  ScrollView settingsScroll;
  ScrollView scrollView;
  ImageView startimage;
  Button hideBtn;
  Button closeBtn;
  TextView timeDateView;
  BitmapDrawable blackgamer;
  LinearLayout.LayoutParams scrlLL;
  LinearLayout.LayoutParams scrlLLExpanded;

  final TextView[] tabs = new TextView[TAB_COUNT];
  final LinearLayout[] patches = new LinearLayout[TAB_COUNT];

  WindowManager mWindowManager;
  WindowManager.LayoutParams params;

  private Handler timeHandler;
  private Runnable updateTimeRunnable;
  private boolean settingsOpen = false;
  boolean stopChecking;

  // ───────────── Native ─────────────
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

  // ───────────── Вспомогательные классы ─────────────

  /** Колбэк для SeekBar, где нужен только onProgressChanged. */
  private interface ProgressCallback {
    void onChanged(SeekBar seekBar, int progress);
  }

  private static class ProgressListener implements SeekBar.OnSeekBarChangeListener {
    private final ProgressCallback callback;

    ProgressListener(ProgressCallback callback) {
      this.callback = callback;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      callback.onChanged(seekBar, progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
  }

  /** Колбэк подтверждения в диалогах ввода. */
  private interface InputCallback {
    void onConfirm(String text);
  }

  public static class EditTextString {
    private String text;

    public void setString(String text) {
      this.text = text;
    }

    public String getString() {
      return text;
    }
  }

  public static class EditTextNum {
    private int value;

    public void setNum(int value) {
      this.value = value;
    }

    public int getNum() {
      return value;
    }
  }

  /** Перетаскивание меню. */
  private class DragTouchListener implements View.OnTouchListener {
    private float initialTouchX;
    private float initialTouchY;
    private int initialX;
    private int initialY;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          initialX = params.x;
          initialY = params.y;
          initialTouchX = event.getRawX();
          initialTouchY = event.getRawY();
          return true;

        case MotionEvent.ACTION_UP:
          int dx = (int) (event.getRawX() - initialTouchX);
          int dy = (int) (event.getRawY() - initialTouchY);
          mExpanded.setAlpha(1.0f);
          mCollapsed.setAlpha(1.0f);
          if (dx < 10 && dy < 10 && isViewCollapsed()) {
            mCollapsed.setVisibility(View.GONE);
            mExpanded.setVisibility(View.VISIBLE);
          }
          return true;

        case MotionEvent.ACTION_MOVE:
          mExpanded.setAlpha(0.5f);
          mCollapsed.setAlpha(0.5f);
          params.x = initialX + (int) (event.getRawX() - initialTouchX);
          params.y = initialY + (int) (event.getRawY() - initialTouchY);
          mWindowManager.updateViewLayout(rootFrame, params);
          return true;

        default:
          return false;
      }
    }
  }

  /** Подгрузка списков функций, пока не загрузится библиотека игры. */
  private class LoadFeaturesTask implements Runnable {
    private final Handler handler;
    private boolean viewLoaded = false;

    LoadFeaturesTask(Handler handler) {
      this.handler = handler;
    }

    @Override
    public void run() {
      if (Preferences.loadPref && !isGameLibLoaded() && !stopChecking) {
        if (!viewLoaded) {
          for (int i = 1; i < TAB_COUNT; i++) {
            featureList(getFeatureListFor(i), patches[i]);
          }
          patches[0].addView(ColorPicker(-1, "Color Picker", 255, 255, 255, 255));
          patches[0].addView(
              Category("Save preferences was been enabled. Waiting for game lib to be loaded...\n\n"
                  + "Force load menu may not apply mods instantly. "
                  + "You would need to reactivate them again"));
          patches[0].addView(Button(-100, "Force load menu"));
          viewLoaded = true;
        }
        handler.postDelayed(this, 600);
        return;
      }
      patches[0].removeAllViews();
      featureList(getFeatureList(), patches[0]);
    }
  }

  /** Обновление даты и времени раз в секунду. */
  private class TimeUpdater implements Runnable {
    @Override
    public void run() {
      Date now = new Date();
      String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(now);
      String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(now);
      timeDateView.setText("🗓 DATE : " + date + " 🗓\n🕒 TIME : " + time + " 🕒");
      timeHandler.postDelayed(this, 1000);
    }
  }

  /** Слушатель выбора в Spinner. */
  private class SpinnerSelectionListener implements AdapterView.OnItemSelectedListener {
    private final Spinner spinner;
    private final int featNum;

    SpinnerSelectionListener(Spinner spinner, int featNum) {
      this.spinner = spinner;
      this.featNum = featNum;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      Preferences.changeFeatureInt(spinner.getSelectedItem().toString(), featNum, position);
      ((TextView) parent.getChildAt(0)).setTextColor(TEXT_COLOR_2);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
  }

  // ───────────── Утилиты ─────────────

  private int overlayType() {
    return Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        : WindowManager.LayoutParams.TYPE_PHONE;
  }

  public int convertDipToPixels(int dip) {
    return (int) ((dip * getResources().getDisplayMetrics().density) + 0.5f);
  }

  public int dp(int value) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
        getResources().getDisplayMetrics());
  }

  private Bitmap decodeBase64(String base64) {
    byte[] bytes = Base64.decode(base64, 0);
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
  }

  private BitmapDrawable drawableFromBase64(String base64) {
    return new BitmapDrawable(decodeBase64(base64));
  }

  private static String toHex(int a, int r, int g, int b) {
    return String.format("#%02x%02x%02x%02x", a, r, g, b).toUpperCase();
  }

  private Spanned html(String text) {
    return Html.fromHtml(text);
  }

  /** "Имя -> <font color>значение" */
  private Spanned coloredValue(String name, String value) {
    return html(name + " -> <font color='" + NumberTxtColor + "'>" + value);
  }

  private void openUrl(String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setData(Uri.parse(url));
    startActivity(intent);
  }

  private void toggleKeyboard(int showFlags, int hideFlags) {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(showFlags, hideFlags);
  }

  private AlertDialog createOverlayDialog() {
    AlertDialog dialog = new AlertDialog.Builder(getApplicationContext(),
        AlertDialog.THEME_HOLO_DARK).create();
    if (Build.VERSION.SDK_INT >= 19) {
      Objects.requireNonNull(dialog.getWindow()).setType(overlayType());
    }
    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface d) {
        toggleKeyboard(InputMethodManager.SHOW_IMPLICIT, 0);
      }
    });
    return dialog;
  }

  private GradientDrawable roundedButtonBackground(int color, int strokeWidth) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setColor(color);
    drawable.setStroke(strokeWidth, Color.WHITE);
    drawable.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    return drawable;
  }

  public void AddColor(View view, int color, float radius, int strokeWidth, int strokeColor) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setColor(color);
    drawable.setCornerRadius(radius);
    drawable.setStroke(strokeWidth, strokeColor);
    view.setBackgroundDrawable(drawable);
  }

  public void AddColor(View view, int color, int strokeWidth, int dashWidth, int dashGap,
      int strokeColor, int r1, int r2, int r3, int r4, int r5, int r6, int r7, int r8) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setColor(color);
    drawable.setCornerRadii(new float[] { r1, r2, r3, r4, r5, r6, r7, r8 });
    drawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap);
    view.setBackgroundDrawable(drawable);
  }

  private String[] getFeatureListFor(int tabIndex) {
    switch (tabIndex) {
      case 0:
        return getFeatureList();
      case 1:
        return getFeatureList2();
      case 2:
        return getFeatureList3();
      case 3:
        return getFeatureList4();
      case 4:
        return getFeatureList5();
      case 5:
        return getFeatureList6();
      case 6:
        return getFeatureList7();
      default:
        return getFeatureList8();
    }
  }

  /** Шрифт из assets; если ассета нет в APK игры - обычный шрифт вместо краша. */
  private Typeface safeFont(String name) {
    try {
      return Typeface.createFromAsset(getAssets(), name);
    } catch (Throwable t) {
      Log.w(TAG, "Font not found: " + name);
      return Typeface.DEFAULT_BOLD;
    }
  }

  // ───────────── Жизненный цикл ─────────────

  @Override
  public void onCreate() {
    super.onCreate();

    Preferences.context = this;

    initFloating();

    final Handler handler = new Handler();

    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        updateVisibility();

        handler.postDelayed(new Runnable() {
          @Override
          public void run() {
            updateVisibility();
          }
        }, 1000);
      }
    }, 1000);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_NOT_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (rootFrame != null) {
      mWindowManager.removeView(rootFrame);
    }
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    super.onTaskRemoved(rootIntent);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopSelf();
  }

  private void updateVisibility() {
    if (rootFrame != null) {
      rootFrame.setVisibility(View.VISIBLE);
    }
  }

  public boolean isViewCollapsed() {
    return rootFrame == null || mCollapsed.getVisibility() == View.VISIBLE;
  }

  public boolean isNotInGame() {
    ActivityManager.RunningAppProcessInfo info = new ActivityManager.RunningAppProcessInfo();
    ActivityManager.getMyMemoryState(info);
    return info.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
  }

  // ───────────── Построение меню ─────────────

  public void initFloating() {
    Typeface titleFont = safeFont("NoName2.ttf");
    Typeface tabFont = safeFont("NoName3.ttf");

    rootFrame = new FrameLayout(this);
    rootFrame.setOnTouchListener(onTouchListener());
    mRootContainer = new RelativeLayout(this);

    mCollapsed = new RelativeLayout(this);
    mCollapsed.setVisibility(View.VISIBLE);
    mCollapsed.setAlpha(ICON_ALPHA);

    mExpanded = new LinearLayout(this);
    mExpanded.setVisibility(View.GONE);
    mExpanded.setOrientation(LinearLayout.VERTICAL);
    mExpanded.setLayoutParams(
        new LinearLayout.LayoutParams(dp(MENU_WIDTH), ViewGroup.LayoutParams.WRAP_CONTENT));
    blackgamer = drawableFromBase64(Background());
    mExpanded.setBackground(blackgamer);

    buildIcon();

    subLayout = new LinearLayout(this);
    subLayout.setOrientation(LinearLayout.VERTICAL);
    subLayout.setPadding(5, 5, 5, 5);

    TextView telegramIcon = buildTelegramIcon();
    buildSettingsPanel();

    RelativeLayout header = buildHeader(titleFont, telegramIcon);
    RainbowActivity marquee = buildMarquee();

    buildTabs(tabFont);
    buildContentArea();
    RelativeLayout buttonsRow = buildBottomButtons();

    params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, overlayType(),
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, android.graphics.PixelFormat.TRANSLUCENT);
    params.gravity = Gravity.TOP | Gravity.LEFT;
    params.x = 0;
    params.y = 100;

    rootFrame.addView(mRootContainer);
    mRootContainer.addView(mCollapsed);
    mRootContainer.addView(mExpanded);
    mCollapsed.addView(startimage);

    mExpanded.addView(subLayout);
    subLayout.addView(header);
    subLayout.addView(marquee);
    subLayout.addView(tabsPanel);

    mExpanded.addView(settingsScroll);
    scrollView.addView(patches[0]);
    mExpanded.addView(scrollView);
    mExpanded.addView(buttonsRow);

    mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    mWindowManager.addView(rootFrame, params);

    Handler handler = new Handler();
    handler.postDelayed(new LoadFeaturesTask(handler), 500);
  }

  private void buildIcon() {
    startimage = new ImageView(this);
    startimage.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
    int size = dp(ICON_SIZE);
    startimage.getLayoutParams().height = size;
    startimage.getLayoutParams().width = size;
    startimage.setScaleType(ImageView.ScaleType.FIT_XY);
    startimage.setImageBitmap(decodeBase64(Icon()));
    ((ViewGroup.MarginLayoutParams) startimage.getLayoutParams()).topMargin = convertDipToPixels(
        10);
	  startimage.setOnTouchListener(onTouchListener());

	  startimage.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  mCollapsed.setVisibility(View.GONE);
				  mExpanded.setVisibility(View.VISIBLE);
			  }
		  });
  }

	private TextView buildTelegramIcon() {
		TextView icon = new TextView(this);

		icon.setText(Build.VERSION.SDK_INT >= 23 ? "☹︎" : "☻︎︎");
		icon.setTextColor(TEXT_COLOR);
		icon.setTypeface(Typeface.DEFAULT_BOLD);
		icon.setTextSize(25.0f);
		icon.setShadowLayer(7.0f, 0.0f, 0.0f, Color.BLACK);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
		);

		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		icon.setLayoutParams(lp);

		icon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					openUrl("https://t.me/sbgamingytgroup");
				}
			});

		return icon;
	}

  private void buildSettingsPanel() {
    settingsScroll = new ScrollView(this);
    settingsScroll.setVisibility(View.GONE);
    settingsScroll.setLayoutParams(
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(220)));
    mSettings = new LinearLayout(this);
    mSettings.setOrientation(LinearLayout.VERTICAL);
    featureList(settingsList(), mSettings);
    settingsScroll.addView(mSettings);
  }

  private RelativeLayout buildHeader(Typeface titleFont, TextView telegramIcon) {
    RelativeLayout header = new RelativeLayout(this);
    header.setPadding(10, 5, 10, 5);
    header.setVerticalGravity(Gravity.CENTER_VERTICAL);

    RainbowActivity title = new RainbowActivity(this);
    title.setTextColor(TEXT_COLOR);
    title.setTextSize(22.0f);
    title.setTypeface(titleFont);
    title.setShadowLayer(7.0f, 0.0f, 0.0f, Color.BLACK);
    title.setGravity(Gravity.CENTER);
    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
    lp.addRule(RelativeLayout.CENTER_VERTICAL);
    title.setLayoutParams(lp);
    setTitleText(title);

    header.addView(title);
    header.addView(telegramIcon);
    return header;
  }

  private RainbowActivity buildMarquee() {
    RainbowActivity marquee = new RainbowActivity(this);
    marquee.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
    marquee.setMarqueeRepeatLimit(-1);
    marquee.setSingleLine(true);
    marquee.setSelected(true);
    marquee.setTextColor(TEXT_COLOR);
    marquee.setBackgroundColor(LST_MAB);
    marquee.setTextSize(10.0f);
    marquee.setGravity(Gravity.CENTER);
    marquee.setPadding(0, 0, 0, 5);
    setHeadingText(marquee);
    return marquee;
  }

  private void buildTabs(Typeface tabFont) {
    LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    separatorParams.setMargins(5, 2, 5, 2);

    LinearLayout.LayoutParams tabParams = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    tabParams.setMargins(-2, 5, -2, 5);

	  LinearLayout row = new LinearLayout(this);
	  row.setOrientation(LinearLayout.HORIZONTAL);

	  for (int i = 0; i < TAB_COUNT; i++) {
		  row.addView(createSeparator(separatorParams));

		  final int index = i;

		  TextView tab = new TextView(this);
		  tab.setLayoutParams(tabParams);
		  tab.setText(TAB_TITLES[i]);
		  tab.setGravity(Gravity.CENTER);
		  tab.setTextColor(TAB_TEXT_COLOR);
		  tab.setTextSize(TAB_TEXT_SIZE);
		  tab.setPadding(30, 15, 30, 15);
		  tab.setShadowLayer(7.0f, 0.0f, 0.0f, Color.BLACK);
		  tab.setTypeface(tabFont, Typeface.BOLD);

		  tab.setOnClickListener(new View.OnClickListener() {
				  @Override
				  public void onClick(View view) {
					  selectTab(index);
				  }
			  });

		  tabs[i] = tab;
		  row.addView(tab);
	  }
    row.addView(createSeparator(separatorParams));

    HorizontalScrollView tabsScroll = new HorizontalScrollView(this);
    tabsScroll.setEnabled(true);
    tabsScroll.setHorizontalScrollBarEnabled(false);
    tabsScroll.addView(row);

    tabsPanel = new LinearLayout(this);
    tabsPanel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
    tabsPanel.setOrientation(LinearLayout.VERTICAL);
    tabsPanel.setPadding(3, 0, 3, 0);
    tabsPanel.addView(createDivider());
    tabsPanel.addView(tabsScroll);
    tabsPanel.addView(createDivider());

    updateTabStyles(0);
  }

  private LinearLayout createSeparator(LinearLayout.LayoutParams lp) {
    LinearLayout separator = new LinearLayout(this);
    separator.setBackgroundColor(TAB_STROKE_COLOR);
    separator.setPadding(2, 5, 1, 5);
    separator.setLayoutParams(lp);
    return separator;
  }

  private LinearLayout createDivider() {
    LinearLayout divider = new LinearLayout(this);
    divider.setBackgroundColor(TAB_STROKE_COLOR);
    divider.setPadding(-1, 2, -1, 1);
    return divider;
  }

  private void selectTab(int index) {
    scrollView.removeAllViews();
    scrollView.addView(patches[index]);
    updateTabStyles(index);
  }

  private void updateTabStyles(int selected) {
    for (int i = 0; i < TAB_COUNT; i++) {
      if (i == selected) {
        AddColor(tabs[i], LST_MAB, 0, 3, TAB_STROKE_COLOR);
      } else {
        AddColor(tabs[i], 0, 0, 0, TAB_STROKE_COLOR);
      }
    }
  }

  private void buildContentArea() {
    scrollView = new ScrollView(this);
    scrlLL = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(MENU_HEIGHT));
    scrlLLExpanded = new LinearLayout.LayoutParams(mExpanded.getLayoutParams());
    scrlLLExpanded.weight = 1.0f;
    scrollView.setLayoutParams(Preferences.isExpanded ? scrlLLExpanded : scrlLL);
    scrollView.setBackgroundColor(MENU_FEATURE_BG_COLOR);

    for (int i = 0; i < TAB_COUNT; i++) {
      patches[i] = new LinearLayout(this);
      patches[i].setOrientation(LinearLayout.VERTICAL);
      patches[i].setPadding(3, 0, 3, 0);
      if (i > 0) {
        featureList(getFeatureListFor(i), patches[i]);
      }
    }
  }

	private RelativeLayout buildBottomButtons() {
		RelativeLayout row = new RelativeLayout(this);
		row.setPadding(0, 0, 0, 0);
		row.setVerticalGravity(Gravity.CENTER);

		// SETTINGS button
		RelativeLayout.LayoutParams settingsParams =
			new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );

		settingsParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		hideBtn = new Button(this);
		hideBtn.setLayoutParams(settingsParams);
		hideBtn.setText("SETTINGS");

		GradientDrawable settingsBg = new GradientDrawable();
		settingsBg.setColor(BTN_COLOR);
		settingsBg.setStroke(3, Color.WHITE);
		settingsBg.setCornerRadii(new float[] {
									  0, 0, 30, 30, 0, 0, 0, 0
								  });

		hideBtn.setBackground(settingsBg);
		hideBtn.setTextColor(TEXT_COLOR);

		hideBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					toggleSettings();
				}
			});

		// MINIMIZE button
		RelativeLayout.LayoutParams closeParams =
			new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );

		closeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		closeBtn = new Button(this);
		closeBtn.setLayoutParams(closeParams);

		GradientDrawable closeBg = new GradientDrawable();
		closeBg.setColor(BTN_COLOR);
		closeBg.setStroke(3, Color.WHITE);
		closeBg.setCornerRadii(new float[] {
								   30, 30, 0, 0, 0, 0, 0, 0
							   });

		closeBtn.setBackground(closeBg);
		closeBtn.setText("MINIMIZE");
		closeBtn.setTextColor(TEXT_COLOR);

		closeBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mCollapsed.setVisibility(View.VISIBLE);
					mCollapsed.setAlpha(ICON_ALPHA);
					mExpanded.setVisibility(View.GONE);
				}
			});

		// Add buttons to layout
		row.addView(hideBtn);
		row.addView(closeBtn);

		return row;
	}

  private void toggleSettings() {
    settingsOpen = !settingsOpen;
    if (settingsOpen) {
      hideBtn.setText("CLOSE");
      settingsScroll.setVisibility(View.VISIBLE);
      tabsPanel.setVisibility(View.GONE);
      scrollView.setVisibility(View.GONE);
    } else {
      hideBtn.setText("SETTINGS");
      settingsScroll.setVisibility(View.GONE);
      tabsPanel.setVisibility(View.VISIBLE);
      scrollView.setVisibility(View.VISIBLE);
    }
  }

  public View.OnTouchListener onTouchListener() {
    return new DragTouchListener();
  }

  // ───────────── Разбор списка функций ─────────────

  public void featureList(String[] features, LinearLayout parent) {
    int offset = 0;
    for (int index = 0; index < features.length; index++) {
      boolean enabled = false;
      String feature = features[index];

      if (feature.contains("True_")) {
        enabled = true;
        feature = feature.replaceFirst("True_", "");
      }

      LinearLayout target = parent;
      if (feature.contains("CollapseAdd_")) {
        target = mCollapse;
        feature = feature.replaceFirst("CollapseAdd_", "");
      }

      String[] first = feature.split("_");
      int featNum;
      if (TextUtils.isDigitsOnly(first[0]) || first[0].matches("-[0-9]*")) {
        featNum = Integer.parseInt(first[0]);
        feature = feature.replaceFirst(first[0] + "_", "");
        offset++;
      } else {
        featNum = index - offset;
      }

      String[] args = feature.split("_");
      switch (args[0]) {
        case "Toggle":
          target.addView(Switch(featNum, args[1], enabled));
          break;
        case "SeekBar":
          target.addView(
              SeekBar(featNum, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3])));
          break;
        case "Button":
          target.addView(Button(featNum, args[1]));
          break;
        case "OffsetFind":
          target.addView(OffsetMethod(featNum, args[1]));
          break;
        case "ButtonOnOff":
          target.addView(ButtonOnOff(featNum, args[1], enabled));
          break;
        case "Spinner":
          target.addView(RichTextView(args[1]));
          target.addView(Spinner(featNum, args[1], args[2]));
          break;
        case "InputText":
          InputText(target, featNum, args[1]);
          break;
        case "InputValue":
          if (args.length == 3) {
            InputNum(target, featNum, args[2], Integer.parseInt(args[1]));
          }
          if (args.length == 2) {
            InputNum(target, featNum, args[1], 0);
          }
          break;
        case "CheckBox":
          target.addView(CheckBox(featNum, args[1], enabled));
          break;
        case "RadioButton":
          target.addView(RadioButton(featNum, args[1], args[2]));
          break;
        case "Collapse":
          Collapse(target, args[1]);
          offset++;
          break;
        case "ButtonLink":
          offset++;
          target.addView(ButtonLink(args[1], args[2]));
          break;
        case "Category":
          offset++;
          target.addView(Category(args[1]));
          break;
        case "RichTextView":
          offset++;
          target.addView(RichTextView(args[1]));
          break;
        case "RichWebView":
          offset++;
          target.addView(RichWebView(args[1]));
          break;
        case "RichTimeDate":
          target.addView(RichTimeDate(args[1]));
          break;
        case "ColorPicker":
          target.addView(ColorPicker(featNum, args[1], Integer.parseInt(args[2]),
              Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])));
          break;
        case "YoutubeLink":
          offset++;
          YoutubeLink(target, featNum, args[1]);
          break;
        case "TeleGroupLink":
          offset++;
          TeleGroupLink(target, featNum, args[1]);
          break;
        case "TeleChannelLink":
          offset++;
          TeleChannelLink(target, featNum, args[1]);
          break;
        case "HrzBttn":
          offset++;
          HorizontalButton(target, featNum, args[1]);
          break;
        default:
          break;
      }
    }
  }

  // ───────────── Дата и время ─────────────

  public View RichTimeDate(String text) {
    timeDateView = new TextView(this);
    GradientDrawable background = new GradientDrawable();
    background.setColor(0);
    background.setStroke(3, Color.parseColor("#FFFFFFFF"));
    background.setCornerRadii(new float[] { 77, 77, 89, 89, 80, 80, 76, 76 });
    timeDateView.setGravity(Gravity.CENTER);
    timeDateView.setTextSize(13);
    timeDateView.setTextColor(Color.WHITE);
    timeDateView.setBackground(background);

    timeHandler = new Handler();
    updateTimeRunnable = new TimeUpdater();
    timeHandler.post(updateTimeRunnable);
    return timeDateView;
  }

  // ───────────── Color Picker ─────────────

  public View ColorPicker(int featNum, final String name, int a, int r, int g, int b) {
    LinearLayout root = new LinearLayout(this);
    LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    rootParams.setMargins(20, 10, 20, 10);
    root.setOrientation(LinearLayout.HORIZONTAL);
    root.setGravity(Gravity.CENTER_VERTICAL);
    root.setPadding(10, 8, 8, 8);
    GradientDrawable rootBg = new GradientDrawable();
    rootBg.setColor(Color.argb(0, 0, 0, 0));
    rootBg.setCornerRadii(new float[] { 60, 60, 0, 0, 60, 60, 0, 0 });
    rootBg.setStroke(4, Color.WHITE, 5, 5);
    root.setBackground(rootBg);
    root.setLayoutParams(rootParams);

    LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    leftParams.weight = 1.0f;
    LinearLayout left = new LinearLayout(this);
    left.setLayoutParams(leftParams);
    left.setGravity(Gravity.START);

    LinearLayout column = new LinearLayout(this);
    column.setOrientation(LinearLayout.VERTICAL);

    TextView title = new TextView(this);
    title.setTextColor(Color.WHITE);
    title.setText(name);
    title.setTextSize(14.0f);
    title.setGravity(Gravity.LEFT);
    title.setPadding(20, 0, 10, 0);

    LinearLayout previewRow = new LinearLayout(this);
    previewRow.setOrientation(LinearLayout.HORIZONTAL);
    previewRow.setPadding(20, 5, 0, 5);
    previewRow.setGravity(Gravity.CENTER_VERTICAL);

    final View colorView = new View(this);
    colorView.setLayoutParams(new LinearLayout.LayoutParams(dp(20), dp(20)));
    colorView.setBackgroundColor(Color.argb(a, r, g, b));

    final TextView colorText = new TextView(this);
    colorText.setText(toHex(a, r, g, b));
    colorText.setTextColor(Color.WHITE);
    colorText.setTextSize(14.0f);
    colorText.setGravity(Gravity.LEFT);
    colorText.setPadding(5, 0, 0, 0);

    Button pickButton = new Button(this);
    pickButton.setText("Pick");
    GradientDrawable pickBg = new GradientDrawable();
    pickBg.setColor(Color.BLACK);
    pickBg.setCornerRadii(new float[] { 60, 60, 0, 0, 60, 60, 0, 0 });
    pickBg.setStroke(3, Color.WHITE, 0, 0);
    pickButton.setBackground(pickBg);
    pickButton.setTextColor(Color.WHITE);

	  final int[] initialProgress = { a, r, g, b };

	  pickButton.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  showColorPickerDialog(
					  name,
					  initialProgress,
					  colorView,
					  colorText
				  );
			  }
		  });

	  root.addView(left);
	  left.addView(column);
	  column.addView(title);
	  column.addView(previewRow);
	  previewRow.addView(colorView);
	  previewRow.addView(colorText);
	  root.addView(pickButton);

	  return root;
  }
  private void showColorPickerDialog(String featName, final int[] initialProgress, final View colorView,
      final TextView colorText) {
    final AlertDialog dialog = createOverlayDialog();

    LinearLayout content = new LinearLayout(getApplicationContext());
    content.setPadding(5, 5, 5, 5);
    content.setOrientation(LinearLayout.VERTICAL);
    GradientDrawable contentBg = new GradientDrawable();
    contentBg.setColor(Color.BLACK);
    contentBg.setStroke(3, Color.WHITE);
    content.setBackground(contentBg);

    TextView title = new TextView(getApplicationContext());
    title.setText(html("<u>" + featName + "</u>"));
    title.setGravity(Gravity.CENTER);
    title.setTextColor(Color.WHITE);
    title.setTextSize(22.0f);
    title.setTypeface(Typeface.DEFAULT_BOLD);

    LinearLayout previewRow = new LinearLayout(getApplicationContext());
    previewRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
    previewRow.setGravity(Gravity.CENTER_VERTICAL);
    previewRow.setPadding(15, 10, 15, 10);
    previewRow.setOrientation(LinearLayout.HORIZONTAL);

    final View previewColor = new View(getApplicationContext());
    previewColor.setLayoutParams(new LinearLayout.LayoutParams(dp(20), dp(20)));
    previewColor.setBackgroundColor(
        Color.argb(initialProgress[0], initialProgress[1], initialProgress[2], initialProgress[3]));

    final TextView previewText = new TextView(getApplicationContext());
    previewText.setText(
        toHex(initialProgress[0], initialProgress[1], initialProgress[2], initialProgress[3]));
    previewText.setTextColor(Color.WHITE);
    previewText.setPadding(5, 0, 0, 0);
    previewText.setTextSize(20.0f);

    LinearLayout seekBarsLayout = new LinearLayout(getApplicationContext());
    seekBarsLayout.setLayoutParams(new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    seekBarsLayout.setPadding(10, 0, 0, 0);
    seekBarsLayout.setOrientation(LinearLayout.VERTICAL);

    final String[] labels = { "Transparency", "Red", "Green", "Blue" };
    int[] channelColors = { Color.WHITE, Color.RED, Color.GREEN, Color.BLUE };
    final SeekBar[] seekBars = new SeekBar[4];

    for (int i = 0; i < 4; i++) {
      final int channel = i;

      LinearLayout seekRow = new LinearLayout(getApplicationContext());
      seekRow.setOrientation(LinearLayout.VERTICAL);
      seekRow.setGravity(Gravity.CENTER);

      final TextView label = new TextView(getApplicationContext());
      label.setText(labels[i] + " (" + initialProgress[i] + ") ");
      label.setTextColor(channelColors[i]);
      label.setGravity(Gravity.START);
      label.setPadding(10, 0, 0, 0);

		SeekBar seekBar = new SeekBar(getApplicationContext());

		seekBar.setLayoutParams(
			new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT
			)
		);

		seekBar.setMax(255);
		seekBar.setProgress(initialProgress[i]);

		seekBar.getProgressDrawable().setColorFilter(
			new PorterDuffColorFilter(channelColors[i], PorterDuff.Mode.MULTIPLY)
		);

		seekBar.getThumb().setColorFilter(
			channelColors[i],
			PorterDuff.Mode.SRC_IN
		);

		seekBar.setOnSeekBarChangeListener(
			new ProgressListener(new ProgressCallback() {
					@Override
					public void onChanged(SeekBar bar, int progress) {
						int pa = seekBars[0].getProgress();
						int pr = seekBars[1].getProgress();
						int pg = seekBars[2].getProgress();
						int pb = seekBars[3].getProgress();

						previewColor.setBackgroundColor(
							Color.argb(pa, pr, pg, pb)
						);

						previewText.setText(
							toHex(pa, pr, pg, pb)
						);

						label.setText(
							labels[channel] + " (" + progress + ") "
						);
					}
				})
		);

		seekRow.addView(label);
		seekRow.addView(seekBar);
		seekBarsLayout.addView(seekRow);

		seekBars[i] = seekBar;
		}
		
		LinearLayout.LayoutParams buttonParams =
			new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
		);

		buttonParams.setMargins(20, 10, 20, 15);

		Button setButton = new Button(getApplicationContext());
		setButton.setLayoutParams(buttonParams);

		GradientDrawable setBg = new GradientDrawable();
		setBg.setColor(Color.argb(40, 0, 0, 0));
		setBg.setCornerRadii(new float[] {
								 60, 60, 0, 0, 60, 60, 0, 0
							 });
		setBg.setStroke(3, Color.WHITE, 0, 0);

		setButton.setBackground(setBg);
		setButton.setTextColor(Color.WHITE);
		setButton.setPadding(15, 10, 15, 10);
		setButton.setText("SET VALUE");

		setButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					for (int i = 0; i < 4; i++) {
						initialProgress[i] = seekBars[i].getProgress();
					}

					int pa = initialProgress[0];
					int pr = initialProgress[1];
					int pg = initialProgress[2];
					int pb = initialProgress[3];

					int argb = Color.argb(pa, pr, pg, pb);

					colorView.setBackgroundColor(argb);
					colorText.setText(toHex(pa, pr, pg, pb));

					dialog.dismiss();

					applyMenuColor(argb);
				}
			});

		content.addView(title);
		content.addView(previewRow);

		previewRow.addView(previewColor);
		previewRow.addView(previewText);

		content.addView(seekBarsLayout);
		content.addView(setButton);

		dialog.setView(content);
		dialog.show();
		}
		
		
  private void applyMenuColor(int argb) {
    mExpanded.setBackgroundColor(argb);

    closeBtn.setBackgroundColor(argb);
    GradientDrawable closeBg = new GradientDrawable();
    closeBg.setStroke(3, Color.WHITE);
    closeBg.setCornerRadii(new float[] { 30, 30, 0, 0, 0, 0, 0, 0 });
    closeBtn.setBackground(closeBg);

    hideBtn.setBackgroundColor(argb);
    GradientDrawable hideBg = new GradientDrawable();
    hideBg.setStroke(3, Color.WHITE);
    hideBg.setCornerRadii(new float[] { 0, 0, 30, 30, 0, 0, 0, 0 });
    hideBtn.setBackground(hideBg);
  }

  // ───────────── Switch ─────────────

	public View Switch(final int featNum, final String name, boolean defaultValue) {
		final Switch switchView = new Switch(this);

		final GradientDrawable track = new GradientDrawable();
		track.setSize(20, 20);
		track.setCornerRadius(100);

		final GradientDrawable thumb = new GradientDrawable();
		thumb.setSize(40, 40);
		thumb.setShape(GradientDrawable.OVAL);

		boolean checked = Preferences.loadPrefBool(name, featNum, defaultValue);

		applySwitchStyle(track, thumb, checked);

		switchView.setText(name);
		switchView.setTextColor(TEXT_COLOR_2);
		switchView.setShadowLayer(7.0f, 0.0f, 0.0f, Color.BLACK);
		switchView.setPadding(10, 5, 10, 5);
		switchView.setThumbDrawable(thumb);
		switchView.setTrackDrawable(track);
		switchView.setChecked(checked);

		switchView.setOnCheckedChangeListener(
			new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(
					CompoundButton button,
					boolean isChecked
				) {
					Preferences.changeFeatureBool(name, featNum, isChecked);

					applySwitchStyle(track, thumb, isChecked);

					handleSpecialSwitch(featNum, isChecked, switchView);
				}
			}
		);

		return switchView;
	}
  private void applySwitchStyle(GradientDrawable track, GradientDrawable thumb, boolean on) {
    if (on) {
      track.setStroke(2, Color.WHITE);
      track.setColor(LST_MAB);
      thumb.setStroke(2, Color.WHITE);
      thumb.setColor(Color.BLUE);
    } else {
      track.setStroke(2, Color.BLUE);
      track.setColor(Color.argb(200, 255, 255, 255));
      thumb.setStroke(2, Color.BLUE);
      thumb.setColor(Color.WHITE);
    }
  }

  private void handleSpecialSwitch(int featNum, boolean on, Switch switchView) {
    switch (featNum) {
      case -7:
        if (on) {
          GradientDrawable bg = new GradientDrawable();
          bg.setColor(Color.parseColor("#AB000000"));
          bg.setStroke(4, Color.WHITE);
          mExpanded.setBackground(bg);
        } else {
          mExpanded.setBackground(blackgamer);
        }
        break;

      case -6:
        if (on) {
          GradientDrawable bg = new GradientDrawable();
          bg.setColor(LST_MAB);
          bg.setStroke(4, Color.WHITE);
          mExpanded.setBackground(bg);
        } else {
          mExpanded.setBackground(blackgamer);
        }
        break;

      case -3:
        Preferences.isExpanded = on;
        scrollView.setLayoutParams(on ? scrlLLExpanded : scrlLL);
        break;

      case -1:
        Preferences.with(switchView.getContext()).writeBoolean(-1, on);
        if (!on) {
          Preferences.with(switchView.getContext()).clear();
        }
        break;

      default:
        break;
    }
  }

  // ───────────── SeekBar ─────────────

	public View SeekBar(final int featNum, final String name, final int min, final int max) {
		int saved = Preferences.loadPrefInt(name, featNum);

		LinearLayout layout = new LinearLayout(this);
		layout.setPadding(10, 5, 0, 5);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);

		final TextView label = new TextView(this);

		if (Preferences.loadPref) {
			label.setText(coloredValue(name, saved + " (SAVED VALUE)"));
		} else {
			label.setText(coloredValue(name, " [DEFAULT]"));
		}

		label.setTextColor(TEXT_COLOR_2);
		label.setShadowLayer(7.0f, 0.0f, 0.0f, Color.BLACK);
		label.setTextSize(13.0f);

		SeekBar seekBar = new SeekBar(this);
		seekBar.setPadding(25, 10, 35, 10);
		seekBar.setMax(max);

		if (Build.VERSION.SDK_INT >= 26) {
			seekBar.setMin(min);
		}

		seekBar.setProgress(saved == 0 ? min : saved);

		GradientDrawable thumb = new GradientDrawable();
		thumb.setShape(GradientDrawable.OVAL);
		thumb.setColor(SeekBarColor);
		thumb.setStroke(dp(2), Color.WHITE);
		thumb.setCornerRadius(15.0f);
		thumb.setSize(dp(18), dp(18));

		seekBar.setThumb(thumb);

		seekBar.getProgressDrawable().setColorFilter(
			SeekBarProgressColor,
			PorterDuff.Mode.SRC_ATOP
		);

		seekBar.setOnSeekBarChangeListener(
			new ProgressListener(new ProgressCallback() {
					@Override
					public void onChanged(SeekBar bar, int progress) {
						onSeekBarChanged(
							bar,
							progress,
							name,
							featNum,
							min,
							label
						);
					}
				})
		);

		layout.addView(label);
		layout.addView(seekBar);

		return layout;
	}

  private void onSeekBarChanged(SeekBar seekBar, int progress, String name, int featNum, int min,
      TextView label) {
    int value = Math.max(progress, min);
    seekBar.setProgress(value);
    Preferences.changeFeatureInt(name, featNum, value);

    if (progress > min) {
      label.setText(coloredValue(name, String.valueOf(value)));
    } else {
      label.setText(coloredValue(name, " [DEFAULT]"));
    }

    switch (featNum) {
      case -10:
        label.setText(html(name + " -> " + progress + "X"));
        if (progress >= 1) {
          RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
              Animation.RELATIVE_TO_SELF, 0.5f);
          rotate.setDuration(360000 / (progress > 10 ? (progress - 10) * 1000 : progress * 100));
          rotate.setInterpolator(new AccelerateDecelerateInterpolator());
          rotate.setRepeatCount(Animation.INFINITE);
          startimage.startAnimation(rotate);
        } else {
          startimage.clearAnimation();
        }
        if (progress == 0) {
          label.setText(coloredValue(name, " [DEFAULT]"));
        } else if (progress == 20) {
          label.setText(coloredValue(name, "[INFINITY]"));
        }
        break;

      case -9:
        label.setText(html(name + " -> " + progress + "X"));
        startimage.getLayoutParams().height = (progress + 13) * 5;
        startimage.getLayoutParams().width = (progress + 13) * 5;
        if (progress == 13 || progress == 1) {
          label.setText(coloredValue(name, "[DEFAULT]"));
        } else if (progress == 99) {
          label.setText(coloredValue(name, "[INFINITY]"));
        }
        break;

      case -8:
        label.setText(html(name + " -> " + progress + "X"));
        ICON_ALPHA = (progress + 1) / 10.0f;
        if (progress == min) {
          ICON_ALPHA = 1.0f;
        }
        if (progress == 10 || progress == 1) {
          label.setText(coloredValue(name, "[DEFAULT]"));
        } else if (progress == 9) {
          label.setText(coloredValue(name, "[INFINITY]"));
        }
        break;

      default:
        break;
    }
  }

  // ───────────── Кнопки ─────────────

	public void HorizontalButton(final LinearLayout parent, final int featNum, final String name) {
		LinearLayout row = new LinearLayout(this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT
		);

		lp.setMargins(5, 7, 5, 7);

		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setLayoutParams(lp);

		Button button = new Button(this);

		AddColor(
			button,
			Color.parseColor("#AFFF3434"),
			3, 0, 0, LST_MAB,
			30, 30, 0, 0, 30, 30, 0, 0
		);

		button.setTextColor(TEXT_COLOR_2);
		button.setText(html(name));

		button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					switch (featNum) {
						case -561:
							Logcat.Clear(getApplicationContext());
							break;

						case -452:
							Logcat.Save(getApplicationContext());
							break;

						default:
							break;
					}

					Preferences.changeFeatureInt(name, featNum, 0);
				}
			});

		row.addView(button);
		parent.addView(row);
	}

	public View Button(final int featNum, final String name) {
		Button button = new Button(this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT
		);

		lp.setMargins(7, 5, 7, 5);

		button.setLayoutParams(lp);
		button.setTextColor(TEXT_COLOR_2);
		button.setAllCaps(false);
		button.setText(html(name));
		button.setBackground(roundedButtonBackground(BTN_COLOR, 3));

		button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					switch (featNum) {
						case -100:
							stopChecking = true;
							break;

						case -12:
							stopSelf();
							break;

						case -11:
							mCollapsed.setVisibility(View.VISIBLE);
							mCollapsed.setAlpha(0);
							mExpanded.setVisibility(View.GONE);
							break;

						case -5:
							Logcat.Clear(getApplicationContext());
							break;

						case -4:
							Logcat.Save(getApplicationContext());
							break;

						default:
							break;
					}

					Preferences.changeFeatureInt(name, featNum, 0);
				}
			});

		return button;
	}

	public View ButtonLink(final String name, final String url) {
		Button button = new Button(this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT
		);

		lp.setMargins(7, 5, 7, 5);

		button.setLayoutParams(lp);
		button.setAllCaps(false);
		button.setTextColor(TEXT_COLOR_2);
		button.setText(html(name));
		button.setBackground(roundedButtonBackground(BTN_COLOR, 3));

		button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					openUrl(url);
				}
			});

		return button;
	}

  public View ButtonOnOff(final int featNum, String name, boolean defaultValue) {
    final Button button = new Button(this);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    lp.setMargins(7, 5, 7, 5);
    button.setLayoutParams(lp);
    button.setTextColor(TEXT_COLOR_2);
    button.setAllCaps(false);

    final String displayName = name.replace("OnOff_", "");
    boolean currentlyOn = Preferences.loadPrefBool(name, featNum, defaultValue);
    applyOnOffStyle(button, displayName, currentlyOn);

	  final boolean[] nextState = { !currentlyOn };

	  button.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  Preferences.changeFeatureBool(
					  displayName,
					  featNum,
					  nextState[0]
				  );

				  applyOnOffStyle(
					  button,
					  displayName,
					  nextState[0]
				  );

				  nextState[0] = !nextState[0];
			  }
		  });

	  return button;
	  }

  private void applyOnOffStyle(Button button, String name, boolean on) {
    button.setText(html(name + (on ? ": ON" : ": OFF")));
    GradientDrawable bg = new GradientDrawable();
    bg.setColor(on ? LST_MAB : BTN_COLOR);
    bg.setStroke(3, Color.WHITE, 4, 4);
    bg.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    button.setBackground(bg);
  }

  // ───────────── Offset finder ─────────────

	public View OffsetMethod(int featNum, String name) {
		Button button = new Button(this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT
		);

		lp.setMargins(7, 5, 7, 5);
		button.setLayoutParams(lp);
		button.setTextColor(TEXT_COLOR_2);
		button.setAllCaps(false);
		button.setText(html(name));
		button.setBackground(roundedButtonBackground(BTN_COLOR, 3));

		final AlertDialog dialog = new AlertDialog.Builder(this).create();

		if (Build.VERSION.SDK_INT >= 19) {
			Objects.requireNonNull(dialog.getWindow()).setType(overlayType());
		}

		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface d) {
					toggleKeyboard(InputMethodManager.SHOW_IMPLICIT, 0);
				}
			});

		LinearLayout content = new LinearLayout(this);
		content.setPadding(5, 5, 5, 5);
		content.setOrientation(LinearLayout.VERTICAL);
		content.setBackground(drawableFromBase64(Sonic()));

		TextView title = new TextView(this);
		title.setText(html("<u>" + name + "</u>"));
		title.setGravity(Gravity.CENTER);
		title.setTypeface(Typeface.DEFAULT_BOLD);
		title.setTextColor(TEXT_COLOR);
		title.setTextSize(22.0f);

		TextView hint = new TextView(this);
		hint.setGravity(Gravity.CENTER);
		hint.setTextSize(14.0f);
		hint.setText("Click \"Set Value\" button to apply changes || outside to cancel");
		hint.setPadding(10, 5, 10, 5);
		hint.setTextColor(TEXT_COLOR);

		LinearLayout results = new LinearLayout(this);
		results.setOrientation(LinearLayout.VERTICAL);

		String[] methods = {
			"get_transform",
			"get_position_Injected",
			"WorldToScreenPoint_Injected",
			"get_main",
			"SetResolution",
			"set_position_Injected"
		};

		for (Map.Entry<String, String> entry : searchMethods(methods).entrySet()) {
			TextView line = new TextView(this);
			line.setText(entry.getKey() + ": " + entry.getValue());
			line.setTextColor(TEXT_COLOR);
			results.addView(line);
		}

		content.addView(title);
		content.addView(hint);
		content.addView(results);

		dialog.setView(content);

		button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.show();
				}
			});

		WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
		windowParams.copyFrom(dialog.getWindow().getAttributes());
		windowParams.height = 1000;
		dialog.getWindow().setAttributes(windowParams);

		return button;
	}

	public static Map<String, String> searchMethods(String[] names) {
		Map<String, String> found = new HashMap<String, String>();

		try {
			FileInputStream fileStream = new FileInputStream(
				"/storage/emulated/0/MT2/libil2cpp.so"
			);

			DataInputStream dataStream = new DataInputStream(fileStream);

			try {
				byte[] buffer = new byte[1024];
				int read;

				while ((read = dataStream.read(buffer)) != -1) {
					String chunk = new String(buffer, 0, read);

					for (String name : names) {
						int index = chunk.indexOf(name);

						if (index != -1) {
							int end = index + name.length() + 8;

							if (end <= chunk.length()) {
								found.put(
									name,
									chunk.substring(index + name.length(), end)
								);
							}
						}
					}
				}
			} finally {
				dataStream.close();
			}

		} catch (IOException e) {
			// Файл недоступен или произошла ошибка чтения.
		} catch (SecurityException e) {
			// Нет разрешения на чтение файла.
		}

		if (found.isEmpty()) {
			found.put("Method and offset not found", "0x0");
		}

		return found;
	}

  // ───────────── Spinner ─────────────

  public View Spinner(int featNum, String name, String itemsCsv) {
    Log.d(TAG, "spinner " + featNum + " " + name + " " + itemsCsv);
    List<String> items = Arrays.asList(itemsCsv.split(","));

    LinearLayout layout = new LinearLayout(this);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.setMargins(7, 2, 7, 5);
    layout.setOrientation(LinearLayout.VERTICAL);
    GradientDrawable bg = new GradientDrawable();
    bg.setColor(LST_MAB);
    bg.setStroke(3, Color.WHITE, 4, 4);
    bg.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    layout.setBackground(bg);
    layout.setLayoutParams(lp);

    Spinner spinner = new Spinner(this, Spinner.MODE_DROPDOWN);
    spinner.setLayoutParams(lp);
    spinner.getBackground().setColorFilter(1, PorterDuff.Mode.SRC_ATOP);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_dropdown_item, items);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setSelection(Preferences.loadPrefInt(name, featNum));
    spinner.setOnItemSelectedListener(new SpinnerSelectionListener(spinner, featNum));

    layout.addView(spinner);
    return layout;
  }

  // ───────────── Ввод значений ─────────────

  /** Иконка-глаз, которая скрывает/показывает значение. */
  private TextView createEyeToggle(final TextView valueText) {
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(30), dp(30));
    lp.gravity = Gravity.BOTTOM;
    lp.setMargins(0, 0, 20, 0);

    final BitmapDrawable eyeOpen = drawableFromBase64(EYE());
    final BitmapDrawable eyeClosed = drawableFromBase64(EYECLOSE());

    final TextView eye = new TextView(this);
    eye.setLayoutParams(lp);
    eye.setBackground(eyeOpen);

	  final boolean[] hidden = { false };

	  eye.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  hidden[0] = !hidden[0];

				  if (hidden[0]) {
					  eye.setBackground(eyeClosed);

					  valueText.setTransformationMethod(
						  PasswordTransformationMethod.getInstance()
					  );
				  } else {
					  eye.setBackground(eyeOpen);

					  valueText.setTransformationMethod(
						  HideReturnsTransformationMethod.getInstance()
					  );
				  }
			  }
		  });

	  return eye;
  }

  private LinearLayout createInputRow() {
    LinearLayout row = new LinearLayout(this);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    lp.setMargins(15, 10, 15, 10);
    row.setLayoutParams(lp);
    row.setOrientation(LinearLayout.HORIZONTAL);
    row.setPadding(10, 8, 8, 8);
    row.setGravity(Gravity.CENTER_VERTICAL);
    GradientDrawable bg = new GradientDrawable();
    bg.setColor(0);
    bg.setStroke(3, Color.WHITE);
    bg.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    row.setBackground(bg);
    return row;
  }

  private LinearLayout createInputTextColumn() {
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.weight = 1.0f;
    LinearLayout column = new LinearLayout(this);
    column.setLayoutParams(lp);
    column.setOrientation(LinearLayout.VERTICAL);
    column.setGravity(Gravity.CENTER);
    return column;
  }

  private Button createEnterButton() {
    GradientDrawable bg = new GradientDrawable();
    bg.setColor(BTN_COLOR);
    bg.setStroke(2, Color.WHITE);
    bg.setCornerRadii(new float[] { 30, 30, 0, 0, 30, 30, 0, 0 });
    Button button = new Button(this);
    button.setText("ENTER");
    button.setAllCaps(false);
    button.setBackground(bg);
    button.setTextColor(TEXT_COLOR_2);
    return button;
  }

  public void InputNum(LinearLayout parent, final int featNum, final String name, final int maxValue) {
    final EditTextNum holder = new EditTextNum();

    LinearLayout row = createInputRow();
    LinearLayout column = createInputTextColumn();

    TextView title = new TextView(this);
    title.setText(name);
    title.setTextColor(TEXT_COLOR_2);
    title.setTextSize(14.0f);
    title.setSingleLine(true);
    title.setGravity(Gravity.LEFT);
    title.setPadding(5, 0, 0, 0);

    int saved = Preferences.loadPrefInt(name, featNum);
    final TextView valueText = new TextView(this);
    valueText.setText(
        html("═▶︎<font color='" + NumberTxtColor + "'>" + (saved == 0 ? 1 : saved) + "</font>"));
    valueText.setTextColor(TEXT_COLOR_2);
    valueText.setTextSize(14.0f);
    valueText.setGravity(Gravity.LEFT);
    valueText.setPadding(5, 0, 0, 0);

    TextView eye = createEyeToggle(valueText);

	  Button enter = createEnterButton();

	  enter.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  showInputDialog(name, true, null, new InputCallback() {

						  @Override
						  public void onConfirm(String text) {
							  int value;

							  try {
								  value = Integer.parseInt(
									  TextUtils.isEmpty(text) ? "0" : text
								  );

								  if (maxValue != 0 && value >= maxValue) {
									  value = maxValue;
								  }

							  } catch (NumberFormatException e) {
								  value = 2147483640;
							  }

							  holder.setNum(value);

							  valueText.setText(
								  html("═▶︎ <font color='" + NumberTxtColor + "'>"
									   + value + "</font>")
							  );

							  Preferences.changeFeatureInt(name, featNum, value);
						  }
					  });
			  }
		  });

	  row.addView(column);
	  column.addView(title);
	  column.addView(valueText);
	  row.addView(eye);
	  row.addView(enter);
	  parent.addView(row);
  }

  public void InputText(LinearLayout parent, final int featNum, final String name) {
    final EditTextString holder = new EditTextString();

    LinearLayout row = createInputRow();
    LinearLayout column = createInputTextColumn();

    TextView title = new TextView(this);
    title.setText(name);
    title.setTextColor(TEXT_COLOR_2);
    title.setGravity(Gravity.LEFT);
    title.setTextSize(14.0f);
    title.setPadding(5, 0, 0, 0);
    title.setSingleLine(true);

    String saved = Preferences.loadPrefString(name, featNum);
    if (saved == null) {
      saved = "";
    }
    holder.setString(saved);

    final TextView valueText = new TextView(this);
    valueText.setText(html("═▶︎<font color='" + NumberTxtColor + "'>" + saved + "</font>"));
    valueText.setTextColor(TEXT_COLOR_2);
    valueText.setGravity(Gravity.LEFT);
    valueText.setTextSize(14.0f);
    valueText.setPadding(5, 0, 0, 0);

    TextView eye = createEyeToggle(valueText);

	  Button enter = createEnterButton();

	  enter.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  showInputDialog(
					  name,
					  false,
					  holder.getString(),
					  new InputCallback() {

						  @Override
						  public void onConfirm(String text) {
							  holder.setString(text);

							  valueText.setText(
								  html("═▶︎<font color='" + NumberTxtColor
									   + "'>" + text + "</font>")
							  );

							  Preferences.changeFeatureString(
								  name,
								  featNum,
								  text
							  );
						  }
					  }
				  );
			  }
		  });

	  row.addView(column);
	  column.addView(title);
	  column.addView(valueText);
	  row.addView(eye);
	  row.addView(enter);
	  parent.addView(row);
	  }
  /** Общий диалог ввода числа или текста. */
  private void showInputDialog(String featName, boolean numeric, String initialText,
      final InputCallback onConfirm) {
    final AlertDialog dialog = createOverlayDialog();
    Context ctx = getApplicationContext();

    LinearLayout content = new LinearLayout(ctx);
    content.setPadding(5, 5, 5, 5);
    content.setOrientation(LinearLayout.VERTICAL);
    content.setBackground(drawableFromBase64(Sonic()));
    if (numeric) {
      content.setElevation(5.0f);
    }

    RainbowActivity title = new RainbowActivity(ctx);
    title.setText(html("<u>" + featName + "</u>"));
    title.setGravity(Gravity.CENTER);
    title.setTypeface(Typeface.DEFAULT_BOLD);
    title.setTextColor(TEXT_COLOR);
    title.setTextSize(22.0f);

    TextView hint = new TextView(ctx);
    hint.setGravity(Gravity.CENTER);
    hint.setTextSize(14.0f);
    hint.setText("Click \"Set Value\" button to apply changes || outside to cancel");
    hint.setPadding(10, 5, 10, 5);
    hint.setTextColor(TEXT_COLOR);

    LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
    ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.WRAP_CONTENT
);

editParams.weight = 1;

final EditText edit = new EditText(ctx);
edit.setLayoutParams(editParams);
edit.setMaxLines(1);
edit.setWidth(dp(300));

edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            toggleKeyboard(
 InputMethodManager.SHOW_FORCED,
 InputMethodManager.HIDE_IMPLICIT_ONLY
            );
        } else {
            toggleKeyboard(
 InputMethodManager.SHOW_IMPLICIT,
 0
            );
        }
    }
});

if (numeric) {
    edit.setHint("Write Value");
    edit.setTextColor(TEXT_COLOR);
    edit.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

    edit.setKeyListener(
        DigitsKeyListener.getInstance("0123456789-")
    );

    edit.setFilters(
        new InputFilter[] {
            new InputFilter.LengthFilter(10)
        }
    );

    edit.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
 case MotionEvent.ACTION_DOWN:
	 edit.setTransformationMethod(
		 PasswordTransformationMethod.getInstance()
	 );
	 return true;

 case MotionEvent.ACTION_UP:
	 edit.setTransformationMethod(
		 HideReturnsTransformationMethod.getInstance()
	 );
	 return true;

 default:
	 return false;
            }
        }
    });

} else {
    edit.setHint("Write Text");
    edit.setTextColor(TEXT_COLOR_2);
    edit.setText(initialText);
}

edit.requestFocus();

LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
    ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.WRAP_CONTENT
);

buttonParams.setMargins(20, 10, 20, 15);

Button setButton = new Button(ctx);

GradientDrawable bg = new GradientDrawable();
bg.setColor(BTN_COLOR);
bg.setStroke(2, Color.WHITE);
bg.setCornerRadii(
    new float[] {
        30, 30,
        0, 0,
        30, 30,
        0, 0
    }
);

setButton.setBackground(bg);
setButton.setLayoutParams(buttonParams);
setButton.setTextColor(TEXT_COLOR_2);
setButton.setPadding(15, 10, 15, 10);
setButton.setText("SET VALUE");

setButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        onConfirm.onConfirm(edit.getText().toString());

        dialog.dismiss();

        edit.setFocusable(false);
    }
});

content.addView(title);
content.addView(hint);
content.addView(edit);
content.addView(setButton);

dialog.setView(content);
dialog.show();
  }

  // ───────────── CheckBox / Radio ─────────────

	public View CheckBox(final int featNum, final String name, boolean defaultValue) {
		CheckBox checkBox = new CheckBox(this);

		checkBox.setText(name);
		checkBox.setTextColor(TEXT_COLOR_2);

		if (Build.VERSION.SDK_INT >= 21) {
			checkBox.setButtonTintList(
				ColorStateList.valueOf(CheckBoxColor)
			);
		}

		checkBox.setChecked(
			Preferences.loadPrefBool(name, featNum, defaultValue)
		);

		checkBox.setOnCheckedChangeListener(
			new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(
					CompoundButton button,
					boolean isChecked
				) {
					Preferences.changeFeatureBool(
						name,
						featNum,
						isChecked
					);
				}
			}
		);

		return checkBox;
	}
  public View RadioButton(final int featNum, final String name, String optionsCsv) {
    List<String> options = Arrays.asList(optionsCsv.split(","));

    final RainbowActivity label = new RainbowActivity(this);
    label.setText(name + ":");
    label.setTextColor(TEXT_COLOR_2);

    final RadioGroup group = new RadioGroup(this);
    group.setPadding(10, 5, 10, 5);
    group.setOrientation(RadioGroup.VERTICAL);
    group.addView(label);

	  for (final String option : options) {
		  final RadioButton radio = new RadioButton(this);

		  radio.setText(option);
		  radio.setTextColor(-3355444);

		  if (Build.VERSION.SDK_INT >= 21) {
			  radio.setButtonTintList(
				  ColorStateList.valueOf(RadioColor)
			  );
		  }

		  radio.setOnClickListener(new View.OnClickListener() {
				  @Override
				  public void onClick(View view) {
					  label.setText(
						  html(name + ": <font color='" + NumberTxtColor
							   + "'>" + option)
					  );

					  Preferences.changeFeatureInt(
						  name,
						  featNum,
						  group.indexOfChild(radio)
					  );
				  }
			  });

		  group.addView(radio);
	  }

	  int saved = Preferences.loadPrefInt(name, featNum);

	  if (saved > 0 && saved < group.getChildCount()) {
		  label.setText(
			  html(name + ": <font color='" + NumberTxtColor
				   + "'>" + options.get(saved - 1))
		  );

		  ((RadioButton) group.getChildAt(saved)).setChecked(true);
	  }

	  return group;
  }

  // ───────────── Ссылки с иконками ─────────────

  public void YoutubeLink(LinearLayout parent, int featNum, String text) {
    addIconLink(parent, text, IconYoutube(),
        "https://youtube.com/@donigbryt_official?si=jixWVg2EdsvFTeRs");
  }

  public void TeleGroupLink(LinearLayout parent, int featNum, String text) {
    addIconLink(parent, text, IconTeleVip(), "https://t.me/DONIfunntgamer");
  }

  public void TeleChannelLink(LinearLayout parent, int featNum, String text) {
    addIconLink(parent, text, IconTele(), "https://t.me/DONIMODZ");
  }

  private void addIconLink(LinearLayout parent, String text, String iconBase64, final String url) {
    LinearLayout row = new LinearLayout(this);
    LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    rowParams.setMargins(5, 7, 5, 7);
    row.setOrientation(LinearLayout.HORIZONTAL);
    row.setGravity(Gravity.CENTER_VERTICAL);
    row.setPadding(5, 8, 8, 8);
    row.setLayoutParams(rowParams);

    LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    columnParams.weight = 1.0f;
    LinearLayout column = new LinearLayout(this);
    column.setLayoutParams(columnParams);
    column.setOrientation(LinearLayout.VERTICAL);
    column.setGravity(Gravity.CENTER);

    RainbowActivity label = new RainbowActivity(this);
    label.setTextColor(TEXT_COLOR_2);
    label.setText(text);
    label.setTextSize(12.0f);
    label.setGravity(Gravity.LEFT);
    label.setSingleLine(true);
    label.setPadding(5, 0, 0, 0);

	  Button button = new Button(this);

	  button.setTextColor(TEXT_COLOR_2);
	  button.setBackground(drawableFromBase64(iconBase64));

	  RelativeLayout.LayoutParams buttonParams =
		  new RelativeLayout.LayoutParams(75, 75);

	  buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	  button.setLayoutParams(buttonParams);

	  button.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  openUrl(url);
			  }
		  });

	  row.addView(column);
	  column.addView(label);
	  row.addView(button);
	  parent.addView(row);
  }

  // ───────────── Collapse / текстовые блоки ─────────────

  public void Collapse(LinearLayout parent, final String text) {
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    lp.setMargins(0, 5, 0, 0);

    LinearLayout container = new LinearLayout(this);
    container.setLayoutParams(lp);
    container.setVerticalGravity(Gravity.CENTER_VERTICAL);
    container.setOrientation(LinearLayout.VERTICAL);

    final LinearLayout body = new LinearLayout(this);
    body.setVerticalGravity(Gravity.CENTER_VERTICAL);
    body.setPadding(0, 5, 0, 5);
    body.setOrientation(LinearLayout.VERTICAL);
    body.setBackgroundColor(0);
    body.setVisibility(View.GONE);
    mCollapse = body;

    final GradientDrawable border = new GradientDrawable();
    border.setStroke(3, Color.parseColor("BLUE"));
    border.setCornerRadii(new float[] { 50, 50, 30, 30, 30, 30, 30, 30 });
    container.setBackground(border);

    final TextView header = new TextView(this);
    header.setBackgroundColor(0);
    header.setText("▽ " + text + " ▽");
    header.setGravity(Gravity.CENTER);
    header.setTextColor(TEXT_COLOR_2);
    header.setTypeface(null, Typeface.BOLD);
    header.setPadding(0, 20, 0, 20);

	  final boolean[] expanded = { false };

	  header.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  expanded[0] = !expanded[0];

				  if (expanded[0]) {
					  body.setVisibility(View.VISIBLE);
					  header.setText("△ " + text + " △");
				  } else {
					  body.setVisibility(View.GONE);
					  header.setText("▽ " + text + " ▽");
				  }

				  border.setStroke(
					  3,
					  Color.parseColor("#3c5afe")
				  );
			  }
		  });

	  container.addView(header);
	  container.addView(body);
	  parent.addView(container);
  }

  public View Category(String text) {
    TextView view = new TextView(this);
    GradientDrawable bg = new GradientDrawable();
    bg.setColor(CategoryBG);
    bg.setCornerRadii(new float[] { 30, 30, 30, 30, 30, 30, 30, 30 });
    bg.setStroke(3, Color.WHITE);
    view.setBackground(bg);
    view.setText(html(text));
    view.setTypeface(safeFont("NoName1.ttf"));
    view.setGravity(Gravity.CENTER);
    view.setTextColor(TEXT_COLOR_2);
    view.setPadding(0, 5, 0, 5);
    return view;
  }

  public View RichTextView(String text) {
    RainbowActivity view = new RainbowActivity(this);
    view.setText(html(text));
    view.setTextColor(TEXT_COLOR_2);
    view.setPadding(10, 5, 10, 5);
    return view;
  }

  public View RichWebView(String text) {
    WebView view = new WebView(this);
    view.loadData(text, "text/html", "utf-8");
    view.setBackgroundColor(0);
    view.setPadding(0, 5, 0, 5);
    return view;
  }
}
