package com.cp.brittany.dixon.model.subscription

data class GetPackagesResponse(
    val data: List<PackageData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class PackageData(
    val duration: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val isSelected: Boolean = false,
    val id: Int? = null,
    val inapp_package_id: String? = null,
    val inapp_android_package: String? = null,
    val description: String? = null,
    val is_active: Int? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: Int? = null,
)