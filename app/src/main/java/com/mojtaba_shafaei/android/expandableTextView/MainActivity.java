package com.mojtaba_shafaei.android.expandableTextView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import com.mojtaba_shafaei.android.ExpandableTextView;

public class MainActivity extends AppCompatActivity {

  ExpandableTextView et;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    et = findViewById(R.id.etv_one);

    et.setTextColor(getResources().getColor(R.color.colorAccent));
    et.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
    //et.setImageResource(android.R.drawable.ic_delete);

    et.setText(
        "Vivamus suscipit tortor eget felis porttitor volutpat. Mauris\n blandit aliquet elit,\n eget tincidunt nibh pulvinar a. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris blandit aliquet elit, eget tincidunt nibh pulvinar a. Cras ultricies ligula sed magna dictum porta. Curabitur aliquet quam id dui posuere blandit. Nulla quis lorem ut libero malesuada feugiat. Donec sollicitudin molestie malesuada. Proin eget tortor risus. Mauris blandit aliquet elit,\n eget tincidunt nibh pulvinar a.");

    //et.expand();

    /*et.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        et.setStatus(Status.COLLAPSED);
        //Log.d("ssss", "onClick: ");
      }
    });*/
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    et.dispose();
  }
}
