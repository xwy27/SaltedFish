package hongzicong.saltedfish.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import hongzicong.saltedfish.R;
import hongzicong.saltedfish.adapter.TableViewAdapter;
import hongzicong.saltedfish.utils.SharedPreferencesUtils;
import hongzicong.saltedfish.utils.UIUtils;
import hongzicong.saltedfish.utils.Util;

/**
 * Created by DELL-PC on 2017/12/31.
 */

public class TableView extends View {

    protected TableViewAdapter mAdapter;

    private int itemWidth = 94;
    private int itemHeight = 94;
    private int textSize=60;
    private int monthSize=55;
    private int totalSize=120;
    private int daySize=120;
    private int itemSpace = 18;
    private int rightTextPadding=20;
    private int leftTextPadding=150;
    private int topTextPadding=140;
    private int bottomTextPadding=200;
    private RectF rectF;

    private Paint paintEmpty = new Paint();
    private Paint paintFill_1 = new Paint();
    private Paint paintFill_2 = new Paint();
    private Paint paintFill_3 = new Paint();
    private Paint paintFill_4 = new Paint();
    private Paint paintFill_5 = new Paint();
    private Paint paintFill_6 = new Paint();
    private Paint paintText=new Paint();

    public final static int BLUE = 0;

    public final static int RED = 1;

    public final static int PURPLE = 2;

    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setColor();
        rectF = new RectF(0, 0, itemWidth, itemHeight);
    }

    private void setColor(){
        paintEmpty.setColor(ContextCompat.getColor(getContext(),R.color.empty_contri));

        int style = SharedPreferencesUtils.getInstance().getInt("style", 0);

        switch (style){
            case BLUE:
                paintFill_1.setColor(ContextCompat.getColor(getContext(),R.color.fill_1_blue));
                paintFill_2.setColor(ContextCompat.getColor(getContext(),R.color.fill_2_blue));
                paintFill_3.setColor(ContextCompat.getColor(getContext(),R.color.fill_3_blue));
                paintFill_4.setColor(ContextCompat.getColor(getContext(),R.color.fill_4_blue));
                paintFill_5.setColor(ContextCompat.getColor(getContext(),R.color.fill_5_blue));
                paintFill_6.setColor(ContextCompat.getColor(getContext(),R.color.fill_6_blue));
                break;
            case RED:
                paintFill_1.setColor(ContextCompat.getColor(getContext(),R.color.fill_1_red));
                paintFill_2.setColor(ContextCompat.getColor(getContext(),R.color.fill_2_red));
                paintFill_3.setColor(ContextCompat.getColor(getContext(),R.color.fill_3_red));
                paintFill_4.setColor(ContextCompat.getColor(getContext(),R.color.fill_4_red));
                paintFill_5.setColor(ContextCompat.getColor(getContext(),R.color.fill_5_red));
                paintFill_6.setColor(ContextCompat.getColor(getContext(),R.color.fill_6_red));
                break;
            case PURPLE:
                paintFill_1.setColor(ContextCompat.getColor(getContext(),R.color.fill_1_purple));
                paintFill_2.setColor(ContextCompat.getColor(getContext(),R.color.fill_2_purple));
                paintFill_3.setColor(ContextCompat.getColor(getContext(),R.color.fill_3_purple));
                paintFill_4.setColor(ContextCompat.getColor(getContext(),R.color.fill_4_purple));
                paintFill_5.setColor(ContextCompat.getColor(getContext(),R.color.fill_5_purple));
                paintFill_6.setColor(ContextCompat.getColor(getContext(),R.color.fill_6_purple));
                break;
        }
        paintEmpty.setAntiAlias(true);
        paintFill_1.setAntiAlias(true);
        paintFill_2.setAntiAlias(true);
        paintFill_3.setAntiAlias(true);
        paintFill_4.setAntiAlias(true);
        paintFill_5.setAntiAlias(true);
        paintFill_6.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int row = 0;
        int column = 0;
        if (this.mAdapter != null) {
            row = mAdapter.getRowCount();
            column = mAdapter.getColumnCount();
        }
        int measureWidth = (column == 0 ? 0 : column * (itemWidth + itemSpace) - itemSpace) + getPaddingLeft() + getPaddingRight()+leftTextPadding+rightTextPadding;
        int measureHeight = (row == 0 ? 0 : row * (itemHeight + itemSpace) - itemSpace) + getPaddingTop() + getPaddingBottom() + topTextPadding + bottomTextPadding;

        int mWidth = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.getSize(widthMeasureSpec) : measureWidth;
        int mHeight = heightMode == MeasureSpec.EXACTLY ? MeasureSpec.getSize(heightMeasureSpec) : measureHeight;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //裁剪画布
        canvas.clipRect(getPaddingLeft(), getPaddingTop(), getRight()- getPaddingRight(), getBottom() - getPaddingBottom());

        //pixel 为单位
        if (mAdapter != null) {
            final int columnCount = mAdapter.getColumnCount();
            final int rowCount = mAdapter.getRowCount();
            final int currDay=mAdapter.getCurrentDay();

            //编写星期
            paintText.setTextSize(textSize);
            paintText.setAntiAlias(true);
            paintText.setTextAlign(Paint.Align.CENTER);
            paintText.setColor(ContextCompat.getColor(getContext(),R.color.week_text));
            drawText(canvas,"M",paintText,leftTextPadding+getPaddingLeft()+itemWidth + itemSpace+itemWidth/2,100);
            drawText(canvas,"W",paintText,leftTextPadding+getPaddingLeft()+3*(itemWidth + itemSpace)+itemWidth/2,100);
            drawText(canvas,"F",paintText,leftTextPadding+getPaddingLeft()+5*(itemWidth + itemSpace)+itemWidth/2,100);

            //编写月份
            paintText.setTextSize(monthSize);
            paintText.setAntiAlias(true);
            paintText.setTextAlign(Paint.Align.RIGHT);
            paintText.setColor(ContextCompat.getColor(getContext(),R.color.month_text));
            drawText(canvas,"Jan",paintText,leftTextPadding-40,getPaddingTop()+topTextPadding+monthSize+2*(itemHeight + itemSpace)+5);
            if(currDay>334) {
                drawText(canvas, "Dec", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 49 * (itemHeight + itemSpace)+5);
            }
            if(currDay>304){
                drawText(canvas, "Nov", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 45 * (itemHeight + itemSpace)+5);
            }
            if(currDay>273){
                drawText(canvas, "Oct", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 41 * (itemHeight + itemSpace)+5);
            }
            if(currDay>243){
                drawText(canvas, "Sep", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 36 * (itemHeight + itemSpace)+5);
            }
            if(currDay>212){
                drawText(canvas, "Aug", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 32 * (itemHeight + itemSpace)+5);
            }
            if(currDay>181){
                drawText(canvas, "Jul", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 27 * (itemHeight + itemSpace)+5);
            }
            if(currDay>151){
                drawText(canvas, "Jun", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 23 * (itemHeight + itemSpace)+5);
            }
            if(currDay>120){
                drawText(canvas, "May", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 19 * (itemHeight + itemSpace)+5);
            }
            if(currDay>90){
                drawText(canvas, "Apr", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 14 * (itemHeight + itemSpace)+5);
            }
            if(currDay>59){
                drawText(canvas, "Mar", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 10 * (itemHeight + itemSpace)+5);
            }
            if(currDay>31){
                drawText(canvas, "Feb", paintText,leftTextPadding-40 , getPaddingTop() + topTextPadding + monthSize + 6 * (itemHeight + itemSpace)+5);
            }

            int tempDay=0;
            for (int week = 0; week < rowCount; ++week) {
                for (int day = 0; day < columnCount; ++day,++tempDay) {
                    if(tempDay==currDay){
                        return;
                    }
                    rectF.left = day * (itemWidth + itemSpace) + getPaddingLeft()+leftTextPadding;
                    rectF.right = rectF.left + itemWidth ;
                    rectF.top = week * (itemHeight + itemSpace) + getPaddingTop()+topTextPadding;
                    rectF.bottom = rectF.top + itemHeight;

                    final int level = mAdapter.getLevel(Util.getDay(week,day));
                    final Paint paintByLevel = getPaintByLevel(level);

                    drawItem(canvas,paintByLevel);

                }
            }
        }
    }

    private Paint getPaintByLevel(int level) {
        switch (level) {
            case 0:
                return paintEmpty;
            case 1:
                return paintFill_1;
            case 2:
                return paintFill_2;
            case 3:
                return paintFill_3;
            case 4:
                return paintFill_4;
            case 5:
                return paintFill_5;
            case 6:
                return paintFill_6;
        }
        return paintEmpty;
    }

    private void drawItem(Canvas canvas, Paint paintByLevel) {
        paintByLevel.setAntiAlias(true);
        canvas.drawCircle((rectF.left + rectF.right) / 2,(rectF.top + rectF.bottom) / 2, Math.min(itemWidth, itemHeight) / 2, paintByLevel);
    }

    private void drawText(Canvas canvas, String text, Paint paint, final float x, final float y){
        canvas.drawText(text, x, y, paint);
    }

    public void setAdapter(TableViewAdapter adapter) {
        this.mAdapter = adapter;
    }

}
