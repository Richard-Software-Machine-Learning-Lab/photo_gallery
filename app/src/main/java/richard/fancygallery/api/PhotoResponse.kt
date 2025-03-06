package richard.fancygallery

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import richard.fancygallery.GalleryItem

@JsonClass(generateAdapter = true)
data class PhotoResponse(
    @Json(name = "photo") val galleryItems: List<GalleryItem>
)