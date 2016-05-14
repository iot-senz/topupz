package com.score.shopz.utils;

import com.score.shopz.pojos.Bill;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Utility class to handle JSON
 *
 * @author eranga bandara(erangaeb@gmail.com)
 */
public class JSONUtils {

    /**
     * Create JSON string from TopUp
     *
     * @param bill Bill object
     * @return JSON string
     * @throws JSONException
     */
    public static String getBillJson(Bill bill) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("acc", "user1");
        jsonObject.put("amnt", "23");

        return jsonObject.toString();
    }

    /**
     * Get current date and time as transaction time
     * format - yyyy/MM/dd HH:mm:ss
     *
     * @return
     */
    private static String getCurrentTime() {
        //date format
        String DATE_FORMAT_NOW = "yyyy/MM/dd HH:mm:ss";

        // generate time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_NOW);

        return simpleDateFormat.format(calendar.getTime());
    }
}
