package com.terminatwin.rssfeed.model

interface ActionListener {
    fun onSuccess(data: Any)
    fun onFail(data: String)
}