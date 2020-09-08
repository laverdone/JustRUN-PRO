package com.glm.chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

class MonthAxisValueFormatter extends ValueFormatter {
	private final String[] mMonths = new String[]{
			"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
	};

	public MonthAxisValueFormatter() {

	}
	@Override
	public String getFormattedValue(float value) {
		int month = (int) value;

		return mMonths[month];
	}
}
