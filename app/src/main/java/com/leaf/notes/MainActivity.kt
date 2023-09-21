package com.leaf.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leaf.notes.fragment.MainFragment
import com.leaf.notes.fragment.RefactorFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().setReorderingAllowed(true)
            .replace(R.id.container, MainFragment()).commit()
    }
}