package com.azharkova.writemesound;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.view.View;



public class VisualizerView extends View {
	private byte[] mBytes;
	private byte[] mFftBytes;
	private float[] mPoints;
	private Rect mRect = new Rect();
	int[] rainbow;
	private Paint mForePaint = new Paint();

	public VisualizerView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mBytes = null;

		mForePaint.setStrokeWidth(2f);
		mForePaint.setAntiAlias(true);
		//mForePaint.setColor(Color.rgb(0, 128, 255));
		mForePaint.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.INNER));
		mForePaint.setStyle(Style.FILL);

	    
	}



	public void updateVisualizer(byte[] bytes) {
		mBytes = bytes;
		invalidate();
	}
	public void updateVisualizerFFT(byte[] bytes) {
		mFftBytes = bytes;
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mBytes == null) {
			return;
		}
		if (mPoints == null || mPoints.length < mBytes.length * 4) {
			mPoints = new float[mBytes.length * 4];
		}
		mRect.set(0, 0, getWidth(), getHeight());
		int d=10;
		for (int i = 0; i < mBytes.length - 1; i+=2) {
			mPoints[i * 4] = mRect.width() * ((i*d)) / (mBytes.length - 1);
			mPoints[i * 4 + 3] =mRect.height();
				//	+ ((byte) (mBytes[i] -128)) * (mRect.height())// / 2)
				//	/ 128;
			mPoints[i * 4 + 2] = mRect.width() * (((i + 1)*d)) / (mBytes.length - 1);
			mPoints[i * 4 + 1] =mRect.height()
					- Math.abs(((byte) (mBytes[i] +128)) * (mRect.height())
					/ 128);
			canvas.drawRect(mPoints[i*4],mPoints[i*4+1],mPoints[i*4+2],mPoints[i*4+3],mForePaint);
		}
		//canvas.drawLines(mPoints, mForePaint);
	}
/*@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mBytes == null) {
			return;
		}

		if (mPoints == null || mPoints.length < mBytes.length * 4) {
			mPoints = new float[mBytes.length * 4];
		}

		mRect.set(0, 0, getWidth(), getHeight());

		for (int i = 0; i < mBytes.length - 1; i++) {
			mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
			mPoints[i * 4 + 1] = mRect.height() / 2
					+ ((byte) (mBytes[i] + 128)) * (mRect.height() / 2)
					/ 128;
			mPoints[i * 4 + 2] = mRect.width() * (i + 1)
					/ (mBytes.length - 1);
			mPoints[i * 4 + 3] = mRect.height() / 2
					- ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2)
					/ 128;
			
			//canvas.drawLine(mPoints[i*4], mPoints[i*4+1], mPoints[i*4+2], mPoints[i*4+3], mForePaint);
		}
		
		mForePaint.setShader(new LinearGradient(mPoints[0], mPoints[1], mPoints[mPoints.length-2], mPoints[mPoints.length-1], rainbow,null, TileMode.MIRROR
				));
		canvas.drawLines(mPoints, mForePaint);

		int d=0;
		for (int i = 0; i < mBytes.length - 1; i++) {
			mPoints[i * 4] = mRect.width() * (i+d) / (mBytes.length - 1);
			mPoints[i * 4 + 1] =  mRect.height() / 2
					+ ((byte) (mBytes[i] + 128)) * (mRect.height() / 2)
					/ 128;
			mPoints[i * 4 + 2] = mRect.width() * (i+d+1)
					/ (mBytes.length - 1);
			mPoints[i * 4 + 3] = getHeight();//mRect.height() / 2
					//+ ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2)
					/// 128;
			d=i+10;
			mForePaint.setShader(new LinearGradient(mPoints[i*4], mPoints[i*4+1], mPoints[i*4+2], mPoints[i*4+3], rainbow,null, TileMode.MIRROR
					));
			canvas.drawRect(mPoints[i*4], mPoints[i*4+1], mPoints[i*4+2], mPoints[i*4+3], mForePaint);
		}
		
		
	}*/
	
}