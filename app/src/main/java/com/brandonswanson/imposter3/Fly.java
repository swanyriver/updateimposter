package com.brandonswanson.imposter3;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import SwansonLibrary.PathCrawler;
import SwansonLibrary.ViewTools;
import SwansonLibrary.WindyPath;

/**
 * Created by Brandon on 4/24/14.
 */
public class Fly extends ImageView {

    private BrainForImposter mBrain;
    private final int MyID = BrainForImposter.FLY;
    private Path mWindyPath;
    private PathCrawler mPathCrawler;

    private long mSpeed = 900*7; //time to cross diagonal


    public Fly(Context context,BrainForImposter brain, Rect bounds, float Speed){
        super(context);
        Speed = (float) Math.pow(Speed,-1);
        mSpeed= (long) (mSpeed*Speed);
        setup(brain,bounds);
    }



    public Fly(Context context,BrainForImposter brain, Rect bounds) {
        super(context);
        setup(brain,bounds);

    }

    private void setup(BrainForImposter brain, Rect bounds) {
        mBrain = brain;

        setLayoutParams(new FrameLayout.LayoutParams(100,100));
        setImageResource(R.drawable.fly);


        double diagonal=ViewTools.getHypotenuse(bounds.width(),bounds.height());


        mWindyPath= new WindyPath(bounds,250);

        mPathCrawler = new PathCrawler(mWindyPath) {
            @Override
            public void onCrawl(ViewTools.PathPosition pathPosition) {
                setRotation(getRotation()+90);
                mBrain.lookHere((int)pathPosition.position.x,(int)pathPosition.position.y,BrainForImposter.FLY);
            }
        };

        mPathCrawler.setView(this);
        mPathCrawler.setDuration((long) ((mPathCrawler.getmCrawlPathLength()/diagonal)*mSpeed));
        mPathCrawler.setInterpolator(new LinearInterpolator());
        mPathCrawler.setRepeatCount(ValueAnimator.INFINITE);
        mPathCrawler.setRepeatMode(ValueAnimator.REVERSE);
        mPathCrawler.start();
    }

}
