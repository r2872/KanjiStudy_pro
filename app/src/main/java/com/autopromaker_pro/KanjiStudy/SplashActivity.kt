package com.autopromaker_pro.KanjiStudy

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.autopromaker_pro.KanjiStudy.databinding.ActivitySplashBinding
import com.autopromaker_pro.room.KanjiDataEntity
import com.autopromaker_pro.room.KanjiDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var mPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        mPref = mContext.getSharedPreferences("checkFirst", MODE_PRIVATE)
        val checkFirst = mPref.getBoolean("checkFirst", false)
        if (!checkFirst) {
            Log.d("SplashActivity", "첫 실행")
            val editor = mPref.edit()
            editor.putBoolean("checkFirst", true).apply()

            CoroutineScope(Dispatchers.IO).launch {
                readJsonInStorage()
            }
        }
        val myHandler = Handler(Looper.getMainLooper())
        myHandler.postDelayed({
            val myIntent = Intent(mContext, MainActivity::class.java)
            startActivity(myIntent)
            finish()
        }, 2000)

//        checkPremium()
    }

    private fun readJsonInStorage() {
        val jsonObj = getJsonObj()

        getJsonArr(jsonObj, "1급")
        getJsonArr(jsonObj, "2급")
        getJsonArr(jsonObj, "3급")
        getJsonArr(jsonObj, "4급")
        getJsonArr(jsonObj, "5급")
        getJsonArr(jsonObj, "6급")
        getJsonArr(jsonObj, "7급")
        getJsonArr(jsonObj, "8급")
        getJsonArr(jsonObj, "준3급")
        getJsonArr(jsonObj, "준4급")
        getJsonArr(jsonObj, "준5급")
        getJsonArr(jsonObj, "준6급")
        getJsonArr(jsonObj, "준7급")
        getJsonArr(jsonObj, "준특급")
        getJsonArr(jsonObj, "특급")
    }

    private fun getJsonObj(): JSONObject {
        val inputStream = resources.openRawResource(R.raw.kanjiclassdata)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        return JSONObject(jsonString)
    }

    private fun getJsonArr(item: JSONObject, classText: String): JSONArray {
        val result = item.getJSONArray(classText)
        var classNum = classText
        var title: String
        var isChecked: String
        val db = KanjiDatabase.getInstance(mContext)
        for (i in 0 until result.length()) {
            val jsonObj = result.getJSONObject(i)
            title = jsonObj.getString("title")
            isChecked = jsonObj.getString("checked")
            db!!.kanjiDao().insert(KanjiDataEntity(classNum, title, isChecked))
        }
        return result
    }

//    private fun checkPremium() {
//        if (mAuth.currentUser != null) {
//            GlobalData.isSignIn = true
//            val userEmail = mAuth.currentUser?.email.toString().split("@")[0]
//            mDatabase = Firebase.database.getReference("Users").child(userEmail)
//            mDatabase.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (data in snapshot.children) {
//                        val getIsPremium = data.value
//                        GlobalData.checkPremium = getIsPremium == true
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
//        }
//    }
}