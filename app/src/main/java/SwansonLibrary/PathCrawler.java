package SwansonLibrary;

import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;

public abstract class PathCrawler extends ValueAnimator{

    public float getmCrawlPathLength() {
        return mCrawlPathLength;
    }

    protected float mCrawlPathLength;
    protected Path mCrawlPath;
    protected PathMeasure mCrawlPathMeasure;
    protected float mPreviousValue=-1;
    protected View mCrawlingView=null;

    @Override
    public void setFloatValues(float... values) {
        //not allowed
    }

    @Override
    public void setIntValues(int... values) {
        //not allowed
    }

    @Override
    public void setObjectValues(Object... values) {
        //not allowed
    }





    public PathCrawler(Path path) {


        mCrawlPath=new Path(path);
        mCrawlPathMeasure=new PathMeasure(path,false);
        mCrawlPathLength = mCrawlPathMeasure.getLength();


        super.setFloatValues(0, mCrawlPathMeasure.getLength());


       addUpdateListener(new AnimatorUpdateListener() {
           @Override
           public void onAnimationUpdate(ValueAnimator animation) {

               float pathPostion = (Float)getAnimatedValue();


               ViewTools.PathPosition pathPosition = ViewTools.getPathPosition(mCrawlPathMeasure,pathPostion);

               if(pathPostion<mPreviousValue){
                   pathPosition.rotation=(pathPosition.rotation+Math.PI)%ViewTools.FULLCIRCLE;
               }
               mPreviousValue=pathPostion;


               if(mCrawlingView!=null){
                   ViewTools.setCenter(mCrawlingView,pathPosition.position);

                   mCrawlingView.setRotation((float) Math.toDegrees(pathPosition.rotation));
               }

               onCrawl(pathPosition);
           }
       });

    }

    public void setpath(Path path){

        mCrawlPath=new Path(path);
        mCrawlPathMeasure=new PathMeasure(path,false);
        mCrawlPathLength = mCrawlPathMeasure.getLength();

        super.setFloatValues(0, mCrawlPathMeasure.getLength());

    }


    public void setView(View view){
        mCrawlingView=view;
    }



    public boolean setSegmenent(float startD, float endD){
        Path segment = new Path();
        if(mCrawlPathMeasure.getSegment(startD,endD,segment,true)){
            mCrawlPath=segment;
            mCrawlPathMeasure.setPath(segment,false);
            mCrawlPathLength=mCrawlPathMeasure.getLength();
            super.setFloatValues(0, mCrawlPathLength);
            return true;
        }else return false;

    }
    public abstract void onCrawl(ViewTools.PathPosition pathPosition);
}
