package com.autopromaker_pro.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autopromaker_pro.KanjiStudy.R
import com.autopromaker_pro.KanjiStudy.databinding.FragmentSearchKanjiBinding
import com.autopromaker_pro.adapters.KanjiSearchListAdapter
import com.autopromaker_pro.datas.KanjiData
import com.autopromaker_pro.viewmodels.MainViewModel
import org.json.JSONArray
import org.json.JSONObject


class SearchKanjiFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchKanjiBinding
    private lateinit var mAdapter: KanjiSearchListAdapter
    private var mCheckSearchType = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchKanjiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.searchCurrentList.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyDataSetChanged()
        })

        setValues()
        setupEvents()
    }

    override fun setupEvents() {

        binding.searchImg.setOnClickListener {
            searchKanji()
        }
        binding.searchEdt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchKanji()
                true
            } else false
        }

        binding.searchRecyclerView.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                hideKeyboard()
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    override fun setValues() {

        setSpinner()

        mAdapter = KanjiSearchListAdapter(mContext, viewModel.getSearchLList())
        binding.searchRecyclerView.adapter = mAdapter
        binding.searchRecyclerView.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
    }

    private fun readJsonInStorage(inputText: String) {
        viewModel.clearSearchLList()

        val jsonObj = getJson()

        getJsonArr(jsonObj, "1???", inputText)
        getJsonArr(jsonObj, "2???", inputText)
        getJsonArr(jsonObj, "3???", inputText)
        getJsonArr(jsonObj, "4???", inputText)
        getJsonArr(jsonObj, "5???", inputText)
        getJsonArr(jsonObj, "6???", inputText)
        getJsonArr(jsonObj, "7???", inputText)
        getJsonArr(jsonObj, "8???", inputText)
        getJsonArr(jsonObj, "???3???", inputText)
        getJsonArr(jsonObj, "???4???", inputText)
        getJsonArr(jsonObj, "???5???", inputText)
        getJsonArr(jsonObj, "???6???", inputText)
        getJsonArr(jsonObj, "???7???", inputText)
        getJsonArr(jsonObj, "?????????", inputText)
        getJsonArr(jsonObj, "??????", inputText)

        mAdapter.notifyDataSetChanged()
    }

    private fun getJsonArr(item: JSONObject, classText: String, inputText: String): JSONArray {

        val result = item.getJSONArray(classText)
        var title: String
        var radical: String
        var mean: String
        var writeCount: String
        var img: String
        var isChecked: String
        for (i in 0 until result.length()) {
            val jsonObj = result.getJSONObject(i)
            title = jsonObj.getString("title")
            radical = jsonObj.getString("radical")
            mean = jsonObj.getString("mean")
            writeCount = jsonObj.getString("writeCount")
            img = jsonObj.getString("img")
            isChecked = jsonObj.getString("checked")
            val meanPix = mean.replace(" ", "")
            val splitSlash = meanPix.split("/")
            val splitComma = meanPix.split(",")
            if (mCheckSearchType) {

                for (e in splitSlash.indices) {

                    if (splitSlash[e].endsWith(inputText)) {

                        viewModel.addSearchList(
                            KanjiData(title, radical, mean, writeCount, img, isChecked)
                        )
                        break

                    } else {
                        for (p in splitComma.indices) {
                            if (splitComma[p].endsWith(inputText)) {

                                viewModel.addSearchList(
                                    KanjiData(title, radical, mean, writeCount, img, isChecked)
                                )

                                break
                            }
                        }
                    }

                }
            } else {
                for (e in splitSlash.indices) {

                    val subtract = splitSlash[e].substring(0, splitSlash[e].length - 1)

                    if (subtract.contains(inputText)) {

                        viewModel.addSearchList(
                            KanjiData(title, radical, mean, writeCount, img, isChecked)
                        )
                        break

                    } else {
                        for (p in splitComma.indices) {

                            val subtract = splitComma[p].substring(0, splitComma[p].length - 1)

                            if (subtract.contains(inputText)) {

                                viewModel.addSearchList(
                                    KanjiData(title, radical, mean, writeCount, img, isChecked)
                                )

                                break
                            }
                        }
                    }

                }
            }
            if (title == inputText) {
                viewModel.addSearchList(
                    KanjiData(title, radical, mean, writeCount, img, isChecked)
                )
            }
        }

        return result
    }

    private fun hideKeyboard() {
        if (activity != null && requireActivity().currentFocus != null) {

            val inputManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun searchKanji() {
        if (binding.searchEdt.length() == 0) {
            Toast.makeText(mContext, "?????? ??? ????????? ?????? ??? ?????????", Toast.LENGTH_SHORT).show()
            return
        } else {
            val inputText = binding.searchEdt.text.toString().replace(" ", "")
            readJsonInStorage(inputText)
            val result = viewModel.getSearchLList()
            if (result.isEmpty()) {
                Toast.makeText(mContext, "?????? ????????? ????????????.", Toast.LENGTH_SHORT).show()
            }
            hideKeyboard()
        }
    }

    private fun setSpinner() {
        val mySpinner = binding.spinner
        val adapter = ArrayAdapter.createFromResource(
            mContext,
            R.array.spinner_item,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = adapter

        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        binding.searchEdt.hint = "?????? ??? ????????? ??? ??? ??????"
                        mCheckSearchType = true
                    }
                    else -> {
                        binding.searchEdt.hint = "?????? ??? ????????? ??? ??? ??????"
                        mCheckSearchType = false
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

}