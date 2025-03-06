package richard.fancygallery

import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = ""

interface FlickrApi {
    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=$API_KEY" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s,geo"
    )
    suspend fun fetchPhotos(@Query("per_page") perPage: Int): FlickrResponse
}