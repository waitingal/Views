package com.as.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.as.demo.R;

/**
 * Created by waiting on 2016/8/5.
 */
public class AncientTextView extends TextView {

    private Paint paint;

    private Context mContext;
    private String mText="";

    private float textsize=30;
    private int testcolor=0xff000000;

    private int mTextWidth = 0;// 绘制宽度
    private int mTextHeight = 0;// 绘制高度

    private int LineNum=0;//总列数
    private int LineTextnum=7;//每列字数，默认值为7

    private int LineSpacing=10;//每列间距
    private int TextSpacing=7;//文字上下间距


    public AncientTextView(Context c){
        super(c,null);
    }

    public AncientTextView(Context c, AttributeSet attrs) {
        super(c,attrs);

        mContext = c;
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);//文字居中

        if(attrs!=null){
            TypedArray ta=c.obtainStyledAttributes(attrs, R.styleable.AncientText);
            SetLineSpacing((int) ta.getDimension(R.styleable.AncientText_lineSpacing,10));
            SetTextSpacing((int)ta.getDimension(R.styleable.AncientText_textSpacing,7));
            SetTextColor(ta.getColor(R.styleable.AncientText_textColor,0xff000000));
            Settextsize(ta.getDimension(R.styleable.AncientText_textSize,30));
            SetLineTextnum(ta.getInt(R.styleable.AncientText_lineTextnum,7));
            SetText(ta.getString(R.styleable.AncientText_setText));
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width=measureWidth(widthMeasureSpec);
        int height=measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);

    }

    private int measureWidth(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = SunWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);// 60,480
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = SunHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);// 60,480
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        Draw(canvas);
    }

    public void SetText(String str){
        mText = str;
        invalidate();
    }

    public String GetText(){
        return  mText;
    }

    public void SetTextColor(int color){
        testcolor = color;
        paint.setColor(testcolor);
    }

    public int GetTextColor(){
        return testcolor;
    }

    public void Settextsize(float size){
        textsize = size;
        paint.setTextSize(textsize);
    }

    public float Gettextsize(){
        return textsize;
    }


    public void SetLineSpacing(int i){
        LineSpacing = i;
    }

    public int GetLineSpacing(){
        return LineSpacing;
    }

    public void SetTextSpacing(int i){
        TextSpacing = i;
    }

    public int GetTextSpacing(){
        return TextSpacing;
    }


    public void SetLineTextnum(int i){
        LineTextnum = i;
    }

    public int GetLineTextnum(){
        return LineTextnum;
    }

    //获取每个字体高度
    private int getFontHeight(){
        FontMetrics  fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    //获取每个字体的宽度
    private int getFontWidth(){
        float[] widths = new float[1];
        paint.getTextWidths("宽", widths);//获取单个汉字的宽度
        return (int) Math.ceil(widths[0] * 1.1 + 2);
    }


    //计算列数
    private int getLineNum(){
        if(TextUtils.isEmpty(mText)){
            return 0;
        }
        int line= mText.length()%LineTextnum>0?
                mText.length()/LineTextnum+1
                :mText.length()/LineTextnum;
        return line;
    }

    //计算所有列 所需宽度
    private int SunWidth(){
        if(TextUtils.isEmpty(mText)){
            return 0;
        }
        int text_w=getFontWidth();
       int width= text_w*getLineNum()+LineSpacing*getLineNum()+getPaddingLeft()+getPaddingRight();
        return width;
    }

    //计算每列文字所需高度
    private int SunHeight(){
        if(TextUtils.isEmpty(mText) || LineTextnum<=0){
            return 0;
        }
        int heigth=getFontWidth()*LineTextnum+(LineTextnum-1)*TextSpacing;
        return heigth;
    }

    private void Draw(Canvas canvas){
        int w=getMeasuredWidth();
        int h=getMeasuredHeight();

//        int w=getWidth();
//        int h=getHeight();


        int Tw=getFontWidth();
        int Th=getFontHeight();

         LineTextnum=h/getFontHeight();//计算每列字数
         LineNum= getLineNum();



        for (int i=0; i<LineNum;i++){
           String str="";
            if(i*LineTextnum+LineTextnum>=mText.length()){
                str = mText.substring(i*LineTextnum,mText.length());
            }else{
                str = mText.substring(i*LineTextnum,i*LineTextnum+LineTextnum);
            }
            for (int j=0;j<str.length();j++){
                canvas.drawText(String.valueOf(str.charAt(j)),w-(Tw*(i+1)),j*Th+Th,paint);
            }

        }
    }
}
