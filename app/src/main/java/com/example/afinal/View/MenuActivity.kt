package com.example.afinal.View

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.afinal.R


class MenuActivity: AppCompatActivity() {
    //private lateinit var binding: ActivityBinding
    //private lateinit var toggle: ActionBarDrawerToggle

    //override fun onCreate(savedInstanceState: Bundle?) {
       // super.onCreate(savedInstanceState)
        //binding = ActivityBinding.inflate(layoutInflater)
       // setContentView(binding.root)

        //configuracion del tolbar de la parte inferior
        //toggle = ActionBarDrawerToggle(this, binding.root, R.string.usuario, R.string.home, R.string.reseña)
        //binding.root.addDrawerListener(toggle)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //binding.navView.setNavigationItemSelectedListener {
           // when (it.itemId){
                //R.id.event -> {
                   // setFragment(TaskActivity())
                   // Toast.makeText(this, "Eventos", Toast.LENGTH_SHORT).show()
               /// }
                //R.id.ñ_event -> {
                   // setFragment(TaskActivity())
                    //Toast.makeText(this, "Añadir_Eventos", Toast.LENGTH_SHORT).show()
               // }
               // R.id.h_event -> {
                   // setFragment(TaskActivity())
                    //Toast.makeText(this, "Historial de Eventos", Toast.LENGTH_SHORT).show()
                //}
                //R.id.ubi -> {
                   // setFragment(TaskActivity())
                   // Toast.makeText(this, "cambio de ubicacion ", Toast.LENGTH_SHORT).show()
               // }

               // else -> false
            //}

           // return@setNavigationItemSelectedListener true
        //}

        //binding.bottomNav.setOnItemSelectedListener {
          //  when (it.itemId){
              //  R.id.perfil-> setFragment(PerfilFragment())
               // R.id.inicio-> setFragment(InicioFragment())
               // R.id.rese-> setFragment(ReseñaFragment())

           // }
           // return@setOnItemSelectedListener true
       // }

   // }

    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentFl, fragment)
            .commit()
    }



}