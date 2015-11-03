package TestFrames;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import SwansonLibrary.ViewTools;
import SwansonLibrary.WindyPath;

/**
 * Created by Brandon on 5/7/14.
 */
public class testWindy extends WindyPath{
    private ArrayList<Point> PointsList = new ArrayList<Point>();
    private int mPointCount=0;
    public testWindy(Rect bounds, int LineNumber){

        inflateData(LineNumber, mMinLengthPercentageDEFAULT, MINIMUM_ANGLE_DEFAULT, bounds);

        super.generate();

        for(int i=0;i<mLinePoints.length;i++)PointsList.add(mLinePoints[0]);


    }


    @Override
    public void generate() {

        mPointCount++;
        if(mPointCount%1000==0) Log.d("PATH", "Point number:" + mPointCount);


        rewind();
        PointsList.remove(0);
        int indexOfLast=PointsList.size()-1;
        PointsList.add(mPointGenerator.makeMap(PointsList.get(indexOfLast-1),PointsList.get(indexOfLast)).getPoint());
        //mLinePoints= (Point[]) PointsList.toArray();
        mLinePoints=PointsList.toArray(new Point[PointsList.size()]);
        makeCurves();

        if(ViewTools.getDistancetoNonSQRTD(PointsList.get(indexOfLast),PointsList.get(indexOfLast-1))<5){
            Log.d("PATH", "Uh-OH, We might be stuck");
        }

    }
}
