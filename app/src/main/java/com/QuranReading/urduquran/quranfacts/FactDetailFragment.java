package com.QuranReading.urduquran.quranfacts;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.QuranReading.urduquran.R;

/**
 * Created by Aamir Riaz on 12/20/2016.
 */

public class FactDetailFragment extends Fragment {
    String detail;
    TextView text;
    WebView webView;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fact_detail,container,false);
        detail=getArguments().getString("factDetail");
        webView=(WebView)view.findViewById(R.id.webview);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.loadData(detail,"text/html", "utf-8");
        final WebSettings webSettings = webView.getSettings();
        Resources res = getResources();
        float fontSize = res.getDimension(R.dimen.text_size_twelve);
        webSettings.setDefaultFontSize((int)fontSize);
        //webSettings.setTextSize(WebSettings.TextSize.LARGER);
        return view;
    }

}


