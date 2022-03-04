#ifndef RLOTTIE_H
#define RLOTTIE_H

#include <android/log.h>
#include <jni.h>

#define LOG_TAG "rlottie_native"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)

#ifndef MAX
#define MAX(x, y) ((x) > (y)) ? (x) : (y)
#endif
#ifndef MIN
#define MIN(x, y) ((x) < (y)) ? (x) : (y)
#endif

using namespace rlottie;

typedef struct LottieInfo {

    ~LottieInfo() {
        if (decompressBuffer != nullptr) {
            delete[]decompressBuffer;
            decompressBuffer = nullptr;
        }
    }

    std::unique_ptr<Animation> animation;
    size_t frameCount = 0;
    int32_t fps = 30;
    bool precache = false;
    bool createCache = false;
    bool limitFps = false;
    std::string path;
    std::string cacheFile;
    uint8_t *decompressBuffer = nullptr;
    uint32_t decompressBufferSize = 0;
    volatile uint32_t maxFrameSize = 0;
    uint32_t imageSize = 0;
    uint32_t fileOffset = 0;
    bool nextFrameIsCacheFrame = false;

    FILE *precacheFile = nullptr;
    char *compressBuffer = nullptr;
    const char *buffer = nullptr;
    bool firstFrame = false;
    int bufferSize = 0;
    int compressBound = 0;
    int firstFrameSize = 0;
    volatile uint32_t framesAvailableInCache = 0;
};

#endif
