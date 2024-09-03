package com.fststep.nxt2me.dashboard.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.MeetPeopleItemInfo
import com.fststep.nxt2me.dashboard.view.adapter.PeopleInterestListAdapter
import com.fststep.nxt2me.databinding.ActivityMeetPeopleBinding

class MeetPeopleActivity: AppCompatActivity() {

    private lateinit var mBinding:ActivityMeetPeopleBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var peopleInterestListAdapter: PeopleInterestListAdapter
    private lateinit var peopleInterestList: MutableList<MeetPeopleItemInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_meet_people)

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }

        linearLayoutManager = LinearLayoutManager(this)
        mBinding.rvInterests.layoutManager = linearLayoutManager
        peopleInterestList = ArrayList()
        dummyData()
        peopleInterestListAdapter = PeopleInterestListAdapter(this,
            peopleInterestList as ArrayList<MeetPeopleItemInfo>
        )
        mBinding.rvInterests.adapter = peopleInterestListAdapter
        mBinding.rvInterests.addItemDecoration(SpaceItemDecoration(20))
    }

    private fun dummyData() {
        peopleInterestList.add(MeetPeopleItemInfo(R.drawable.ic_groceries,"Looking for cycling group"))
        peopleInterestList.add(MeetPeopleItemInfo(R.drawable.ic_groceries,"Good restuarant near by"))
        peopleInterestList.add(MeetPeopleItemInfo(R.drawable.ic_groceries,"Any Laughing group ?"))
        peopleInterestList.add(MeetPeopleItemInfo(R.drawable.ic_groceries,"Good restuarant near by"))
        peopleInterestList.add(MeetPeopleItemInfo(R.drawable.ic_groceries,"Looking for cycling group"))
        peopleInterestList.add(MeetPeopleItemInfo(R.drawable.ic_groceries,"Good restuarant near by"))
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