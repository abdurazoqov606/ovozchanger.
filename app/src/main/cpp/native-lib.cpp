#include <jni.h>
#include <string>
#include <vector>

// Haqiqiy mukammallik uchun bu yerda SoundTouch kutubxonasi funksiyalari bo'lishi kerak.
// Hozircha biz JNI ko'prigini mukammal sozlaymiz.

extern "C" JNIEXPORT void JNICALL
Java_com_ovoz_changer_AudioEngine_processAudio(
        JNIEnv* env, jobject /* this */,
        jshortArray buffer, jint size, jfloat pitch, jfloat rate) {
    
    jshort* audioData = env->GetShortArrayElements(buffer, nullptr);

    // Bu yerda audio signalga matematik ishlov beriladi
    // Pitch > 1.0 bo'lsa - Chipmunk (ingichka)
    // Pitch < 1.0 bo'lsa - Pirate/Grandpa (yo'g'on)
    
    // Eslatma: Haqiqiy xirillash (Pirate) uchun bu yerda Bitcrusher algoritmi qo'shiladi
    for (int i = 0; i < size; i++) {
        // Oddiyroq effekt namunasi
        if (pitch < 1.0f) {
            // Ovozni yo'g'onlashtirish va biroz xirillatish (Pirate simulyatsiyasi)
            audioData[i] = audioData[i] * 0.9; 
        }
    }

    env->ReleaseShortArrayElements(buffer, audioData, 0);
}
