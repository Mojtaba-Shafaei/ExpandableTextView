package com.mojtaba_shafaei.android;

import static com.mojtaba_shafaei.android.Status.COLLAPSED;
import static com.mojtaba_shafaei.android.Status.EXPANDED;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.mojtaba_shafaei.com.library.R;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExpandableTextView extends RelativeLayout {

  private final String TAG = getClass().getSimpleName();
  private TextView tvDescription;
  private AppCompatImageView btnExpand;
  private int status = COLLAPSED;

  private int MIN_ROW = 2;

  private ValueAnimator expandingRotateAnimator;

  //////////////////////////////////////
  private final java.lang.String STATUS_KEY = "EXPANDED";

  //////////////////////////////////////
  private OnClickListener onClickListener = new OnClickListener() {
    @Override
    public void onClick(View view) {
      if (status == COLLAPSED) {
        setStatus(EXPANDED);
      } else {
        setStatus(COLLAPSED);
      }
    }
  };

  public ExpandableTextView(Context context) {
    super(context);
    init(context);
  }

  public ExpandableTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public ExpandableTextView(Context context,
      @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  @TargetApi(21)
  public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context);
  }

  private void init(Context context) {
    inflate(context, R.layout.layout, this);
    tvDescription = findViewById(R.id.tv_description);
    btnExpand = findViewById(R.id.btnExpand);

    btnExpand.setImageDrawable(
        ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_down_24dp)
    );

    tvDescription.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (onClickListener != null) {
          onClickListener.onClick(view);
        }
      }
    });

    btnExpand.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (onClickListener != null) {
          onClickListener.onClick(view);
        }
      }
    });

    expandingRotateAnimator = ValueAnimator.ofFloat(0, 180);
    expandingRotateAnimator.addUpdateListener(new AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        btnExpand.setRotation((Float) valueAnimator.getAnimatedValue());
      }
    });

    btnExpand.setRotation(0);
  }

  public void setText(String text) {
    tvDescription.setText(text);
    if (text != null && !text.isEmpty()) {
      tvDescription.post(new Runnable() {
        @Override
        public void run() {
          if (tvDescription.getLineCount() <= 1) {
            btnExpand.setVisibility(INVISIBLE);
          } else {
            btnExpand.setVisibility(VISIBLE);
          }
        }
      });
    }

    refreshDrawableState();
  }


  private void collapseTextView(final int numLines) {
    try {
      tvDescription.post(new Runnable() {
        @Override
        public void run() {
          if (Build.VERSION.SDK_INT > 16) {
            ObjectAnimator animation = ObjectAnimator.ofInt(tvDescription, "maxLines", numLines);
            animation.setDuration(300).start();
            tvDescription.setMaxLines(numLines);
          } else {
            tvDescription.setMaxLines(numLines);
          }
        }
      });

    } catch (Exception e) {
      Log.e("Utility", "collapseTextView: " + e);
    }
  }

  private void expandTextView() {
    try {
      tvDescription.post(new Runnable() {
        @Override
        public void run() {
          if (Build.VERSION.SDK_INT > 16) {
            int lc = tvDescription.getLineCount();
            ObjectAnimator animation = ObjectAnimator.ofInt(tvDescription, "maxLines", lc);
            animation.setDuration(Math.max(lc * 10, 300)).start();
          } else {
            tvDescription.setMaxLines(tvDescription.getLineCount());
          }
        }
      });
    } catch (Exception e) {
      Log.e("Utility", "expandTextView: " + e);
    }

  }

  public void setMinRows(@IntRange(from = 0, to = 100) int minRows) {
    this.MIN_ROW = minRows;
  }


  public void setTypeface(Typeface typeface) {
    tvDescription.setTypeface(typeface);
  }

  public void setTextSize(float textSize) {
    tvDescription.setTextSize(textSize);
  }

  public void setTextSize(int unit, float textSize) {
    tvDescription.setTextSize(unit, textSize);
  }

  public void setTextColor(@ColorInt int color) {
    tvDescription.setTextColor(color);
  }

  public void setTextColor(ColorStateList color) {
    tvDescription.setTextColor(color);
  }


  public void setStatus(@Status int status) {
    this.status = status;
    if (status == COLLAPSED) {
      collapseTextView(MIN_ROW);
      expandingRotateAnimator.reverse();
    } else {
      expandTextView();
      expandingRotateAnimator.start();
    }
  }

  public void setImageResource(@DrawableRes int resId) {
    btnExpand.setImageResource(resId);
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
//    btnExpand.setEnabled(enabled);
//    tvDescription.setEnabled(enabled);
  }

  @Override
  public void setOnClickListener(@Nullable OnClickListener l) {
    this.onClickListener = l;
  }

  @Nullable
  @Override
  protected Parcelable onSaveInstanceState() {
    Bundle bundle = new Bundle();
    bundle.putParcelable("superState", super.onSaveInstanceState());
    bundle.putInt(STATUS_KEY, this.status);
    return bundle;
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    if (state instanceof Bundle) {
      Bundle bundle = (Bundle) state;
      setStatus(bundle.getInt(STATUS_KEY));

      state = bundle.getParcelable("superState");
    }

    super.onRestoreInstanceState(state);
  }

  public void dispose() {
    expandingRotateAnimator.removeAllUpdateListeners();
    expandingRotateAnimator.removeAllListeners();
    expandingRotateAnimator = null;
  }
}
