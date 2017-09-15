package com.mcms.pda.constants;

public class Constants {


    /**
     * 记录当前软件的版本号 的文件，储存在 /sdcard/9apps/目录下 例如： version_code:1
     */
    public static final String BACKUP_FILE_NAME = "nineapps_backup.txt";

    /**
     * 数据缓存目录
     **/
    public static final String DATA_CACHE_DIR = "data";
    /**
     * 图片缓存目录
     **/
    public static final String IMAGE_CACHE_DIR = "image";
    /**
     * 友盟登录标识
     **/
    public static final String DESCRIPTOR = "com.umeng.login";
    /**
     * 友盟分享标识
     **/
    public static final String SHARE_DESCRIPTOR = "com.umeng.share";
    /**
     * 二维码扫描回调
     **/
    public static final int SCAN_CODE = 0x001;


    /**
     * 登录过期或未登录
     */
    public static final int NOT_LOGGED_IN = 1000;

    /**
     * 时间戳过期
     */
    public static final int LOGIN_OVERDUE = 1101;






}
