package com.leaf.notes.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.leaf.notes.R
import com.leaf.notes.databinding.FragmentRefactorBinding

class RefactorFragment : Fragment(R.layout.fragment_refactor) {
    private val binding: FragmentRefactorBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}