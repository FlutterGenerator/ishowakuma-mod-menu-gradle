#include "Icon.h"
#include "Background.h"
#include "EYE.h"
#include "SONIC.h"
#include "TELE.h"
#include "TELEVIP.h"
#include "YOUTUBE.h"

bool titleValid, headingValid, iconValid, settingsValid, isLeeched;

void *antiLeech(void *) {
    sleep(20);

    if (!titleValid || !headingValid || !iconValid || !settingsValid) {
        int *p = 0;
        *p = 0;
    }
    return NULL;
}

void setText(JNIEnv *env, jobject obj, const char* text){
    //https://stackoverflow.com/a/33627640/3763113
    //A little JNI calls here. You really really need a great knowledge if you want to play with JNI stuff
    //Html.fromHtml("");
    jclass html = (*env).FindClass(OBFUSCATE("android/text/Html"));
    jmethodID fromHtml = (*env).GetStaticMethodID(html, OBFUSCATE("fromHtml"), OBFUSCATE("(Ljava/lang/String;)Landroid/text/Spanned;"));

    //setText("");
    jclass textView = (*env).FindClass(OBFUSCATE("android/widget/TextView"));
    jmethodID setText = (*env).GetMethodID(textView, OBFUSCATE("setText"), OBFUSCATE("(Ljava/lang/CharSequence;)V"));

    //Java string
    jstring jstr = (*env).NewStringUTF(text);
    (*env).CallVoidMethod(obj, setText,  (*env).CallStaticObjectMethod(html, fromHtml, jstr));
}

extern "C" {
JNIEXPORT void JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_setTitleText(JNIEnv *env, jobject thiz, jobject obj) {
    setText(env, obj, OBFUSCATE("<b>Modded by IShowAkuma</b>"));

    titleValid = true;
}

JNIEXPORT void JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_setHeadingText(JNIEnv *env, jobject thiz, jobject obj) {
    setText(env, obj, OBFUSCATE("<b><marquee><p style=\"font-size:30\">"
                                      "<p style=\"color:green;\">Modded by IShowAkuma</p> | "
                                      "Subscribe My YouTube Channel</p>"
                                      "</marquee></b>"));

    headingValid = true;
}

JNIEXPORT jstring JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_Icon(JNIEnv *env, jobject thiz) {
    iconValid = true;

    //Use https://www.base64encode.org/ to encode your image to base64
    return env->NewStringUTF(
            ICON_B64);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_Background(
        JNIEnv *env,
        jobject thiz) {
    return env->NewStringUTF(Background_B64);
}

JNIEXPORT jstring JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_IconWebViewData(JNIEnv *env, jobject thiz) {
    iconValid = true;
    //WebView support GIF animation. Upload your image or GIF on imgur.com or other sites

    // From internet (Requires android.permission.INTERNET)
    // return env->NewStringUTF(OBFUSCATE("https://i.imgur.com/SujJ85j.gif"));

    // Base64 html:
    // return env->NewStringUTF("data:image/png;base64, <encoded base64 here>");

    // To disable it, return NULL. It will use normal image above:
    // return NULL

    //return env->NewStringUTF(OBFUSCATE_KEY("https://i.imgur.com/SujJ85j.gif", 'u'));
    return NULL;
}

JNIEXPORT jobjectArray JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_settingsList(JNIEnv *env, jobject activityObject) {
    jobjectArray ret;

    const char *features[] = {
            OBFUSCATE("Category_SETTINGS"),
            OBFUSCATE("RichTimeDate_Time"),
            OBFUSCATE("-1_Toggle_Save feature preferences"), //-1 is checked on Preferences.java
            OBFUSCATE("-3_Toggle_True_Auto size vertically"),
            OBFUSCATE("Category_LOGCAT"),
            OBFUSCATE("-4_Button_Save logcat to file"),
            OBFUSCATE("-5_Button_Clear logcat"),
            OBFUSCATE("Category_BACKGROUND SETTINGS"),
            OBFUSCATE("-7_Toggle_Remove Background"),
            OBFUSCATE("-2_ColorPicker_Color Picker_255_255_255_255"),
            OBFUSCATE("-6_Toggle_No Color"),
            OBFUSCATE("Category_ICON SETTINGS"),
            OBFUSCATE("-8_SeekBar_Icon Opacity_1_9"),
            OBFUSCATE("-9_SeekBar_Icon Size_1_99"),
            OBFUSCATE("-10_SeekBar_Spin Icon_0_20"),
            OBFUSCATE("Category_MENU SETTINGS"),
            OBFUSCATE("-11_Button_Hide Mod Menu"),
            OBFUSCATE("-12_Button_Kill Mod Menu"),
            OBFUSCATE("Category_CONNECT WITH ME"),
            // Replace the links with your own
            OBFUSCATE("ButtonLink_YouTube_https://youtube.com/"),
            OBFUSCATE("ButtonLink_Telegram Group_https://telegram.org/"),
            OBFUSCATE("ButtonLink_Telegram Channel_https://telegram.org/"),
    };

    int Total_Feature = (sizeof features /
                         sizeof features[0]); //Now you dont have to manually update the number everytime;
    ret = (jobjectArray)
            env->NewObjectArray(Total_Feature, env->FindClass(OBFUSCATE("java/lang/String")),
                                env->NewStringUTF(""));
    int i;
    for (i = 0; i < Total_Feature; i++)
        env->SetObjectArrayElement(ret, i, env->NewStringUTF(features[i]));

    settingsValid = true;

    return (ret);
}
}

// ---- Natives declared in FloatingModMenuService.java (were missing => UnsatisfiedLinkError) ----
#define IMG_NATIVE_B64(NAME, DATA) \
JNIEXPORT jstring JNICALL Java_uk_lgl_modmenu_FloatingModMenuService_##NAME(JNIEnv *env, jobject thiz) { \
    return env->NewStringUTF(DATA); }
#define EMPTY_LIST_NATIVE(NAME) \
JNIEXPORT jobjectArray JNICALL Java_uk_lgl_modmenu_FloatingModMenuService_##NAME(JNIEnv *env, jobject thiz) { \
    return env->NewObjectArray(0, env->FindClass("java/lang/String"), env->NewStringUTF("")); }

extern "C" {
IMG_NATIVE_B64(EYE, EYE_B64)
IMG_NATIVE_B64(EYECLOSE, EYECLOSE_B64)
IMG_NATIVE_B64(IconTele, TELE_B64)
IMG_NATIVE_B64(IconTeleVip, TELEVIP_B64)
IMG_NATIVE_B64(IconYoutube, YOUTUBE_B64)
IMG_NATIVE_B64(Sonic, SONIC_B64)
}
