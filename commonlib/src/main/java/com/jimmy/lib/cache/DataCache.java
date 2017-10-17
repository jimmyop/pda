package com.jimmy.lib.cache;

import android.content.Context;

import com.jimmy.lib.BaseApplication;
import com.jimmy.lib.constants.Constants;
import com.jimmy.lib.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;


/**
 * 数据缓存类
 * This class holds our data caches (memory and disk).
 */
public class DataCache {
    private static final String TAG = "DataCache";

    private static DataCache mInstance = null;
    private Context mContext;

    private DataCache() {
        this.mContext = BaseApplication.getApplicationInstance();
        init();
    }

    public static DataCache getInstance() {
        if (mInstance == null) {
            synchronized (DataCache.class) {
                if (mInstance == null)
                    mInstance = new DataCache();
            }
        }
        return mInstance;
    }

    private File mCacheFile = null;

    /**
     * Initialize the cache, providing all parameters.
     */
    private void init() {
        mCacheFile = FileUtils.getDiskCacheDir(mContext, Constants.DATA_CACHE_DIR);
        if (!mCacheFile.exists()) {
            mCacheFile.mkdirs();
        }
    }

    /**
     * 数据对象加入缓存
     *
     * @param key
     * @param data
     */
    public void addDataToDiskCache(String key, Object data) {
        if (key == null || null == data || mCacheFile == null) {
            return;
        }
        String path = mCacheFile.getAbsolutePath() + File.separator + key;
        try {
            File fiel = new File(path);
            if (!fiel.exists()) {
                fiel.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(fiel.getPath());
            ObjectOutputStream p = new ObjectOutputStream(out);
            p.writeObject(data);
            p.flush();
            out.close();
            p.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 根据Key获取缓存的数据对象
     *
     * @param key
     * @return
     */
    public Object getDataFromDiskCache(String key) {
        if (key != null && mCacheFile != null) {
            FileInputStream in = null;
            ObjectInputStream p = null;
            try {
                String path = mCacheFile.getAbsolutePath() + File.separator + key;
                File file = new File(path);
                if (file.exists()) {
                    in = new FileInputStream(path);
                    p = new ObjectInputStream(in);
                    Object obj = p.readObject();
                    return obj;
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (p != null) {
                        p.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void clearCaches() {
        if (null != mCacheFile) {
            if (mCacheFile.exists()) {
                FileUtils.deleteFolder(mCacheFile);
            }
        }
    }

}
