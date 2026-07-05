package com.barbos.loader;

import android.app.Service;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class OverlayService extends Service {
    private WindowManager wm;
    private FrameLayout overlayView;
    private Paint paint;
    private Handler handler;
    private boolean running = true;

    @Override public void onCreate() {
        super.onCreate();
        paint = new Paint();
        paint.setAntiAlias(true);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        handler = new Handler();
        createOverlay();
        startLoop();
    }

    private void createOverlay() {
        overlayView = new FrameLayout(this) {
            @Override protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                drawESP(canvas);
                invalidate();
            }
        };
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            android.graphics.PixelFormat.TRANSLUCENT
        );
        wm.addView(overlayView, params);
    }

    private void drawESP(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(100, 100, 200, 300, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        canvas.drawText("ESP ACTIVE", 100, 90, paint);
    }

    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override public void run() {
                if (running) {
                    overlayView.invalidate();
                    handler.postDelayed(this, 50);
                }
            }
        }, 50);
    }

    @Override public void onDestroy() {
        running = false;
        if (overlayView != null) wm.removeView(overlayView);
        super.onDestroy();
    }
    @Override public IBinder onBind(Intent i) { return null; }
}
