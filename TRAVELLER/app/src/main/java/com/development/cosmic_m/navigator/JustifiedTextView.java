package com.development.cosmic_m.navigator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.StringTokenizer;

/**
 * Created by Cosmic_M on 21.09.2017.
 */

public class JustifiedTextView extends View {
    private static final String TAG = "JustifiedTextView";
    private String mText;
    private Paint mPaint;
    private Paint.FontMetricsInt mFontMetrics;
    private int mSpace;
    private Context mContext;
    private int mWidthScreen;
    private boolean flag, newFlag;

    public JustifiedTextView(Context context, String text, int widthScreen){
            super(context);
        mText = text;
        mWidthScreen = widthScreen - 10;
        }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String word, sp, line = "";
        int wordCount = 0, y = 0, x = 5, nextX = 0;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(26);
        mFontMetrics = new Paint.FontMetricsInt();
        int b1 =  (int) Math.abs(mPaint.ascent());
        int fh =  (int) mPaint.descent() + b1;
        mSpace = (int) mPaint.measureText(" ");
        flag = true;

        Log.i(TAG, "b1 = " + b1 + ", fh = " + fh);

        if (mText == null) return;
        StringTokenizer stringTokenizer = new StringTokenizer(mText);
        while (stringTokenizer.hasMoreTokens()){
            word = stringTokenizer.nextToken();
            if (word.equals("<P>")){
                drawString(canvas, line, wordCount, (int) mPaint.measureText(line), y+b1);
                line = "";
                wordCount = 0;
                x = 5;
                y = y + (fh * 2);
                flag = true;
            }
            else{
                if (flag){
                    x = 30;
                    flag = false;
                    newFlag = true;
                }
                int w = (int) mPaint.measureText(word);
                if ((nextX = (x + mSpace + w)) > mWidthScreen){
                    drawString(canvas, line, wordCount, (int) mPaint.measureText(line), y+b1);
                    line = "";
                    wordCount = 0;
                    x = 0;
                    y = y + fh;
                }
                if (x != 5){
                    sp = " ";
                }
                else{
                    sp = "";
                }
                line = line + sp + word;
                x = x + mSpace + w;
                wordCount++;
            }
        }
            //turn antialiasing on
        drawString(canvas, line, wordCount, (int) mPaint.measureText(line), y+b1);
        //canvas.drawText("Style.FILL", 75, 110, paint);
    }

    private void drawString(Canvas canvas, String line, int wCount, int lineWidth, int y){
        Log.i(TAG, "drawString called, y = " + y);
        if (lineWidth < .75 * mWidthScreen){
            canvas.drawText(line, 5, y, mPaint);
        }
        else{
            int toFill = (mWidthScreen - lineWidth) / wCount;
            int nudge = mWidthScreen - lineWidth - (toFill * wCount);
            StringTokenizer st = new StringTokenizer(line);
            int x = 5;
            if (newFlag){
                x = 30;
                newFlag = false;
            }
            while (st.hasMoreTokens()){
                String word = st.nextToken();
                canvas.drawText(word, x, y, mPaint);
                if (nudge > 0){
                    x = x + (int) mPaint.measureText(word) + mSpace + toFill + 1;
                    nudge--;
                }
                else{
                    x = x + (int) mPaint.measureText(word) + mSpace + toFill;
                }
            }
        }
    }
}