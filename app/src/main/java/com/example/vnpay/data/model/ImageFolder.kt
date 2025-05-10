package com.example.vnpay.data.model

data class ImageFolder(
    var path: String = "",
    var folderName: String = "",
    var firstImage: String? = null,
    val images: MutableList<Image> = mutableListOf()
) {
    fun addImages(image: Image) {
        images.add(image)
    }

    fun getNumberOfImages() = images.size
}
