package com.pukka.ydepg.common.utils.uiutil;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.view.View;
import android.widget.ImageView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.utils.fileutil.PathManager;

import java.io.File;


public class Drawables
{
    private static Drawables instance = null;

    public synchronized static Drawables getInstance()
    {
        if (null == instance)
        {
            instance = new Drawables();
        }

        return instance;
    }

    public Bitmap decodeBitmap(String srcName)
    {
        Bitmap bitmap = null;
        String drawablePath = PathManager.getPeelResourcePath() +File.separator+"light"+
                Constant.RESOURCE_IMAGE_PATH + srcName + Constant.IMAGE_SUFFIX;
        File file = new File(drawablePath);
        if (file.exists())
        {
          bitmap =   BitmapFactory.decodeFile(drawablePath);

//
//                    ImageLoader.getInstance().loadImageSync(Constant.FILE_URI_PREFIX +
//                    drawablePath, new DisplayImageOptions.Builder().cacheInMemory(true).build());
        }
        return bitmap;
    }


    public Drawable getDrawable(Resources resources, int resId)
    {
        Drawable drawable = resources.getDrawable(resId);
        if (OTTApplication.getContext().isNeedLoadResource())
        {
            String resName = resources.getResourceEntryName(resId);
            // Set the background by image
            Bitmap dBitmap = decodeBitmap(resName);
            if (null != dBitmap)
            {
                Bitmap sBitmap = BitmapFactory.decodeResource(resources, resId);
                if (null != sBitmap)
                {
                    dBitmap = Bitmap.createScaledBitmap(dBitmap, sBitmap.getWidth(), sBitmap
                            .getHeight(), true);
                }
                drawable = new BitmapDrawable(resources, dBitmap);
            }
        }
        return drawable;
    }



    public Drawable getDrawableColor(Resources resources, int resId)
    {
        int color = Colors.getInstance().getColor(resources, resId);
        return new ColorDrawable(color);
    }

    public void setDrawable(View view, int resourceId, boolean isBackground)
    {
        Drawable drawable = null;
        if (!isBackground && view instanceof ImageView)
        {
            drawable = ((ImageView) view).getDrawable();
        }
        else
        {
            drawable = view.getBackground();
        }

        if (null != drawable)
        {
            if (drawable instanceof ColorDrawable)
            {
                if (isBackground)
                {
                    view.setBackgroundDrawable(Drawables.getInstance().getDrawableColor(view
                            .getResources(), resourceId));
                }
                else
                {
                    ((ImageView) view).setImageDrawable(Drawables.getInstance().getDrawableColor
                            (view.getResources(), resourceId));
                }
            }
            else if (drawable instanceof BitmapDrawable)
            {
                if (isBackground)
                {
                    view.setBackgroundDrawable(Drawables.getInstance().getDrawable(view
                            .getResources(), resourceId));
                }
                else
                {
                    ((ImageView) view).setImageDrawable(Drawables.getInstance().getDrawable(view
                            .getResources(), resourceId));
                }
            }
        }
    }

    /**
     * Get the nine patch image
     *
     * @param resources
     * @param resId
     * @return
     */
    public Drawable getNinePatchDrawable(Resources resources, int resId)
    {
        Drawable drawable = resources.getDrawable(resId);
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);
        byte[] chunk = bitmap.getNinePatchChunk();

        if (OTTApplication.getContext().isNeedLoadResource())
        {
            String resName = resources.getResourceEntryName(resId);
            Bitmap dBitmap = decodeBitmap(resName + Constant.NINE_PATCH_IMAGE_SUFFIX);
            if (null != dBitmap)
            {
                bitmap = Bitmap.createScaledBitmap(dBitmap, bitmap.getWidth(), bitmap.getHeight()
                        , true);
            }
        }

        if (NinePatch.isNinePatchChunk(chunk))
        {
            drawable = new NinePatchDrawable(resources, bitmap, chunk, new Rect(), null);
        }

        return drawable;
    }
}