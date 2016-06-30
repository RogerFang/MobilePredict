package edu.whu.irlab.util;

import java.util.List;

/**
 * Created by Roger on 2016/5/19.
 */
public class StrUtil {

    /**
     * to make String list to a String
     * @param list
     * @param delimiter delimiter to combine list
     * @return
     */
    public static String strJoin(List<String> list, String delimiter){
        if(list == null) {
            throw new IllegalArgumentException();
        }

        if(list.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for(Object item : list) {
            builder.append(item.toString() + delimiter);
        }
        builder.delete(builder.length() - delimiter.length(), builder.length());
        return builder.toString();
    }
}
