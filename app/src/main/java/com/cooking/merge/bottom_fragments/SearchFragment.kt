package com.cooking.merge.bottom_fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooking.merge.MainActivity
import com.cooking.merge.R
import com.cooking.merge.search.SearchDetailsActivity
import com.cooking.merge.adapters.Permissions.CAMERA_PERMISSIONS
import com.cooking.merge.adapters.HotitemsAdapter
import com.cooking.merge.models.HotitemsModel
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    lateinit var adapter: HotitemsAdapter
    lateinit var hotlist_rv: RecyclerView
    lateinit var hotlist: ArrayList<HotitemsModel>
    lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)


        //Open Camera
        val Launch_Camera_btn = view.findViewById<View>(R.id.Launch_Camera_btn) as Button
        Launch_Camera_btn.setOnClickListener {
            if ((activity as MainActivity).checkPermissions(CAMERA_PERMISSIONS[0])) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            } else {
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hotlist_rv = view.findViewById(R.id.hotlist_rv)
        gridLayoutManager = GridLayoutManager(
            hotlist_rv.context, 1,
            LinearLayoutManager.VERTICAL, false
        )
        hotlist_rv.layoutManager = gridLayoutManager
        hotlist_rv.setHasFixedSize(true)

        hotlist = getList()
        adapter = HotitemsAdapter(hotlist)
        hotlist_rv.adapter = adapter

        ingredients_search.isSubmitButtonEnabled = true        //?????????????????????????????????
        ingredients_search.isSoundEffectsEnabled = true
        ingredients_search.isSaveEnabled = true
        //customize searchView
        val searchIcon = ingredients_search.findViewById<ImageView>(R.id.search_mag_icon)
        val searchBtn = ingredients_search.findViewById<ImageView>(R.id.search_button)
        val cancel = ingredients_search.findViewById<ImageView>(R.id.search_close_btn)
        val submit = ingredients_search.findViewById<ImageView>(R.id.search_go_btn)
        val searchText= ingredients_search.findViewById<TextView>(R.id.search_src_text)

        searchIcon.setColorFilter(Color.WHITE)
        searchBtn.setColorFilter(Color.WHITE)
        cancel.setColorFilter(Color.WHITE)
        submit.setColorFilter(Color.WHITE)
        searchText.setTextColor(Color.WHITE)
        searchText.setHintTextColor(Color.LTGRAY)
        //customize searchView

        //??????searchView??????text???listener
        ingredients_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //?????????input, input????????????query(???????????????String?)
            var ingredients_array: MutableList<String> = ArrayList()
            override fun onQueryTextSubmit(query: String?): Boolean {

                //????????????input????????????????????????"(?????????)"????????????ingredients_array
                ingredients_array = query?.split(" ")!!.toMutableList()


                val intent = Intent(context, SearchDetailsActivity::class.java)
                val intentAllList = Intent(context,SearchDetailsActivity::class.java)

                // distinct ????????????????????????????????????
                intent.putStringArrayListExtra("passsearched", ArrayList(ingredients_array.distinct()))
                startActivity(intent)

                return false
            }

            //???query??????????????????????????????????????????(??????)???????????????????????????recyclerView??????????????????????????????????????????
            //??????????????????
            override fun onQueryTextChange(newText: String?): Boolean {
                ingredients_array.clear()
                return false
            }

        })
        //getList() ???????????????????????????????????????function


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = data?.extras?.get("data") as Bitmap
            //pictureView.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 1114
    }

    private fun getList(): ArrayList<HotitemsModel> {
        //??????????????????????????????
        val hotlist_items = arrayOf(
            "????????????", "?????????", "?????????", "???????????????", "????????????", "??????"
        )

        val hotlist_images = arrayOf(
            R.drawable.fastdinner, R.drawable.cabbage, R.drawable.potato,
            R.drawable.easyhomefood, R.drawable.oniongingergarlic,R.drawable.sugarfree
        )

        //??????RecyclerView_Adapter??????????????????ArrayList??????
        val hotList = ArrayList<HotitemsModel>()
        //??????????????????Array??????string?????????ArrayList???
        for (i in hotlist_items.indices) {
            hotList.add(HotitemsModel(hotlist_images[i], hotlist_items[i]))
        }

        return hotList
    }


}