package com.arsvechkarev.frest.other

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.frest.BuildConfig
import com.arsvechkarev.frest.R
import kotlinx.android.synthetic.main.activity_about.layoutThirdPartyIcons
import kotlinx.android.synthetic.main.activity_about.textVersion

class AboutActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_about)
    layoutThirdPartyIcons.setOnClickListener { openThirdPartyIconsDialog() }
    textVersion.text = getString(R.string.text_version, BuildConfig.VERSION_NAME)
  }
  
  private fun openThirdPartyIconsDialog() {
    ThirdPartyIconsDialog().show(supportFragmentManager, THIRD_PARTY_ICONS_DIALOG_ID)
  }
  
  companion object {
    const val THIRD_PARTY_ICONS_DIALOG_ID = "third_party_dialog_id"
  }
}
