package com.jimmy.lib.utils;


import java.util.Collection;

/**
 * 集合工具类
 * Created by zengxiangbin on 2016/5/10.
 */
public class CollectionUtils {
    private CollectionUtils(){

    }

    /**
     * 判断集合是否为空
     * @param collection
     * @return
     */
    public static boolean isNullOrEmpty(Collection<?> collection){
        return collection == null || collection.isEmpty();
    }

    /**
     * 获取集合的长度
     * @param collection
     * @return
     */
    public static int getSize(Collection<?> collection){
        if(isNullOrEmpty(collection)){
            return 0;
        }
        return collection.size();
    }

    /**
     * 判断集合长度是否与目标值相等
     * @param collection
     * @param targetSize
     * @return
     */
    public static boolean isEqual(Collection<?> collection, int targetSize){
        return getSize(collection) == targetSize;
    }

    /**
     * 判断集合长度是否比目标值小
     * @param collection
     * @param targetSize
     * @return
     */
    public static boolean isSmallThan(Collection<?> collection, int targetSize){
        return getSize(collection) < targetSize;
    }

    /**
     * 判断集合长度是否比目标值小或者相等
     * @param collection
     * @param targetSize
     * @return
     */
    public static boolean isSmallThanOrEqual(Collection<?> collection, int targetSize){
        return getSize(collection) <= targetSize;
    }

    /**
     * 判断集合长度是否比目标值大
     * @param collection
     * @param targetSize
     * @return
     */
    public static boolean isBigThan(Collection<?> collection, int targetSize){
        return getSize(collection) > targetSize;
    }

    /**
     * 判断集合长度是否比目标值大或者相等
     * @param collection
     * @param targetSize
     * @return
     */
    public static boolean isBigThanOrEqual(Collection<?> collection, int targetSize){
        return getSize(collection) >= targetSize;
    }
}
