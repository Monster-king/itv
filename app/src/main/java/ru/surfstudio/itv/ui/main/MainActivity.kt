package ru.surfstudio.itv.ui.main

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import ru.surfstudio.itv.R

class MainActivity : DaggerAppCompatActivity(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
