package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election
import org.koin.android.ext.android.inject
import timber.log.Timber

class ElectionsFragment: BaseFragment(), ElectionListener {

    private lateinit var binding: FragmentElectionBinding
    private  val viewModel: ElectionsViewModel by lazy {
        ViewModelProvider(this).get(ElectionsViewModel::class.java)
    }
    private lateinit var upcomingElectionAdapter : ElectionListAdapter
    private lateinit var savedElectionAdapter : ElectionListAdapter


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

         binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_election,
                container,
                false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        upcomingElectionAdapter = ElectionListAdapter(this)
        binding.upcomingElectionRecyclerview.adapter = upcomingElectionAdapter

        savedElectionAdapter = ElectionListAdapter(this)
        binding.savedElectionRecyclerview.adapter = savedElectionAdapter


        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            it?.let {
                upcomingElectionAdapter.submitList(it)
            }
        })

        viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            it?.let {
                savedElectionAdapter.submitList(it)
            }
        })

        viewModel.showLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading == true){
                binding.electionFragmentProgressBar.visibility = View.VISIBLE
            }
            else{
                binding.electionFragmentProgressBar.visibility = View.GONE
            }
        })

        return binding.root

    }




    override val _viewModel: BaseViewModel
        get() = viewModel


    override fun onClick(election: Election) {
        viewModel.navigationCommand.value = NavigationCommand.To(ElectionsFragmentDirections
                .actionElectionsFragmentToVoterInfoFragment(election.id,election.division))
    }
}