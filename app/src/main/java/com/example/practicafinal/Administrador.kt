package com.example.practicafinal

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.atomic.AtomicInteger

class Administrador : AppCompatActivity() {
    lateinit var navegation: BottomNavigationView

    private val mOnNavMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.cartas -> {
                supportFragmentManager.commit {
                    replace<CartasFragment>(R.id.fragment_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.eventos -> {
                supportFragmentManager.commit {
                    replace<EventosFragment>(R.id.fragment_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
                R.id.pedidos -> {
                    supportFragmentManager.commit {
                        replace<PedidosFragment>(R.id.fragment_container)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                    return@OnNavigationItemSelectedListener true
                }

        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador)
        supportActionBar?.hide()

        navegation = findViewById(R.id.navMenu)
        navegation.setOnNavigationItemSelectedListener(mOnNavMenu)

        supportFragmentManager.commit {
            replace<CartasFragment>(R.id.fragment_container)
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }


    }



    override fun onBackPressed() {
        cerrarApp()
    }
    fun cerrarApp() {
        this.finishAffinity()
    }
}