package com.fststep.nxt2me.core.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.fststep.nxt2me.core.data.models.Category
import com.fststep.nxt2me.core.data.models.CategoryResponse
import com.fststep.nxt2me.core.data.models.CountryCodeResponse
import com.fststep.nxt2me.core.data.models.MyLocation
import com.fststep.nxt2me.core.data.models.SubCategory
import com.fststep.nxt2me.core.data.models.TermsResponse
import com.fststep.nxt2me.core.data.models.User
import com.fststep.nxt2me.highflyer.registration.HighFlyer
import com.google.gson.Gson

/**
 * Created by Shubham Singh on 26/06/21.
 */
@SuppressLint("StaticFieldLeak")
object Preferences {
    lateinit var context:Context
    lateinit var Preferences:SharedPreferences
    fun init(context: Context){
        this.context=context
        Preferences=PreferenceManager.getDefaultSharedPreferences(context)
    }
    fun storeString(data: String, key: String) {
        Preferences.edit {
            putString(key, data)
        }
    }
    fun fetchString(key: String, defaultValue: String = ""): String {
        return Preferences.getString(key, defaultValue) ?: defaultValue
    }

    fun storeInt(data: Int, key: String) {
        Preferences.edit {
            putInt(key, data)
        }
    }
    fun fetchInt(key: String, defaultValue: Int = 0): Int {
        return Preferences.getInt(key, defaultValue)
    }

    fun storeBoolean(data: Boolean, key: String) {
        Preferences.edit {
            putBoolean(key, data)

        }
    }
    fun fetchBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return Preferences.getBoolean(key, defaultValue)
    }

    fun storeToken(token:String) {
        storeString(token,Constants.KEY_LOGIN_TOKEN)
    }
    fun fetchToken(): String {
        val defaultValue=""
        return Preferences.getString(Constants.KEY_LOGIN_TOKEN,defaultValue) ?: defaultValue
    }

    fun storeUsername(data:String) {
        storeString(data,Constants.KEY_LOGIN_USERNAME)
    }
    fun fetchUsername(): String {
        val defaultValue=""
        return Preferences.getString(Constants.KEY_LOGIN_USERNAME,defaultValue) ?: defaultValue
    }
    fun storePassword(data:String) {
        storeString(data,Constants.KEY_LOGIN_PASSWORD)
    }
    fun fetchPassword(): String {
        val defaultValue=""
        return Preferences.getString(Constants.KEY_LOGIN_PASSWORD,defaultValue) ?: defaultValue
    }

    fun storeUser(data: User) {
        val mUserAccount = Gson().toJson(data)
        storeString(mUserAccount,Constants.KEY_USER_ACCOUNT)
        performSubCatStore(data)
    }

    fun performSubCatStore(data:User){
        if(data.highFlyerDto!=null){
            val cat=fetchCategories()?.data?.categoryList?.find { cat -> cat.id== data.highFlyerDto!!.categoryId }
            val subCat=cat?.subCategoryList?.find { subcat -> subcat.id==data.highFlyerDto!!.subCategoryId }
            storeMySubCat(subCat!!)
            storeMyCat(cat)
        }
    }
    fun fetchUser(): User? {
        val mUserAccount = fetchString( Constants.KEY_USER_ACCOUNT, "")
        return if (mUserAccount.isNotEmpty()) {
            Gson().fromJson(mUserAccount, User::class.java)
        } else {
            null
        }
    }
    fun storeMySubCat(data: SubCategory) {
        val subCat = Gson().toJson(data)
        storeString(subCat, Constants.KEY_SUB_CATEGORY)
    }
    fun fetchMySubCat(): SubCategory? {
        val subCat = fetchString( Constants.KEY_SUB_CATEGORY, "")
        return if (subCat.isNotEmpty()) {
            Gson().fromJson(subCat, SubCategory::class.java)
        } else {
            null
        }
    }
    fun storeMyCat(data: Category?) {
        val cat = Gson().toJson(data)
        storeString(cat, Constants.KEY_CATEGORY)
    }
    fun fetchMyCat(): Category? {
        val cat = fetchString( Constants.KEY_CATEGORY, "")
        return if (cat.isNotEmpty()) {
            Gson().fromJson(cat, Category::class.java)
        } else {
            null
        }
    }
    fun storeLocation(data: MyLocation) {
        val location = Gson().toJson(data)
        storeString(location, Constants.KEY_LOCATION)
    }
    fun fetchLocation(): MyLocation? {
        val location = fetchString( Constants.KEY_LOCATION)
        return if (location.isNotEmpty()) {
            Gson().fromJson(location, MyLocation::class.java)
        } else {
            null
        }
    }

    fun storeHighFlyer(data: HighFlyer) {
        val mHighFlyer = Gson().toJson(data)
        storeString(mHighFlyer, Constants.KEY_HIGH_FLYER)
    }

    fun fetchHighFlyerAccount(isTemp: Boolean = false): HighFlyer? {
        val mHighFlyer = fetchString(Constants.KEY_HIGH_FLYER, "")
        return if (mHighFlyer.isNotEmpty()) {
            Gson().fromJson(mHighFlyer, HighFlyer::class.java)
        } else {
            null
        }
    }

    fun clearAllPreferences() {
        Preferences.edit {
            clear()
        }
    }


    fun clearPreference(key: String) {
        Preferences.edit(commit = true) {
            remove(key)
        }
    }
    fun clearAllAccountData(){
        clearPreferences(arrayListOf(Constants.KEY_HIGH_FLYER,Constants.KEY_LOGIN_TOKEN,Constants.KEY_LOGIN_USERNAME,Constants.KEY_LOGIN_PASSWORD,Constants.KEY_USER_ACCOUNT,Constants.KEY_LOCATION,Constants.KEY_SUB_CATEGORY,Constants.KEY_CATEGORY))
    }

    fun clearPreferences(keyList: ArrayList<String>) {
        keyList.forEach {
            clearPreference(it)
        }
    }

    fun storeTerms(data: TermsResponse) {
        val terms = Gson().toJson(data)
        storeString(terms, Constants.KEY_TERMS)
    }
    fun fetchTerms(): TermsResponse? {
        val terms = fetchString(Constants.KEY_TERMS, "")
        return if (terms.isNotEmpty()) {
            Gson().fromJson(terms, TermsResponse::class.java)
        } else {
            null
        }
    }

    fun storeCountry(data: CountryCodeResponse) {
        val countries = Gson().toJson(data)
        storeString(countries, Constants.KEY_COUNTRY_CODES)
    }
    fun fetchCountry(): CountryCodeResponse? {
        val countryList = fetchString(Constants.KEY_COUNTRY_CODES, "")
        return if (countryList.isNotEmpty()) {
            Gson().fromJson(countryList, CountryCodeResponse::class.java)
        } else {
            null
        }
    }


    fun storeCategories(data: CategoryResponse) {
        val mCategories = Gson().toJson(data)
        storeString(mCategories, Constants.KEY_CATEGORIES)
    }
    fun fetchCategories(): CategoryResponse? {
        val mCategories = fetchString(Constants.KEY_CATEGORIES, "")
        return if (mCategories.isNotEmpty()) {
            Gson().fromJson(mCategories, CategoryResponse::class.java)
        } else {
            null
        }
    }
}