package com.sippitechnologes.annotateapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


public class IconCropView extends View {

    //contants strings
    private static final String TAG = "IconCropView";

    //drawing objects
    private Paint paint,painttxt;

    //point objects
    private Point[] points;
    private Point start;
    private Point offset;

    //variable ints
    private int minimumSideLength;
    private int sideX, sideY,side;
    private int halfCorner;
    private int cornerColor;
    private int edgeColor;
    private int textColor;
    private int outsideColor;
    private int corner = 5;
    private int textZize;
    private String text;
    private Typeface bold;

    //variable booleans
    private boolean initialized = false;

    //drawables
    private Drawable moveDrawable1;
    private Drawable resizeDrawable1, resizeDrawable2, resizeDrawable3;

    //context
    Context context;

    float x1, x2, y1, y2, dx, dy;
    String direction;


    public IconCropView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public IconCropView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public IconCropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    public IconCropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs){

        paint = new Paint();
        painttxt = new Paint();
        start = new Point();
        offset = new Point();

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconCropView, 0, 0);

        //initial dimensions
        minimumSideLength = ta.getDimensionPixelSize(R.styleable.IconCropView_minimumSide, 20);
        sideX = minimumSideLength;
        sideY = minimumSideLength;
        side = minimumSideLength;
        halfCorner = (ta.getDimensionPixelSize(R.styleable.IconCropView_edgecornerSize, 20))/2;
        textZize = ta.getDimensionPixelSize(R.styleable.IconCropView_android_textSize, 22);


        text = ta.getString(R.styleable.IconCropView_android_text);
        bold = Typeface.create("",Typeface.BOLD);
        //colors
        cornerColor = ta.getColor(R.styleable.IconCropView_cornerColor, Color.BLACK);
        edgeColor = ta.getColor(R.styleable.IconCropView_edgeColor, Color.WHITE);
        textColor = edgeColor;
        outsideColor = ta.getColor(R.styleable.IconCropView_outsideCropColor, Color.parseColor("#00000088"));

        //initialize corners;
        points = new Point[4];

        points[0] = new Point();
        points[1] = new Point();
        points[2] = new Point();
        points[3] = new Point();

        //init corner locations;
        //top left
        points[0].x = 0;
        points[0].y = 0;

        //top right
        points[1].x = minimumSideLength;
        points[1].y = 0;

        //bottom left
        points[2].x = 0;
        points[2].y = minimumSideLength;

        //bottom right
        points[3].x = minimumSideLength;
        points[3].y = minimumSideLength;

        //init drawables
        moveDrawable1 = ta.getDrawable(R.styleable.IconCropView_moveCornerDrawable);
        resizeDrawable1 = ta.getDrawable(R.styleable.IconCropView_resizeCornerDrawable);
        resizeDrawable2 = ta.getDrawable(R.styleable.IconCropView_resizeCornerDrawable);
        resizeDrawable3 = ta.getDrawable(R.styleable.IconCropView_resizeCornerDrawable);

        //set drawable colors
        moveDrawable1.setTint(cornerColor);
        resizeDrawable1.setTint(cornerColor);
        resizeDrawable2.setTint(cornerColor);
        resizeDrawable3.setTint(cornerColor);

        //recycle attributes
        ta.recycle();

        //set initialized to true
        initialized = true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //set paint to draw edge, stroke
        if(initialized) {
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setColor(edgeColor);
            paint.setStrokeWidth(18);

            painttxt.setAntiAlias(true);
            painttxt.setStyle(Paint.Style.FILL_AND_STROKE);
            painttxt.setColor(textColor);
            painttxt.setTextSize(textZize);
            painttxt.setTypeface(bold);

            //crop rectangle
            canvas.drawRect(points[0].x + halfCorner, points[0].y + halfCorner, points[0].x + halfCorner + sideX, points[0].y + halfCorner + sideY, paint);
            canvas.drawText(text,points[0].x + halfCorner+20,points[0].y + halfCorner-20,painttxt);
            //set paint to draw outside color, fill
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(outsideColor);

            //top rectangle
            canvas.drawRect(0, 0, canvas.getWidth(), points[0].y + halfCorner, paint);
            //left rectangle
            canvas.drawRect(0, points[0].y + halfCorner, points[0].x + halfCorner, canvas.getHeight(), paint);
            //right rectangle
            canvas.drawRect(points[0].x + halfCorner + sideX, points[0].y + halfCorner, canvas.getWidth(), points[0].y + halfCorner + sideY, paint);
            //bottom rectangle
            canvas.drawRect(points[0].x + halfCorner, points[0].y + halfCorner + sideY, canvas.getWidth(), canvas.getHeight(), paint);


            //set bounds of drawables
            moveDrawable1.setBounds(points[0].x, points[0].y, points[0].x + halfCorner*2, points[0].y + halfCorner*2);
            resizeDrawable1.setBounds(points[1].x, points[1].y, points[1].x + halfCorner*2, points[1].y + halfCorner*2);
            resizeDrawable2.setBounds(points[2].x, points[2].y, points[2].x + halfCorner*2, points[2].y + halfCorner*2);
            resizeDrawable3.setBounds(points[3].x, points[3].y, points[3].x + halfCorner*2, points[3].y+ halfCorner*2);


            //place corner drawables
            moveDrawable1.draw(canvas);
            resizeDrawable1.draw(canvas);
            resizeDrawable2.draw(canvas);
            resizeDrawable3.draw(canvas);


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN: {
                x1 = event.getX();
                y1 = event.getY();
                //get the coordinates
                start.x = (int) event.getX();
                start.y = (int) event.getY();

                //get the corner touched if any
                corner = getCorner(start.x, start.y);

                //get the offset of touch(x,y) from corner top-left point
                offset = getOffset(start.x, start.y, corner);

                //account for touch offset in starting point
                start.x = start.x - offset.x;
                start.y = start.y - offset.y;

                break;
            }
            case MotionEvent.ACTION_UP: {

            }
            case MotionEvent.ACTION_MOVE: {
                x2 = event.getX();
                y2 = event.getY();
                dx = x2-x1;
                dy = y2-y1;

                // Use dx and dy to determine the direction of the move
                if(Math.abs(dx) > Math.abs(dy)) {
                    if(dx>0)
                        direction = "right";
                    else
                        direction = "left";
                } else if(Math.abs(dx) < Math.abs(dy)) {
                    if(dy>0)
                        direction = "down";
                    else
                        direction = "up";
                }
                else direction = "diagonal";



                if (corner == 1) {
                    if (direction.equals("left") || direction.equals("right") ) {

                        sideX = Math.min(Math.min((Math.max(minimumSideLength, (int) (sideX + Math.floor(event.getX()) - start.x - offset.x))), sideX + (getWidth() - points[1].x - 2 * halfCorner)), sideX + (getHeight() - points[2].y - 2 * halfCorner));
                        points[1].x = points[0].x + sideX;
                        points[3].x = points[0].x + sideX;
                        start.x = points[1].x;
                    }
                    invalidate();
                } else if (corner == 2) {
                    if (direction.equals("up") || direction.equals("down") ) {
                        sideY = Math.min((Math.min((Math.max(minimumSideLength, (int) (sideY + Math.floor(event.getY()) - start.y - offset.y))), sideY + (getHeight() - points[2].y - 2 * halfCorner))), sideY + (getWidth() - points[1].x - 2 * halfCorner));
                        points[2].y = points[0].y + sideY;
                        points[3].y = points[0].y + sideY;
                        start.y = points[2].y;
                    } else if (direction.equals("left") || direction.equals("right") )  {
                    }

                    invalidate();

                }
                else if(corner == 3)
                {
                    if (direction.equals("left") || direction.equals("right") )
                    {
                        sideX = Math.min(Math.min((Math.max(minimumSideLength, (int) (sideX + Math.floor(event.getX()) - start.x - offset.x))), sideX + (getWidth() - points[1].x - 2 * halfCorner)), sideX + (getHeight() - points[2].y - 2 * halfCorner));
                        points[1].x = points[0].x + sideX;
                        points[3].x = points[0].x + sideX;
                        start.x = points[1].x;
                    }
                    else if (direction.equals("up") || direction.equals("down"))
                    {
                        sideY = Math.min((Math.min((Math.max(minimumSideLength, (int) (sideY + Math.floor(event.getY()) - start.y - offset.y))), sideY + (getHeight() - points[2].y - 2 * halfCorner))), sideY + (getWidth() - points[1].x - 2 * halfCorner));
                        points[2].y = points[0].y + sideY;
                        points[3].y = points[0].y + sideY;
                        start.y = points[2].y;
                    }
                    else if (direction.equals("diagonal"))
                    {
                        side = Math.min((Math.min((Math.min((Math.max(minimumSideLength, (int)(side + Math.floor(event.getX()) - start.x - offset.x))), side + (getWidth() - points[3].x - 2* halfCorner ))),side + (getHeight() - points[3].y - 2* halfCorner ))), Math.min((Math.min((Math.max(minimumSideLength, (int)(side + Math.floor(event.getY()) - start.y - offset.y))), side + (getHeight() - points[3].y - 2* halfCorner ))),side + (getWidth() - points[3].x - 2* halfCorner )));
                        points[1].x = points[0].x + side;
                        points[3].x = points[0].x + side;
                        points[3].y = points[0].y + side;
                        points[2].y = points[0].y + side;
                        start.x = points[3].x;

                        points[2].y = points[0].y + side;
                        points[3].y = points[0].y + side;
                        points[3].x = points[0].x + side;
                        points[1].x = points[0].x + side;
                        start.y = points[3].y;
                    }
                    invalidate();

                }
                /*else if (corner == 0 )
                {
                    if (direction.equals("left") || direction.equals("right") )
                    {
                        sideX = Math.min(Math.min((Math.max(minimumSideLength, (int) (sideX + Math.floor(event.getX()) - start.x - offset.x))), sideX + (getWidth() - points[0].x - 2 * halfCorner)), sideX + (getHeight() - points[1].y - 2 * halfCorner));
                        points[0].x = points[1].x + sideX;
                        points[2].x = points[1].x + sideX;
                        start.x = points[0].x;
                    }
                    invalidate();
                }*/
                else {
                    points[0].x = Math.max(points[0].x + (int) Math.min(Math.floor((event.getX() - start.x - offset.x)), Math.floor(getWidth() - points[0].x - 2 * halfCorner - sideX)), 0);
                    points[1].x = Math.max(points[1].x + (int) Math.min(Math.floor((event.getX() - start.x - offset.x)), Math.floor(getWidth() - points[1].x - 2 * halfCorner)), sideX);
                    points[2].x = Math.max(points[2].x + (int) Math.min(Math.floor((event.getX() - start.x - offset.x)), Math.floor(getWidth() - points[2].x - 2 * halfCorner - sideX)), 0);
                    points[3].x = Math.max(points[3].x + (int) Math.min(Math.floor((event.getX() - start.x - offset.x)), Math.floor(getWidth() - points[3].x - 2 * halfCorner)), sideX);

                    points[0].y = Math.max(points[0].y + (int) Math.min(Math.floor((event.getY() - start.y - offset.y)), Math.floor(getHeight() - points[0].y - 2 * halfCorner - sideY)), 0);
                    points[1].y = Math.max(points[1].y + (int) Math.min(Math.floor((event.getY() - start.y - offset.y)), Math.floor(getHeight() - points[1].y - 2 * halfCorner - sideY)), 0);
                    points[2].y = Math.max(points[2].y + (int) Math.min(Math.floor((event.getY() - start.y - offset.y)), Math.floor(getHeight() - points[2].y - 2 * halfCorner)), sideY);
                    points[3].y = Math.max(points[3].y + (int) Math.min(Math.floor((event.getY() - start.y - offset.y)), Math.floor(getHeight() - points[3].y - 2 * halfCorner)), sideY);

                    start.x = points[0].x;
                    start.y = points[0].y;
                    invalidate();
                }
                break;
            }
        }
        return true;
    }

    private int getCorner(float x, float y){
        int corner = 5;
        for (int i = 0; i < points.length; i++){
            float dx = x - points[i].x;
            float dy = y - points[i].y;
            int max = halfCorner * 2;
            if(dx <= max && dx >= 0 && dy <= max && dy >= 0){
                return i;
            }
        }
        return corner;
    }

    private Point getOffset(int left, int top, int corner){
        Point offset = new Point();
        if(corner == 5){
            offset.x = 0;
            offset.y = 0;
        }else{
            offset.x = left - points[corner].x ;
            offset.y = top - points[corner].y ;
        }
        return offset;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public Void setText(String annotation_name)
    {
        this.text = annotation_name;
        return null;

    }
    public Void changeEdgeColor(int color)
    {
        this.edgeColor = color;
        return null;
    }
    public Void changetextColor(int color1)
    {
        this.textColor = color1;
        return null;
    }
}