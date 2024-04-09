package com.arsfutura.imdb.presentation.common

class BaseMvp {
    interface View {
        fun showError(throwable: Throwable)
    }

    interface Presenter {
        fun initialize()
        fun subscribe()
        fun destroy()
    }
}