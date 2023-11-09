package com.leaf.notes.fragment

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.leaf.notes.MyWorker
import com.leaf.notes.R
import com.leaf.notes.database.DataBase
import com.leaf.notes.databinding.FragmentRefactorBinding
import com.leaf.notes.model.Note
import java.util.Calendar
import java.util.concurrent.TimeUnit

class RefactorFragment : Fragment(R.layout.fragment_refactor) {
    private lateinit var textView: TextView
    private var boolean = false
    private val binding: FragmentRefactorBinding by viewBinding()
    private val database by lazy { DataBase.getData(requireContext()) }
    private lateinit var notification: Notification.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var notif: Notification

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
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
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val timePicker = TimePickerDialog(
            context,
            { view, hourOfDay, minute ->
                val currentMillis = System.currentTimeMillis()

                val selectedTime = Calendar.getInstance()
                selectedTime[Calendar.HOUR_OF_DAY] = hourOfDay
                selectedTime[Calendar.MINUTE] = minute
                selectedTime[Calendar.SECOND] = 0
                selectedTime[Calendar.MILLISECOND] = 0

                val selectedMillis = selectedTime.timeInMillis

                val distanceMillis = selectedMillis - currentMillis
                if (distanceMillis < 0) return@TimePickerDialog
                Toast.makeText(requireContext(), "$distanceMillis", Toast.LENGTH_SHORT)
                    .show()
                val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
                    .addTag("$id")
                    .setInputData(Data.Builder().put("name", nameEdt.text.toString()).build())
                    .setInitialDelay(distanceMillis, TimeUnit.MILLISECONDS)
                    .build()

                WorkManager.getInstance(requireContext())
                    .enqueueUniqueWork("$id", ExistingWorkPolicy.REPLACE, workRequest)
                WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workRequest.id)
                    .observe(viewLifecycleOwner) { workInfo ->
                        if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                            notification = Notification.Builder(requireContext())
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle(nameEdt.text.toString())
                                .setContentText("Time to remember")
                            notificationManager =
                                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.createNotificationChannel(createNotificationChannel())
                            notification.setChannelId("channel")
                            notif = notification.build()
                            notificationManager.notify(1, notif)
                        }
                    }
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
                database.noteDao().addNote(Note(id = 0, name = a, note = noteEdt.text.toString()))
                id = database.noteDao().notes().last().id
            }
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

                        R.id.delete12 -> {
                            database.noteDao().deleteNote(id)
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT)
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
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction().replace(R.id.container, MainFragment()).commit()
                }
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        return NotificationChannel("channel", "Notes", NotificationManager.IMPORTANCE_HIGH)
    }
}
