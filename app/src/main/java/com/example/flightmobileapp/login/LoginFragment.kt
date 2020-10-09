package com.example.flightmobileapp.login

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flightmobileapp.MyAdapter
import com.example.flightmobileapp.R
import com.example.flightmobileapp.Repository
import kotlinx.android.synthetic.main.login_fragment.*


/**
 * Fragment of Main Page
 */
class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    /**
     * viewModel object
     */
   // private lateinit var viewModel: LoginViewModel

    private val viewModel: LoginViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this,
            LoginViewModelFactory(activity.application)
        )
            .get(LoginViewModel::class.java)
    }

    /**
     * called to inflate the layout of the fragment and initialize GUI on creation of the fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    /**
     * called after the onCreate() method of the Parent activity.  used to assign this fragment's View vars
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
     //   viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel



//        viewModel = ViewModelProviders.of(this, LoginViewModelFactory(activity?.application))
//            .get(LoginViewModel::class.java)


        val adapter = MyAdapter(object: MyAdapter.Iselected {
            override fun onSelected(url: String) {
            //    viewModel.currentUrl = url
                viewModel.setUrlForNetwork(url)
                connect.isEnabled = true
            }
        })



        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

        type_url.setOnClickListener(View.OnClickListener {
            url_edit.visibility = View.VISIBLE
        })

        url_edit.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                var txt = url_edit.text
                viewModel.addUrl(txt.toString())
                url_edit.visibility = View.INVISIBLE
                url_edit.setText("")
                return@OnKeyListener true
            }
            false
        })

        connect.setOnClickListener {
            viewModel.connect()
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_mainFragment)
        }

        viewModel.urls.observe(viewLifecycleOwner, Observer {
            Log.d("Login observe", it.toString())
            adapter.setUrls(it as ArrayList<String>)
        })

        viewModel.image.observe(viewLifecycleOwner, Observer {
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_mainFragment)
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            if(it == Repository.ApiStatus.ERROR){
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
            }
        })
    }

}
