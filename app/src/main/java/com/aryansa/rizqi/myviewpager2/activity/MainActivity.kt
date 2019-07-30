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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.control_layout.*

class MainActivity : AppCompatActivity() {


    private var isHorizontal : Boolean = true
    private val viewModel : ItemTabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2.adapter = TabAdapter(this, viewModel).apply { setHasStableIds(true) }

        TabLayoutMediator(tabLayout2, viewPager2) { tab, position ->
            tab.text = viewModel.items[position]
        }.attach()

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            viewPager2.apply {
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                currentItem = viewModel.size
            }
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
            val newPosition = (0 until viewModel.size).indexOfLast {
                viewModel.itemId(it) == currentItemId }
            viewPager2.apply {
                adapter?.notifyDataSetChanged()
                currentItem = newPosition + 1
            }
        } else {
            viewPager2.apply {
                currentItem = oldPosition - 1
                adapter?.notifyDataSetChanged()
            }
        }
    }
}
