package com.pukka.ydepg.customui.tv.cache;

import android.graphics.Bitmap;

import androidx.collection.LruCache;

/**
 * Bitmap缓存，简单缓存.
 */
public class BitmapMemoryCache {

	private static BitmapMemoryCache sInstance = new BitmapMemoryCache();

	private LruCache<String, Bitmap> mMemoryCache;

	/**
	 * 单例模式.
	 */
	public static BitmapMemoryCache getInstance() {
		return BitmapMemoryCache.sInstance;
	}

	private BitmapMemoryCache() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 重写此方法来衡量每张图片的大小，默认返回图片数量。
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	/**
	 * 移除缓存
	 */
	public synchronized void removeImageCache(String key) {
		if (key != null) {
			if (mMemoryCache != null) {
				Bitmap bm = mMemoryCache.remove(key);
				if (bm != null) {
					bm.recycle();
				}
			}
		}
	}
}