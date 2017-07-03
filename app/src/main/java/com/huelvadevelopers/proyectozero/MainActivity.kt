package com.huelvadevelopers.proyectozero

import android.app.AlertDialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.*
import com.huelvadevelopers.proyectozero.model.Category
import com.unnamed.b.atv.model.TreeNode
import kotlinx.android.synthetic.main.app_bar_main.*
import com.unnamed.b.atv.view.AndroidTreeView
import kotlinx.android.synthetic.main.tags_fragment.*
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.tags_fragment.view.*
import android.content.DialogInterface
import android.graphics.Color
import android.widget.EditText
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.layout_icon_node.*
import android.widget.Toast
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener




class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var mFrameLayout : FrameLayout
    lateinit var databaseManager : DataBaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseManager = DataBaseManager(this)

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
        supportFragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(section)).commit()
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
            fab!!.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(this)
                val inflater = this.layoutInflater
                val dialogView = inflater.inflate(R.layout.add_category_dialog, null)
                dialogBuilder.setView(dialogView)

                //Name EditText
                val edt : EditText = dialogView.findViewById(R.id.add_category_name) as EditText
                //Parent Spinner
                val spinner : Spinner = dialogView.findViewById(R.id.add_category_parent) as Spinner
                val categories = databaseManager.getCategories()
                val nameParents = ArrayList<String>()
                nameParents.add("None")
                val categoryParent = ArrayList<Category>()
                for ( c : Category in categories ){
                    if(c.parent==null){
                        nameParents.add(c.name)
                        categoryParent.add(c)
                    }
                }
                val adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, nameParents.toTypedArray())
                spinner.adapter = adapter
                //Icons GridView
                val gridView : GridView = dialogView.findViewById(R.id.add_category_icon) as GridView
                gridView.adapter = ImageAdapter(this)
                (gridView.adapter as ImageAdapter).selectionId=-1

                dialogBuilder.setTitle(getString(R.string.sNewCategory))
                dialogBuilder.setPositiveButton("Done") { dialog, whichButton ->
                    //Vacio porque abajo lo sobrescribimos para decidir si hacer dismiss o no
                }
                dialogBuilder.setNegativeButton("Cancel") { dialog, whichButton ->
                    //pass
                }
                val dialog = dialogBuilder.create()
                dialog.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    var dismiss = true
                    val errorDialogBuilder = AlertDialog.Builder(this)
                    errorDialogBuilder.setTitle("Error")
                    if(edt.text.toString().isEmpty()) {
                        dismiss = false
                        errorDialogBuilder.setMessage("Tienes que seleccionar un nombre")
                        errorDialogBuilder.setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
                            //pass
                        }
                        errorDialogBuilder.create().show()
                        return@setOnClickListener
                    }
                    else if((gridView.adapter as ImageAdapter).selectionId == -1) {
                        dismiss = false
                        errorDialogBuilder.setTitle("Error")
                        errorDialogBuilder.setMessage("Tienes que seleccionar un icono")
                        errorDialogBuilder.setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
                            //pass
                        }
                        errorDialogBuilder.create().show()
                        return@setOnClickListener
                    }
                    val category = Category(-1, edt.text.toString(), (gridView.adapter as ImageAdapter).selectionId, 1)
                    if(spinner.selectedItemPosition != 0)
                        categoryParent[spinner.selectedItemPosition-1].addChild(category)
                    databaseManager.addCategory(category)
                    goSection(3)
                    if(dismiss) dialog.dismiss()
                }
            }
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
                var v = (activity as MainActivity).databaseManager.getCategories()
                val root = TreeNode.root().setViewHolder(TreeViewHolder(activity))

                for(category : Category in v){
                    val node = TreeNode(category)
                    if(category.parent != null)
                        for(n : TreeNode in root.children) {
                            if ((n.value as Category).name.equals(category.parent?.name)) {
                                n.addChild(node)
                                break
                            }
                        }
                    else
                        root.addChild(node)
                }

                val tView = MyAndroidTreeView(activity, root)
                tView.setDefaultViewHolder(TreeViewHolder::class.java)
                (rootView.layoutTag.findViewById(R.id.treeViewContainer) as LinearLayout).addView(tView.view)

                tView.expandAll()

                //Quitamos la flechita a los nodos de la raiz sin hijos
                for(n : TreeNode in root.children) {
                    if (n.children.size == 0)
                        n.viewHolder.view.findViewById(R.id.arrow_icon).visibility = View.INVISIBLE
                }

                rootView.findViewById(R.id.removeCategory).setOnDragListener { v, event ->
                    if(event.action==DragEvent.ACTION_DROP){
                        val item = event.clipData.getItemAt(0)
                        var dragData : String = item.text as String
                        v.setBackgroundColor(resources.getColor(R.color.colorDarkRed))
                        (activity as MainActivity).databaseManager.removeCategoryById(dragData.toInt())
                        (activity as MainActivity).goSection(3)
                    }
                    else if(event.action == DragEvent.ACTION_DRAG_ENTERED)
                        v.setBackgroundColor(resources.getColor(R.color.colorDarkRed))
                    else if(event.action == DragEvent.ACTION_DRAG_EXITED)
                        v.setBackgroundColor(resources.getColor(R.color.colorLightRed))
                    true
                }

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
