package com.example.cadastroalunos;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class PdfActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Carrega o link do PDF na webview
        webView.loadUrl("https://drive.google.com/file/d/10fmOw56ET-Gk3zNGkUZjprUH_6ip3cYF/view?usp=drive_link");
    }


}


