package com.group2.sinow.utils.exceptions

import com.google.gson.Gson
import com.group2.sinow.data.network.api.model.common.BaseResponse
import retrofit2.Response

class ApiException(
    override val message: String?,
    val httpCode : Int,
    val errorResponse: Response<*>?
) : Exception() {

    fun getParsedError() : BaseResponse? {
        val body = errorResponse?.errorBody()?.string().orEmpty()
        return try {
            val bodyObj = Gson().fromJson(body,BaseResponse::class.java)
            bodyObj
        }catch (e : Exception){
            e.printStackTrace()
            null
        }
    }
}