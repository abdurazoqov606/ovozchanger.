package com.ovoz.changer

import android.media.*

class AudioEngine {
    private var isRunning = false

    external fun processAudio(buffer: ShortArray, size: Int, pitch: Float, rate: Float)

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    fun startLiveEffect(pitchValue: Float) {
        isRunning = true
        val sampleRate = 44100
        val bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        
        val recorder = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize)
        val player = AudioTrack.Builder()
            .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build())
            .setAudioFormat(AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(sampleRate).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
            .setBufferSizeInBytes(bufferSize)
            .build()

        val buffer = ShortArray(bufferSize)
        recorder.startRecording()
        player.play()

        Thread {
            while (isRunning) {
                val read = recorder.read(buffer, 0, buffer.size)
                // C++ DA QAYTA ISHLASH (PIRATE/GRANDPA)
                processAudio(buffer, read, pitchValue, 1.0f)
                player.write(buffer, 0, read)
            }
            recorder.stop()
            player.stop()
        }.start()
    }

    fun stop() { isRunning = false }
}
