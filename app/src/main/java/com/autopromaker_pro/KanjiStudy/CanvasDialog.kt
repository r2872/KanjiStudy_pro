package com.autopromaker_pro.KanjiStudy

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

class CanvasDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val mDialogView = LayoutInflater.from(this).inflate(R.layout.item_view, null)
        val myCanvas = CanvasView(context)
        val mBuilder =
            AlertDialog.Builder(
                requireContext(),
                androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
            )
                .setView(myCanvas)
//            .setPositiveButton("지우기",DialogInterface.OnClickListener(){
//                dialogInterface, i ->
////                CanvasView(context).path.reset()
//            })

//        val alertDialog = mBuilder.show()
        return mBuilder.create()
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }
//        alertDialog.findViewById<Button>(R.id.closeBtn)?.setOnClickListener {
//            alertDialog.dismiss()
//        }
}