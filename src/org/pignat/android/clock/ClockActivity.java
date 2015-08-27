/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.pignat.android.clock;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

/**
 * This is an example of using the accelerometer to integrate the device's
 * acceleration to a position using the Verlet method. This is illustrated with
 * a very simple particle system comprised of a few iron balls freely moving on
 * an inclined wooden table. The inclination of the virtual table is controlled
 * by the device's accelerometer.
 * 
 * @see SensorManager
 * @see SensorEvent
 * @see Sensor
 */

public class ClockActivity extends Activity {

    private TimeView mTimeView;
    private Paint mPaint;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // instantiate our simulation view and set it as the activity's content
        mTimeView = new TimeView(this);
        setContentView(mTimeView);
        
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTypeface(Typeface.MONOSPACE);
    }

    @Override
    protected void onResume() {
        super.onResume();    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    class TimeView extends View {

        public TimeView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            
            mPaint.setColor(Color.BLACK); 
            mPaint.setStyle(Style.FILL); 
            canvas.drawPaint(mPaint); 

            mPaint.setColor(Color.WHITE); 
            
            Calendar c = Calendar.getInstance(); 
            int h = c.get(Calendar.HOUR);
            int m = c.get(Calendar.MINUTE);
            String text = String.format(Locale.getDefault(), "%02d:%02d", h, m);
            
            setTextSizeForWidth(mPaint, canvas.getWidth(), text); 
            

            canvas.save();
            canvas.rotate(90, canvas.getHeight()/2, canvas.getWidth()/2);
            canvas.drawText(text, canvas.getWidth() / 2, canvas.getHeight() / 2, mPaint);
            canvas.restore();

            // and make sure to redraw asap
            invalidate();
        }
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
        	getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }
    
    private static void setTextSizeForWidth(Paint paint, float desiredWidth,
            String text) {

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 100f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * desiredWidth / bounds.width();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }
}
