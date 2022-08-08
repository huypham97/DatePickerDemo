package com.example.mpchart.interfaces.dataprovider;

import com.example.mpchart.components.YAxis;
import com.example.mpchart.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
