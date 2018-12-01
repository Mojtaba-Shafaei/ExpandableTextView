package com.mojtaba_shafaei.android;

import static com.mojtaba_shafaei.android.ExpandableTextView.Status.COLLAPSED;
import static com.mojtaba_shafaei.android.ExpandableTextView.Status.EXPANDED;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.mojtaba_shafaei.com.library.R;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ExpandableTextView extends AppCompatTextView{

private final String TAG = "ExpandableTextView";
private Status status = COLLAPSED;

private int MIN_LINES = 2;
private int MAX_LINES = 100;

//////////////////////////////////////
private Drawable _drawable;

//////////////////////////////////////

public ExpandableTextView(Context context){
  super(context);
  init(context);
}

public ExpandableTextView(Context context, AttributeSet attrs){
  super(context, attrs);
  init(context);
}

public ExpandableTextView(Context context,
    @Nullable AttributeSet attrs, int defStyleAttr){
  super(context, attrs, defStyleAttr);
  init(context);
}

private void init(Context context){
  _drawable = ContextCompat.getDrawable(context, R.drawable.etv_ic_arrow_drop_down_24dp);

  setDrawable(_drawable);
  setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.etv_drawable_padding));

  setOnClickListener(new OnClickListener(){
    @Override
    public void onClick(View v){
      toggle();
    }
  });

}

public void setText(String text){
  super.setText(text);
  collapseTextView();
}

public void setDrawable(Drawable drawable){
  setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
}

public void setDrawableTintColor(@ColorInt int color){
  _drawable.setColorFilter(color, Mode.SRC_ATOP);
  refreshDrawableState();
}

public void collapseTextView(){
  try{
    clearAnimation();
    post(new Runnable(){
      @Override
      public void run(){
        final int lineCount = getLineCount();

        if(lineCount <= MIN_LINES){
          setDrawable(null);
        } else{
          final ValueAnimator animator = ValueAnimator.ofInt(lineCount, MIN_LINES).setDuration(250);
          animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            int lastValue = -1;

            @Override
            public void onAnimationUpdate(ValueAnimator animation){
              int value = (int) animation.getAnimatedValue();
              if(value == lastValue){
                return;
              }
              lastValue = value;

              setMaxLines(value);
            }
          });
          animator.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
              super.onAnimationEnd(animation);
              animator.removeAllListeners();
              animator.removeAllUpdateListeners();
            }
          });
          animator.start();
          setDrawable(_drawable);
        }
      }
    });
    status = COLLAPSED;
  } catch(Exception e){
    Log.e("Utility", "collapseTextView: " + e);
  }

}

public void expandTextView(){
  try{
    clearAnimation();
    final int lineCount = getLineCount();
    if(lineCount > MIN_LINES){
      final ValueAnimator animator = ValueAnimator.ofInt(MIN_LINES, 100).setDuration(400);
      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

        @Override
        public void onAnimationUpdate(ValueAnimator animation){
          int value = (int) animation.getAnimatedValue();
          setMaxLines(value);
        }
      });
      animator.addListener(new AnimatorListenerAdapter(){
        @Override
        public void onAnimationEnd(Animator animation){
          super.onAnimationEnd(animation);
          animator.removeAllListeners();
          animator.removeAllUpdateListeners();
          setDrawable(getRotateDrawable(_drawable, 180));
        }
      });
      animator.start();
    }
    setEllipsize(null);
    status = EXPANDED;

  } catch(Exception e){
    Log.e("Utility", "ExpandTextView: " + e);
  }
}

public void setMinRows(@IntRange(from = 1, to = 100) int minRows){
  this.MIN_LINES = minRows;
}

public void toggle(){
  if(status == COLLAPSED){
    expandTextView();
  } else{
    collapseTextView();
  }
}

public enum Status{

  EXPANDED, COLLAPSED
}

private Drawable getRotateDrawable(final Drawable d, final float angle){
  final Drawable[] arD = {d};
  return new LayerDrawable(arD){
    @Override
    public void draw(final Canvas canvas){
      canvas.save();
      canvas.rotate(angle, d.getBounds().width() / 2, d.getBounds().height() / 2);
      super.draw(canvas);
      canvas.restore();
    }
  };
}
}
