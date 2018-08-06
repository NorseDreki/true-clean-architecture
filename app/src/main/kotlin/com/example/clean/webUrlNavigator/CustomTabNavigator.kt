package com.example.clean.webUrlNavigator

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.text.TextUtils


class CustomTabNavigator(private val context: Context)  {

    fun navigateToUrl(url: String) {
        val packageName = CustomTabHelper.getPackageNameToUse(context)

        val intentBuilder = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .enableUrlBarHiding()
                //.setCloseButtonIcon(getCloseBitmap(context))
                .build()

        intentBuilder.intent.`package` = packageName
        intentBuilder.launchUrl(context, Uri.parse(url))

    }

    /*private Bitmap getCloseBitmap(Context context) {
        return BitmapUtils.getTintedDrawableAsBitmap(context,
                R.drawable.ic_back_material_24dp, R.color.gray4);
    }*/

    fun canOpenUrl(): Boolean {
        val packageName = CustomTabHelper.getPackageNameToUse(context)

        return !TextUtils.isEmpty(packageName)
    }
}
