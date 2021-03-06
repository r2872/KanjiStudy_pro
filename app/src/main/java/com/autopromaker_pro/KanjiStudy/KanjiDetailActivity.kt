package com.autopromaker_pro.KanjiStudy

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.autopromaker_pro.KanjiStudy.databinding.ActivityKanjiDetailBinding
import com.autopromaker_pro.adapters.KanjiDetailAdapter
import com.autopromaker_pro.datas.KanjiClassData

class KanjiDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityKanjiDetailBinding
    private lateinit var mAdapter: KanjiDetailAdapter
    private var position = 0
    private lateinit var mPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKanjiDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setValues()
        setupEvents()
        supportActionBar?.let {
            setCustomActionBar()
            kanjiClassText.text = titleText
            backButton.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun setupEvents() {

        binding.settingBtn.setOnClickListener {
            viewSet()
        }
        binding.canvasBtn.setOnClickListener {
            createAlertCanvas()
        }

    }

    override fun setValues() {

        mPref = mContext.getSharedPreferences("viewSetting", Context.MODE_PRIVATE)

        val getKanjiClassData = intent.getSerializableExtra("kanjiListData") as KanjiClassData
        titleText = getKanjiClassData.className
        position = intent.getIntExtra("clickedPosition", 0)
        mAdapter =
            KanjiDetailAdapter(mContext, getKanjiClassData.detail, getKanjiClassData.className)
        binding.detailRecyclerView.apply {
            adapter = mAdapter
            layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            scrollToPosition(position)
        }
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.detailRecyclerView)
    }

    private fun viewSet() {

        val viewAllInSet = mPref.getBoolean("viewAll", true)

        val customView =
            LayoutInflater.from(mContext).inflate(R.layout.my_custom_alert_setting, null)
        val radioGroup = customView.findViewById<RadioGroup>(R.id.radioGroup)
        val viewAllRadio = customView.findViewById<RadioButton>(R.id.viewAll_radio)
        val viewKanjiOnlyRadio =
            customView.findViewById<RadioButton>(R.id.viewKanjiOnly_radio)
        if (viewAllInSet) {
            viewAllRadio.isChecked = true
        } else {
            viewKanjiOnlyRadio.isChecked = true
        }
        var viewAll = viewAllInSet
        radioGroup.setOnCheckedChangeListener { _, _ ->
            if (viewAllRadio.isChecked) {
                viewAll = true
            } else if (viewKanjiOnlyRadio.isChecked) {
                viewAll = false
            }
        }
        val myAlert = AlertDialog.Builder(mContext)
            .setView(customView)
            .setTitle("?????? ??????")
            .setPositiveButton("??????") { _, _ ->
                mPref.edit().putBoolean("viewAll", viewAll).apply()
                mAdapter.viewAll = viewAll
                mAdapter.notifyDataSetChanged()
            }
            .show()
    }

    private fun createAlertCanvas() {
        val myAlert = CanvasDialog()
        myAlert.show(supportFragmentManager, null)

    }

    fun upDateView() {
        mAdapter.notifyDataSetChanged()
    }

}