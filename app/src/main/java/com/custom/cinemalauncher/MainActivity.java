package com.custom.cinemalauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private final int BG = Color.rgb(13, 16, 22);
    private final int PANEL = Color.rgb(27, 32, 42);
    private final int MUTED = Color.rgb(177, 187, 202);
    private final int ACCENT = Color.rgb(93, 143, 245);
    private LinearLayout root;
    private TextView clock;
    private LinearLayout appRow;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        buildUi();
        updateClock();
    }

    private void buildUi() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(38), dp(24), dp(38), dp(24));
        root.setBackgroundColor(BG);
        setContentView(root);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setOrientation(LinearLayout.HORIZONTAL);
        root.addView(top, new LinearLayout.LayoutParams(-1, dp(42)));

        TextView brand = text("CINEMA HOME", 16, Color.WHITE, true);
        brand.setLetterSpacing(0.12f);
        top.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));

        clock = text("", 14, MUTED, false);
        top.addView(clock);

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(Gravity.CENTER_VERTICAL);
        hero.setPadding(dp(28), dp(22), dp(28), dp(22));
        GradientDrawable heroBg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{Color.rgb(40, 54, 79), Color.rgb(22, 27, 38), Color.rgb(19, 22, 29)});
        heroBg.setCornerRadius(dp(18));
        hero.setBackground(heroBg);
        LinearLayout.LayoutParams heroLp = new LinearLayout.LayoutParams(-1, dp(210));
        heroLp.topMargin = dp(14);
        root.addView(hero, heroLp);

        TextView eyebrow = text("YOUR ENTERTAINMENT, IN ONE PLACE", 12, Color.rgb(160, 190, 255), true);
        eyebrow.setLetterSpacing(0.1f);
        hero.addView(eyebrow);

        TextView title = text("Movie night starts here.", 29, Color.WHITE, true);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(-1, -2);
        titleLp.topMargin = dp(8);
        hero.addView(title, titleLp);

        TextView subtitle = text("Open a favourite app or browse everything installed on your TV.", 14, MUTED, false);
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(-1, -2);
        subLp.topMargin = dp(7);
        hero.addView(subtitle, subLp);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams actionsLp = new LinearLayout.LayoutParams(-1, dp(42));
        actionsLp.topMargin = dp(15);
        hero.addView(actions, actionsLp);

        Button watch = button("▶  Watch YouTube");
        watch.setOnClickListener(v -> launchPackage("com.google.android.youtube.tv", "YouTube"));
        actions.addView(watch, new LinearLayout.LayoutParams(dp(180), -1));

        Button browse = button("Browse apps");
        LinearLayout.LayoutParams browseLp = new LinearLayout.LayoutParams(dp(145), -1);
        browseLp.leftMargin = dp(10);
        browse.setOnClickListener(v -> showAllApps());
        actions.addView(browse, browseLp);

        TextView section = text("QUICK PICKS", 13, MUTED, true);
        section.setLetterSpacing(0.12f);
        LinearLayout.LayoutParams sectionLp = new LinearLayout.LayoutParams(-1, -2);
        sectionLp.topMargin = dp(18);
        root.addView(section, sectionLp);

        HorizontalScrollView scroller = new HorizontalScrollView(this);
        scroller.setHorizontalScrollBarEnabled(false);
        scroller.setFillViewport(false);
        LinearLayout.LayoutParams scrollLp = new LinearLayout.LayoutParams(-1, 0, 1);
        scrollLp.topMargin = dp(9);
        root.addView(scroller, scrollLp);

        appRow = new LinearLayout(this);
        appRow.setOrientation(LinearLayout.HORIZONTAL);
        scroller.addView(appRow, new HorizontalScrollView.LayoutParams(-2, -1));
        addQuickApp("YouTube", "com.google.android.youtube.tv", "▶");
        addQuickApp("Netflix", "com.netflix.ninja", "N");
        addQuickApp("Live TV", "com.droidlogic.droidlivetv", "TV");
        addSettingsCard();
        addBrowseCard();

        TextView footer = text("Use the remote arrows to move · OK to open · Back to return", 11, MUTED, false);
        LinearLayout.LayoutParams footerLp = new LinearLayout.LayoutParams(-1, -2);
        footerLp.topMargin = dp(10);
        root.addView(footer, footerLp);
    }

    private void addQuickApp(String label, String pkg, String mark) {
        if (!isLaunchable(pkg)) return;
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(12), dp(8), dp(12), dp(8));
        card.setBackground(round(PANEL, 12));
        card.setFocusable(true);
        card.setClickable(true);
        card.setOnClickListener(v -> launchPackage(pkg, label));
        card.setOnFocusChangeListener((v, focused) -> {
            v.setBackground(round(focused ? Color.rgb(45, 61, 88) : PANEL, 12));
            v.setScaleX(focused ? 1.035f : 1f);
            v.setScaleY(focused ? 1.035f : 1f);
        });

        TextView icon = text(mark, 27, Color.WHITE, true);
        icon.setGravity(Gravity.CENTER);
        card.addView(icon, new LinearLayout.LayoutParams(-1, dp(42)));
        TextView name = text(label, 13, Color.WHITE, true);
        name.setGravity(Gravity.CENTER);
        card.addView(name);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(142), dp(100));
        lp.rightMargin = dp(10);
        appRow.addView(card, lp);
    }

    private void addSettingsCard() {
        LinearLayout card = makeCard("⚙", "Settings");
        card.setOnClickListener(v -> {
            try { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
            catch (Exception e) { toast("Settings could not be opened"); }
        });
        addCardToRow(card);
    }

    private void addBrowseCard() {
        LinearLayout card = makeCard("＋", "All apps");
        card.setOnClickListener(v -> showAllApps());
        addCardToRow(card);
    }

    private LinearLayout makeCard(String symbol, String label) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setBackground(round(PANEL, 12));
        card.setFocusable(true);
        card.setClickable(true);
        card.setOnFocusChangeListener((v, focused) -> {
            v.setBackground(round(focused ? Color.rgb(45, 61, 88) : PANEL, 12));
            v.setScaleX(focused ? 1.035f : 1f);
            v.setScaleY(focused ? 1.035f : 1f);
        });
        TextView icon = text(symbol, 27, Color.WHITE, true);
        icon.setGravity(Gravity.CENTER);
        card.addView(icon, new LinearLayout.LayoutParams(-1, dp(42)));
        TextView name = text(label, 13, Color.WHITE, true);
        name.setGravity(Gravity.CENTER);
        card.addView(name);
        return card;
    }

    private void addCardToRow(LinearLayout card) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(142), dp(100));
        lp.rightMargin = dp(10);
        appRow.addView(card, lp);
    }

    private void showAllApps() {
        Intent query = new Intent(Intent.ACTION_MAIN);
        query.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> apps = getPackageManager().queryIntentActivities(query, 0);
        if (apps == null || apps.isEmpty()) {
            query.removeCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
            query.addCategory(Intent.CATEGORY_LAUNCHER);
            apps = getPackageManager().queryIntentActivities(query, 0);
        }
        List<ResolveInfo> sorted = new ArrayList<>(apps == null ? Collections.emptyList() : apps);
        Collections.sort(sorted, (a, b) -> String.CASE_INSENSITIVE_ORDER.compare(
                String.valueOf(a.loadLabel(getPackageManager())),
                String.valueOf(b.loadLabel(getPackageManager()))));
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        list.setPadding(dp(12), dp(6), dp(12), dp(6));
        ScrollView scroll = new ScrollView(this);
        scroll.addView(list);
        for (ResolveInfo info : sorted) {
            String pkg = info.activityInfo.packageName;
            String label = String.valueOf(info.loadLabel(getPackageManager()));
            if (pkg.equals(getPackageName())) continue;
            Button item = button(label);
            item.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            item.setOnClickListener(v -> launchPackage(pkg, label));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(48));
            lp.bottomMargin = dp(5);
            list.addView(item, lp);
        }
        new AlertDialog.Builder(this)
                .setTitle("All apps")
                .setView(scroll)
                .setNegativeButton("Close", null)
                .show();
    }

    private boolean isLaunchable(String pkg) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(pkg);
        return intent != null;
    }

    private void launchPackage(String pkg, String label) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(pkg);
        if (intent == null) {
            toast(label + " isn't available as a launchable app");
            return;
        }
        try { startActivity(intent); }
        catch (Exception e) { toast("Couldn't open " + label); }
    }

    private void updateClock() {
        if (clock != null) {
            clock.setText(new SimpleDateFormat("EEE, d MMM  ·  h:mm a", Locale.getDefault()).format(new Date()));
            clock.postDelayed(this::updateClock, 30000);
        }
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setGravity(Gravity.CENTER_VERTICAL);
        return t;
    }

    private Button button(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(13);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setFocusable(true);
        b.setBackground(round(ACCENT, 9));
        b.setPadding(dp(12), 0, dp(12), 0);
        b.setOnFocusChangeListener((v, focused) -> v.setBackground(round(
                focused ? Color.rgb(122, 164, 255) : ACCENT, 9)));
        return b;
    }

    private GradientDrawable round(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showAllApps();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
