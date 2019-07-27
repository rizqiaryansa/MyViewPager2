package com.aryansa.rizqi.myviewpager2.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import com.aryansa.rizqi.myviewpager2.R
import com.aryansa.rizqi.myviewpager2.adapter.TabAdapter
import com.aryansa.rizqi.myviewpager2.viewmodel.ItemTabViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.control_layout.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout : TabLayout
    private var isHorizontal : Boolean = true
    private val viewModel : ItemTabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2 = findViewById(R.id.viewPager2)
        tabLayout = findViewById(R.id.tabLayout2)
        viewPager2.adapter = TabAdapter(this, viewModel).apply { setHasStableIds(true) }

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = viewModel.items[position]
        }.attach()

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            viewPager2.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }

        changeOrientation()
        addPage()
        removePage()
    }

    private fun changeOrientation() {
        btnOrientation.setOnClickListener {
            when {
                isHorizontal -> {
                    viewPager2.orientation = ORIENTATION_VERTICAL
                    isHorizontal = false
                    btnOrientation.text = getString(R.string.horizontal)
                }
                else -> {
                    viewPager2.orientation = ORIENTATION_HORIZONTAL
                    isHorizontal = true
                    btnOrientation.text = getString(R.string.vertical)
                }
            }
        }
    }

    private fun addPage() {
        btnAddPage.setOnClickListener {
            changeItem { viewModel.addNewItem(viewPager2.currentItem+1) }
        }
    }

    private fun removePage() {
        btnRemovePage.setOnClickListener {
            changeItem { viewModel.removeItem(viewPager2.currentItem) }
        }
    }

    private fun changeItem(performChanges: () -> Unit) {
        val oldPosition = viewPager2.currentItem
        val currentItemId = viewModel.itemId(oldPosition)
        performChanges()
        if(viewModel.contains(currentItemId)) {
            viewPager2.adapter?.notifyDataSetChanged()
            val newPosition = (0 until viewModel.size).indexOfLast {
                viewModel.itemId(it) == currentItemId }
            viewPager2.currentItem = newPosition + 1
        } else {
            viewPager2.currentItem = oldPosition - 1
            viewPager2.adapter?.notifyDataSetChanged()
        }
    }
}
