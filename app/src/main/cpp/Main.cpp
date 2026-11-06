#include <list>
#include <vector>
#include <string.h>
#include <pthread.h>
#include <cstring>
#include <jni.h>
#include <unistd.h>
#include <fstream>
#include <iostream>
#include <dlfcn.h>
#include "Includes/Logger.h"
#include "Includes/obfuscate.h"
#include "Includes/Utils.h"

#include "KittyMemory/MemoryPatch.h"
#include "Menu.h"

//Target lib here
#define targetLibName OBFUSCATE("libil2cpp.so")

#include "Includes/Macros.h"
#include "Tools.h"

// fancy struct for patches for kittyMemory
struct My_Patches {
    // let's assume we have patches for these functions for whatever game
    // like show in miniMap boolean function
    MemoryPatch GodMode, GodMode2, SliderExample;
    // etc...
} hexPatches;

bool feature1, feature2, featureHookToggle, Health;
int sliderValue = 1, level = 0;
void *instanceBtn;

// Hooking examples. Assuming you know how to write hook
void (*AddMoneyExample)(void *instance, int amount) = NULL;

bool (*old_get_BoolExample)(void *instance);
bool get_BoolExample(void *instance) {
    if (instance != NULL && featureHookToggle) {
        return true;
    }
    return old_get_BoolExample(instance);
}

float (*old_get_FloatExample)(void *instance);
float get_FloatExample(void *instance) {
    if (instance != NULL && sliderValue > 1) {
        return (float) sliderValue;
    }
    return old_get_FloatExample(instance);
}

int (*old_Level)(void *instance);
int Level(void *instance) {
    if (instance != NULL && level) {
        return (int) level;
    }
    return old_Level(instance);
}

void (*old_FunctionExample)(void *instance);
void FunctionExample(void *instance) {
    instanceBtn = instance;
    if (instance != NULL) {
        if (Health) {
            *(int *) ((uint64_t) instance + 0x48) = 999;
        }
    }
    return old_FunctionExample(instance);
}

// we will run our hacks in a new thread so our while loop doesn't block process main thread
void *hack_thread(void *) {
    LOGI(OBFUSCATE("pthread created"));

    //Check if target lib is loaded
    do {
        sleep(1);
    } while (!isLibraryLoaded(targetLibName));

    //Anti-lib rename
    /*
    do {
        sleep(1);
    } while (!isLibraryLoaded("libYOURNAME.so"));*/

    LOGI(OBFUSCATE("%s has been loaded"), (const char *) targetLibName);

#if defined(__aarch64__) //To compile this code for arm64 lib only.
    // Placeholder hooks/patches from the LGL template were removed: they pointed at fake
    // offsets (0x123456, "str") and non-existent libs, which makes getAbsoluteAddress()
    // return 0 and crashes the game with SIGSEGV as soon as libil2cpp.so is loaded.
    // Put YOUR real offsets here. Example (ARM64: MOV X0,#0 ; RET = 00 00 80 D2 C0 03 5F D6):
    //
    // hexPatches.GodMode = MemoryPatch::createWithHex(targetLibName,
    //                                                 string2Offset(OBFUSCATE("0xREAL_OFFSET")),
    //                                                 OBFUSCATE("00 00 80 D2 C0 03 5F D6"));
    // HOOK("0xREAL_OFFSET", FunctionExample, old_FunctionExample);
    // AddMoneyExample = (void(*)(void *,int))getAbsoluteAddress(targetLibName, 0xREAL_OFFSET);

    LOGI(OBFUSCATE("Done"));

#else //To compile this code for armv7 lib only.
    // Same as arm64: put YOUR real offsets here. Example (ARMv7: MOV R0,#0 ; BX LR = 00 00 A0 E3 1E FF 2F E1):
    //
    // hexPatches.GodMode = MemoryPatch::createWithHex(targetLibName,
    //                                                 string2Offset(OBFUSCATE("0xREAL_OFFSET")),
    //                                                 OBFUSCATE("00 00 A0 E3 1E FF 2F E1"));
    // HOOK("0xREAL_OFFSET", FunctionExample, old_FunctionExample);
    // AddMoneyExample = (void (*)(void *, int)) getAbsoluteAddress(targetLibName, 0xREAL_OFFSET);

    LOGI(OBFUSCATE("Done"));
#endif

    return NULL;
}

//JNI calls
extern "C" {

// Do not change or translate the first text unless you know what you are doing
// Assigning feature numbers is optional. Without it, it will automatically count for you, starting from 0
// Assigned feature numbers can be like any numbers 1,3,200,10... instead in order 0,1,2,3,4,5...
// ButtonLink, Category, RichTextView and RichWebView is not counted. They can't have feature number assigned
// Toggle, ButtonOnOff and Checkbox can be switched on by default, if you add True_. Example: CheckBox_True_The Check Box
// To learn HTML, go to this page: https://www.w3schools.com/

JNIEXPORT jobjectArray
JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_getFeatureList(JNIEnv *env, jobject context) {
    jobjectArray ret;

    //Toasts added here so it's harder to remove it
    MakeToast(env, context, OBFUSCATE("Modded by IShowAkuma"), Toast::LENGTH_LONG);

    const char *features[] = {
            OBFUSCATE("Category_LIBRARY SECTION"),

        OBFUSCATE("10_InputText_Input lib Name"),

        OBFUSCATE("11_Button_Set Lib"),

        OBFUSCATE("12_OffsetFind_Show Offsets"),
        
        "YoutubeLink_YouTube ──▶\xEF\xB8\x8E",

        "TeleGroupLink_Telegram Group ──▶\xEF\xB8\x8E",

        "TeleChannelLink_Telegram Channel ──▶\xEF\xB8\x8E",
    };

    //Now you dont have to manually update the number everytime;
    int Total_Feature = (sizeof features / sizeof features[0]);
    ret = (jobjectArray)
            env->NewObjectArray(Total_Feature, env->FindClass(OBFUSCATE("java/lang/String")),
                                env->NewStringUTF(""));

    for (int i = 0; i < Total_Feature; i++)
        env->SetObjectArrayElement(ret, i, env->NewStringUTF(features[i]));

    pthread_t ptid;
    pthread_create(&ptid, NULL, antiLeech, NULL);

    return (ret);
}

JNIEXPORT void JNICALL
Java_uk_lgl_modmenu_Preferences_Changes(JNIEnv *env, jclass clazz, jobject obj,
                                        jint featNum, jstring featName, jint value,
                                        jboolean boolean, jstring str) {
    std::string text;
    if (str != NULL) {
        const char *c = env->GetStringUTFChars(str, 0);
        if (c) {
            text = c;
            env->ReleaseStringUTFChars(str, c);
        }
    }
    LOGD(OBFUSCATE("Feature: %d | Value: %d | Bool: %d | Text: %s"), (int) featNum, (int) value,
         (int) boolean, text.c_str());
    tools::handle(env, obj, (int) featNum, (int) value, boolean == JNI_TRUE, text);
}

// ---- feature lists of the tabs (PATCH OFFSET ... EAZY PATCH). Ids are explicit so tabs never collide.
static jobjectArray makeList(JNIEnv *env, const char *const *items, int n) {
    jobjectArray ret = env->NewObjectArray(n, env->FindClass(OBFUSCATE("java/lang/String")),
                                           env->NewStringUTF(""));
    for (int i = 0; i < n; i++) env->SetObjectArrayElement(ret, i, env->NewStringUTF(items[i]));
    return ret;
}
#define LIST_SIZE(a) ((int) (sizeof(a) / sizeof((a)[0])))

JNIEXPORT jobjectArray JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_getFeatureList2(JNIEnv *env, jobject thiz) {
    const char *f[] = {
            OBFUSCATE("Category_PATCH OFFSET"),
            OBFUSCATE("20_InputText_Input Offset"),
            OBFUSCATE("21_InputText_Input Hex"),
            OBFUSCATE("22_Toggle_Patch"),
            OBFUSCATE("23_Button_PATCH"),
            OBFUSCATE("24_Button_RESTORE"),
    };
    return makeList(env, f, LIST_SIZE(f));
}

JNIEXPORT jobjectArray JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_getFeatureList3(JNIEnv *env, jobject thiz) {
    const char *f[] = {
            OBFUSCATE("Category_HOOK OFFSET"),
            OBFUSCATE("30_RadioButton_Hook_OFF,Hook Bool,Hook Int,Hook Float"),
            OBFUSCATE("31_InputText_Enter Offset"),
            OBFUSCATE("32_InputText_Enter Value"),
            OBFUSCATE("33_Toggle_Hook Enable"),
    };
    return makeList(env, f, LIST_SIZE(f));
}

JNIEXPORT jobjectArray JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_getFeatureList4(JNIEnv *env, jobject thiz) {
    const char *f[] = {
            OBFUSCATE("Category_HOOK FIELD OFFSET"),
            OBFUSCATE("40_RadioButton_Hook_OFF,bool,int,float"),
            OBFUSCATE("41_InputText_Enter Offset"),
            OBFUSCATE("42_InputText_Enter Value"),
            OBFUSCATE("43_InputText_Enter Update"),
            OBFUSCATE("44_Toggle_Hook Enable"),
    };
    return makeList(env, f, LIST_SIZE(f));
}

JNIEXPORT jobjectArray JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_getFeatureList5(JNIEnv *env, jobject thiz) {
    const char *f[] = {
            OBFUSCATE("Category_HOOK VOID"),
            OBFUSCATE("50_RadioButton_Hook_OFF,Void (bool value),Void (int Value),Void (float Value)"),
            OBFUSCATE("51_InputText_Enter Offset"),
            OBFUSCATE("52_InputText_Enter Value"),
            OBFUSCATE("53_InputText_Enter Update"),
            OBFUSCATE("54_Toggle_Hook Enable"),
    };
    return makeList(env, f, LIST_SIZE(f));
}

JNIEXPORT jobjectArray JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_getFeatureList6(JNIEnv *env, jobject thiz) {
    const char *f[] = {
            OBFUSCATE("Category_DISABLE VOID (ANTICRASH/ANTIDETECT TESTER)"),
            OBFUSCATE("60_RadioButton_Hook_OFF,Disactive Void"),
            OBFUSCATE("61_InputText_Enter Offset"),
            OBFUSCATE("62_Toggle_Hook Enable"),
    };
    return makeList(env, f, LIST_SIZE(f));
}

JNIEXPORT jobjectArray JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_getFeatureList7(JNIEnv *env, jobject thiz) {
    const char *f[] = {
            OBFUSCATE("Category_HOOK CALL VOID"),
            OBFUSCATE("70_RadioButton_Hook_OFF,Call Void"),
            OBFUSCATE("71_InputText_Enter Offset"),
            OBFUSCATE("72_Toggle_Hook Enable"),
    };
    return makeList(env, f, LIST_SIZE(f));
}

JNIEXPORT jobjectArray JNICALL
Java_uk_lgl_modmenu_FloatingModMenuService_getFeatureList8(JNIEnv *env, jobject thiz) {
    std::string t = std::string("80_Spinner_Choose Type_") + tools::EP_TYPE_LIST;
    std::string i = std::string("82_Spinner_Select Value For Int_") + tools::EP_INT_LIST;
    std::string fl = std::string("83_Spinner_Select Value For Float_") + tools::EP_FLT_LIST;
    std::string b = std::string("84_Spinner_Select Value For Bool_") + tools::EP_BOOL_LIST;
    const char *f[] = {
            OBFUSCATE("Category_EASY PATCHER"),
            t.c_str(),
            OBFUSCATE("81_InputText_Enter Offset"),
            i.c_str(),
            fl.c_str(),
            b.c_str(),
            OBFUSCATE("85_Toggle_Start Patching"),
    };
    return makeList(env, f, LIST_SIZE(f));
}
}

//No need to use JNI_OnLoad, since we don't use JNIEnv
//We do this to hide OnLoad from disassembler
__attribute__((constructor))
void lib_main() {
    // Create a new thread so it does not block the main thread, means the game would not freeze
    pthread_t ptid;
    pthread_create(&ptid, NULL, hack_thread, NULL);
}

/*
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *globalEnv;
    vm->GetEnv((void **) &globalEnv, JNI_VERSION_1_6);
    return JNI_VERSION_1_6;
}
 */
