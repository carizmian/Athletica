package com.dimitrioskanellopoulos.athletica.grid.columns;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.util.Pair;

import com.dimitrioskanellopoulos.athletica.helpers.SunriseSunsetHelper;

import java.util.TimeZone;

public class SunsetColumn extends LocationColumn {
    private static final String TAG = "SunsetColumn";
    public SunsetColumn(Context context, Typeface paintTypeface, Float paintTextSize, int paintColor) {
        super(context, paintTypeface, paintTextSize, paintColor);
    }

    @Override
    public void updateLocation(Location location) {
        super.updateLocation(location);
        Pair<String, String> sunriseSunset = SunriseSunsetHelper.getSunriseAndSunset(lastLocation, TimeZone.getDefault().getID());
        setText(sunriseSunset.second);
        Log.d(TAG, "Successfully updated sunset");
    }
}
