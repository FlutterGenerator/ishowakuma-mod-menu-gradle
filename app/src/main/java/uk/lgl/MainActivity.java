
package uk.lgl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    static {
        System.loadLibrary("IShowAkuma");
    }

    private static final String GAME_ACTIVITY =
	"com.unity3d.player.UnityPlayerActivity";

    private boolean gameLaunched = false;
    private boolean menuStarted = false;

    @TargetApi(22)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupCodeCards();

        if (savedInstanceState != null) {
            gameLaunched = savedInstanceState.getBoolean(
				"gameLaunched", false);
        }

        if (!gameLaunched) {
            launchGame();
        }

        startModMenu();
    }

    private void setupCodeCards() {

        int[] layouts = {
			R.id.layout1,
			R.id.layout2,
			R.id.layout3
        };

        String[] subtitles = {
			"1. Add permission to AndroidManifest.xml",
			"2. Add service inside application tag",
			"3. Add this call inside Unity Activity onCreate"
        };

        String[] headings = {
			"Floating Permission",
			"Service Permission",
			"MainActivity Text"
        };

        String[] codes = {

			"<uses-permission android:name=\"android.permission.SYSTEM_ALERT_WINDOW\"/>",

			"<service\n" +
			"    android:name=\"uk.lgl.modmenu.FloatingModMenuService\"\n" +
			"    android:enabled=\"true\"\n" +
			"    android:exported=\"false\"\n" +
			"    android:stopWithTask=\"true\" />",

			"invoke-static {p0}, Luk/lgl/MainActivity;->Start(Landroid/content/Context;)V"
        };

        for (int i = 0; i < layouts.length; i++) {

            LinearLayout layout = findViewById(layouts[i]);

            if (layout == null) {
                continue;
            }

            TextView subtitle =
				layout.findViewById(R.id.subtitle);

            TextView heading =
				layout.findViewById(R.id.textheading);

            TextView code =
				layout.findViewById(R.id.textcode);

            TextView copy =
				layout.findViewById(R.id.btncopy);

            if (subtitle != null) {
                subtitle.setText(subtitles[i]);
            }

            if (heading != null) {
                heading.setText(headings[i]);
            }

            if (code != null) {
                code.setText(codes[i]);
            }

            if (copy != null && code != null) {

                final TextView codeView = code;
                final TextView copyButton = copy;

                copyButton.setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							ClipboardManager clipboard =
                                (ClipboardManager)
                                getSystemService(
								Context.CLIPBOARD_SERVICE);

							if (clipboard != null) {

								clipboard.setPrimaryClip(
                                    ClipData.newPlainText(
										"Code",
										codeView.getText()
                                    )
								);

								copyButton.setText("✓ Copied!");

								copyButton.removeCallbacks(
                                    resetCopyText);

								copyButton.postDelayed(
                                    resetCopyText,
                                    2000
								);
							}
						}

						private final Runnable resetCopyText =
						new Runnable() {

							@Override
							public void run() {
								copyButton.setText("❏ Copy Code");
							}
						};
					});
            }
        }
    }

    private void launchGame() {

        // Unity Activity exists only inside a Unity game APK.
        try {

            Class.forName(GAME_ACTIVITY);

        } catch (ClassNotFoundException e) {

            gameLaunched = true;
            return;
        }

        try {

            Intent intent = new Intent();

            intent.setClassName(
				this,
				GAME_ACTIVITY
            );

            startActivity(intent);

            gameLaunched = true;

        } catch (Exception e) {

            Toast.makeText(
				this,
				"Unity Activity error: " + e.getMessage(),
				Toast.LENGTH_LONG
            ).show();
        }
    }

    public static void Start(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
			&& !Settings.canDrawOverlays(context)) {

            Toast.makeText(
				context,
				"Overlay permission is required.",
				Toast.LENGTH_LONG
            ).show();

            Intent intent = new Intent(
				Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
				Uri.parse(
					"package:" + context.getPackageName())
            );

            intent.addFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK
            );

            context.startActivity(intent);

            return;
        }

        startMenuService(context);
    }

    private void startModMenu() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
			&& !Settings.canDrawOverlays(this)) {

            Start(this);
            return;
        }

        menuStarted = true;

        startMenuService(this);
    }

    private static void startMenuService(Context context) {

        try {

            Intent intent = new Intent();

            intent.setClassName(
				context,
				"uk.lgl.modmenu.FloatingModMenuService"
            );

            // Start a normal service.
            context.startService(intent);

        } catch (Exception e) {

            Toast.makeText(
				context,
				"Mod Menu service error: " + e.getMessage(),
				Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (!menuStarted
			&& (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
			|| Settings.canDrawOverlays(this))) {

            menuStarted = true;

            startMenuService(this);
        }
    }

    @TargetApi(22)
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(
			"gameLaunched",
			gameLaunched
        );

        super.onSaveInstanceState(outState);
    }
}

