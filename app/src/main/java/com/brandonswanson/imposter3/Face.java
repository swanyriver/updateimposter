package com.brandonswanson.imposter3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import EyeBalls.EyeBall;
import EyeBalls.EyeDrawables;
import EyeBalls.EyeFocus;
import EyeBalls.EyeSet;
import SwansonLibrary.ViewTools;



/**
 * Created by Brandon on 4/18/14.
 */
public class Face extends FrameLayout{

    private Face MySelf = this;



    private EyeSet mEyeset;

    private int[] mCenterInWindow = new int[]{0,0};
    private int mWidth;
    private int mHeigth;

    public int getCenterX(){return mCenterInWindow[0];}
    public int getCenterY(){return mCenterInWindow[1];}

    private int mFocuesTarget = BrainForImposter.UNFOCUSED;
    public int getFocus() {return mFocuesTarget; }
    public void setFocus(int focusTarget){
        mFocuesTarget=focusTarget;
        if(focusTarget==BrainForImposter.UNFOCUSED)Eyes().request(EyeBall.PLEASE_LOOSE_FOCUS);
    }


    public Face(Context context, int width, int height) {
        super(context);

        mWidth=width;
        mHeigth=height;

        setBackgroundResource(R.drawable.face);


        post(new Runnable() {
            @Override
            public void run() {
                ViewTools.findCenterInWindow(MySelf, mCenterInWindow);
            }
        });

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ViewTools.findCenterInWindow(MySelf,mCenterInWindow);
            }
        });

        setWillNotDraw(false);

        setLayoutParams(new LayoutParams(mWidth, mWidth));


        mEyeset=new EyeSet(this);
        mEyeset.addEyeball((int)(mWidth*.45),(int)(mWidth*.45),mWidth/3,(mWidth/3)*1.25f,EyeDrawables.getEyeDraw(2,getContext()));
        mEyeset.addEyeball((int)(mWidth*.45),(int)(mWidth*.45),mWidth/3*2,(mWidth/3)*1.25f,EyeDrawables.getEyeDraw(2,getContext()));


    }


    public EyeFocus Eyes(){
        return mEyeset;
    }
    public void SetEyeAdjustDuration(boolean snappy){
        mEyeset.setAdjustDuration(snappy);
    }
}
