package com.example.mendofeel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mendofeel.databinding.ActivityMainBinding
import com.example.mendofeel.databinding.SamplePostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    lateinit var lnLayout:LinearLayoutManager
    lateinit var myAdapter:MenDoAdapter
    lateinit var tempList: ArrayList<MenDo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        lnLayout= LinearLayoutManager(this)
        tempList= arrayListOf<MenDo>()
        binding.activityRecyclerView.layoutManager=lnLayout
        binding.shimmer.startShimmer()
        //for getting Api Data
        if(!checkForInternet(applicationContext)){
            Toast.makeText(applicationContext," Check Your internet Connection", Toast.LENGTH_SHORT).show()
        }
        getData();


        binding.swipeReferesh.setOnRefreshListener {
            binding.shimmer.startShimmer()
            binding.shimmer.visibility=View.VISIBLE
            binding.swipeReferesh.visibility=View.GONE
            getData()
        }

        binding?.search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tempList.clear()
                binding?.activityRecyclerView?.adapter?.notifyDataSetChanged()
                return false
            }

        })



    }


    private fun getData() {
        var rf= Retrofit.Builder().baseUrl(MendoApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var API=rf.create(MendoApi::class.java)
        var call=API.getData

        call?.enqueue(object : Callback<MenDo?>{
            override fun onResponse(call: Call<MenDo?>, response: Response<MenDo?>) {

                if(response.isSuccessful()) {

                    if(response.code()==200) {
                        Log.d("errorbody"," the  " + response.errorBody())
                        binding.swipeReferesh.visibility=View.VISIBLE
                        var Mendolist = response.body()!!
                        tempList.add(Mendolist)
                        myAdapter = MenDoAdapter(applicationContext, Mendolist)
                        binding.activityRecyclerView.adapter = myAdapter
                        binding.swipeReferesh.isRefreshing=false

                    }

                    binding.shimmer.stopShimmer()
                    binding.shimmer.visibility = View.GONE
                }else{
                    binding.swipeReferesh.isRefreshing=false

                    Toast.makeText(applicationContext,"the error is "+ response.errorBody(),Toast.LENGTH_LONG).show()
                    if(response.body()==null){
                        Toast.makeText(applicationContext,"Some API Issue",Toast.LENGTH_LONG).show()
                    }

                }

            }

            override fun onFailure(call: Call<MenDo?>, t: Throwable) {
                Log.d("APIERROR"," on failure" + t.message)
                Toast.makeText(applicationContext,"the error is "+ t.message,Toast.LENGTH_LONG).show()
                onBackPressed()
            }
        })


    }
    private fun checkForInternet(context: Context): Boolean {


        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            val network = connectivityManager.activeNetwork ?: return false


            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true


                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}


