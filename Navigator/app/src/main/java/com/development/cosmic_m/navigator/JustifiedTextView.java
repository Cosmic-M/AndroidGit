package com.development.cosmic_m.navigator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.util.StringTokenizer;

/**
 * Created by Cosmic_M on 21.09.2017.
 */

public class JustifiedTextView extends View {
    private static final String TAG = "TAG";
    private String mText;
    private Paint mPaint;
    private Paint.FontMetricsInt mFontMetrics;
    private int mSpace;
    private int mIndent;
    private Context mContext;
    private int mWidthScreen;
    private boolean flag, newFlag;
    int mHeightScreen = 0;


    public JustifiedTextView(Context context){
        super(context);
        Log.i(TAG, "JustifiedTextView/JustifiedTextView(Context context)");
        mContext = context;
    }

    public JustifiedTextView(Context context, AttributeSet attr){
        super(context, attr);
        Log.i(TAG, "JustifiedTextView/JustifiedTextView(Context context, AttributeSet attr)");
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure");
        int desiredWidth = mWidthScreen;
        int desiredHeight = mHeightScreen + 5;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    public void setText(String text){
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWidthScreen = wm.getDefaultDisplay().getWidth();
        mText = text;
        draw(new Canvas());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "JustifiedTextView/onDraw called");
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
        mIndent = (int) mPaint.measureText("    ");
        flag = true;

        if (mText == null) return;
        StringTokenizer stringTokenizer = new StringTokenizer(mText);
        while (stringTokenizer.hasMoreTokens()){
            word = stringTokenizer.nextToken();
            if (word.equals("<P>")){
                drawString(canvas, line, wordCount, (int) mPaint.measureText(line), y+b1);
                line = "";
                wordCount = 0;
                x = mIndent;
                y = y + (fh * 2);
                flag = true;
            }
            else{
                if (flag){
                    x = mIndent;
                    flag = false;
                    newFlag = true;
                }
                int w = (int) mPaint.measureText(word);
                if ((nextX = (x + mSpace + w)) > mWidthScreen - 5){
                    drawString(canvas, line, wordCount, (int) mPaint.measureText(line), y+b1);
                    line = "";
                    wordCount = 0;
                    x = 5;
                    y = y + fh;
                }
                if (x == 5){
                    sp = "";
                }
                else if (x == mIndent){
                    sp = "   ";
                }
                else{
                    sp = " ";
                }
                line += sp + word;
                x += mSpace + w;
                wordCount++;
            }
        }
        //turn antialiasing on
        drawString(canvas, line, wordCount, (int) mPaint.measureText(line), y+b1);
        //canvas.drawText("Style.FILL", 75, 110, paint);
    }

    private void drawString(Canvas canvas, String line, int wCount, int lineWidth, int y){
        mHeightScreen = y;
        Log.i(TAG, "JustifiedTextView/drawString() called");
        if (lineWidth < .75 * mWidthScreen){
            canvas.drawText(line, 5, y, mPaint);
        }
        else{
            int toFill = (mWidthScreen - 5 - lineWidth) / wCount;
            int nudge = mWidthScreen - 5 - lineWidth - (toFill * wCount);
            StringTokenizer st = new StringTokenizer(line);
            int x = 5;
            if (newFlag){
                x = mIndent;
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