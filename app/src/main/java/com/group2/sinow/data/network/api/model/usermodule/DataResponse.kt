package com.group2.sinow.data.network.api.model.usermodule

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.usermodule.DataUserModule

@Keep
data class DataResponse(
    @SerializedName("module")
    val module: ModuleResponse?
)

fun DataResponse.toDataUserModule() = DataUserModule(
    module = this.module?.toModuleData()
)
