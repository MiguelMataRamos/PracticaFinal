package com.example.practicafinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class Cliente : AppCompatActivity() {
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
            R.id.usuario -> {
                supportFragmentManager.commit {
                    replace<UserFragment>(R.id.fragment_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    //cuando se pulse el boton atras del dispositivo se cierra la app
    override fun onBackPressed() {
        cerrarApp()
    }
    fun cerrarApp() {
        this.finishAffinity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        supportActionBar?.hide()

        navegation = findViewById(R.id.navMenu)
        navegation.setOnNavigationItemSelectedListener(mOnNavMenu)

        supportFragmentManager.commit {
            replace<CartasFragment>(R.id.fragment_container)
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }
    }
}