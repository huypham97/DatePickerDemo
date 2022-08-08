package com.example.mpchart.interfaces.dataprovider;

import com.example.mpchart.components.YAxis;
import com.example.mpchart.data.BarLineScatterCandleBubbleData;
import com.example.mpchart.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);
    boolean isInverted(YAxis.AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
