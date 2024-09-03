package com.fststep.nxt2me.dashboard.view

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.EventInfo
import com.fststep.nxt2me.dashboard.view.adapter.EventListAdapter
import com.fststep.nxt2me.databinding.ActivityEventsInYourAreaBinding

class EventsInYourAreaActivity: AppCompatActivity() {

    private lateinit var mBinding:ActivityEventsInYourAreaBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var eventListAdapter: EventListAdapter
    private lateinit var eventList: MutableList<EventInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_events_in_your_area)

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }

        mBinding.btnCreateYourEvent.setOnClickListener {
            startActivity(Intent(this,CreateEventActivity::class.java))
        }

        linearLayoutManager = LinearLayoutManager(this)
        mBinding.rvEvents.layoutManager = linearLayoutManager
        eventList = ArrayList()
        dummyData()
        eventListAdapter = EventListAdapter(this, eventList as ArrayList<EventInfo>)
        mBinding.rvEvents.adapter = eventListAdapter
        mBinding.rvEvents.addItemDecoration(SpaceItemDecoration(20))
    }

    private fun dummyData() {
        eventList.add(EventInfo(R.drawable.ic_groceries,"Cotton factory Exhibition","50% discount on cotton shirts"))
        eventList.add(EventInfo(R.drawable.ic_groceries,"Cotton factory Exhibition","50% discount on cotton shirts"))
        eventList.add(EventInfo(R.drawable.ic_groceries,"Cotton factory Exhibition","50% discount on cotton shirts"))
        eventList.add(EventInfo(R.drawable.ic_groceries,"Cotton factory Exhibition","50% discount on cotton shirts"))
        eventList.add(EventInfo(R.drawable.ic_groceries,"Cotton factory Exhibition","50% discount on cotton shirts"))
        eventList.add(EventInfo(R.drawable.ic_groceries,"Cotton factory Exhibition","50% discount on cotton shirts"))
    }

    class SpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }

}