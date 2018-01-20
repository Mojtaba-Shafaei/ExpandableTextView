package com.mojtaba_shafaei.android;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({Status.EXPANDED, Status.COLLAPSED})
public @interface Status {

  int EXPANDED = 1;
  int COLLAPSED = 0;
}
