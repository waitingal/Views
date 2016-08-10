package com.as.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import com.as.demo.R;

/**
 * Created by waiting on 2016/8/10.
 */
public class ArcProgress extends View {

    public static final int Rad180=1,Rad225=2,Rad360=3;

    private Context mContext;
    private Paint paint,paint1,paint2;

    private int radius=70;//半径
    private float lineWidth=7;//圆弧线条宽度
    private float textSize=50;//字体大小

    private int textColor=0xff20CE66;//字体颜色

    private int roundColor=0xff1ABDE6;//圆弧第一层颜色
    private int secondColor=0xFFFF88E7;//圆弧第二层颜色

    private int[] SecondColorArr=new int[]{0xFFFF88E7};

    private int width,height;

    private int type=Rad180;//圆弧类型

    private float MaxProgres=100,Progress=0;//最大进度和当前进度

    private float currentValue = 0f;

    public ArcProgress(Context context) {
        super(context,null);
    }

    public ArcProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if(attrs!=null){
            TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.myArcProgress);
            Setradius((int)ta.getDimension(R.styleable.myArcProgress_arc_radius,0));
            SetlineWidth((int)ta.getDimension(R.styleable.myArcProgress_arc_lineWidth,0));
            SettextSize((int)ta.getDimension(R.styleable.myArcProgress_arc_textSize,0));
            SetroundColor((int)ta.getColor(R.styleable.myArcProgress_arc_roundColor,0xffffffff));
            SettextColor((int)ta.getColor(R.styleable.myArcProgress_arc_textColor,0xffffffff));
            SetType(ta.getInt(R.styleable.myArcProgress_arc_type,Rad180));
            SetMax(ta.getFloat(R.styleable.myArcProgress_arc_maxProgress,100));
            SetProgress(ta.getFloat(R.styleable.myArcProgress_arc_Progress,0));
            SetsecondColor(ta.getColor(R.styleable.myArcProgress_arc_secondColor,0xffffffff));
        }

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);//圆角圆弧
        paint.setColor(roundColor);

        paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(lineWidth);
        paint1.setStrokeCap(Paint.Cap.ROUND);//圆角圆弧
        paint1.setColor(secondColor);

        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setTextSize(textSize);
        paint2.setColor(textColor);
        paint2.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        width=radius*2+getPaddingLeft()+getPaddingRight()+(int)lineWidth;
        height=radius*2+getPaddingTop()+getPaddingBottom()+(int)lineWidth;

        setMeasuredDimension(width, height);
    }

    public void SetMax(float i){
        MaxProgres = i;
    }

    public float GetMax(){
        return MaxProgres;
    }

    public void SetProgress(float i){

        Progress = i;
        if(Progress>MaxProgres){
            Progress=MaxProgres;
        }
        //invalidate();
        currentValue=0;
        setValue(Progress);
    }

    public float GetProgress(){
        return Progress;
    }

    public void Setradius(int i){
        radius = i;
    }

    public int Getradius(){
        return radius;
    }

    public void SetlineWidth(int i){
        lineWidth=i;
    }

    public void SettextSize(int i){
        textSize = i;
    }

    public void SetroundColor(int i){
        roundColor = i;
    }

    public void SetsecondColor(int i){
        secondColor = i;
    }

    public void SettextColor(int i){
        textColor = i;
    }

    public void SetType(int i){
        if(i!=Rad180 && i!=Rad225 && i!=Rad360){
            type=Rad180;
        }else{
            type=i;
        }
    }

    public int GetType(){
        return type;
    }

    public void SetSecondColorArr(int[] arr){
        if(arr== null || arr.length<=1){
           return;
        }else{
            SecondColorArr = arr;
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        DrawPath(canvas);
        DrawSecondPath(canvas);
        DrawProgressText(canvas);
    }

    private void DrawPath(Canvas canvas){

        RectF rectF=new RectF();
        rectF.left=width/2-radius;
        rectF.top=width/2-radius;
        rectF.right=width/2+radius;
        rectF.bottom=width/2+radius;

        if(type==Rad180){
            canvas.drawArc(rectF,-180,180,false,paint);
        }else if(type==Rad225){
            canvas.drawArc(rectF,-225,270,false,paint);
        }else if(type==Rad360){
            canvas.drawArc(rectF,0,360,false,paint);
        }
    }

    private void DrawSecondPath(Canvas canvas){

        RectF rectF=new RectF();
        rectF.left=width/2-radius;
        rectF.top=width/2-radius;
        rectF.right=width/2+radius;
        rectF.bottom=width/2+radius;


        int DrawProgress = GetProgressRad();

        if(DrawProgress<=0){
            return;
        }
        Log.e("","DrawProgress==="+DrawProgress);


        if(type==Rad180){
            canvas.drawArc(rectF,-180,DrawProgress,false,paint1);
        }else if(type==Rad225){
            canvas.drawArc(rectF,-225,DrawProgress,false,paint1);
        }else if(type==Rad360){
            canvas.drawArc(rectF,-90,DrawProgress,false,paint1);
        }

    }

    private int GetProgressRad(){
        if(MaxProgres<=0 || Progress<=0){
            return 0;
        }

        Log.e("","Progress==="+Progress);
        if(type==Rad180){
            return (int)(currentValue*((float)(180/MaxProgres)));
        }else if(type==Rad225){
            return (int)(currentValue*((float)(270/MaxProgres)));
        }else if(type==Rad360){
            return (int)(currentValue*((float)(360/MaxProgres)));
        }
        return 0;
    }


    private void DrawProgressText(Canvas canvas){

            String text="";
        if(currentValue>=MaxProgres){
            text="100%";
        }else if(MaxProgres<=0 || Progress<=0){
            text="0%";
        }else{
            text=(int)((currentValue/MaxProgres)*100)+"%";
        }
        float textWidth = paint2.measureText(text);   //测量字体宽度

        if(type==Rad225||type==Rad360){
            Paint.FontMetricsInt fontMetrics = paint2.getFontMetricsInt();
            int baseline = height/2 + (fontMetrics.descent- fontMetrics.ascent)/2 - fontMetrics.descent;
            canvas.drawText(text,width/2,baseline,paint2);
        }else{
            canvas.drawText(text,width/2,width/2,paint2);
        }

    }

    public void setValue(float value) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentValue, value);
        valueAnimator.setDuration(6000);
        valueAnimator.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float v) {
                return 1-(1-v)*(1-v)*(1-v);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();


    }

}
