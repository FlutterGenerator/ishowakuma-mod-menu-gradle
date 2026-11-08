// Tools.h - native logic for the menu tabs:
// LIBRARY, PATCH OFFSET, HOOK OFFSET, FIELD OFFSET, HOOK VOID, DISABLE VOID, CALL VOID, EAZY PATCH
// Must be included AFTER Includes/Macros.h (needs A64HookFunction / MSHookFunction).
#pragma once

#include <jni.h>
#include <string>
#include <vector>
#include <cstdint>
#include <cstring>
#include <cstdlib>
#include <cctype>

namespace tools {

// ---------------------------------------------------------------- feature ids
enum {
    F_LIB_NAME = 10, F_LIB_SET = 11, F_LIB_SHOW = 12,
    F_P_OFF = 20, F_P_HEX = 21, F_P_TOGGLE = 22, F_P_APPLY = 23, F_P_RESTORE = 24,
    F_HO_MODE = 30, F_HO_OFF = 31, F_HO_VAL = 32, F_HO_ON = 33,
    F_FD_MODE = 40, F_FD_OFF = 41, F_FD_VAL = 42, F_FD_UPD = 43, F_FD_ON = 44,
    F_HV_MODE = 50, F_HV_OFF = 51, F_HV_VAL = 52, F_HV_UPD = 53, F_HV_ON = 54,
    F_DV_MODE = 60, F_DV_OFF = 61, F_DV_ON = 62,
    F_CV_MODE = 70, F_CV_OFF = 71, F_CV_ON = 72,
    F_EP_TYPE = 80, F_EP_OFF = 81, F_EP_INT = 82, F_EP_FLT = 83, F_EP_BOOL = 84, F_EP_ON = 85
};

// ---------------------------------------------------------------- helpers
static std::string gLib = "libil2cpp.so";

static std::string trim(const std::string &in) {
    size_t b = 0, e = in.size();
    while (b < e && isspace((unsigned char) in[b])) b++;
    while (e > b && isspace((unsigned char) in[e - 1])) e--;
    return in.substr(b, e - b);
}

static bool parseHex(const std::string &in, uintptr_t &out) {
    std::string s = trim(in);
    if (s.empty()) return false;
    char *end = nullptr;
    unsigned long long v = strtoull(s.c_str(), &end, 16); // accepts optional 0x
    if (end == s.c_str() || *end != '\0') return false;
    out = (uintptr_t) v;
    return true;
}

static bool parseBool(const std::string &in) {
    std::string s = trim(in);
    for (auto &c : s) c = (char) tolower((unsigned char) c);
    return s == "1" || s == "true" || s == "on";
}

static void toast(JNIEnv *env, jobject ctx, const char *msg) {
    if (env && ctx) MakeToast(env, ctx, msg, 0);
}

static bool doHook(void *target, void *stub, void **orig) {
    if (!target) return false;
#if defined(__aarch64__)
    A64HookFunction(target, stub, orig);
#else
    MSHookFunction(target, stub, orig);
#endif
    return true;
}

// ---------------------------------------------------------------- memory patches
static std::string gInfo; // "abs=0x... was=XXXXXXXX" of the last patch (shown in toast + logcat)

static void rememberInfo(MemoryPatch &mp) {
    char b[96];
    snprintf(b, sizeof(b), "abs=0x%llx was=", (unsigned long long) mp.get_TargetAddress());
    gInfo = std::string(b) + mp.get_CurrBytes();
    LOGI("%s", gInfo.c_str());
}

static bool applyHex(MemoryPatch &mp, uintptr_t off, const std::string &hex) {
    if (mp.isValid()) mp.Restore();
    mp = MemoryPatch::createWithHex(gLib.c_str(), off, hex);
    if (!mp.isValid()) return false;
    rememberInfo(mp);
    return mp.Modify();
}

static bool applyBytes(MemoryPatch &mp, uintptr_t off, const std::vector<uint8_t> &bytes) {
    if (bytes.empty()) return false;
    if (mp.isValid()) mp.Restore();
    mp = MemoryPatch(gLib.c_str(), off, bytes.data(), bytes.size());
    if (!mp.isValid()) return false;
    rememberInfo(mp);
    return mp.Modify();
}

static bool restorePatch(MemoryPatch &mp) {
    return mp.isValid() && mp.Restore();
}

static void put32(std::vector<uint8_t> &v, uint32_t w) {
    for (int i = 0; i < 4; i++) v.push_back((uint8_t) ((w >> (8 * i)) & 0xFF));
}

// Code that makes a function return a constant. type: 1 = int/bool (w0/r0), 2 = float
static bool buildReturn(int type, uint32_t bits, std::vector<uint8_t> &out) {
    out.clear();
    uint32_t lo = bits & 0xFFFF, hi = bits >> 16;
#if defined(__aarch64__)
    if (type == 2) {
        put32(out, 0x52800008 | (lo << 5));   // movz w8, #lo
        put32(out, 0x72A00008 | (hi << 5));   // movk w8, #hi, lsl 16
        put32(out, 0x1E270100);               // fmov s0, w8
    } else {
        put32(out, 0x52800000 | (lo << 5));   // movz w0, #lo
        if (hi) put32(out, 0x72A00000 | (hi << 5)); // movk w0, #hi, lsl 16
    }
    put32(out, 0xD65F03C0);                   // ret
    return true;
#elif defined(__arm__)
    // softfp ABI: float is returned in r0 as well
    put32(out, 0xE3000000 | ((lo >> 12) << 16) | (lo & 0xFFF)); // movw r0, #lo
    if (hi) put32(out, 0xE3400000 | ((hi >> 12) << 16) | (hi & 0xFFF)); // movt r0, #hi
    put32(out, 0xE12FFF1E);                   // bx lr
    (void) type;
    return true;
#else
    (void) type; (void) bits;
    return false;
#endif
}

static bool buildRet(std::vector<uint8_t> &out) {
    out.clear();
#if defined(__aarch64__)
    put32(out, 0xD65F03C0);
    return true;
#elif defined(__arm__)
    put32(out, 0xE12FFF1E);
    return true;
#else
    return false;
#endif
}

// ---------------------------------------------------------------- state
struct HookState {
    bool installed = false;
    uintptr_t off = 0;
};

static JNIEnv *g_env_for_toast = nullptr;
static jobject g_ctx_for_toast = nullptr;

static bool installHook(HookState &st, uintptr_t off, void *stub, void **orig, std::string &err) {
    if (st.installed) {
        if (st.off != off) {
            char tmp[96];
            snprintf(tmp, sizeof(tmp), "Already hooked at 0x%llx. Restart the game to hook another offset",
                     (unsigned long long) st.off);
            err = tmp;
            return false;
        }
        return true;
    }
    uintptr_t addr = (uintptr_t) getAbsoluteAddress(gLib.c_str(), off);
    if (!addr) { err = "Library not loaded: " + gLib; return false; }
    if (!doHook((void *) addr, stub, orig)) { err = "Hook failed"; return false; }
    st.installed = true;
    st.off = off;
    LOGI("Hook installed at abs=0x%llx", (unsigned long long) addr);
    toast(g_env_for_toast, g_ctx_for_toast, "Hook installed");
    return true;
}

// raw text of every input
static std::string sLibIn, sPOff, sPHex;
static std::string sHoOff, sHoVal;
static std::string sFdOff, sFdVal, sFdUpd;
static std::string sHvOff, sHvVal, sHvUpd;
static std::string sDvOff, sCvOff, sEpOff;

// radio / spinner / toggle state (radio: 1 = OFF, 2.. = options)
static int mHo = 1, mFd = 1, mHv = 1, mDv = 1, mCv = 1;
static int epType = 0, epInt = 0, epFlt = 0, epBool = 0;
static bool onHo = false, onFd = false, onHv = false, onDv = false, onCv = false, onEp = false;

static MemoryPatch gPatch, gDisable, gEasy;

// ---------------------------------------------------------------- HOOK OFFSET (return value)
static bool hoBool = false;
static int hoInt = 0;
static float hoFloat = 0;
static HookState stHoB, stHoI, stHoF;

static bool (*oldHoB)(void *);
static bool newHoB(void *i) {
    if (onHo && mHo == 2) return hoBool;
    return oldHoB ? oldHoB(i) : false;
}
static int (*oldHoI)(void *);
static int newHoI(void *i) {
    if (onHo && mHo == 3) return hoInt;
    return oldHoI ? oldHoI(i) : 0;
}
static float (*oldHoF)(void *);
static float newHoF(void *i) {
    if (onHo && mHo == 4) return hoFloat;
    return oldHoF ? oldHoF(i) : 0.f;
}

static void refreshHookOffset(JNIEnv *env, jobject ctx) {
    hoBool = parseBool(sHoVal);
    hoInt = atoi(trim(sHoVal).c_str());
    hoFloat = (float) atof(trim(sHoVal).c_str());
    if (!onHo || mHo < 2) return;
    uintptr_t off;
    if (!parseHex(sHoOff, off)) { toast(env, ctx, "Hook Offset: enter a valid offset"); return; }
    std::string err;
    bool ok = false;
    if (mHo == 2) ok = installHook(stHoB, off, (void *) newHoB, (void **) &oldHoB, err);
    if (mHo == 3) ok = installHook(stHoI, off, (void *) newHoI, (void **) &oldHoI, err);
    if (mHo == 4) ok = installHook(stHoF, off, (void *) newHoF, (void **) &oldHoF, err);
    if (!ok) toast(env, ctx, err.c_str());
}

// ---------------------------------------------------------------- HOOK FIELD OFFSET
// Hooks a void instance method (e.g. Update) and writes "Update" into the field at "Value" offset.
static uintptr_t fdField = 0;
static bool fdBool = false;
static int fdInt = 0;
static float fdFloat = 0;
static HookState stFd;

static void (*oldFd)(void *, void *, void *, void *);
static void newFd(void *i, void *a, void *b, void *c) {
    if (i && onFd && mFd >= 2 && fdField) {
        if (mFd == 2) *(bool *) ((uintptr_t) i + fdField) = fdBool;
        else if (mFd == 3) *(int *) ((uintptr_t) i + fdField) = fdInt;
        else if (mFd == 4) *(float *) ((uintptr_t) i + fdField) = fdFloat;
    }
    if (oldFd) oldFd(i, a, b, c);
}

static void refreshField(JNIEnv *env, jobject ctx) {
    if (!parseHex(sFdVal, fdField)) fdField = 0;
    fdBool = parseBool(sFdUpd);
    fdInt = atoi(trim(sFdUpd).c_str());
    fdFloat = (float) atof(trim(sFdUpd).c_str());
    if (!onFd || mFd < 2) return;
    uintptr_t off;
    if (!parseHex(sFdOff, off)) { toast(env, ctx, "Field: enter a valid method offset"); return; }
    if (!fdField) { toast(env, ctx, "Field: enter field offset in 'Enter Value'"); return; }
    std::string err;
    if (!installHook(stFd, off, (void *) newFd, (void **) &oldFd, err)) toast(env, ctx, err.c_str());
}

// ---------------------------------------------------------------- HOOK VOID (setter-like: void f(this, T value))
// Value = new argument. If Update is filled: only replace when argument == Value, with Update.
static HookState stHvB, stHvI, stHvF;
static bool hvHasUpd = false;
static bool hvValB = false, hvUpdB = false;
static int hvValI = 0, hvUpdI = 0;
static float hvValF = 0, hvUpdF = 0;

static void (*oldHvB)(void *, bool);
static void newHvB(void *i, bool v) {
    if (onHv && mHv == 2) {
        if (!hvHasUpd) v = hvValB; else if (v == hvValB) v = hvUpdB;
    }
    if (oldHvB) oldHvB(i, v);
}
static void (*oldHvI)(void *, int);
static void newHvI(void *i, int v) {
    if (onHv && mHv == 3) {
        if (!hvHasUpd) v = hvValI; else if (v == hvValI) v = hvUpdI;
    }
    if (oldHvI) oldHvI(i, v);
}
static void (*oldHvF)(void *, float);
static void newHvF(void *i, float v) {
    if (onHv && mHv == 4) {
        if (!hvHasUpd) v = hvValF; else if (v == hvValF) v = hvUpdF;
    }
    if (oldHvF) oldHvF(i, v);
}

static void refreshHookVoid(JNIEnv *env, jobject ctx) {
    hvHasUpd = !trim(sHvUpd).empty();
    hvValB = parseBool(sHvVal);  hvUpdB = parseBool(sHvUpd);
    hvValI = atoi(trim(sHvVal).c_str());  hvUpdI = atoi(trim(sHvUpd).c_str());
    hvValF = (float) atof(trim(sHvVal).c_str());  hvUpdF = (float) atof(trim(sHvUpd).c_str());
    if (!onHv || mHv < 2) return;
    uintptr_t off;
    if (!parseHex(sHvOff, off)) { toast(env, ctx, "Hook Void: enter a valid offset"); return; }
    std::string err;
    bool ok = false;
    if (mHv == 2) ok = installHook(stHvB, off, (void *) newHvB, (void **) &oldHvB, err);
    if (mHv == 3) ok = installHook(stHvI, off, (void *) newHvI, (void **) &oldHvI, err);
    if (mHv == 4) ok = installHook(stHvF, off, (void *) newHvF, (void **) &oldHvF, err);
    if (!ok) toast(env, ctx, err.c_str());
}

// ---------------------------------------------------------------- DISABLE VOID (function -> immediate return)
static void refreshDisable(JNIEnv *env, jobject ctx) {
    uintptr_t off;
    if (onDv && mDv == 2) {
        if (!parseHex(sDvOff, off)) { toast(env, ctx, "Disable Void: enter a valid offset"); return; }
        std::vector<uint8_t> ret;
        if (!buildRet(ret)) { toast(env, ctx, "Unsupported CPU architecture"); return; }
        if (!applyBytes(gDisable, off, ret)) toast(env, ctx, "Disable Void failed (library not loaded / bad offset)");
        else toast(env, ctx, ("Disable Void ON " + gInfo).c_str());
    } else {
        if (restorePatch(gDisable)) toast(env, ctx, "Disable Void: restored original code");
    }
}

// ---------------------------------------------------------------- CALL VOID (calls method with captured instance)
static HookState stCv;
static void *cvInstance = nullptr;
static void (*oldCv)(void *);
static void newCv(void *i) {
    cvInstance = i;
    if (oldCv) oldCv(i);
}

static void refreshCall(JNIEnv *env, jobject ctx) {
    if (!onCv || mCv < 2) return;
    uintptr_t off;
    if (!parseHex(sCvOff, off)) { toast(env, ctx, "Call Void: enter a valid offset"); return; }
    std::string err;
    if (!installHook(stCv, off, (void *) newCv, (void **) &oldCv, err)) { toast(env, ctx, err.c_str()); return; }
    if (cvInstance && oldCv) oldCv(cvInstance);
    else toast(env, ctx, "Call Void: waiting until the game calls this method once");
}

// ---------------------------------------------------------------- EAZY PATCH
// Spinner tables. Index 0 of every table is "Off - 00".
static const char *EP_TYPE_LIST = "Off,Int,Float,Bool";
static const char *EP_INT_LIST = "Off - 00,1,2,5,10,50,100,500,999,9999,99999,999999";
static const char *EP_FLT_LIST = "Off - 00,0.0,0.5,1.0,2.0,5.0,10.0,50.0,100.0,500.0,999.0,9999.0";
static const char *EP_BOOL_LIST = "Off - 00,True,False";
static const int EP_INT_V[] = {0, 1, 2, 5, 10, 50, 100, 500, 999, 9999, 99999, 999999};
static const float EP_FLT_V[] = {0, 0.0f, 0.5f, 1.0f, 2.0f, 5.0f, 10.0f, 50.0f, 100.0f, 500.0f, 999.0f, 9999.0f};

static void refreshEasy(JNIEnv *env, jobject ctx) {
    bool want = onEp && epType >= 1;
    if (!want) { if (restorePatch(gEasy)) toast(env, ctx, "Easy Patch: restored original code"); return; }
    uintptr_t off;
    if (!parseHex(sEpOff, off)) { toast(env, ctx, "Easy Patch: enter a valid offset"); return; }
    uint32_t bits = 0;
    int kind = 1;
    if (epType == 1) {
        if (epInt < 1 || epInt > 11) { toast(env, ctx, "Easy Patch: select an Int value"); return; }
        bits = (uint32_t) EP_INT_V[epInt];
    } else if (epType == 2) {
        if (epFlt < 1 || epFlt > 11) { toast(env, ctx, "Easy Patch: select a Float value"); return; }
        float f = EP_FLT_V[epFlt];
        memcpy(&bits, &f, 4);
        kind = 2;
    } else {
        if (epBool < 1 || epBool > 2) { toast(env, ctx, "Easy Patch: select a Bool value"); return; }
        bits = (epBool == 1) ? 1u : 0u;
    }
    std::vector<uint8_t> code;
    if (!buildReturn(kind, bits, code)) { toast(env, ctx, "Unsupported CPU architecture"); return; }
    if (!applyBytes(gEasy, off, code)) toast(env, ctx, "Easy Patch failed (library not loaded / bad offset)");
    else toast(env, ctx, ("Easy Patch ON " + gInfo).c_str());
}

// ---------------------------------------------------------------- toast for EVERY action
#include <time.h>
static const char *featName(int n) {
    switch (n) {
        case F_LIB_NAME: return "Lib: name";      case F_LIB_SET: return "Lib: Set Lib";
        case F_LIB_SHOW: return "Lib: Show Offsets";
        case F_P_OFF: return "Patch: offset";     case F_P_HEX: return "Patch: hex";
        case F_P_TOGGLE: return "Patch: toggle";  case F_P_APPLY: return "Patch: PATCH";
        case F_P_RESTORE: return "Patch: RESTORE";
        case F_HO_MODE: return "HookOffset: mode"; case F_HO_OFF: return "HookOffset: offset";
        case F_HO_VAL: return "HookOffset: value"; case F_HO_ON: return "HookOffset: enable";
        case F_FD_MODE: return "Field: type";     case F_FD_OFF: return "Field: method offset";
        case F_FD_VAL: return "Field: field offset"; case F_FD_UPD: return "Field: update value";
        case F_FD_ON: return "Field: enable";
        case F_HV_MODE: return "HookVoid: mode";  case F_HV_OFF: return "HookVoid: offset";
        case F_HV_VAL: return "HookVoid: value";  case F_HV_UPD: return "HookVoid: update";
        case F_HV_ON: return "HookVoid: enable";
        case F_DV_MODE: return "DisableVoid: mode"; case F_DV_OFF: return "DisableVoid: offset";
        case F_DV_ON: return "DisableVoid: enable";
        case F_CV_MODE: return "CallVoid: mode";  case F_CV_OFF: return "CallVoid: offset";
        case F_CV_ON: return "CallVoid: enable";
        case F_EP_TYPE: return "Easy: type";      case F_EP_OFF: return "Easy: offset";
        case F_EP_INT: return "Easy: int value";  case F_EP_FLT: return "Easy: float value";
        case F_EP_BOOL: return "Easy: bool value"; case F_EP_ON: return "Easy: enable";
        default: return "Feature";
    }
}

static bool isToggleOrButton(int n) {
    return n == F_P_TOGGLE || n == F_HO_ON || n == F_FD_ON || n == F_HV_ON || n == F_DV_ON ||
           n == F_CV_ON || n == F_EP_ON;
}
static bool isTextInput(int n) {
    return n == F_LIB_NAME || n == F_P_OFF || n == F_P_HEX || n == F_HO_OFF || n == F_HO_VAL ||
           n == F_FD_OFF || n == F_FD_VAL || n == F_FD_UPD || n == F_HV_OFF || n == F_HV_VAL ||
           n == F_HV_UPD || n == F_DV_OFF || n == F_CV_OFF || n == F_EP_OFF;
}

// ---------------------------------------------------------------- dispatcher
static void handle(JNIEnv *env, jobject ctx, int n, int val, bool b, const std::string &s) {
    g_env_for_toast = env;
    g_ctx_for_toast = ctx;
    // The menu fires every feature once while it is being built: stay quiet for the first 3 s
    static time_t quietUntil = 0;
    time_t now = time(nullptr);
    if (quietUntil == 0) quietUntil = now + 3;
    if (now >= quietUntil && n >= 10) {
        std::string t = std::string("[") + featName(n) + "] ";
        if (isToggleOrButton(n)) t += b ? "ON" : "OFF";
        else if (isTextInput(n)) t += "= " + (s.empty() ? std::string("(empty)") : s);
        else if (n == F_LIB_SET || n == F_LIB_SHOW || n == F_P_APPLY || n == F_P_RESTORE) t += "pressed";
        else t += "= " + std::to_string(val);
        toast(env, ctx, t.c_str());
    }
    switch (n) {
        // ---- library
        case F_LIB_NAME: sLibIn = trim(s); break;
        case F_LIB_SET:
            if (sLibIn.empty()) { toast(env, ctx, "Enter a library name first"); break; }
            gLib = sLibIn;
            toast(env, ctx, isLibraryLoaded(gLib.c_str()) ? ("Lib set: " + gLib).c_str()
                                                           : ("Lib set (not loaded yet): " + gLib).c_str());
            break;
        case F_LIB_SHOW: {
            uintptr_t base = (uintptr_t) findLibrary(gLib.c_str());
            char buf[256];
            snprintf(buf, sizeof(buf), "%s base: 0x%llx", gLib.c_str(), (unsigned long long) base);
            toast(env, ctx, base ? buf : ("Not loaded: " + gLib).c_str());
            break;
        }
        // ---- patch offset
        case F_P_OFF: sPOff = s; break;
        case F_P_HEX: sPHex = s; break;
        case F_P_TOGGLE:
        case F_P_APPLY:
        case F_P_RESTORE: {
            bool apply = (n == F_P_APPLY) || (n == F_P_TOGGLE && b);
            if (!apply) { if (restorePatch(gPatch)) toast(env, ctx, "Patch: restored original code"); break; }
            uintptr_t off;
            if (!parseHex(sPOff, off) || trim(sPHex).empty()) {
                toast(env, ctx, "Patch: enter offset and hex");
                break;
            }
            if (!applyHex(gPatch, off, trim(sPHex))) toast(env, ctx, "Patch failed (library not loaded / bad offset / bad hex)");
            else toast(env, ctx, ("Patched OK " + gInfo).c_str());
            break;
        }
        // ---- hook offset
        case F_HO_MODE: mHo = val; refreshHookOffset(env, ctx); break;
        case F_HO_OFF: sHoOff = s; refreshHookOffset(env, ctx); break;
        case F_HO_VAL: sHoVal = s; refreshHookOffset(env, ctx); break;
        case F_HO_ON: onHo = b; refreshHookOffset(env, ctx); break;
        // ---- field
        case F_FD_MODE: mFd = val; refreshField(env, ctx); break;
        case F_FD_OFF: sFdOff = s; refreshField(env, ctx); break;
        case F_FD_VAL: sFdVal = s; refreshField(env, ctx); break;
        case F_FD_UPD: sFdUpd = s; refreshField(env, ctx); break;
        case F_FD_ON: onFd = b; refreshField(env, ctx); break;
        // ---- hook void
        case F_HV_MODE: mHv = val; refreshHookVoid(env, ctx); break;
        case F_HV_OFF: sHvOff = s; refreshHookVoid(env, ctx); break;
        case F_HV_VAL: sHvVal = s; refreshHookVoid(env, ctx); break;
        case F_HV_UPD: sHvUpd = s; refreshHookVoid(env, ctx); break;
        case F_HV_ON: onHv = b; refreshHookVoid(env, ctx); break;
        // ---- disable void
        case F_DV_MODE: mDv = val; refreshDisable(env, ctx); break;
        case F_DV_OFF: sDvOff = s; refreshDisable(env, ctx); break;
        case F_DV_ON: onDv = b; refreshDisable(env, ctx); break;
        // ---- call void
        case F_CV_MODE: mCv = val; break;
        case F_CV_OFF: sCvOff = s; break;
        case F_CV_ON: onCv = b; refreshCall(env, ctx); break;
        // ---- easy patch
        case F_EP_TYPE: epType = val; refreshEasy(env, ctx); break;
        case F_EP_OFF: sEpOff = s; refreshEasy(env, ctx); break;
        case F_EP_INT: epInt = val; refreshEasy(env, ctx); break;
        case F_EP_FLT: epFlt = val; refreshEasy(env, ctx); break;
        case F_EP_BOOL: epBool = val; refreshEasy(env, ctx); break;
        case F_EP_ON: onEp = b; refreshEasy(env, ctx); break;
        default: break;
    }
}

} // namespace tools
