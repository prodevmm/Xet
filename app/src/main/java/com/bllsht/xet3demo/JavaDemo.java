package com.bllsht.xet3demo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bllsht.xet3.Xet;
import com.bllsht.xet3.dto.Response;
import com.bllsht.xet3.exceptions.XetException;

public class JavaDemo {
    private void fetchUsingXet(Context context, String url) {
        Xet.INSTANCE.fetch(url, new Xet.Callback() {
            @Override
            public void onSuccess(@NonNull Response response) {
                String message = "SD URL : " + response.getSdUrl() +
                        "\n\nHD Available : " + response.isHDAvailable() +
                        "\nHD URL : " + response.getHdUrl() +
                        "\n\nHLS Available : " + response.isHlsAvailable() +
                        "\nHLS URL : " + response.getHlsUrl();

                new AlertDialog.Builder(context)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }

            @Override
            public void onFailure(@NonNull XetException exception) {
                new AlertDialog.Builder(context)
                        .setMessage(exception.toString())
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });
    }
}
