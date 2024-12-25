//package com.fststep.nxt2me.dashboard.view
//
//import android.content.Intent
//import android.content.res.Resources
//import android.os.Bundle
//import android.util.TypedValue
//import android.view.Gravity
//import android.view.ViewGroup
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.AppCompatTextView
//import androidx.core.content.ContextCompat
//import androidx.core.content.res.ResourcesCompat
//import androidx.core.view.contains
//import androidx.core.view.marginStart
//import androidx.databinding.DataBindingUtil
//import com.fststep.nxt2me.R
//import com.fststep.nxt2me.dashboard.model.Booking
//import com.fststep.nxt2me.dashboard.model.BookingItemListInfo
//import com.fststep.nxt2me.databinding.ActivityBookingSlotBinding
//import java.io.Serializable
//import java.text.DateFormat
//import java.text.DateFormatSymbols
//import java.text.ParseException
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.ArrayList
//
//class BookingSlotActivity: AppCompatActivity() {
//
//    private lateinit var mBinding: ActivityBookingSlotBinding
//    private lateinit var itemList: ArrayList<BookingItemListInfo>
//    private var currentWeekNumber = 0 // current week
//    private var weekDatesList = arrayListOf<String>()
//    private var gridSlots: GridLayout? = null
//    private lateinit var timeSlotList: Array<String>
//    private lateinit var dayList: Array<String>
//    private var bookingList = arrayListOf<Booking>()
//
//    private val displayDate: DateFormat = SimpleDateFormat("d MMMM, yyyy", Locale.ENGLISH)
//    private val serverDate: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
//    private val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_booking_slot)
//
//        mBinding.ivBackArrow.setOnClickListener {
//            onBackPressed()
//        }
//
//        itemList = ArrayList()
//        itemList = intent.getSerializableExtra("itemlist") as ArrayList<BookingItemListInfo>
//
//        dayList = resources.getStringArray(R.array.week_day_entries)
//        timeSlotList = resources.getStringArray(R.array.time_slot_entries)
//
//        mBinding.btNext.setOnClickListener {
//            currentWeekNumber += 7 // increment day by 7 to go to next week
//            updateWeek()
//        }
//
//        mBinding.btPrev.setOnClickListener {
//            currentWeekNumber -= 7 // decrement day by 7 to go to prev week
//            updateWeek()
//        }
//
//        mBinding.buttonBookYourSlot.setOnClickListener {
//            val intent = Intent(this,BookingPaymentActivity::class.java).apply {
//                putExtra("itemlist", itemList as Serializable)
//            }
//            startActivity(intent)
//        }
//
//        updateWeek()
//    }
//
//    private fun String.formatDisplayDate(): String {
//        val date = this.dropLast(6)
//        val dateSplit = date.split(" ")
//
//        return if (dateSplit[0].toInt() in 11..13) {
//            dateSplit[0] + "th " + dateSplit[1]
//        } else when (dateSplit[0].toInt() % 10) {
//            1 -> dateSplit[0] + "st " + dateSplit[1]
//            2 -> dateSplit[0] + "nd " + dateSplit[1]
//            3 -> dateSplit[0] + "rd " + dateSplit[1]
//            else -> dateSplit[0] + "th " + dateSplit[1]
//        }
//    }
//
//    private fun String.formatDisplayDateFromServer(): String {
//        val date = this.dropLast(5)
//        val dateSplit = date.split("/")
//
//        val monthString = DateFormatSymbols().months[dateSplit[1].toInt() - 1]
//
//        return if (dateSplit[0].toInt() in 11..13) {
//            dateSplit[0] + "th " + monthString
//        } else when (dateSplit[0].toInt() % 10) {
//            1 -> dateSplit[0] + "st " + monthString
//            2 -> dateSplit[0] + "nd " + monthString
//            3 -> dateSplit[0] + "rd " + monthString
//            else -> dateSplit[0] + "th " + monthString
//        }
//    }
//
//    private fun updateWeek() {
//        val calenderThisWeek = Calendar.getInstance()
//        if (currentWeekNumber != 0) {
//            calenderThisWeek.add(Calendar.DAY_OF_WEEK, if (currentWeekNumber> 0) +currentWeekNumber else currentWeekNumber)
//        }
//        calenderThisWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
//        val startWeek: String = displayDate.format(calenderThisWeek.time)
//
//        calenderThisWeek.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
//        val endWeek: String = displayDate.format(calenderThisWeek.time)
//
//        weekDatesList = getDates(startWeek, endWeek)
//
//        mBinding.tvDate.text = "${startWeek.formatDisplayDate()} to ${endWeek.formatDisplayDate()}"
//
//        initWeekView()
//    }
//
//    private fun getDates(dateString1: String, dateString2: String): ArrayList<String> {
//        val dates = ArrayList<String>()
//        var date1: Date? = null
//        var date2: Date? = null
//        try {
//            date1 = displayDate.parse(dateString1)
//            date2 = displayDate.parse(dateString2)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        val cal1 = Calendar.getInstance()
//        cal1.time = date1
//        val cal2 = Calendar.getInstance()
//        cal2.time = date2
//        while (!cal1.after(cal2)) {
//            dates.add(serverDate.format(cal1.time))
//            cal1.add(Calendar.DATE, 1)
//        }
//        return dates
//    }
//
//    private fun initWeekView() {
//        clearLayout()
//        val markForMCTypeface = ResourcesCompat.getFont(this, R.font.markformc)
//        gridSlots = GridLayout(this)
//        gridSlots?.layoutParams = ViewGroup.MarginLayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//
//        gridSlots?.columnCount = 8 // 7 days + 1 for header
//        gridSlots?.rowCount = 11 // 10 time slots + 1 for header
//        (gridSlots?.layoutParams as ViewGroup.MarginLayoutParams).setMargins(16.px, 16.px, 16.px, 16.px)
//        gridSlots?.marginStart
//        gridSlots?.useDefaultMargins = true
//
//        for (rowCount in 0 until gridSlots!!.rowCount) {
//            for (colCount in 0 until gridSlots!!.columnCount) {
//
//                // Add Space at 0,0
//                if (rowCount == 0 && colCount == 0) {
//                    val spacer = Space(this)
//                    spacer.tag = getBookingBySlotPos(rowCount, colCount)
//                    spacer.layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                    gridSlots?.addView(spacer, getGridSpecParams(rowCount, colCount))
//                } else if (rowCount == 0) { // Add day headers
//                    val dateHeader = AppCompatTextView(this)
//                    dateHeader.tag = getIndexByRowCol(rowCount, colCount)
//                    dateHeader.layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                    dateHeader.text = dayList[colCount - 1]
//                    dateHeader.gravity = Gravity.CENTER
//                    dateHeader.typeface = markForMCTypeface
//                    dateHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
//                    dateHeader.setTextColor(ContextCompat.getColor(this, R.color.header_text_color))
//                    gridSlots?.addView(dateHeader, getGridSpecParams(rowCount, colCount))
//                } else if (colCount == 0) { // Add time slot headers
//                    val timeSlotHeader = AppCompatTextView(this)
//                    timeSlotHeader.tag = getIndexByRowCol(rowCount, colCount)
//                    timeSlotHeader.layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                    timeSlotHeader.text = timeSlotList[rowCount - 1]
//                    timeSlotHeader.gravity = Gravity.CENTER
//                    timeSlotHeader.typeface = markForMCTypeface
//                    timeSlotHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
//                    timeSlotHeader.setTextColor(ContextCompat.getColor(this, R.color.header_text_color))
//                    gridSlots?.addView(timeSlotHeader, getGridSpecParams(rowCount, colCount))
//                } else { // Add checkboxes
//                    val slotCheckBox = CheckBox(this)
//                    slotCheckBox.layoutParams = ViewGroup.LayoutParams(21.px, 21.px)
//                    slotCheckBox.tag = getIndexByRowCol(rowCount, colCount)
//                    slotCheckBox.isClickable = true
//                    slotCheckBox.isFocusable = true
//                    slotCheckBox.isChecked = isSlotBooked(rowCount - 1, colCount - 1)
//                    slotCheckBox.background = ContextCompat.getDrawable(this, R.drawable.cb_slot)
//                    slotCheckBox.buttonDrawable = null
//                    slotCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
//                        if(isPastSlot(rowCount - 1, colCount - 1)) {
//                            buttonView.isChecked = !isChecked
//                            Toast.makeText(this,"Cannot book a slot on an older date",Toast.LENGTH_SHORT).show()
//                        } else if (isSlotBooked(rowCount - 1, colCount - 1)) {
//                            Toast.makeText(this,"Slot already booked",Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    gridSlots?.addView(
//                        slotCheckBox,
//                        getGridSpecParams(rowCount, colCount).apply {
//                            width = 21.px
//                            height = 21.px
//                            setGravity(Gravity.CENTER)
//                        }
//                    )
//                }
//            }
//        }
//
//        mBinding.gridHolder.addView(gridSlots)
//    }
//
//    private fun isPastSlot(row: Int, col: Int): Boolean {
//        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.ENGLISH)
//        val now = Date()
//
//        val date = weekDatesList.getOrNull(col)
//        val time = timeSlotList.getOrNull(row)
//
//        if (date != null && time != null) {
//            val suffix = if (time.split(":")[0].toInt() in 10..11) "AM" else "PM"
//            val dateString = "$date $time $suffix"
//            val slot = dateFormat.parse(dateString)
//
//            return slot.time < now.time
//        } else {
//            return false
//        }
//    }
//
//    private fun isSlotBooked(row: Int, col: Int): Boolean {
//        val date = weekDatesList.getOrNull(col)
//        val time = timeSlotList.getOrNull(row)
//
//        return if (date != null && time != null) {
//            val booking =
//                bookingList.filter { booking -> (date == booking.date) && (time == booking.getTimeVal()) }
//            booking.isNotEmpty()
//        } else {
//            false
//        }
//    }
//
//    private fun getBookingBySlotPos(row: Int, col: Int): Booking? {
//        val date = weekDatesList.getOrNull(col)
//        val time = timeSlotList.getOrNull(row)
//
//        return if (date != null && time != null) {
//            val booking = bookingList.filter { booking -> (date == booking.date) && (time == booking.getTimeVal()) }
//            if (booking.isNotEmpty()) booking[0] else null
//        } else {
//            null
//        }
//    }
//
//    private fun clearLayout() {
//        gridSlots?.let {
//            if (mBinding.gridHolder.contains(it)) {
//                mBinding.gridHolder.removeView(it)
//            }
//        }
//    }
//
//    private fun getGridSpecParams(row: Int, col: Int): GridLayout.LayoutParams {
//        return GridLayout.LayoutParams(GridLayout.spec(row, 1f), GridLayout.spec(col, 1f))
//    }
//
//    private fun getIndexByRowCol(row: Int, col: Int): Int {
//        var index = -1
//        for (rowCount in 0 until 11) {
//            for (colCount in 0 until 8) {
//                index += 1
//                if (rowCount == row && colCount == col) {
//                    return index
//                }
//            }
//        }
//        return index
//    }
//
//}