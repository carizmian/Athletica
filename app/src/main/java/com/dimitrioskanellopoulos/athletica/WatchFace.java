package com.dimitrioskanellopoulos.athletica;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Pair;
import android.util.TypedValue;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public class WatchFace {
    private static final String TAG = "Watchface";
    private static final String TIME_FORMAT_WITHOUT_SECONDS = "%02d:%02d";
    private static final String TIME_FORMAT_WITH_SECONDS = TIME_FORMAT_WITHOUT_SECONDS + ":%02d";
    private static final String DATE_FORMAT = "%02d.%02d.%d";
    private static final int DATE_AND_TIME_DEFAULT_COLOUR = Color.WHITE;
    private static final int TEXT_DEFAULT_COLOUR = Color.WHITE;
    private static final int BACKGROUND_DEFAULT_COLOUR = Color.BLACK;

    // Standard Paints -> Time and Battery
    private final LinkedHashMap<String, AbstractTextPaint> standardPaints = new LinkedHashMap<String, AbstractTextPaint>();

    // Extra Paints -> Dynamic
    private final LinkedHashMap<String, AbstractTextPaint> extraPaints = new LinkedHashMap<String, AbstractTextPaint>();

    // Icons
    private final String sunIcon;
    private final String moonIcon;
    private final String areaChartIcon;

    private final Float rowVerticalMargin;

    private final Calendar calendar;

    private boolean shouldShowSeconds = true;

    private boolean isRound;
    private int chinSize;

    public WatchFace(Context context) {

        // Create fontAwesome typeface
        Typeface fontAwesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");

        // Define the size of the rows for vertical
        rowVerticalMargin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                context.getResources().getDimension(R.dimen.row_vertical_margin),
                context.getResources().getDisplayMetrics());

        // Add paint for background
        BackgroundPaint backgroundPaint = new BackgroundPaint();
        backgroundPaint.setColor(BACKGROUND_DEFAULT_COLOUR);
        standardPaints.put("backgroundPaint", backgroundPaint);

        // Add paint for time
        TimePaint timePaint = new TimePaint();
        timePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        timePaint.setColor(DATE_AND_TIME_DEFAULT_COLOUR);
        timePaint.setTextSize(context.getResources().getDimension(R.dimen.time_size));
        timePaint.setAntiAlias(true);
        standardPaints.put("timePaint", timePaint);

        // Add paint for battery level
        BatteryPaint batteryPaint = new BatteryPaint();
        batteryPaint.setTypeface(fontAwesome);
        batteryPaint.setColor(TEXT_DEFAULT_COLOUR);
        batteryPaint.setTextSize(context.getResources().getDimension(R.dimen.battery_text_size));
        batteryPaint.setAntiAlias(true);
        standardPaints.put("batteryPaint", batteryPaint);

        // Add paint for date
        DatePaint datePaint = new DatePaint();
        datePaint.setColor(DATE_AND_TIME_DEFAULT_COLOUR);
        datePaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
        datePaint.setAntiAlias(true);
        datePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        extraPaints.put("datePaint", datePaint);

        // Add paint for sunrise
        SensorPaint sunriseSunsetPaint = new SensorPaint();
        sunriseSunsetPaint.setTypeface(fontAwesome);
        sunriseSunsetPaint.setColor(TEXT_DEFAULT_COLOUR);
        sunriseSunsetPaint.setTextSize(context.getResources().getDimension(R.dimen.text_size));
        sunriseSunsetPaint.setAntiAlias(true);
        extraPaints.put("sunriseSunsetPaint", sunriseSunsetPaint);

        // Add paint for altitude
        SensorPaint altitudePaint = new SensorPaint();
        altitudePaint.setTypeface(fontAwesome);
        altitudePaint.setColor(TEXT_DEFAULT_COLOUR);
        altitudePaint.setTextSize(context.getResources().getDimension(R.dimen.text_size));
        altitudePaint.setAntiAlias(true);
        extraPaints.put("altitudePaint", altitudePaint);

        // Add the icons
        sunIcon = context.getResources().getString(R.string.sun_icon);
        moonIcon = context.getResources().getString(R.string.moon_icon);
        areaChartIcon = context.getResources().getString(R.string.area_chart_icon);

        calendar = Calendar.getInstance();
    }

    public void draw(Canvas canvas, Rect bounds) {

        // Update time
        calendar.setTimeInMillis(System.currentTimeMillis());

        // First draw background
        canvas.drawRect(0, 0, bounds.width(), bounds.height(), standardPaints.get("backgroundPaint"));

        // Draw Time
        AbstractTextPaint timePaint = standardPaints.get("timePaint");
        timePaint.setText(String.format(
                shouldShowSeconds ?
                        TIME_FORMAT_WITH_SECONDS :
                        TIME_FORMAT_WITHOUT_SECONDS,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)));
        canvas.drawText(timePaint.getText(), computeXOffset(timePaint, bounds), computeFirstRowYOffset(timePaint, bounds), timePaint);

        // Draw battery
        AbstractTextPaint batteryPaint = standardPaints.get("batteryPaint");
        canvas.drawText(batteryPaint.getText(), computeXOffset(batteryPaint, bounds), computeLastRowYOffset(batteryPaint, bounds), batteryPaint);

        // Set the text of the data
        extraPaints.get("datePaint").setText(String.format(DATE_FORMAT, calendar.get(calendar.DAY_OF_MONTH), calendar.get(calendar.MONTH), calendar.get(calendar.YEAR)));

        Float yOffset = computeFirstRowYOffset(timePaint, bounds);
        for (Map.Entry<String, AbstractTextPaint> entry : extraPaints.entrySet()) {
            AbstractTextPaint paint = entry.getValue();
            yOffset = yOffset + computeRowYOffset(paint);
            Float xOffset = computeXOffset(paint, bounds);
            canvas.drawText(paint.getText(), xOffset, yOffset, paint);
        }
    }

    private float computeXOffset(AbstractTextPaint paint, Rect watchBounds) {
        return  watchBounds.exactCenterX() - (paint.measureText(paint.getText()) / 2.0f);
    }

    private float computeFirstRowYOffset(AbstractTextPaint firstRowPaint, Rect watchBounds) {
        float centerY = watchBounds.exactCenterY() - 15.0f;
        Rect textBounds = new Rect();
        firstRowPaint.getTextBounds(firstRowPaint.getText(), 0, firstRowPaint.getText().length(), textBounds);
        int textHeight = textBounds.height();
        return centerY + (textHeight / 2.0f);
    }

    private float computeLastRowYOffset(AbstractTextPaint lastRowPaint, Rect watchBounds) {
        Rect textBounds = new Rect();
        lastRowPaint.getTextBounds(lastRowPaint.getText(), 0, lastRowPaint.getText().length(), textBounds);
        int textHeight = textBounds.height();
        return watchBounds.bottom - chinSize - textHeight ;
    }

    private float computeRowYOffset(AbstractTextPaint paint) {
        Rect textBounds = new Rect();
        paint.getTextBounds(paint.getText(), 0, paint.getText().length(), textBounds);
        return textBounds.height() + rowVerticalMargin;
    }

    public void setAntiAlias(boolean antiAlias) {
        for (Map.Entry<String, AbstractTextPaint> entry : standardPaints.entrySet()) {
            entry.getValue().setAntiAlias(antiAlias);
        }
    }

    public void updateTimeZoneWith(TimeZone timeZone) {
        calendar.setTimeZone(timeZone);
    }

    public void setShowSeconds(boolean showSeconds) {
        shouldShowSeconds = showSeconds;
    }

    public void setIsRound(boolean round) {
        isRound = round;
    }

    public void setChinSize(Integer chinSize) {
        this.chinSize = chinSize;
    }

    public void updateAltitude(String altitude) {
        extraPaints.get("altitudePaint").setText(areaChartIcon + " " + altitude + "m");
    }

    public void updateBatteryLevel(Integer batteryPercentage) {
        standardPaints.get("batteryPaint").setText(batteryPercentage.toString());
    }

    public void updateSunriseSunset(Pair<String, String> sunriseSunset) {
        extraPaints.get("sunriseSunsetPaint").setText(sunIcon + " " + sunriseSunset.first + "    " + moonIcon + " " + sunriseSunset.second);
    }

}
