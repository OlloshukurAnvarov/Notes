package com.leaf.notes.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.leaf.notes.R
import com.leaf.notes.database.DataBase
import com.leaf.notes.databinding.FragmentRefactorBinding
import com.leaf.notes.model.Note

class RefactorFragment : Fragment(R.layout.fragment_refactor) {
    private val binding: FragmentRefactorBinding by viewBinding()
    private val database by lazy { DataBase.getData(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nameEdt = binding.name
        val noteEdt = binding.editNote
        var id = -1L
        if (arguments?.containsKey("id") == true) id = arguments?.getLong("id")!!
        if (id != -1L) {
            val note = database.noteDao().getNote(id)
            nameEdt.setText(note.name)
            noteEdt.setText(note.note)
        }
        binding.buttonBack.setOnClickListener {
            parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.container, MainFragment()).commit()
        }
        binding.buttonDone.setOnClickListener {
            if (noteEdt.text.toString().isNullOrBlank()) {
                Toast.makeText(requireContext(), "First Create Some Note!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            var a = nameEdt.text.toString()
            if (nameEdt.text.toString().isNullOrBlank()) {
                a = "Untitled"
            }
            if (id != -1L) {
                database.noteDao().editNote(id, a, noteEdt.text.toString())
            } else database.noteDao()
                .addNote(Note(id = 0, name = a, note = noteEdt.text.toString()))
            parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.container, MainFragment()).commit()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.container, MainFragment()).commit()
                }
            })
    }
}