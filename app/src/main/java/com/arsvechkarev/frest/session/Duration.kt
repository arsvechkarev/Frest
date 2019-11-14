package com.arsvechkarev.frest.session

/**
 * Class for storing duration and also send current session time through EventBus
 * to [com.arsvechkarev.frest.session.TimerService] and
 * [com.arsvechkarev.frest.home.managers.HomeFragmentPresenter]
 *
 * @author Arseniy Svechkarev
 */
class Duration(var seconds: Long)