package com.example.scratcher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;

public class DialView extends View {

//    inisiasi variabel
    private static int SELECTION_COUNT = 4; //jumlah angka yang akan ditampilkan di scratcher
    private float mWidth;
    private float mHeight;
    private Paint mTextPaint;
    private Paint mDialPaint;
    private float mRadius;
    private int mActiveSelection;
    private final StringBuffer mTempLabel = new StringBuffer(8); //untuk membuat jumlah karakter
    private final float[] mTempResult = new float[2];


    private void init(){
        //inisiasi untuk text paint yang dipake untuk membuat lingkaran dan "dot" di scratchernya
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //inisiasi untuk dot nya
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40f);

        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //untuk lingkarannya
        mDialPaint.setColor(Color.GRAY);

        mActiveSelection = 0; //nilai awal untuk si scratchernya

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                rumus untuk menentukan di angka berapa indikator berhenti
                mActiveSelection = (mActiveSelection + 1) % SELECTION_COUNT;

                if (mActiveSelection >= 1) {
                    mDialPaint.setColor(Color.BLUE); //untuk ubah warna
                }
                else {
                    mDialPaint.setColor(Color.GREEN);
                }

                invalidate(); //untuk kembali ke awal
            }
        });
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh){ //jika ada perubahan pada indikator (yang dihitung dengan rumus)
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w; //ubah width dengan value dari w
        mHeight = h; //ubah height dengan value dari h
        mRadius = (float) (Math.min(mWidth, mHeight/2*0.8)); //menghitung jari-jari
    }

    private float [] computeXYForPosition (final int pos, final float radius){ //mencari posisi XY dari titik
        //rumus untuk menempatkan indikator "dot" nya.
        float[] result = mTempResult;
        Double startAngle = Math.PI * (9/8d);
        Double angle = startAngle + (pos * (Math.PI / 4));
        result [0] = (float) (radius * Math.cos(angle)) + (mWidth/2);
        result [1] = (float) (radius * Math.sin(angle)) + (mHeight/2);
        return result;
    }

    protected void onDraw (Canvas canvas){ //menempatkan posisi tracker
        super.onDraw(canvas);
        canvas.drawCircle(mWidth/2, mHeight/2, mRadius, mDialPaint);

        final float labelRadius = mRadius + 20; //untuk label radius
        StringBuffer label = mTempLabel;
         for (int i = 0;i < SELECTION_COUNT; i++){
             float [] xyData = computeXYForPosition(i, labelRadius);
             float x = xyData [0];
             float y = xyData [1];
             label.setLength(0);
             label.append(i);
             canvas.drawText(label, 0, label.length(), x, y, mTextPaint);
        }

         final float markerRadius = mRadius - 35;
         float[] xyData = computeXYForPosition(mActiveSelection, markerRadius);
         float x = xyData[0];
         float y = xyData [1];

         canvas.drawCircle(x,y, 20, mTextPaint);

    }
    public DialView(Context context) {
        super(context);
        init();
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
