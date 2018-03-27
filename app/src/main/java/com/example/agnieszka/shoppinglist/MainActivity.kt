package com.example.agnieszka.shoppinglist

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var db: MySQLiteHelper
    lateinit var viewAdapter: MyAdapter
    lateinit var myDataset: ArrayList<Product>
    lateinit var fragment: MyPreferenceFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = MySQLiteHelper(this)
        myDataset = db.allProducts

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(myDataset)

        fragment = MyPreferenceFragment(myDataset, viewAdapter)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply{
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        var button = findViewById<FloatingActionButton>(R.id.my_fab)
        button.setOnClickListener {view ->
            showAddDialog(myDataset)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("ResourceType")
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId == R.id.settings_button) {
            var fm = fragmentManager.beginTransaction()
            var fab = findViewById<FloatingActionButton>(R.id.my_fab)
            if(!fragment.isAdded) {
                fm.add(R.id.my_main_layout, fragment, getString(R.string.settings_fragment)).commit()
                fab.hide()
            }
            else{
                fm.remove(fragment).commit()
                fab.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showAddDialog(myDataset: ArrayList<Product>) {

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
            viewAdapter.notifyItemInserted(viewAdapter.itemCount)
            if(fragment.preferenceManager.sharedPreferences.getBoolean(getString(R.string.sort_setting_key), false)){
                myDataset.sortBy { it.Desc }
                viewAdapter.notifyDataSetChanged()
            }
        })
        builder.setNegativeButton(getString(R.string.cancel), { dialog, whichButton ->
            //pass
        })
        val b = builder.create()
        b.show()

    }
}

public class Product(var Id: Long, var Desc: String)







