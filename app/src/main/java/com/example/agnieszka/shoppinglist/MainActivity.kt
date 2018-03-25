package com.example.agnieszka.shoppinglist

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var db: MySQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = MySQLiteHelper(this)

        var myDataset = db.allProducts

        viewManager = LinearLayoutManager(this)
        var viewAdapter = MyAdapter(myDataset)

        var fragment: PreferenceFragment
        if (savedInstanceState == null) {
            fragment = MyPreferenceFragment(myDataset, viewAdapter)
            fragmentManager.beginTransaction().add(R.id.my_recycler_view, fragment, getString(R.string.settings_fragment))
        }

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply{
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter
        }

        var button = findViewById<FloatingActionButton>(R.id.my_fab)

        button.setOnClickListener {view ->

            showAddDialog(myDataset, viewAdapter)

        }
    }

    fun showAddDialog(myDataset: ArrayList<Product>, adapter: MyAdapter) {

        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_dialog, null)
        builder.setView(dialogView)

        val editText = dialogView.findViewById<EditText>(R.id.add_edit_text)

        builder.setTitle(getString(R.string.add_new_text))
        builder.setMessage(getString(R.string.add_new_desc))
        builder.setPositiveButton(getString(R.string.save), { dialog, whichButton ->

            var prod = db.addProduct(editText.text.toString())
            myDataset.add(prod)
            adapter.notifyItemInserted(adapter.itemCount)

        })
        builder.setNegativeButton(getString(R.string.cancel), { dialog, whichButton ->
            //pass
        })
        val b = builder.create()
        b.show()

    }

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

                if(newValue == true)
                {
                    myDataset.sortBy { it.Desc }
                    viewAdapter.notifyDataSetChanged()
                }


                true
            }

            return super.onCreateView(inflater, container, savedInstanceState)
        }
    }


}

public class Product(var Id: Long, var Desc: String)







