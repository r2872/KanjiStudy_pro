package com.autopromaker_pro.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.autopromaker_pro.KanjiStudy.KanjiDetailActivity
import com.autopromaker_pro.KanjiStudy.R
import com.autopromaker_pro.datas.KanjiData
import com.autopromaker_pro.room.KanjiDatabase

class KanjiDetailAdapter(
    private val mContext: Context,
    private val mList: List<KanjiData>,
    private val classNum: String
) : RecyclerView.Adapter<KanjiDetailAdapter.ViewHolder>() {

    private val mPref = mContext.getSharedPreferences("viewSetting", Context.MODE_PRIVATE)
    var viewAll = mPref.getBoolean("viewAll", true)
    private val db = KanjiDatabase.getInstance(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.kanji_detail_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mList[position])
    }

    inner class ViewHolder(view: View) : BaseViewHolder(mContext, view) {

        private val meanText = view.findViewById<TextView>(R.id.mean_txt)
        private val titleText = view.findViewById<TextView>(R.id.title_txt)
        private val radicalText = view.findViewById<TextView>(R.id.radical_txt)
        private val writeCountText = view.findViewById<TextView>(R.id.writeCount_txt)
        private val checkedImg = view.findViewById<ImageView>(R.id.checked_img)
        private val backgroundLayout = view.findViewById<LinearLayout>(R.id.backgroundLayout)

        @SuppressLint("CommitPrefEdits", "SetTextI18n", "ClickableViewAccessibility")
        fun bind(item: KanjiData) {

            titleText.text = item.title
            meanText.text = item.mean
            titleText.text = item.title
            radicalText.text = "부수 \n ${item.radical}"
            writeCountText.text = "획수 \n ${item.writeCount}"

            val isChecked = db!!.kanjiDao().getChecked(item.title)
            if (isChecked == "true") {
                checkedImg.setImageResource(R.drawable.ic_baseline_check_box_24)
            } else {
                checkedImg.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)
            }

//            if (item.isChecked == "true") {
//                checkedImg.setImageResource(R.drawable.ic_baseline_star_rate_24)
//            }

            if (viewAll) showAll()
            else kanjiOnly()

            backgroundLayout.setOnTouchListener { _, event ->
                Log.d("viewAll", viewAll.toString())
                if (!viewAll) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            showAll()
                        }
                        MotionEvent.ACTION_UP -> {
                            kanjiOnly()
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            kanjiOnly()
                        }
                    }
                }
                true
            }

            checkedImg.setOnClickListener {
                db.kanjiDao().update(item.title)
                (mContext as KanjiDetailActivity).upDateView()
            }
        }

        private fun kanjiOnly() {
            meanText.visibility = View.GONE
            titleText.visibility = View.GONE
            radicalText.visibility = View.GONE
            writeCountText.visibility = View.GONE
            titleText.visibility = View.VISIBLE
        }

        private fun showAll() {
            titleText.visibility = View.VISIBLE
            meanText.visibility = View.VISIBLE
            titleText.visibility = View.VISIBLE
            radicalText.visibility = View.VISIBLE
            writeCountText.visibility = View.VISIBLE
        }
    }
}
