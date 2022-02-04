package com.autopromaker_pro.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.autopromaker_pro.KanjiStudy.KanjiClassActivity
import com.autopromaker_pro.KanjiStudy.R
import com.autopromaker_pro.datas.KanjiClassData
import com.autopromaker_pro.datas.KanjiData
import com.autopromaker_pro.room.KanjiDatabase

class KanjiClassListAdapter(
    private val mContext: Context,
    private val mList: List<KanjiClassData>
) : RecyclerView.Adapter<KanjiClassListAdapter.ViewHolder>() {

    private val db = KanjiDatabase.getInstance(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.kanji_class_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mList[position])
    }

    inner class ViewHolder(view: View) : BaseViewHolder(mContext, view) {

        private val backgroundLayout = view.findViewById<ConstraintLayout>(R.id.backgroundLayout)
        private val kanjiClassText = view.findViewById<TextView>(R.id.kanjiClass_Txt)
        private val kanjiListCountText = view.findViewById<TextView>(R.id.kanjiListCount_txt)
        private val progressBar = view.findViewById<ProgressBar>(R.id.study_progressBar)

        @SuppressLint("SetTextI18n")
        fun bind(item: KanjiClassData) {

            val dbData = db!!.kanjiDao().getClass(item.className)
            Log.d("KanjiClassListAdapter", dbData.toString())

            val itemSize = dbData.size

//            val itemSize = item.detail.size
            val className = item.className
            progressBar.max = itemSize
            kanjiClassText.text = className
            val checkedCount = db.kanjiDao().getCheckCount(item.className)
            Log.d("KanjiClassListAdapter", checkedCount.toString())
            kanjiListCountText.text = "$checkedCount / $itemSize"
            progressBar.progress = checkedCount

            backgroundLayout.setOnClickListener {

                val list = ArrayList<KanjiData>()
                list.addAll(item.detail)

                val intent = Intent(mContext, KanjiClassActivity::class.java)
                intent.putExtra("kanjiClassName", list)
                intent.putExtra("classNum", className)
                mContext.startActivity(intent)

//                val customView =
//                    LayoutInflater.from(mContext).inflate(R.layout.custom_alert_kanji_detail, null)
//                val myAlert = AlertDialog.Builder(mContext)
//                    .setView(customView)
//                    .show()
//                val randomBtn =
//                    customView.findViewById<Button>(R.id.random_btn).setOnClickListener {
//
//                        val random = Random()
//                        val intent = Intent(mContext, KanjiDetailActivity::class.java)
//                        intent.putExtra("kanjiListData", item)
//                        intent.putExtra("classNum", className)
//                        intent.putExtra("clickedPosition", random.nextInt(item.detail.size + 1))
//                        mContext.startActivity(intent)
//                        myAlert.dismiss()
//                    }
//                val listBtn = customView.findViewById<Button>(R.id.list_btn).setOnClickListener {
//                    val list = ArrayList<KanjiData>()
//                    list.addAll(item.detail)
//
//                    val intent = Intent(mContext, KanjiClassActivity::class.java)
//                    intent.putExtra("kanjiClassName", list)
//                    intent.putExtra("classNum", className)
//                    mContext.startActivity(intent)
//                    myAlert.dismiss()
//                }
            }
        }
    }
}