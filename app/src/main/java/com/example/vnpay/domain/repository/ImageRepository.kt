package com.example.vnpay.domain.repository

import com.example.vnpay.data.model.Image
import com.example.vnpay.data.model.ImageFolder

interface ImageRepository {
    fun getAllImageFolder(): List<ImageFolder>
}