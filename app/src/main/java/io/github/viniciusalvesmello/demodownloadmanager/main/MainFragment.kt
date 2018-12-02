package io.github.viniciusalvesmello.demodownloadmanager.ui.main

import android.Manifest
import android.app.DownloadManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.viniciusalvesmello.demodownloadmanager.R
import io.github.viniciusalvesmello.demodownloadmanager.core.checkPermissionIsGranted
import io.github.viniciusalvesmello.demodownloadmanager.core.requestPermissionOnFragment
import io.github.viniciusalvesmello.demodownloadmanager.model.DownloadImage
import kotlinx.android.synthetic.main.main_fragment.*
import android.content.ContextWrapper
import android.os.Environment
import java.io.File


class MainFragment : Fragment() {

    private var listOfDownloadImage: MutableList<DownloadImage>? = null
    private var downloadManager: DownloadManager? = null
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent?.action) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
                System.out.println("download id=$downloadId")
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inflaterFragment = inflater.inflate(R.layout.main_fragment, container, false)
        downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return inflaterFragment
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadManager(listOfDownloadImage!!)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.data.observe(this, Observer { data ->
            listOfDownloadImage = data
        })
        buttonDownload.setOnClickListener {
            if (listOfDownloadImage == null) return@setOnClickListener
            if (context == null) return@setOnClickListener
            if (activity == null) return@setOnClickListener
            if (checkPermissionIsGranted(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                downloadManager(listOfDownloadImage!!)
                return@setOnClickListener
            }
            requestPermissionOnFragment(this, listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    private fun downloadManager(downloadImages: MutableList<DownloadImage>) {
        val dir : File? = context?.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE)
        downloadImages.forEach { downloadImage ->
            DownloadManager.Request(Uri.parse(downloadImage.url)).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                setTitle(downloadImage.title)
                setDescription(downloadImage.description)
                setVisibleInDownloadsUi(true)
                allowScanningByMediaScanner()
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(dir?.absolutePath, downloadImage.title)
                context?.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
                downloadImage.id = downloadManager?.enqueue(this) ?: -1L
            }
        }
    }

}
