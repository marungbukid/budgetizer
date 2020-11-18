package com.budgetizer.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.budgetizer.R
import com.budgetizer.dagger.inject
import com.budgetizer.databinding.FragmentChallengeBinding
import com.budgetizer.ui.HomeActivity
import com.budgetizer.ui.HomeEvents
import com.budgetizer.ui.HomeViewModel
import com.budgetizer.ui.challenge.items.Challenge
import javax.inject.Inject

class ChallengeFragment : Fragment() {

    @Inject
    lateinit var viewModel: HomeViewModel

    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChallengeAdapter

    private val noItemsEmptyText by lazy {
        val view = requireView().findViewById<ViewStub>(R.id.stub_no_items).inflate() as CardView
        val viewText = view.findViewById<TextView>(R.id.no_items)

        val emptyText = getText(R.string.no_challenges)

        with(viewText) {
            text = emptyText
            view
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ChallengeAdapter(requireActivity())

        setHasOptionsMenu(true)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preInsertChallenges()
        initBindingResources()
        initViewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkEmptyState()
    }

    override fun onResume() {
        super.onResume()

        fetchChallenges()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        setupToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbar() {
        (requireActivity() as? HomeActivity)?.setSupportActionBar(binding.toolbar)
        updateTitle()
    }

    private fun updateTitle() {
        (requireActivity() as? HomeActivity)?.supportActionBar?.apply {
            title = "Challenges"
        }
    }

    private fun initBindingResources() {
        binding.recyclerView.adapter = adapter
    }

    private fun initViewModelObservers() {
        viewModel.events.observe(viewLifecycleOwner, { _events ->
            val events = _events ?: return@observe

            if (events is HomeEvents.ChallengesFetched) {
                val activeChallenges = ChallengeHolder(
                    type = ChallengeType.ACTIVE,
                    items = events.items.filter { it.isActive && !it.isFinished }
                )
                val availableChallenges = ChallengeHolder(
                    type = ChallengeType.AVAILABLE,
                    items = events.items.filter { !it.isActive && !it.isFinished }
                )
                val finishedChallenges = ChallengeHolder(
                    type = ChallengeType.FINISHED,
                    items = events.items.filter { it.isFinished }
                )

                val items = arrayListOf<ChallengeHolder>()

                if (activeChallenges.items.isNotEmpty()) {
                    items.add(activeChallenges)
                }
                if (availableChallenges.items.isNotEmpty()) {
                    items.add(availableChallenges)
                }
                if (finishedChallenges.items.isNotEmpty()) {
                    items.add(finishedChallenges)
                }

                adapter.items = items

                checkEmptyState()
            }
        })
    }

    private fun preInsertChallenges() {
        viewModel.insertChallenges(Challenge.preInsert)
    }

    private fun fetchChallenges() {
        viewModel.getChallenges()
    }

    private fun checkEmptyState() {
        if (adapter.items.isEmpty()) {
            setNoItemsVisibility(View.VISIBLE)
        } else {
            setNoItemsVisibility(View.GONE)
        }
    }

    private fun setNoItemsVisibility(visibility: Int) {
        noItemsEmptyText.visibility = visibility
    }
}