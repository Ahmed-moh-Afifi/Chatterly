package com.example.chatterly.utils;

import android.util.Log;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import com.google.gson.internal.bind.util.ISO8601Utils;

public class CustomDateAdapter implements JsonDeserializer<java.util.Date>, JsonSerializer<java.util.Date> {

    @Override
    public java.util.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String dateStr = json.getAsString();

        if (!dateStr.endsWith("Z")) {
            dateStr += "Z";
        }

        try {
            return ISO8601Utils.parse(dateStr, new ParsePosition(0));
        } catch (Exception e) {
            Log.d("CustomDateAdapter", "Exception!");
            Log.d("CustomDateAdapter", dateStr);
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(java.util.Date src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            // Use SimpleDateFormat to remove the timezone
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensures consistent formatting
            String formattedDate = dateFormat.format(src);
            return new JsonPrimitive(formattedDate);
        } catch (Exception e) {
            Log.d("CustomDateAdapter", "Exception during serialization!");
            throw new JsonParseException(e);
        }
    }
}