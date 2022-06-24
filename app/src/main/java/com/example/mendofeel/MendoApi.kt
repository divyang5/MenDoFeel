package com.example.mendofeel

import retrofit2.Call
import retrofit2.http.GET

interface MendoApi {

    @get:GET("secure.notion-static.com/e8583282-c7a5-4de2-9b7a-269d705b015a/posts.json?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220624%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220624T131819Z&X-Amz-Expires=86400&X-Amz-Signature=d5a9076b05d3c2e735e868be16043e4cc404e8fc0c4c14b3ae2758165ab185dc&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22posts.json%22&x-id=GetObject" )
    val getData: Call<MenDo?>
    companion object {
        var BASE_URL = "https://s3.us-west-2.amazonaws.com/"
    }
}
