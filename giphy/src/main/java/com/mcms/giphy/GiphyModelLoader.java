package com.mcms.giphy;

import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import java.io.InputStream;

/**
 * A model loader that translates a POJO mirroring a JSON object representing a single image from
 * Giphy's api into an {@link InputStream} that can be decoded into an
 * {@link android.graphics.drawable.Drawable}.
 */
public class GiphyModelLoader extends BaseGlideUrlLoader<Api.GifResult> {

  @Override
  public boolean handles(Api.GifResult model) {
    return true;
  }

  /**
   * The default factory for {@link com.bumptech.glide.samples.giphy.GiphyModelLoader}s.
   */
  public static class Factory implements ModelLoaderFactory<Api.GifResult, InputStream> {


    @Override
    public ModelLoader<Api.GifResult, InputStream> build(MultiModelLoaderFactory multiFactory) {
      return new GiphyModelLoader(multiFactory.build(GlideUrl.class, InputStream.class));
    }

    @Override
    public void teardown() {
      // Do nothing.
    }
  }

  public GiphyModelLoader(ModelLoader<GlideUrl, InputStream> urlLoader) {
    super(urlLoader);
  }

  @Override
  protected String getUrl(Api.GifResult model, int width, int height, Options options) {
    Api.GifImage fixedHeight = model.images.fixed_height;
    int fixedHeightDifference = getDifference(fixedHeight, width, height);
    Api.GifImage fixedWidth = model.images.fixed_width;
    int fixedWidthDifference = getDifference(fixedWidth, width, height);
    if (fixedHeightDifference < fixedWidthDifference && !TextUtils.isEmpty(fixedHeight.url)) {
      return fixedHeight.url;
    } else if (!TextUtils.isEmpty(fixedWidth.url)) {
      return fixedWidth.url;
    } else if (!TextUtils.isEmpty(model.images.original.url)) {
      return model.images.original.url;
    } else {
      return null;
    }
  }

  private static int getDifference(Api.GifImage gifImage, int width, int height) {
    return Math.abs(width - gifImage.width) + Math.abs(height - gifImage.height);
  }
}
