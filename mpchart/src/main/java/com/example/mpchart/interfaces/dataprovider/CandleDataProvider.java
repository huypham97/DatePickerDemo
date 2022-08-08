package com.example.mpchart.interfaces.dataprovider;

import com.example.mpchart.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
