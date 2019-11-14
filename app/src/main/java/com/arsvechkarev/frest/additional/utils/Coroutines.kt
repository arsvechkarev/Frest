package com.arsvechkarev.frest.additional.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun inBackground(block: () -> Unit) {
  GlobalScope.launch(Dispatchers.Default) {
    block.invoke()
  }
}