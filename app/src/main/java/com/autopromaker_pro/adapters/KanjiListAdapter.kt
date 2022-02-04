package com.autopromaker_pro.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.autopromaker_pro.KanjiStudy.KanjiDetailActivity
import com.autopromaker_pro.KanjiStudy.R
import com.autopromaker_pro.datas.KanjiClassData
import com.autopromaker_pro.datas.KanjiData
import com.autopromaker_pro.room.KanjiDatabase

class KanjiListAdapter(
    private val mContext: Context,
    private val mList: List<KanjiData>,
    private val classNum: String
) : RecyclerView.Adapter<KanjiListAdapter.ViewHolder>() {

    private val db = KanjiDatabase.getInstance(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.kanji_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mList[position], position)
    }

    inner class ViewHolder(view: View) : BaseViewHolder(mContext, view) {

        private val backgroundLayout = view.findViewById<ConstraintLayout>(R.id.backgroundLayout)
        private val kanjiMean = view.findViewById<TextView>(R.id.kanjiMean_txt)
        private val kanji = view.findViewById<TextView>(R.id.kanji_txt)
        private val isCheckedImg = view.findViewById<ImageView>(R.id.isChecked_img)

        fun bind(item: KanjiData, position: Int) {

            kanji.text = item.title
            val firstMean = item.mean.split(",")
            val reSplit = firstMean[0].split("/")
            if (firstMean[0] == reSplit[0]) {
                kanjiMean.text = firstMean[0]
            } else {
                kanjiMean.text = reSplit[0]
            }
            val isChecked = db!!.kanjiDao().getChecked(item.title)

            isCheckedImg.isVisible = isChecked == "true"

            backgroundLayout.setOnClickListener {

                val intent = Intent(mContext, KanjiDetailActivity::class.java)
                intent.putExtra("kanjiListData", KanjiClassData(classNum, mList))
                intent.putExtra("clickedPosition", position)
                mContext.startActivity(intent)
            }
        }
    }
}