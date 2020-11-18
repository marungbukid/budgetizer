package com.budgetizer.challenge.ui.fiftytwo

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.budgetizer.challenge.dagger.inject
import com.budgetizer.challenge.databinding.FragmentFiftytwoWeekBinding
import com.budgetizer.challenge.ui.ChallengeActivity
import com.budgetizer.challenge.ui.FragmentsEvents
import com.budgetizer.challenge.ui.FragmentsViewModel
import com.budgetizer.challenge.ui.NotificationPublisher
import com.budgetizer.core.challenge.data.ChallengeLocalDataSource
import com.budgetizer.core.data.entry.model.Entry
import com.budgetizer.core.data.entry.model.EntryRange
import com.budgetizer.core.data.entry.model.EntrySource
import com.budgetizer.core.data.entry.model.EntryType
import java.util.Calendar
import javax.inject.Inject

class FiftyTwoWeekFragment : Fragment() {

    @Inject
    lateinit var viewModel: FragmentsViewModel

    private var _binding: FragmentFiftytwoWeekBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FiftyTwoWeekAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFiftytwoWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = FiftyTwoWeekAdapter(requireActivity())

        setupToolbar()

        initBindingResources()
        initEventBindings()
        initViewModelObservers()
    }

    override fun onResume() {
        super.onResume()
        fetchEntries()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbar() {
        (requireActivity() as? ChallengeActivity)?.setSupportActionBar(binding.toolbar)
        (requireActivity() as? ChallengeActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initBindingResources() {
        binding.recyclerView.adapter = adapter
    }

    private fun initEventBindings() {
        adapter.onItemClicked = {
            viewModel.updateEntry(it.copy(type = EntryType.SAVINGS))
        }
        adapter.showSavings = !binding.showSavings.isChecked
        binding.showSavings.setOnCheckedChangeListener { _, isChecked ->
            adapter.showSavings = !isChecked
        }
        binding.increment.setEndIconOnClickListener {
            if (binding.increment.editText?.text?.isEmpty() == true) {
                binding.increment.editText?.error =
                    getString(com.budgetizer.core.R.string.error_required_fields)
                return@setEndIconOnClickListener
            }
            updateEntries()
        }
    }

    private fun initViewModelObservers() {
        viewModel.events.observe(viewLifecycleOwner, { _events ->
            val events = _events ?: return@observe

            when (events) {
                is FragmentsEvents.IncrementsUpdate ->
                    binding.increment.editText?.setText(events.increments.toString())
                is FragmentsEvents.EntriesFetched ->
                    adapter.items = events.items
                FragmentsEvents.EntryUpdated -> fetchEntries()
            }
        })
    }

    private fun fetchEntries() {
        adapter.items = emptyList()
        viewModel.getAllEntries(EntrySource.challengeSource(SOURCE_TAG))
    }

    private fun updateEntries() {
        val increments = binding.increment.editText?.text.toString().toDouble()
        if (increments <= 0) {
            binding.increment.editText?.error = "Please enter valid value"
            return
        }

        viewModel.deleteChallengeEntries(EntrySource.challengeSource(SOURCE_TAG))

        val calendar = Calendar.getInstance()
        val entries = arrayListOf<Entry>()
        var incrementsValue = increments

        viewModel.save(ChallengeLocalDataSource.KEY_INCREMENTS, increments)
        for (i in 1 until 52) {
            entries.add(
                Entry(
                    type = EntryType.EXPENSE,
                    label = TITLE,
                    amount = incrementsValue,
                    tags = emptyList(),
                    source = EntrySource.challengeSource(SOURCE_TAG),
                    entryRange = EntryRange.WEEKLY,
                    createdAt = calendar.time,
                    updatedAt = calendar.time
                )
            )
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
            scheduleNotification(requireContext(), calendar.timeInMillis, 1)
            incrementsValue += increments
        }

        viewModel.addAllEntries(entries)
        fetchEntries()
    }

    private fun scheduleNotification(
        context: Context,
        delay: Long,
        notificationId: Int
    ) { //delay is after how much time(in millis) from current time you want to schedule the notification
        val builder =
            NotificationCompat.Builder(requireContext(), NotificationPublisher.NOTIFICATION_ID)
                .setContentTitle(TITLE)
                .setContentText("Sample")
                .setAutoCancel(true)
                .setSmallIcon(com.budgetizer.R.mipmap.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        val intent = Intent(context, ChallengeActivity::class.java)
        val activity = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        builder.setContentIntent(activity)
        val notification: Notification = builder.build()
        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val futureInMillis: Long = SystemClock.elapsedRealtime() + delay
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
    }

    companion object {
        private const val TITLE = "52 Week Challenge"
        private const val SOURCE_TAG = "52WK"
    }
}

