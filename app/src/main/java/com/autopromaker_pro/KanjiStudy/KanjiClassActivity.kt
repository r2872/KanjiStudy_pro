package com.autopromaker_pro.KanjiStudy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.autopromaker_pro.KanjiStudy.databinding.ActivityKanjiClassBinding
import com.autopromaker_pro.adapters.KanjiListAdapter
import com.autopromaker_pro.datas.KanjiClassData
import com.autopromaker_pro.datas.KanjiData
import com.autopromaker_pro.viewmodels.MainViewModel

class KanjiClassActivity : BaseActivity() {

    private lateinit var binding: ActivityKanjiClassBinding
    private lateinit var mAdapter: KanjiListAdapter
    private val classNUm by lazy {
        intent.getStringExtra("classNum") as String
    }
    private val kanjiClassData by lazy {
        intent.getSerializableExtra("kanjiClassName") as List<KanjiData>
    }
    private lateinit var mPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKanjiClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setValues()
        setupEvents()
        supportActionBar?.let {
            setCustomActionBar()
            kanjiClassText.text = titleText
            randomTest.visibility = View.VISIBLE
            backButton.setOnClickListener {
                onBackPressed()
            }
            randomTest.setOnClickListener {
                randomTest()
            }
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun setupEvents() {

    }

    override fun onResume() {
        super.onResume()
        mAdapter.notifyDataSetChanged()
    }

    override fun setValues() {

        mPref = mContext.getSharedPreferences("viewSetting", Context.MODE_PRIVATE)

        titleText = classNUm

        mAdapter =
            KanjiListAdapter(mContext, kanjiClassData, intent.getStringExtra("classNum") as String)
        binding.kanjiRecyclerView.adapter = mAdapter
        val gridLayoutManager = GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false)
        binding.kanjiRecyclerView.layoutManager = gridLayoutManager
    }

    private fun randomTest() {

        mPref.edit().putBoolean("viewAll", false).apply()
        val myIntent = Intent(mContext, KanjiDetailActivity::class.java)
        myIntent.putExtra("kanjiListData", KanjiClassData(classNUm, kanjiClassData.shuffled()))
        startActivity(myIntent)
    }
}

