package com.leaf.notes.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import cn.pedant.SweetAlert.SweetAlertDialog
import com.leaf.notes.R
import com.leaf.notes.adapter.NoteAdapter
import com.leaf.notes.database.DataBase
import com.leaf.notes.databinding.FragmentMainBinding
import com.leaf.notes.model.Note

class MainFragment : Fragment(R.layout.fragment_main) {
    private val binding: FragmentMainBinding by viewBinding()
    private lateinit var data: ArrayList<Note>
    private val adapter by lazy { NoteAdapter(data) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val database = DataBase.getData(requireContext())
        data = ArrayList()
        binding.addNote.setOnClickListener {
            parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.container, RefactorFragment()).commit()
        }
        data = database.noteDao().notes() as ArrayList<Note>
        binding.recycler.adapter = adapter
        binding.sortMode.setOnClickListener {
            if (binding.sortMode.text.toString() == "DATE") {
                binding.sortMode.setText("MODE")
                data.clear()
                data.addAll(database.noteDao().notes() as ArrayList<Note>)
            } else {
                binding.sortMode.setText("DATE")
                data.clear()
                data.addAll(database.noteDao().notes() as ArrayList<Note>)
                data.reverse()
            }
            adapter.notifyDataSetChanged()
        }

        adapter.setOnClickListener { i ->
            parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                .replace(R.id.container, RefactorFragment::class.java, bundleOf("id" to data[i].id))
                .commit()
        }
        adapter.setOnLongClickListener { i ->
            val pDialog = SweetAlertDialog(
                requireContext(),
                SweetAlertDialog.WARNING_TYPE
            ).setTitleText("Are you sure?").setContentText("Won't be able to recover this file!")
                .setConfirmButton("Yes,delete it!") {
                    database.noteDao().deleteNote(data[i].id)
                    data.removeAt(i)
                    adapter.notifyItemRemoved(i)
                    it.cancel()
                }.show()
        }
        binding.search.addTextChangedListener {
            if (it.toString().isNullOrEmpty()) {
                data.clear()
                data.addAll(database.noteDao().notes() as ArrayList<Note>)
            } else {
                data.clear()
                data.addAll(database.noteDao().getCertainNotes(it.toString()) as ArrayList<Note>)
            }
            adapter.notifyDataSetChanged()
        }
    }
}