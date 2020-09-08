package com.glm.chart;


import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by gianluca on 31/08/15.
 */
public class GlmValueFormatter extends ValueFormatter {


        private DecimalFormat mFormat;

        public GlmValueFormatter() {
            mFormat = new DecimalFormat("###.###");
        }

        public GlmValueFormatter(boolean decimal) {
            mFormat = new DecimalFormat("###,###.##");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);
        }

}
