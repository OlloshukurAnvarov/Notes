package com.leaf.notes.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.leaf.notes.R
import com.leaf.notes.database.DataBase
import com.leaf.notes.databinding.FragmentRefactorBinding
import com.leaf.notes.model.Note
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RefactorFragment : Fragment(R.layout.fragment_refactor) {
    private lateinit var textView: TextView
    private var boolean = false
    private val binding: FragmentRefactorBinding by viewBinding()
    private val database by lazy { DataBase.getData(requireContext()) }


    private fun updateLable() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nameEdt = binding.name
        val noteEdt = binding.editNote
        textView = view.findViewById(R.id.button_done)
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
        val timePicker = TimePickerDialog(
            context,
            { view, hourOfDay, minute ->
                updateLable()
            },
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),
            false
        )

        binding.buttonDone.setOnClickListener {
            if (noteEdt.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "First Create Some Note!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            var a = nameEdt.text.toString()
            if (nameEdt.text.isNullOrBlank()) {
                a = "Untitled"
            }
            if (id != -1L) {
                database.noteDao().editNote(id, a, noteEdt.text.toString())
            } else {
                database.noteDao()
                    .addNote(Note(id = 0, name = a, note = noteEdt.text.toString()))
                textView.setBackgroundResource(R.drawable.baseline_more_vert_24)
                textView.text = ""
                boolean = true
                textView.setOnClickListener {

                    var popupMenu = PopupMenu(requireActivity(), textView)
                    val subPopupMenu = PopupMenu(requireContext(), textView)

                    val inflater = subPopupMenu.menuInflater
                    inflater.inflate(R.menu.muhimlik, subPopupMenu.menu)
                    popupMenu.inflate(R.menu.item_menu)
                    popupMenu.setOnMenuItemClickListener { menu ->
                        when (menu.itemId) {
                            R.id.bildirishnoma -> {
                                timePicker.show()

                                return@setOnMenuItemClickListener true
                            }

                            R.id.muhimlik -> {
                                subPopupMenu.show()

                                return@setOnMenuItemClickListener true
                            }

                            R.id.delete12 -> {


                                Toast.makeText(requireContext(), "delete", Toast.LENGTH_SHORT)
                                    .show()

                                return@setOnMenuItemClickListener true
                            }

                            else -> {
                                return@setOnMenuItemClickListener true
                            }
                        }

                    }
                    popupMenu.show()
                }
            }
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