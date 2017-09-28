package com.mcms.giphy;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.Collections;
import java.util.List;

/**
 * The primary activity in the Giphy sample that allows users to view trending animated GIFs from
 * Giphy's api.
 */
public class MainActivity extends Activity implements Api.Monitor {
    private GifAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView giphyLogoView = (ImageView) findViewById(R.id.giphy_logo_view);

        GlideApp.with(this)
                .load(R.raw.large_giphy_logo)
                .into(giphyLogoView);

        RecyclerView gifList = (RecyclerView) findViewById(R.id.gif_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        gifList.setLayoutManager(layoutManager);

        RequestBuilder<Drawable> gifItemRequest = GlideApp.with(this)
                .asDrawable();

        ViewPreloadSizeProvider<Api.GifResult> preloadSizeProvider =
                new ViewPreloadSizeProvider<>();
        adapter = new GifAdapter(this, gifItemRequest, preloadSizeProvider);
        gifList.setAdapter(adapter);
        RecyclerViewPreloader<Api.GifResult> preloader =
                new RecyclerViewPreloader<>(GlideApp.with(this), adapter, preloadSizeProvider, 4);
        gifList.addOnScrollListener(preloader);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Api.get().addMonitor(this);
        if (adapter.getItemCount() == 0) {
            Api.get().getTrending();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Api.get().removeMonitor(this);
    }

    @Override
    public void onSearchComplete(Api.SearchResult result) {
        adapter.setResults(result.data);
    }

    private static class GifAdapter extends RecyclerView.Adapter<GifViewHolder>
            implements ListPreloader.PreloadModelProvider<Api.GifResult> {
        private static final Api.GifResult[] EMPTY_RESULTS = new Api.GifResult[0];

        private final Activity activity;
        private RequestBuilder<Drawable> requestBuilder;
        private ViewPreloadSizeProvider<Api.GifResult> preloadSizeProvider;

        private Api.GifResult[] results = EMPTY_RESULTS;

        GifAdapter(Activity activity, RequestBuilder<Drawable> requestBuilder,
                   ViewPreloadSizeProvider<Api.GifResult> preloadSizeProvider) {
            this.activity = activity;
            this.requestBuilder = requestBuilder;
            this.preloadSizeProvider = preloadSizeProvider;
        }

        void setResults(Api.GifResult[] results) {
            if (results != null) {
                this.results = results;
            } else {
                this.results = EMPTY_RESULTS;
            }
            notifyDataSetChanged();
        }

        @Override
        public GifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = activity.getLayoutInflater().inflate(R.layout.gif_list_item, parent, false);
            return new GifViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GifViewHolder holder, int position) {
            final Api.GifResult result = results[position];
            holder.gifView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard =
                            (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip =
                            ClipData.newPlainText("giphy_url", result.images.fixed_height.url);
                    clipboard.setPrimaryClip(clip);

                    Intent fullscreenIntent = FullscreenActivity.getIntent(activity, result);
                    activity.startActivity(fullscreenIntent);
                }
            });

            requestBuilder.load(result).into(holder.gifView);

            preloadSizeProvider.setView(holder.gifView);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return results.length;
        }

        @NonNull
        @Override
        public List<Api.GifResult> getPreloadItems(int position) {
            return Collections.singletonList(results[position]);
        }

        @NonNull
        @Override
        public RequestBuilder<Drawable> getPreloadRequestBuilder(Api.GifResult item) {
            return requestBuilder.load(item);
        }
    }

    private static class GifViewHolder extends RecyclerView.ViewHolder {
        private final ImageView gifView;

        GifViewHolder(View itemView) {
            super(itemView);
            gifView = (ImageView) itemView.findViewById(R.id.gif_view);
        }
    }
}
