package com.pukka.ydepg;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.Preconditions;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created on 2017/3/9.
 */
@GlideModule
public class OTTGlideModule extends AppGlideModule {

    private static final String TAG = OTTGlideModule.class.getSimpleName();

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //memory cache
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        //builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor(24, "disk-cache", GlideExecutor.UncaughtThrowableStrategy.DEFAULT));
        builder.setSourceExecutor(GlideExecutor.newSourceExecutor(24, "source", GlideExecutor.UncaughtThrowableStrategy.DEFAULT));
        //builder.setDefaultRequestOptions(new RequestOptions().skipMemoryCache(true));

        int diskCacheSize = 524288000;//50 M  262144000
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSize));
        SuperLog.debug(TAG, "Glide Cache info is as followed:  "    +
                "\n\tDiskCacheSize(M)   = " + (diskCacheSize >> 20)        +
                "\n\tBitmapPoolSize(M)  = " + (customBitmapPoolSize >> 20) +
                "\n\tMemoryCacheSize(M) = " + (customMemoryCacheSize >> 20));
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        // 自定义OkHttpClient
        //OkHttpClient client = UnSafeOkHttpClient.getUnsafeOkHttpClient();

        //解决SecDroid扫描问题自定义HTTP客户端
        OkHttpClient client = new OkHttpClient();

        // 采用自定义的CustomOkHttpUrlLoader
        registry.replace(GlideUrl.class, InputStream.class, new CustomOkHttpUrlLoader.Factory(client));
    }

    @Override
    public boolean isManifestParsingEnabled()
    {
        return false;
    }
}

// CustomOkHttpUrlLoader.java
// 按照Glide提供的OkHttpUrlLoader.java复制过来的，把里面的OkHttpUrlLoader全部替换为CustomOkHttpUrlLoader
class CustomOkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private final Call.Factory client;

    // Public API.
    @SuppressWarnings("WeakerAccess")
    public CustomOkHttpUrlLoader(@NonNull Call.Factory client) {
        this.client = client;
    }

    @Override
    public boolean handles(@NonNull GlideUrl url) {
        return true;
    }

    @Override
    public LoadData<InputStream> buildLoadData(@NonNull GlideUrl model, int width, int height, @NonNull Options options) {

        // 注意这里，不再是原来的OkHttpStreamFetcher，而是改为自定义的CustomOkHttpStreamFetcher
        return new LoadData<>(model, new CustomOkHttpStreamFetcher(client, model));
    }

    /**
     * The default factory for
     */
    // Public API.
    @SuppressWarnings("WeakerAccess")
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private static volatile Call.Factory internalClient;
        private final Call.Factory client;

        private static Call.Factory getInternalClient() {
            if (internalClient == null) {
                synchronized (CustomOkHttpUrlLoader.Factory.class) {
                    if (internalClient == null) {
                        internalClient = new OkHttpClient();
                    }
                }
            }
            return internalClient;
        }

        /**
         * Constructor for a new Factory that runs requests using a static singleton client.
         */
        public Factory() {
            this(getInternalClient());
        }

        /**
         * Constructor for a new Factory that runs requests using given client.
         *
         * @param client this is typically an instance of {@code OkHttpClient}.
         */
        public Factory(@NonNull Call.Factory client) {
            this.client = client;
        }

        @NonNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new CustomOkHttpUrlLoader(client);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }
}


// CustomOkHttpStreamFetcher.java
// 这个类也是复制的OkHttpStreamFetcher.java，需要注意的是改动了一个地方
class CustomOkHttpStreamFetcher implements DataFetcher<InputStream>, okhttp3.Callback {
    private static final String TAG = "OkHttpFetcher";
    private final Call.Factory client;
    private final GlideUrl url;
    private InputStream stream;
    private ResponseBody responseBody;
    private DataCallback<? super InputStream> callback;
    // call may be accessed on the main thread while the object is in use on other threads. All other
    // accesses to variables may occur on different threads, but only one at a time.
    private volatile Call call;

    // Public API.
    @SuppressWarnings("WeakerAccess")
    public CustomOkHttpStreamFetcher(Call.Factory client, GlideUrl url) {
        this.client = client;
        this.url = url;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull final DataCallback<? super InputStream> callback) {
        // 不再是url.toStringUrl()，而是改为了 url.getCacheKey()
        // 这样Glide不会将url进行转义了，或者说自己对url进行转义，不再采用GlideUrl中提供的方法
        Request.Builder requestBuilder = new Request.Builder().url(url.getCacheKey());
        for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
            String key = headerEntry.getKey();
            requestBuilder.addHeader(key, headerEntry.getValue());
        }
        Request request = requestBuilder.build();
        this.callback = callback;

        call = client.newCall(request);
        call.enqueue(this);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "OkHttp failed to obtain result", e);
        }

        callback.onLoadFailed(e);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        responseBody = response.body();
        if (response.isSuccessful()) {
            long contentLength = Preconditions.checkNotNull(responseBody).contentLength();
            stream = ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength);
            callback.onDataReady(stream);
        } else {
            callback.onLoadFailed(new HttpException(response.message(), response.code()));
        }
    }

    @Override
    public void cleanup() {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // Ignored
        }
        if (responseBody != null) {
            responseBody.close();
        }
        callback = null;
    }

    @Override
    public void cancel() {
        Call local = call;
        if (local != null) {
            local.cancel();
        }
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}