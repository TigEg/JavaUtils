package com.ozz.utils.list;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static <T> List<T> removeRepeat(List<T> list) {
        List<T> newList = new ArrayList<T>();
        for (T item : list) {
            if (!newList.contains(item)) {
                newList.add(item);
            }
        }
        return newList;
    }

    public static <T> List<List<T>> splitListBySize(List<T> list, int subListSize) {
        List<List<T>> subList = new ArrayList<List<T>>();
        subList.add(new ArrayList<T>());
        List<T> tempList;
        for (int i = 0; i < list.size(); i++) {
            tempList = subList.get(subList.size() - 1);
            if (tempList.size() >= subListSize) {
                tempList = new ArrayList<T>();
                subList.add(tempList);
            }
            tempList.add(list.get(i));
        }

        return subList;
    }

}
