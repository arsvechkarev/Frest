package com.arsvechkarev.frest.other

import android.app.Dialog
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.arsvechkarev.frest.R

open class ThirdPartyIconsDialog : androidx.fragment.app.DialogFragment() {
  
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    activity?.let {
      val message = SpannableString(getString(R.string.link_third_party_icons))
      Linkify.addLinks(message, Linkify.ALL)
      
      return AlertDialog.Builder(it)
          .setTitle(getString(R.string.text_third_party_icons))
          .setMessage(HtmlCompat.fromHtml(getString(R.string.link_third_party_icons),
            HtmlCompat.FROM_HTML_MODE_LEGACY))
          .create()
    } ?: throw IllegalStateException("Activity cannot be null")
  }
  
  override fun onStart() {
    super.onStart()
    val textMsg = dialog.findViewById<TextView>(android.R.id.message)
    textMsg.movementMethod = LinkMovementMethod.getInstance()
  }
}