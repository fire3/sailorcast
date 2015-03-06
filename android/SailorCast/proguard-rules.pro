# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/fire3/AndroidDevelop/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Obfuscation parameters:
#-dontobfuscate
-useuniqueclassmembernames
-keepattributes SourceFile,LineNumberTable
-allowaccessmodification
# Ignore warnings:
#-dontwarn org.mockito.**
#-dontwarn org.junit.**
#-dontwarn com.robotium.**
#-dontwarn org.joda.convert.**
# Ignore warnings: We are not using DOM model
-dontwarn com.fasterxml.jackson.databind.ext.DOMSerializer
# Ignore warnings: https://github.com/square/okhttp/wiki/FAQs
-dontwarn com.squareup.okhttp.internal.huc.**
# Ignore warnings: https://github.com/square/okio/issues/60
-dontwarn okio.**
# Ignore warnings: https://github.com/square/retrofit/issues/435
-dontwarn com.google.appengine.api.urlfetch.**
# Keep the pojos used by GSON or Jackson
-keep class com.futurice.project.models.pojo.** { *; }
# Keep GSON stuff
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
# Keep Jackson stuff
-keep class org.codehaus.** { *; }
-keep class com.fasterxml.jackson.annotation.** { *; }
# Keep these for GSON and Jackson
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
# Keep Retrofit
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
@retrofit.** *;
}
-keepclassmembers class * {
@retrofit.** *;
}
# Keep Picasso
-keep class com.squareup.picasso.** { *; }
-keepclasseswithmembers class * {
@com.squareup.picasso.** *;
}
-keepclassmembers class * {
@com.squareup.picasso.** *;
}

# jetty
-keep class org.eclipse.** { *; }
-keepclasseswithmembers class * {
@org.eclipse.** *;
}
-keepclassmembers class * {
@org.eclipse.** *;
}

# cling
-keep class org.fourthline.** { *; }
-keepclasseswithmembers class * {
@org.fourthline.** *;
}
-keepclassmembers class * {
@org.fourthline.** *;
}

-keep class org.seamless.** { *; }
-keepclasseswithmembers class * {
@org.seamless.** *;
}
-keepclassmembers class * {
@org.seamless.** *;
}

-dontwarn org.fourthline.cling.**
-dontwarn org.seamless.**
-dontwarn org.eclipse.jetty.**
-dontwarn org.slf4j.**
-dontwarn javax.servlet.**


-keep class org.fourthline.cling.** { *;}
-keep class org.seamless.** { *;}
-keep class org.eclipse.jetty.** { *;}
-keep class org.slf4j.** { *;}
-keep class javax.servlet.** { *;}
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keep class com.crixmod.sailorcast.uiutils.** {*;}
-keep class com.crixmod.sailorcast.utils.** {*;}
-keep class com.crixmod.sailorcast.model.** {*;}

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep class com.baidu.** { *; }

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }


-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class com.crixmod.sailorcast.R$*{
    public static final int *;
}

-keep public class com.umeng.fb.ui.ThreadView {
}

-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**


-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

-keep class io.vov.utils.** { *; }
-keep class io.vov.vitamio.** { *; }

# For Vitamio classes
-keep public class io.vov.vitamio.MediaPlayer { *; }
-keep public class io.vov.vitamio.IMediaScannerService { *; }
-keep public class io.vov.vitamio.MediaScanner { *; }
-keep public class io.vov.vitamio.MediaScannerClient { *; }
-keep public class io.vov.vitamio.VitamioLicense { *; }
-keep public class io.vov.vitamio.Vitamio { *; }
-keep public class io.vov.vitamio.MediaMetadataRetriever { *; }
#-libraryjars libs/SocialSDK_QQZone_2.jar

-keep class cn.pedant.SweetAlert.Rotate3dAnimation {
    public <init>(...);
 }
