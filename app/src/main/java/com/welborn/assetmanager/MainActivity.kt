package com.welborn.assetmanager

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.welborn.assetmanager.databinding.ActivityMainBinding
import com.welborn.assetmanager.ui.AssetAdapterClick
import com.welborn.assetmanager.ui.AssetModel
import com.welborn.assetmanager.ui.AssetsListAdapter
import com.welborn.assetmanager.ui.AssetsViewModel
import com.welborn.bitmapconverter.module.Controller
import java.io.IOException


class MainActivity : AppCompatActivity(), AssetAdapterClick {
    private lateinit var adapter: AssetsListAdapter

    private lateinit var binding: ActivityMainBinding
    private var mainList: MutableList<AssetModel> = mutableListOf()
    var selectedPOS: Int = 0

    private val viewModel by lazy {
        ViewModelProviders.of(this!!).get(AssetsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initialiseView()
        addData()
    }
    private fun initialiseView(){

        binding.recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = AssetsListAdapter(mainList, this)
        binding.recycler.adapter = adapter

        binding.submitbutton.setOnClickListener {
            if(mainList.isNotEmpty())
            showDialog(mainList[selectedPOS].imageDrawable)
            else
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT)
        }

        observeData()
    }

    private fun showDialog(drawable: Drawable) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_drawableview)
        val imageView = dialog.findViewById(R.id.imageView) as ImageView
        val closeBtn = dialog.findViewById(R.id.closeBtn) as ImageView
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        // Library instance to convert the drawable to bitmap
        imageView.setImageBitmap(Controller.getInstance(this).convertToBitmap(drawable))

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        lp.gravity = Gravity.CENTER

        dialog.window!!.attributes = lp
        dialog.show()
    }

    fun observeData(){
        viewModel.lst.observe(this, Observer {
            adapter.loadItems(it)
            adapter.notifyDataSetChanged()
        })
    }


    fun addData(){
        var drawablesArray = getImagesfromAssets("myfolder")

        for (i in 0 until drawablesArray.size) {
            var assetModel= AssetModel(drawablesArray[i].title, drawablesArray[i].imageDrawable)
            viewModel.add(assetModel)
        }
        adapter.loadItems(drawablesArray)
        adapter.notifyDataSetChanged()
    }

    fun getImagesfromAssets(path: String): MutableList<AssetModel>
    {
        var drawables = mutableListOf<AssetModel>()
        mainList = drawables
        try {
            val assetManager = getAssets()
            val images = assetManager.list(path)
           // val drawables = images?.let { arrayOfNulls<Drawable>(it.size) }
            if (images != null) {
                for (i in 0 until images.size) {
                    val inputStream = getAssets().open(path + "/" + images.get(i))
                    val drawable = Drawable.createFromStream(inputStream, null)
                    var assetModel = AssetModel(images.get(i), drawable)
                    drawables.add(assetModel)
                    inputStream.close()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return drawables
    }

    override fun getClickedPos(pos: Int) {
        selectedPOS = pos
    }
}