package com.example.chat_app.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.chat_app.R
import com.example.chat_app.addroom.AddRoomActivity
import com.example.chat_app.chatactivity.ChatActivity
import com.example.chat_app.common.Message
import com.example.chat_app.common.provider.SessionProvider
import com.example.chat_app.databinding.ActivityHomeBinding
import com.example.chat_app.datails.DetailsActivity
import com.example.chat_app.firestore.model.User
import com.example.chat_app.splash.SplashActivity
import com.example.news_app.dialogextension.showMessage
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var toggel: ActionBarDrawerToggle
    var adapter = RoomsAdapter(null)
    var tabIndex: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initViews()
        observeLiveData()
        initHeader()
    }

    private fun initHeader() {
        val header = viewBinding.nav.getHeaderView(0)
        val name = header.findViewById<TextView>(R.id.name)
        val profile = header.findViewById<ImageView>(R.id.profile2)
        name.text = SessionProvider.user?.email.toString()
    }

    override fun onStart() {
        super.onStart()
        viewBinding.tabLayout.getTabAt(tabIndex ?: 0)?.select()
    }

    private fun logout() {
        viewBinding.drawer.closeDrawers()
        showMessage(
            Message(
                "Are you Sure you want to exit ?",
                posMessage = "Yes",
                posAction = { dialog, _ ->
                    dialog.dismiss()
                    clearData()
                    navigateToSplash()
                },
                negMessage = "No",
                negAction = { dialog, _ -> dialog.dismiss() })
        )

    }

    private fun tabsData() {
        viewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                chooseTab(tab)
            }

            override fun onTabUnselected(tab: Tab?) {

            }

            override fun onTabReselected(tab: Tab?) {
                chooseTab(tab)
            }
        })
        viewBinding.tabLayout.getTabAt(0)?.select()
    }

    private fun initViews() {
        initViewModel()
        createTabs(viewModel.tabsTitle)
        viewBinding.rec.adapter = adapter
        initDrawer()
        tabsData()
        checkActivity()
    }

    private fun clearData() {
        Firebase.auth.signOut()
        SessionProvider.user = null
    }

    private fun initDrawer() {
        toggel =
            ActionBarDrawerToggle(
                this,
                viewBinding.drawer,
                viewBinding.toolbar,
                R.string.open,
                R.string.close
            )
        toggel.drawerArrowDrawable.color = resources.getColor(R.color.white)
        viewBinding.drawer.addDrawerListener(toggel)
        toggel.syncState()

        viewBinding.nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> logout()
            }
            return@setNavigationItemSelectedListener true
        }
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
    }

    private fun observeLiveData() {
        viewModel.homeViewEvents.observe(this, ::handleEvents)
        viewModel.roomsData.observe(this) {
            adapter.bind(it)
        }
    }

    private fun navigateToSplash() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    private fun navigateToAddRoom() {
        startActivity(Intent(this, AddRoomActivity::class.java))
    }

    private fun navigateToDetails() {
        val i = Intent(this, DetailsActivity::class.java)
        i.putExtra("room", viewModel.room)
        startActivity(i)
    }

    private fun navigateToChat() {
        val i = Intent(this, ChatActivity::class.java)
        i.putExtra("room", viewModel.room)
        startActivity(i)
    }

    private fun chooseTab(tab: Tab?) {
        val tag = tab?.tag as User?
        tabIndex = tab?.position
        viewModel.loadData(tag)
    }

    private fun checkActivity() {
        adapter.setOnRoomClickListener =
            RoomsAdapter.SetOnRoomClickListener { position, item ->
                viewModel.roomSetter(item)
                viewModel.checkJoining()
            }
    }

    private fun createTabs(list: List<String>) {
        list.forEach {
            val tab = viewBinding.tabLayout.newTab()
            tab.text = it
            viewBinding.tabLayout.addTab(tab)
        }
        viewBinding.tabLayout.getTabAt(0)?.tag = SessionProvider.user
    }

    private fun handleEvents(homeViewEvents: HomeViewEvents?) {
        when (homeViewEvents) {
            HomeViewEvents.GoToAddRoomActivity -> navigateToAddRoom()
            HomeViewEvents.GoToDetailsActivity -> navigateToDetails()
            HomeViewEvents.GoToChatActivity -> navigateToChat()
            else -> {}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggel.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}