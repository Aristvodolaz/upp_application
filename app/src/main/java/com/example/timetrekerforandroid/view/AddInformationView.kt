package com.example.timetrekerforandroid.view

interface AddInformationView {
    fun msgSuccess(msg: String)
    fun msgError(msg: String)
    fun msgSuccessDuplicate(msg: String)
    fun errorMessage(msg: String)
    fun success()
    fun createNewShk(shk: String);
}