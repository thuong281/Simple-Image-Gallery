package com.example.vnpay.domain.usecase

import com.example.vnpay.data.model.ImageFolder
import com.example.vnpay.domain.repository.ImageRepository
import javax.inject.Inject

class GetImageFolderUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    operator fun invoke(): List<ImageFolder> {
        return imageRepository.getAllImageFolder()
    }
}