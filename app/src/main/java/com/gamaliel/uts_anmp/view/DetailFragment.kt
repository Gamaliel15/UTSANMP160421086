package com.gamaliel.uts_anmp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gamaliel.uts_anmp.databinding.FragmentDetailBinding
import com.gamaliel.uts_anmp.model.News
import com.gamaliel.uts_anmp.viewmodel.DetailViewModel
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var index = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        val newsId = arguments?.getString("newsID")
        newsId?.let {
            viewModel.fetch(newsId)
        }

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading == true) {
                binding.progressBar.visibility = View.VISIBLE
                binding.constraintLayout.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.constraintLayout.visibility = View.VISIBLE
            }
        })

        viewModel.newsLoadErrorLD.observe(viewLifecycleOwner, Observer { isError ->
            if (isError == true) {
                Toast.makeText(requireContext(), "Error loading news detail", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.newsLD.observe(viewLifecycleOwner, Observer { news ->
            news?.let {
                updateUI(news)
            }
        })

        binding.btnNext.setOnClickListener {
            showNextSection()
            updateSectionNumber()
        }

        binding.btnPrevious.setOnClickListener {
            showPreviousSection()
            updateSectionNumber()
        }
    }

    private fun updateUI(news: News) {
        binding.txtTitle.text = news.judul
        binding.txtPenulis.text = news.penulis
        binding.txtIsi.text = news.isi
        binding.txtParagraf.text = news.paragraf[index]

        Picasso.get()
            .load(news.url_photo)
            .into(binding.imageViewGame) // ImageView to load the image into

        updateSectionNumber()
    }

    private fun updateSectionNumber() {
        val news = viewModel.newsLD.value
        val totalSections = news?.paragraf?.size ?: 0
        val currentSectionNumber = index + 1
        binding.txtPage.text = "$currentSectionNumber of $totalSections"
    }

    private fun showNextSection() {
        val news = viewModel.newsLD.value
        if (news != null && index < news.paragraf.size - 1) {
            index++
            binding.txtParagraf.text = news.paragraf[index]
        }
    }

    private fun showPreviousSection() {
        val news = viewModel.newsLD.value
        if (news != null && index > 0) {
            index--
            binding.txtParagraf.text = news.paragraf[index]
        }
    }
}
