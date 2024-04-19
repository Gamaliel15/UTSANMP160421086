package com.gamaliel.uts_anmp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamaliel.uts_anmp.R
import com.gamaliel.uts_anmp.databinding.FragmentHomeBinding
import com.gamaliel.uts_anmp.viewmodel.NewsListViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: NewsListViewModel
    private val newsListAdapter = NewsAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(NewsListViewModel::class.java)
        viewModel.refresh()

        binding.recViewNews.layoutManager = LinearLayoutManager(context)
        binding.recViewNews.adapter = newsListAdapter

        observeViewModel()

        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
            binding.recViewNews.visibility = View.GONE
            binding.txtError.visibility = View.GONE
            binding.progressLoad.visibility = View.VISIBLE
            binding.refreshLayout.isRefreshing = false
        }

    }

    fun observeViewModel(){
        viewModel.newsLD.observe(viewLifecycleOwner, Observer {
            newsListAdapter.updateNewsList(it)
        })

        viewModel.newsLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it==true){
                binding.txtError.visibility = View.VISIBLE
            }else{
                binding.txtError.visibility = View.GONE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it==true){
                binding.progressLoad.visibility = View.VISIBLE
                binding.recViewNews.visibility = View.GONE
            }else{
                binding.progressLoad.visibility = View.GONE
                binding.recViewNews.visibility = View.VISIBLE
            }
        })
    }

}