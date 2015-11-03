package SwansonLibrary;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by Brandon on 5/1/14.
 */
public class PathPlus extends Path {

    private PathMeasure mPathMeasuere = new PathMeasure(this,false);
    public PathMeasure measure(){return mPathMeasuere;}

    private boolean isClosed=false;
    private boolean isClosed(){return isClosed;}

    public PathPlus() {

    }

    public PathPlus(Path src, boolean forceClosed) {
        super(src);
        isClosed=true;
    }

    public Path makeLine(Point start, Point end){

       ViewTools.Line line = new ViewTools.Line(start,end);

        return makeLine(line);
    }
    public PathPlus makeLine(ViewTools.Line line){

        moveTo(line.start.x,line.start.y);
        lineTo(line.end.x,line.end.y);

        return this;
    }

    public PathPlus makeLine(PointF start, PointF end){
        moveTo(start.x,start.y);
        lineTo(end.x,end.y);
        return this;
    }

    public static PathPlus textLine(Point start, Point end){
        return textLine(new ViewTools.Line(start, end));
    }
    public static PathPlus textLine(ViewTools.Line line){
        return new PathPlus().makeLine(line);
    }
    public static PathPlus textLine(PointF start, PointF end){
        return new PathPlus().makeLine(start,end);
    }

    public static void getDrawableSegment(Path segment, Path dstPath){
        getDrawableSegment(segment,dstPath,.1f);
    }

    public static void getDrawableSegment(Path segment, Path dstPath, float granularity){
        dstPath.rewind();
        PathMeasure pathMeasure = new PathMeasure(segment,false);
        float length = pathMeasure.getLength();
        ViewTools.PathPosition pathPosition = ViewTools.getPathPosition(pathMeasure,0);
        dstPath.moveTo(pathPosition.position.x,pathPosition.position.y);
        for(float distance=0;distance<=length;distance+=granularity){
            pathPosition=ViewTools.getPathPosition(pathMeasure,distance);
            dstPath.lineTo(pathPosition.position.x,pathPosition.position.y);
        }

    }

    /*public void Close(){
        if(isClosed) return;
        float pos[] = new float[2];
        float tan[] = new float[2];
        mPathMeasuere.getPosTan(0,pos,tan);
        lineTo(pos[0],pos[1]);

        isClosed=true;
    }*/

    @Override
    public void close(){
        super.close();
        isClosed=true;
    }

}
