cmake_minimum_required(VERSION 3.6.0)

project(rlottie)

set(CMAKE_SYSROOT /data/data/com.termux/files/home/ndk-r21e-sysroot/sysroot)

set(CMAKE_CXX_FLAGS "-std=c++14 -DANDROID -g")
set(CMAKE_C_FLAGS "-w -std=c11 -DANDROID -g")
set(CMAKE_ASM_FLAGS "${CFLAGS} -x assembler-with-cpp")

if (${ANDROID_ABI} STREQUAL "armeabi-v7a" OR ${ANDROID_ABI} STREQUAL "arm64-v8a")
    enable_language(ASM)
else()
    enable_language(ASM_NASM)
endif()

#
#lz4
#
add_library(lz4 STATIC
        lz4/lz4.c
        lz4/lz4frame.c
        lz4/xxhash.c)
target_compile_options(lz4 PUBLIC -w -O3)
set_target_properties(lz4 PROPERTIES ANDROID_ARM_MODE arm)
        
#
#rlottielib
#
add_library(rlottielib STATIC
        rlottie/src/lottie/lottieanimation.cpp
        rlottie/src/lottie/lottieitem.cpp
        rlottie/src/lottie/lottiekeypath.cpp
        rlottie/src/lottie/lottieloader.cpp
        rlottie/src/lottie/lottiemodel.cpp
        rlottie/src/lottie/lottieparser.cpp
        rlottie/src/lottie/lottieproxymodel.cpp
        rlottie/src/vector/freetype/v_ft_math.cpp
        rlottie/src/vector/freetype/v_ft_raster.cpp
        rlottie/src/vector/freetype/v_ft_stroker.cpp
        rlottie/src/vector/pixman/vregion.cpp
        rlottie/src/vector/stb/stb_image.cpp
        rlottie/src/vector/vbezier.cpp
        rlottie/src/vector/vbitmap.cpp
        rlottie/src/vector/vbrush.cpp
        rlottie/src/vector/vcompositionfunctions.cpp
        rlottie/src/vector/vdasher.cpp
        rlottie/src/vector/vdebug.cpp
        rlottie/src/vector/vdrawable.cpp
        rlottie/src/vector/vdrawhelper.cpp
        rlottie/src/vector/vdrawhelper_neon.cpp
        rlottie/src/vector/velapsedtimer.cpp
        rlottie/src/vector/vimageloader.cpp
        rlottie/src/vector/vinterpolator.cpp
        rlottie/src/vector/vmatrix.cpp
        rlottie/src/vector/vpainter.cpp
        rlottie/src/vector/vpath.cpp
        rlottie/src/vector/vpathmesure.cpp
        rlottie/src/vector/vraster.cpp
        rlottie/src/vector/vrect.cpp
        rlottie/src/vector/vrle.cpp)
target_compile_options(rlottielib PUBLIC -Wall -fno-rtti -finline-functions -ffast-math -Os -fno-unwind-tables -fno-asynchronous-unwind-tables -Wnon-virtual-dtor -Woverloaded-virtual -Wno-unused-parameter -fvisibility=hidden)
set_target_properties(rlottielib PROPERTIES ANDROID_ARM_MODE arm)
target_compile_definitions(rlottielib PUBLIC HAVE_PTHREAD NDEBUG)
target_include_directories(rlottielib PUBLIC
        ./
        rlottie/inc
        rlottie/src/vector/
        rlottie/src/vector/pixman
        rlottie/src/vector/freetype
        rlottie/src/vector/stb)
        
if (${ANDROID_ABI} STREQUAL "armeabi-v7a")
    target_compile_options(rlottielib PUBLIC -fno-integrated-as)
    target_compile_definitions(rlottielib PUBLIC USE_ARM_NEON)
    target_sources(rlottielib PRIVATE rlottie/src/vector/pixman/pixman-arm-neon-asm.S)
elseif(${ANDROID_ABI} STREQUAL "arm64-v8a")
    target_compile_options(rlottielib PUBLIC -fno-integrated-as)
    target_compile_definitions(rlottielib PUBLIC USE_ARM_NEON __ARM64_NEON__)
    target_sources(rlottielib PRIVATE rlottie/src/vector/pixman/pixman-arma64-neon-asm.S)
endif()
        
#
#rlottie
#
add_library(rlottie SHARED lottie.cpp)
target_compile_options(rlottie PUBLIC -ffast-math -Os -funroll-loops -ffast-math -fno-strict-aliasing -fno-math-errno)

if (${ANDROID_ABI} STREQUAL "armeabi-v7a")
    set_target_properties(rlottie PROPERTIES
            ANDROID_ARM_NEON FALSE
            ANDROID_ARM_MODE arm)
    target_compile_definitions(rlottie PUBLIC
            ANDROID_ARM_NEON=false)
else()
    set_target_properties(rlottie PROPERTIES
            ANDROID_ARM_NEON FALSE
            ANDROID_ARM_MODE arm)
endif()

target_include_directories(rlottie PUBLIC 
    lz4 
    rlottie/inc)

target_link_libraries(rlottie
    lz4 
    rlottielib 
    jnigraphics 
    c++_static 
    log)
