package com.example.faststudy

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.recorder.*
import java.util.*

class Recorder : AppCompatActivity() {

    // 클래스에 맞게 layout을 초기화
    private val recordButton: RecordButton by lazy { findViewById(R.id.record_bt) }
    private val visualView: RecordVisualizerView by lazy { findViewById(R.id.visual_view) }
    private val recordTimer: RecordTimer by lazy { findViewById(R.id.record_timer) }

    // 권한 승인을할때 오디오와 권한 저장소를 가져와서 승인을 받아야하기 때문에 미리 선언 해둠
    private val requiresPermissions = arrayOf(
        android.Manifest.permission.RECORD_AUDIO, // 권한 오디오 확인
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )// 권한 저장소 확인

    // 레코딩파일 경로를 : 문자열로 정의
    private val recordingFilepath: String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }


    private var recorder: MediaRecorder? = null

    //레코더 사용한다고 선언하지만 바로 사용하지 않기에 Null 값으로 둠
    private var player: MediaPlayer? = null
    private var state = State.BEFORE_RECORDING
        set(value) { //state의 레코딩 하기전의 값을 가져옴
            field = value //필드는 벨류 값을 뜻함
            record_reset.isEnabled = (value == State.AFTER_RECORDING) || (value == State.ON_PLAYING)
            //리셋 버튼을 비활성화 시킴 = 값이 레코딩 하고나서 녹음 실행중에는
            recordButton.iconWithState(value)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recorder)

        bindViews()
        initViews()
        requestAudioPermission()
    }

    private fun bindViews() {
        visualView.onRequestCurrentAmplitude = {
            recorder?.maxAmplitude ?: 0 // RepeatAtion이 반복해서 동작하는 것을
            /*todo Recorder에서  onRequestCurrentAmplitude()함수를 정의를 해줬고
            *  이함수가 호출 될때 현재 가지고 있는 Recorderd에 MaxAmplitude값을 반환을 하게 됨
            *  RecorderVisualizerView에서는 정의된 onRequestCurrentAmplitude 함수를 호출을 해서
            *  Recorder에서 전달하는 오디오레코더에 MaxAmplitude값을 가져오고
            *  만약에  onRequestCurrentAmplitude가 null 값이 오게되면 0을 반환하게 됨  */

        }
        record_reset.setOnClickListener {
            stopPlay()
            state = State.BEFORE_RECORDING
            recordTimer.clearCountTime()
            visualView.clearVisualization()
        }
        recordButton.setOnClickListener {
            when (state) {
                State.BEFORE_RECORDING -> {
                    startRecording()
                }
                State.ON_RECORDING -> {
                    stopRecording()
                }
                State.AFTER_RECORDING -> {
                    startPlay()
                }
                State.ON_PLAYING -> {
                    stopPlay()
                }
            }
        }
    }

    //승인결과를 받는 값이며 파라미터 값을 잘보자..
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 기록승인값 = 요구값 == 그 값은 201 일때 승인값은 만족한다<안할경우 null 반환> == 권환을 승인
        //todo firstOrNull()을 활용하면 해당조건에 만족하는 데이터가 없는경우 null 값을 반환한다.
        val recordGranted = requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        // 기록승인 값이 아닐때는 앱을 끊다는 표현이 맞는듯
        if (!recordGranted) {
            finish()
        }

    }

    // 오디오 권한을 요구하는 값
    private fun requestAudioPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //권한요구 함수에
            requestPermissions(requiresPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
        }
    }

    //
    private fun initViews() {
        recordButton.iconWithState(state)
    }

    // 허가 받는데 리퀘스트 코드가 어떤 값인지 201 설정
    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilepath)
            prepare() // 녹음하는 상태가되는 거임
        }
        recordTimer.startCountUp()
        recorder?.start()
        visualView.startVisualizing(false)
        state = State.ON_RECORDING
    }

    private fun stopRecording() {
        recorder?.run {
            stop()
            release() //공개하다 밝히다. 등의 뜻이 있음
        }
        recorder = null
        recordTimer.stopCountUp()
        state = State.AFTER_RECORDING
    }

    private fun startPlay() {
        //리디어 레코드랑 동일하게 스테이트가 존재함
        // 인스턴트가 생성되면
        // Idle(setDataSource) -> Initialized (prepared) -> prepared (start) -> started
        // 그후 Paused 와 Stop을 할 수 있음..
        player = MediaPlayer().apply {
            setDataSource(recordingFilepath)
            prepare() }
        //재생이 끝나면 다시 돌아오는 걸 말함
        player?.setOnCompletionListener {
            stopPlay()
            state =State.AFTER_RECORDING }
        recordTimer.startCountUp()
        player?.start()
        state = State.ON_PLAYING
        visualView.startVisualizing(true)
    }

    private fun stopPlay() {
        player?.release()
        player = null
        state = State.AFTER_RECORDING
        recordTimer.stopCountUp()
        visualView.stopVisualizing()
    }

    //각가의 메소드 안에서 스테이트를 업데이트 해주는 코드를 처리해 준다.

}