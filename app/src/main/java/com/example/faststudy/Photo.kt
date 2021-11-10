package com.example.faststudy

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Insets
import android.graphics.Insets.add
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.photo.*

class Photo : AppCompatActivity() {

    /* 챕터05 전자액자
     - TODO Android Permission : 접근권한을 뜻하며 CheckSelfPermission -> READ_EXTERNAL_STORAGE(저장소 접근)
        -> Popup을 통해 접근이유 여부 등을 묻고 -> 확인하면 requsetCode와 함께 결과값에 넣어줌

     - TODO ViewAnimation : navigatePhotos(), PhotoFrame-> photoTimer()여기 확인 해보셈

     - TODO Content Provider를 사용 -> SAF(Storage Access Framework)
     */


    private val photoList : List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(findViewById(R.id.photo_img1))
            add(findViewById(R.id.photo_img2))
            add(findViewById(R.id.photo_img3))
            add(findViewById(R.id.photo_img4))
            add(findViewById(R.id.photo_img5))
            add(findViewById(R.id.photo_img6))
            add(findViewById(R.id.photo_img7))
            add(findViewById(R.id.photo_img8))
            add(findViewById(R.id.photo_img9))
        }

    }

    private val imageUriList: MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo)

    initAddPhotoBt() // 코드 추상화라고 함
    initStartPhotoBt()

    }

    private fun initStartPhotoBt() {
        photo_start.setOnClickListener {
            val startPhoto = Intent(this,PhotoFrame::class.java)
            imageUriList.forEachIndexed(){ index, uri ->
                startPhoto.putExtra("photo$index", uri.toString())
            }
            startPhoto.putExtra("photoListSize",imageUriList.size)
            startActivity(startPhoto)
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요행 ㅇㅅㅇ")
            .setMessage("전자액자에 앱에서 사진을 불러오기 위해 권한이 필요행 ㅜㅅㅜ")
            .setPositiveButton("동의하기"){ _, _ ->
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                }
            }
            .setNegativeButton("취소하기"){ _, _ -> }
            .create()
            .show()
    }


    private fun initAddPhotoBt() {
        photo_add.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                // SDK_INT 버전에서 M 버전으로 간거임.
                when{ // 다양한 조건식으로 인해 when 문을 사용함
                    ContextCompat.checkSelfPermission(
                        //Permission을 수행하기 위해 ContextCompat을 실행 해준다.
                        this, // 여기에서
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                        //안드로이드의 명백한 권한은 외부 저장소에서 읽을 수 있다.
                    ) == PackageManager.PERMISSION_GRANTED-> {
                        navigatePhotos()
                        //앱에 이미 권한이 부여되었는지 확인할 때 사용 [허가가_부여된]
                        //TODO 권한이 잘부여 되었을 때 갤러리에서 사진을 선택하는 기능
                    }
                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        showPermissionContextPopup()
                        //TODO 교육욕 팝업 확인 후 권한 팝업을 띄우는 기능
                       }
                    else -> {
                        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                        //Permissions는 복수형이기 때문에 array를 통해 전부 가지고 옮 1000이라는건 나중에 알려줌
                    }
                }
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //grantResults 비워져 있고, 0번째 인덱스가 권한이 부여가 된다
                    //todo 권한이 부여된 것입니다
                    navigatePhotos()
                } else {
                    Toast.makeText(this, "권한을 거부 했어", Toast.LENGTH_SHORT).show()
                }
            }
            else->{
                //구현하지마
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            //resultCode가 액티비이가 아니라면 다시 돌아간다.
         return
        }
        when(requestCode){
            2000 ->{
                val selectedImageUrl: Uri? = data?.data

                if(selectedImageUrl != null){// 이렇게하면 Uri는 절대로 null이 될 수 없다.

                   if(imageUriList.size == 9){
                       Toast.makeText(this, "사진이 꽉 차부렸당깽",Toast.LENGTH_SHORT).show()
                   }
                    imageUriList.add(selectedImageUrl) // 여기서 add를 해주었기 때문에
                    photoList[imageUriList.size - 1].setImageURI(selectedImageUrl) // 요건 0이 될 수 없다.!!!!!!!
                }else{
                    Toast.makeText(this,"사진을 가져오지 못했엉 TㅁT",Toast.LENGTH_SHORT).show()
                }

            }else->{
                Toast.makeText(this,"사진을 가져오지 못했엉 TㅁT",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigatePhotos(){

        //에세이 에프는 다른앱에서도 에세이 에프 기능을 사용 했을때 똑같은 화면이 뜨기 때문에
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type ="image/*"
        startActivityForResult(intent, 2000)
    }

}