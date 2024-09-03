package com.fststep.nxt2me.core.data.models

data class CategoryResponse (
    val timestamp: String? = null,
    val data: CategoryData? = null,
    val errors: Any? = null
)

data class CategoryData (
    val categoryTypeMappingList: MutableList<CategoryTypeMappingList>? = null,
    val categoryList: MutableList<Category>? = null,
    val highFlyerCategoryList: MutableList<Category>? = mutableListOf(),
)

data class Category (
    val id: Long? = null,
    val categoryName: String? = null,
    val subCategoryList: MutableList<SubCategory>? = null
) {
    override fun toString(): String {
        return categoryName ?: ""
    }
}

data class SubCategory (
    var parentId: Long? = null,
    val id: Long? = null,
    val subCategoryName: String? = null,
    val imageName: String? = null,
    val categoryType: CategoryTypeClass? = null
) {
    override fun toString(): String {
        return subCategoryName ?: ""
    }
}

data class CategoryTypeClass (
    val id: Long? = null,
    val categoryType: String? = null,
    val categoryCode: CategoryCode? = null
) {
    fun getCategoryTypeEnum(): CategoryTypeEnum? {
        return CategoryTypeEnum.fromType(categoryType ?: "")
    }
}

enum class CategoryCode {
    B,
    D,
    G,
    P
}

enum class CategoryTypeEnum(val type: String) {
    Booking("Booking"),
    Delivery("Delivery"),
    Good("Good"),
    PhoneNumber("Phone Number"),
    Property("Property"); // not yet implemented property like this in server. property is in Goods in server and we have to check it by category name

    companion object {
        fun fromType(type: String): CategoryTypeEnum? {
            return values().find { it.type.equals(type, ignoreCase = true) }
        }
    }
}

data class CategoryTypeMappingList (
    val id: Long? = null,
    val catTypeOptionName: CatTypeOptionName? = null,
    val categoryType: CategoryTypeClass? = null
)

enum class CatTypeOptionName {
    Both,
    Home,
    Shop
}
