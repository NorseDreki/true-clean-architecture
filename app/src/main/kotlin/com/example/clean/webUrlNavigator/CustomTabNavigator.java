package com.example.clean.webUrlNavigator;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;

import com.example.domain.submitProposal2.webUrlNavigator.WebUrlNavigator;

public class CustomTabNavigator implements WebUrlNavigator {
    private final Context context;

    public CustomTabNavigator(Context context) {
        this.context = context;
    }

    @Override
    public void navigateToUrl(String url) {
        String packageName = CustomTabHelper.getPackageNameToUse(context);

        CustomTabsIntent intentBuilder = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                .enableUrlBarHiding()
                //.setCloseButtonIcon(getCloseBitmap(context))
                .build();

        intentBuilder.intent.setPackage(packageName);
        intentBuilder.launchUrl(context, Uri.parse(url));

    }

    /*private Bitmap getCloseBitmap(Context context) {
        return BitmapUtils.getTintedDrawableAsBitmap(context,
                R.drawable.ic_back_material_24dp, R.color.gray4);
    }*/

    public boolean canOpenUrl() {
        String packageName = CustomTabHelper.getPackageNameToUse(context);

        return !TextUtils.isEmpty(packageName);
    }
}
