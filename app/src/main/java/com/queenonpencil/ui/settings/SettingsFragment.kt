package com.queenonpencil.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.queenonpencil.R
import com.queenonpencil.notification.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)
        timePicker.hour = AlarmScheduler.getHour(requireContext())
        timePicker.minute = AlarmScheduler.getMinute(requireContext())

        timePicker.setOnTimeChangedListener { _, hour, minute ->
            AlarmScheduler.saveTime(requireContext(), hour, minute)
            CoroutineScope(Dispatchers.Main).launch {
                AlarmScheduler.rescheduleAll(requireContext())
            }
        }
    }
}
