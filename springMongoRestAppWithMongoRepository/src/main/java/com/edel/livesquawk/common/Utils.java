package com.edel.livesquawk.common;

/**
 * Created by iqbal on 5/25/18.
 */
public class Utils {
    public static int getPageNum(String strPageNum) {
        if (strPageNum == null) {
            return 0;
        } else if (!isInteger(strPageNum)) {
            return -1;
        } else {
            return Integer.parseInt(strPageNum);
        }
    }

    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
