package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultBinding

class FragmentResult: Fragment() {

    private var _replace: StartAppNew? = null
    private val replace get() = _replace!!

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tempText: String = arguments?.getString(ResultQuiz) ?: resources.getString(R.string.DefaultValue)
        binding.textView.text = "Your result $tempText %"
        binding.back.setOnClickListener {
            replace.startAppNew()
        }
        binding.close.setOnClickListener {
            replace.closeApp()
        }
        binding.share.setOnClickListener {
            replace.shareApp(binding.textView.text.toString())
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(resultQuiz: String): FragmentResult {

            val fragment = FragmentResult()
            val args = Bundle()
            args.putString(ResultQuiz, resultQuiz)
            fragment.arguments = args
            return fragment

        }

        private const val ResultQuiz = "ResultQuiz"

    }

    interface StartAppNew{
        fun startAppNew()
        fun closeApp()
        fun shareApp(resultQuiz: String)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _replace = null
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is StartAppNew)
            _replace = context
        else
            Toast.makeText(context, "Error create fragment to host", Toast.LENGTH_LONG).show()
    }

}