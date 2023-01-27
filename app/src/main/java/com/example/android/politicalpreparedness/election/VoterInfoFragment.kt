package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import timber.log.Timber

class VoterInfoFragment : Fragment() {

    private lateinit var binding:FragmentVoterInfoBinding
    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_voter_info,
                container,
                false)

        val bundle = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionId = bundle.argElectionId
        val division = bundle.argDivision


        viewModel = ViewModelProvider(this, VoterInfoViewModelFactory(electionId,
                division,
                requireActivity().application)).get(VoterInfoViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.urlIntent.observe(viewLifecycleOwner, Observer {
            it?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    startActivity(intent)

            }
        })

        viewModel.isElectionFollowed.observe(viewLifecycleOwner, Observer {  isElectionFollowed ->
            if (isElectionFollowed == true) {
                binding.followElectionButton.text = getString(R.string.unfollow_election)
            } else {
                binding.followElectionButton.text = getString(R.string.follow_election)
            }
        })

        return binding.root
    }



}