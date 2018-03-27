package com.example.agnieszka.shoppinglist

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

class MyPreferenceFragment() : PreferenceFragment(){

    lateinit var myDataset: ArrayList<Product>
    lateinit var viewAdapter: MyAdapter

    @SuppressLint("ValidFragment")
    constructor(dataset: ArrayList<Product>, adapter: MyAdapter) : this(){
        myDataset = dataset
        viewAdapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        var switchPreference = preferenceManager.findPreference(this.getString(R.string.sort_setting_key))
        switchPreference.setOnPreferenceChangeListener { preference, newValue ->
            if(newValue == true) {
                myDataset.sortBy { it.Desc }
                viewAdapter.notifyDataSetChanged()
            }
            else{
                myDataset.sortBy { it.Id }
                viewAdapter.notifyDataSetChanged()
            }
            true
        }

        var editPreference = preferenceManager.findPreference(this.getString(R.string.font_size_setting))
        editPreference.setOnPreferenceChangeListener { preference, newValue ->

            var value = newValue.toString().toFloatOrNull()
            if(value == null){
                Toast.makeText(activity ,"Insert numeric value", Toast.LENGTH_SHORT)
            }
            else{
                activity.findViewById<TextView>(R.id.my_text_view).textSize = value
            }
            true
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        view.setBackgroundColor(Color.WHITE)
        super.onActivityCreated(savedInstanceState)
    }
}