package com.huelvadevelopers.proyectozero

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var mFrameLayout : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val projectAbbreviation = resources.getString(R.string.project_abbreviation)
        toolbar.title = projectAbbreviation + "/" + resources.getString(R.string.menu_dashboard)
        setSupportActionBar(toolbar)

        mFrameLayout = container

        fab!!.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer!!.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView?
        navigationView!!.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().add(R.id.container,PlaceholderFragment.newInstance(0)).commit()
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun goSection(section: Int) {
        supportFragmentManager.beginTransaction().replace(R.id.container,PlaceholderFragment.newInstance(section)).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        val projectAbbreviation = resources.getString(R.string.project_abbreviation)
        if (id == R.id.nav_dashboard) {
            toolbar.title = projectAbbreviation + "/" + resources.getString(R.string.menu_dashboard)
            goSection(0)
        } else if (id == R.id.nav_transactions) {
            toolbar.title = projectAbbreviation + "/" + resources.getString(R.string.menu_transactions)
            goSection(1)
        } else if (id == R.id.nav_accounts) {
            toolbar.title = projectAbbreviation + "/" + resources.getString(R.string.menu_accounts)
            goSection(2)
        } else if (id == R.id.nav_tags) {
            toolbar.title = projectAbbreviation + "/" + resources.getString(R.string.menu_tag)
            goSection(3)
        } else if (id == R.id.nav_profile) {
            toolbar.title = projectAbbreviation + "/" + resources.getString(R.string.menu_profile)
            goSection(4)
        }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {
        val ARG_SECTION_NUMBER = "section_number"

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView: View
            if (this.arguments.getInt(this.ARG_SECTION_NUMBER) == 0) {
                rootView = inflater!!.inflate(R.layout.dashboard_fragment, container, false)
            } else if (this.arguments.getInt(this.ARG_SECTION_NUMBER) == 1) {
                rootView = inflater!!.inflate(R.layout.transactions_fragment, container, false)
            } else if (this.arguments.getInt(this.ARG_SECTION_NUMBER) == 2) {
                rootView = inflater!!.inflate(R.layout.accounts_fragment, container, false)
            } else if (this.arguments.getInt(this.ARG_SECTION_NUMBER) == 3) {
                rootView = inflater!!.inflate(R.layout.tags_fragment, container, false)
            } else {
                rootView = inflater!!.inflate(R.layout.profile_fragment, container, false)
            }
            return rootView
        }


        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
