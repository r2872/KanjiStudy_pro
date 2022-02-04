package com.autopromaker_pro.fragments

//import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.autopromaker_pro.KanjiStudy.R
import com.autopromaker_pro.viewmodels.MainViewModel
import org.json.JSONObject

abstract class BaseFragment : Fragment() {

    lateinit var mContext: Context
    lateinit var viewModel: MainViewModel
//    lateinit var mAuth: FirebaseAuth

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mContext = requireContext()
    }

    abstract fun setupEvents()
    abstract fun setValues()

    fun getJson(): JSONObject {
        val inputStream = resources.openRawResource(R.raw.kanjiclassdata)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        return JSONObject(jsonString)
    }

}