package com.example.faststudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.faststudy.model.Quotes
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.android.synthetic.main.quote.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

/*Remote Config

앱을 업데이트하지 않아도
하루 활성 사용자 수 제한 없이 무료로 앱의 동작과 모양을 변경할 수 있음

간단하게 firebase 콘솔에 리모트 콘솔값을 저장

1. 최상위 조건의 값을 내려줌
2. 기본값을 내려줌
3. 패치를 했을때 곧바로 반영되는 게아님

첫번째는 서버로 부터 받은값을 액티비티

주의 해야 할부분

사용자가 승인해야 하는 앱 업데이트에는 원격 구성을 사용하지마세요.
무단 업데이트는 앱의 신뢰성을 해칠 수 있음.
remote config에 대해서는 기밀 문서 작업을 하면 안됨
원격 구성을 사용하여 앱의 타겟 플랫폼에서 요구하는 조건을 우회하려고 시도하지마셈

한도가 있음
// 프로젝트에서 최대 2000개의 매개변수 와 최대 500개의 조건을 사용 할 수 있음

매개 변수 키의 길이는 256자이고
밑줄 또는 영문자A~Z a~z로 시작해야하며 숫자도 포함할 수 있음.
한 프로젝트에서 매개변수 값 문자열의 총 길이는 800000자를 초과 할 수 없음

firebaes에서 사용 사례
1. 비율 출시 메커니즘
-> 처음에 모든 사용자에게 노출하는게 아니라 어떤 조건을 가진 사용자에게 몇5 노출 하게 한다. 이런식으러 점진적으로 사용가능

2. 앱의 플랫폼 및 언어별 프로모션 배너
어떤 상황에 따라 내가 원하는 이미지 글을 변경하는 법 <가장 많이 사용>

3. 새 기능 테스트
어떤 그룹에 재한을해서 테스트하는 그

4. 여러가지 속성값을 수정할 수 있음.

Loading 전략이 필요함

1. 로드 시 가져와 활성화 -> 앱을 시작할때 가져와서 UI모양이 크게 변경되지 않는 부분에 적합하다
2. 로딩 화면 뒤에서 활성화 -> 무난한 전략 >
3. 다음 시작 시 새 값 로드함 -> 앱을 시작했을때 바로 패치를 하지만 에티비틱를 시작하지않고 그다음에 실행할때 패치함
-> 급하게 바꾸싶을때 못함

제한은 개발을할때는 setMinimumFetchIntervalInSeconds를 낮게 설정해 바로바로 변경된것을
2. 기본값이 12시간을 준수해주는 좋음

*/

class Quote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quote)

        initData()
        initViews()

    }


    private fun initViews(){
        //position은 현재위치를 뜻함 처음에 보이는 뷰는 0임 우측은 1 좌측은 -1이 됨
        // 1. 가로 세로 어떻게 이동하느냐?
        // 2. 스케일 얼마나 커지느냐?
        // 3. 알파 값 투명도
        viewPager2.setPageTransformer { page, position ->
          // 3. 알파값을 써서 입체감 있게 보여주려고 함
            when{
                //alpha 1이 다보이는 것 0이 하나도 보이지 않는 것
                    //absoluteValue = 뭐지?
                position.absoluteValue >= 1F ->{
                    page.alpha = 0F
                }
                position == 0F-> {
                    page.alpha =1F
                }
                else ->{
                    page.alpha = 1F - 2 * position.absoluteValue
                }

            }
        }
    }

    private fun initData(){
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            quote_pr.visibility = View.GONE
            if(it.isSuccessful){
                val quotes = paresQuotesJson(remoteConfig.getString(("quotes")))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
                displayQuotesPager(quotes, isNameRevealed)

            }
        }
    }




    private fun paresQuotesJson(json: String): List<Quotes>{
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for(index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let{
                jsonList = jsonList + it
            }
        }

        return jsonList.map{
            Quotes(it.getString("quote"),
                it.getString("name"))
        }
    }
    // 좌측으로 이동 할수 있게
    private fun displayQuotesPager(quotes: List<Quotes>, isNameRevealed: Boolean) {
        val adapter = QuotesPagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        )
        viewPager2.adapter = adapter
        viewPager2.setCurrentItem(adapter.itemCount / 2 -1, false)
    }
}