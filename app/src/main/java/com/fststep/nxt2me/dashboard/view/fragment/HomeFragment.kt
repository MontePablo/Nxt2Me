package com.fststep.nxt2me.dashboard.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.MyLocation
import com.fststep.nxt2me.core.view.CommonDialogs
import com.fststep.nxt2me.dashboard.helper.AutoSwitcherViewPager
import com.fststep.nxt2me.dashboard.view.SellerListActivity
import com.fststep.nxt2me.dashboard.view.adapter.BigAdvertisementViewPagerAdapter
import com.fststep.nxt2me.dashboard.view.adapter.CategoryAdapter
import com.fststep.nxt2me.dashboard.view.adapter.SmallAdvertisementViewPagerAdapter
import com.fststep.nxt2me.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), SubcategoryListener {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mBinding: FragmentHomeBinding
    private var vpSmallAdvertisement: AutoSwitcherViewPager? = null
    private lateinit var smallAdvertisementViewPagerAdapter: SmallAdvertisementViewPagerAdapter
    private var vpBigAdvertisement: AutoSwitcherViewPager? = null
    private lateinit var bigAdvertisementViewPagerAdapter: BigAdvertisementViewPagerAdapter

    @RequiresApi(Build.VERSION_CODES.S)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("TAGG", "location Granted")
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show()
                currentLocation()
            } else {
                Log.d("TAGG", "location Denied")
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Permission was not granted and the user did not check "Don't ask again"
                    requestLocationPermission()
                } else {
                    // Permission was denied with "Don't ask again"
                    showLocationPermissionDeniedDialog()
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(layoutInflater)
        val home: View = mBinding.root
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        vpSmallAdvertisement = mBinding.vpSmallAdvertisement
        vpBigAdvertisement = mBinding.vpBigAdvertisement

        val smallAdvertisementList = intArrayOf(
            R.drawable.small_advertisement_1,
            R.drawable.small_advertisement_2,
            R.drawable.small_advertisement_3,
            R.drawable.small_advertisement_4,
            R.drawable.small_advertisement_5,
            R.drawable.small_advertisement_6,
            R.drawable.small_advertisement_7,
            R.drawable.small_advertisement_8,
            R.drawable.small_advertisement_9,
            R.drawable.small_advertisement_10,
            R.drawable.small_advertisement_11
        )

        val bigAdvertisementList = intArrayOf(
            R.drawable.big_advertisement_1,
            R.drawable.big_advertisement_2,
            R.drawable.big_advertisement_3,
            R.drawable.big_advertisement_4,
            R.drawable.big_advertisement_5,
            R.drawable.big_advertisement_6,
            R.drawable.big_advertisement_7
        )

        smallAdvertisementViewPagerAdapter = SmallAdvertisementViewPagerAdapter(requireContext(), smallAdvertisementList)
        vpSmallAdvertisement!!.adapter = smallAdvertisementViewPagerAdapter
        bigAdvertisementViewPagerAdapter = BigAdvertisementViewPagerAdapter(requireContext(), bigAdvertisementList)
        vpBigAdvertisement!!.adapter = bigAdvertisementViewPagerAdapter

        val catList = Preferences.fetchCategories()?.data?.categoryList!!.apply {
            removeIf { it.categoryName!!.startsWith("Delivery") }
            removeIf { it.categoryName!!.startsWith("High") } // High Flyer
        }
        for (i in catList.indices)
            mBinding.tabLayoutCategory.addTab(mBinding.tabLayoutCategory.newTab())

        val highflyerCatList = Preferences.fetchCategories()?.data?.highFlyerCategoryList!!

        mBinding.apply {
            recyclerCategory.let {
                it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                it.adapter = CategoryAdapter(this@HomeFragment, catList)
                it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        tabLayoutCategory.getTabAt((it.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition())?.select()
                    }
                })
            }
            recyclerHighflyer.let {
                it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                it.adapter = CategoryAdapter(this@HomeFragment, highflyerCatList)
            }
            PagerSnapHelper().attachToRecyclerView(recyclerCategory)
            PagerSnapHelper().attachToRecyclerView(recyclerHighflyer)
        }

        currentLocation()

        return home
    }

    override fun onSubCategoryClick(categoryId: Long, subCategoryId: Long, subCatName: String) {
        startActivity(Intent(requireContext(), SellerListActivity::class.java)
            .putExtra(Constants.KEY_CATEGORY_ID, categoryId.toString())
            .putExtra(Constants.KEY_SUB_CATEGORY_ID, subCategoryId.toString())
            .putExtra(Constants.KEY_SUB_CATEGORY, subCatName)
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun currentLocation() {
        Log.d("TAG", "currentlocation running")
        if (checkLocationPermission()) {
            Log.d("TAG", "checkLocationpermission true")

            if (isLocationEnabled()) {
                Log.d("TAG", "islocation enabled true")
                getLocationFromFusionProvider()
            } else {
                Log.d("TAG", "islocation enabled false")
                Toast.makeText(requireContext(), "Turn on Location first", Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            Log.d("TAG", "checkLocationpermission false")
            requestLocationPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    private fun getLocationFromFusionProvider() {
        Log.d("TAG", "getlocationfromFusionprovider running")

        val locationRequest = LocationRequest.create().apply {
            interval = 60000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                Log.d("TAG", "locationCallback running")
                Log.d("TAG", "p0: ${p0.lastLocation}")

                super.onLocationResult(p0)
                if (p0.lastLocation == null) {
                    Log.d("TAG", "requestLocation is null")
                } else {
                    Log.d("TAG", "location not null")
                    saveLocation(p0.lastLocation)
                }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkLocationPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestLocationPermission() {
        Log.d("TAG", "requestLocationPermission running")
        requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun showLocationPermissionDeniedDialog() {
        CommonDialogs().showDialogWithTwoButtons(
            requireContext(),
            "WARNING!",
            "Location permission is required for this app to function correctly. Please grant the permission in the app settings.",
            "Go to Settings",
            "Cancel",
            { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireContext().packageName, null)
                }
                startActivity(intent)
            },
            { dialog, _ ->
                dialog.dismiss()
            }

        )
    }

    private fun saveLocation(location: Location) {
        Preferences.storeLocation(MyLocation(location.longitude, location.latitude))
        Log.d("TAG", "location stored: $location")
    }
}


interface SubcategoryListener{
    fun onSubCategoryClick(categoryId:Long,subCategoryId:Long,subCatName:String)
}
