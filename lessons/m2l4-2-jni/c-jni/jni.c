#include "ru_otus_otuskotlin_interop_NativeExample.h"

JNIEXPORT jint JNICALL Java_ru_otus_otuskotlin_interop_NativeExample_native_1add
  (JNIEnv *env, jobject jo, jint a, jint b) {
  return a + b;
}
