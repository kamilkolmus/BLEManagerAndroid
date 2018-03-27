package com.i7xaphe.blemanager

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothProfile.*
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.*
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import android.support.v4.view.PagerAdapter
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent






class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private var adapter: MyPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        floatingactionbutton.setOnClickListener { view ->
            val myIntent = Intent(this@MainActivity, GraphActivity::class.java)
          //  myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT )
            this@MainActivity.startActivity(myIntent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            Snackbar.make(view, "Graphical data visualization will be added soon", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
        }
        floatingactionbutton.hide()

        adapter = MyPagerAdapter(supportFragmentManager)
        mpager!!.offscreenPageLimit = 30
        mpager!!.adapter=adapter;
        val pageMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources
                .displayMetrics).toInt()
        mpager!!.pageMargin = pageMargin
        mtabs!!.setViewPager(mpager)

        //add addOnPageChangeListener to viewpager that holds all fragments
        mpager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                println("PAGER"+position)
                when(mpager.currentItem){
                    //and update toolbar TextView to SCAN
                    //Scanner is always stopped after changing tab
                    0->{toolbar_textview.text=getString(R.string.scan)
                        floatingactionbutton.hide()
                    }

                    //read state of current FragmentService and update toolbarTextView textField
                    else->{ if((adapter!!.getItem(mpager.currentItem)as FragmentBleServices).connected==STATE_CONNECTED){
                        toolbar_textview.text=getString(R.string.disconnect)

                    }else if ((adapter!!.getItem(mpager.currentItem)as FragmentBleServices).connected== STATE_DISCONNECTED){
                        toolbar_textview.text=getString(R.string.connect)
                    }else if((adapter!!.getItem(mpager.currentItem)as FragmentBleServices).connected== STATE_DISCONNECTING){
                        Toast.makeText(applicationContext,"Device is disconnecting",Toast.LENGTH_SHORT).show()
                    } else if((adapter!!.getItem(mpager.currentItem)as FragmentBleServices).connected== STATE_CONNECTING){
                        Toast.makeText(applicationContext,"Device is connecting",Toast.LENGTH_SHORT).show()
                    }
                        floatingactionbutton.show()
                    }
                }
            }

        })

        toolbar_textview.setOnClickListener(View.OnClickListener {
            //mpager.currentItem==0 means that FragmentBleDevice is displayed in ViewPager
            //mpager.currentItem>=1 means that FragmentBleService is displayed in ViewPager
            if(mpager.currentItem==0){
                //for currentItem=0 set action to FragmentDevices Object {start or stop scanning}
                if((adapter!!.getItem(0)as FragmentBleDevices).isScanning){
                    //stop scanning BLE devices
                    (adapter!!.getItem(0)as FragmentBleDevices).scanLeDevice(false)
                }else{
                    //start scanning ble devices
                    (adapter!!.getItem(0)as FragmentBleDevices).clearLeDevices()
                    (adapter!!.getItem(0)as FragmentBleDevices).scanLeDevice(true)
                }



            }else{
                //check connection with the BLE device
                if( (adapter!!.getItem(mpager.currentItem)as FragmentBleServices).connected== STATE_CONNECTED){

                    //NOTHING TO DO WHEN DEVICES ARE TRYING TO CONNECT
                    if(!toolbar_textview.text.equals(R.string.connectiong)){
                        //disconnect with the device
                        (adapter!!.getItem(mpager.currentItem)as FragmentBleServices).disconnect()
                        //davice is disconnected so change toolbarTextView to CONNECT
                  //      toolbar_textview.text=getString(R.string.connect)

                    }

                }else if ((adapter!!.getItem(mpager.currentItem)as FragmentBleServices).connected== STATE_DISCONNECTED){
                    //NOTHING TO DO WHEN DEVICES ARE TRYING TO CONNECT

                        //connect to device
                        (adapter!!.getItem(mpager.currentItem)as FragmentBleServices).connect()
                        //device is disconnected so change toolbarTextView to CONNECTING
                        //After the connection, the text will be changed using the function overwriteToolbarTextView() in MainActivity
                 //       toolbar_textview.text=getString(R.string.connectiong)


                }

            }
        })


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                            1)
                }
            }
        }



        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
            finish()
        }



        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

    }




    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(ContentValues.TAG, "coarse location permission granted")
                } else {
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Functionality limited")
                    alertDialog.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.")
                    alertDialog.setPositiveButton(android.R.string.ok, null)
                    alertDialog.setOnDismissListener { }
                    alertDialog.show()
                }
                return
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            System.exit(0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up charProperties, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_about ->
            {Snackbar.make(textView, "Created by Kamil Kolmus ", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_scan -> {
                (adapter!!.getItem(0)as FragmentBleDevices).scanLeDevice(true)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    fun addNewTab(name:String,address:String){

        adapter!!.addFragment(name,address)

    }


    inner class MyPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private val fragments = mutableListOf<Fragment>(FragmentBleDevices())
        private val titles = mutableListOf<String>("Devices")
        override fun getPageTitle(position: Int): CharSequence {
            return titles.get(position)
        }

        override fun getCount(): Int {
            return fragments.size
        }

//        override fun getItemPosition(`object`: Any?): Int {
//            return PagerAdapter.POSITION_NONE
//        }

        override fun getItem(position: Int): Fragment {
            return fragments.get(position)
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            super.destroyItem(container, position, `object`)
        }

        fun addFragment(name:String,address:String){

            var fragment = FragmentBleServices()
            val bundle = Bundle()
            bundle.putString(FragmentBleServices.EXTRAS_DEVICE_NAME, name)
            bundle.putString(FragmentBleServices.EXTRAS_DEVICE_ADDRESS, address)
            bundle.putInt(FragmentBleServices.EXTRAS_TAB_INDEX, fragments.size)
            fragment!!.arguments=bundle

            titles.add(name)
            fragments.add(fragment)

            adapter!!.notifyDataSetChanged()
            mtabs!!.notifyDataSetChanged()

        }

        fun removeFragment(position:Int){
            titles.removeAt(position)
            fragments.removeAt(position)
            adapter!!.notifyDataSetChanged()
            mtabs!!.notifyDataSetChanged()
         //   destroyItem(null,position,null)

        }

    }
    //function to overwrite text i toolbarTextView
    //this function will be called from fragments (FragmentBleDevice and FragmentBleService)
    fun overwriteToolbarTextView(pagePosition:Int,text:String){
        if(pagePosition==mpager.currentItem){
            toolbar_textview.text=text
        }
    }

    fun closeTab(tabId:Int){
        mpager.currentItem=tabId-1
        adapter!!.removeFragment(tabId)

    }

    companion object {
        private val PERMISSION_REQUEST_COARSE_LOCATION = 1
    }
}


