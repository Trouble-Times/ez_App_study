#include <jni.h>
#include <string>

extern "C" JNIEXPORT jint JNICALL
Java_com_example_myapplication_BtnActivity_add(JNIEnv* env, jobject /* this */, 
                                              jint a, jint b, jint c) {
    return a + b + c;  // 返回三个参数的和
}