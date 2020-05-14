package com.github.ivanshafran.noteapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val LIST_TAG = "LIST"
        private const val DETAIL_TAG = "DETAIL"
    }

    private var detailContainer: Int = R.id.contentFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isTabletConfiguration = contentFrameLayout == null
        val listContainer: Int
        if (isTabletConfiguration) {
            listContainer = R.id.leftContentFrameLayout
            detailContainer = R.id.rightContentFrameLayout
        } else {
            listContainer = R.id.contentFrameLayout
            detailContainer = R.id.contentFrameLayout
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(listContainer, NoteListFragment.newInstance(), LIST_TAG)
                .commit()
        }
    }

    fun openDetailNoteScreen(id: Long) {
        if (supportFragmentManager.findFragmentByTag(DETAIL_TAG) != null) {
            supportFragmentManager.popBackStack()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(detailContainer, NoteDetailFragment.newInstance(id), DETAIL_TAG)
            .addToBackStack(null)
            .commit()
    }

}
