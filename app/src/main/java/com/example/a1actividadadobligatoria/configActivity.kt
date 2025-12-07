package com.example.a1actividadadobligatoria

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a1actividadadobligatoria.databinding.ActivityConfigBinding
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

class configActivity : AppCompatActivity() {

    private lateinit var confBinding : ActivityConfigBinding

    private lateinit var sharedFich : SharedPreferences

    private lateinit var nameSharedPhone : String

    private lateinit var nameSharedNombre : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        confBinding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(confBinding.mainConfig)

        init()
        start()
    }



    private fun init(){
        val nameSharedFich = getString(R.string.name_shared_fich)
        this.nameSharedPhone = getString(R.string.name_shared_phone)
        this.nameSharedNombre = getString(R.string.name_shered_name)

        //abrir el fichero de preferencias compartidad
        this.sharedFich = getSharedPreferences(nameSharedFich, MODE_PRIVATE)

        radios()
    }

    private fun start(){
        /// buscamos el telefono
        val sharedPhone : String? = sharedFich.getString(nameSharedPhone,  null)
        val sharedName : String? = sharedFich.getString(nameSharedNombre, null)

        sharedPhone?.let{ phone ->
            sharedName?.let{ name ->
                startMainActivity(phone, name)
            }
        }
        //boton de confirmacion de datos
        confBinding.btnAceptar.setOnClickListener {
            val numberPhone = confBinding.editPhone.text.toString()
            val nameUser = confBinding.editNameUser.text.toString()
            if(numberPhone.isEmpty() && nameUser.isEmpty()){
                Toast.makeText(this, R.string.msg_empty_namephoner, Toast.LENGTH_LONG).show()
            }else if (numberPhone.isEmpty()){
                Toast.makeText(this, R.string.name_shared_phone, Toast.LENGTH_LONG).show()
            }else if(nameUser.isEmpty()){
                Toast.makeText(this, R.string.name_shered_name, Toast.LENGTH_LONG).show()
            }else{
                if(!isValidPhoneNumber(numberPhone, "ES")){
                    Toast.makeText(this, R.string.msg_not_valid_number, Toast.LENGTH_LONG).show()
                }else{
                    //registramos el telefono verificado
                    val edit = sharedFich.edit()
                    edit.putString(nameSharedPhone, numberPhone)
                    edit.putString(nameSharedNombre, nameUser)
                    edit.apply()
                    startMainActivity(numberPhone, nameUser)
                }
            }


        }





    }

    //metodo para lanzar el activity principal pasandole los datos necesarios
    private fun startMainActivity(phone: String, name: String){
        val intent = Intent(this@configActivity, pantallaprincipal::class.java)

        intent.apply{
            putExtra(getString(R.string.string_phone), phone) //pasamos informacion
            putExtra(getString(R.string.name_shered_name), name)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP) //limpiamos la pila
        }
        startActivity(intent) // lanzamos el activity
    }

    private fun radios(){
        confBinding.radioSi.setOnClickListener {
            confBinding.racioNo.isChecked = false
        }
        confBinding.racioNo.setOnClickListener {
            confBinding.radioSi.isChecked = false
        }
    }






    //clase de Android que valida un teléfono.

    fun isValidPhoneNumber(phoneNumber: String, countryCode: String): Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance()
        return try {
            // Parseamos el número de teléfono en base al código de país proporcionado
            val number = phoneUtil.parse(phoneNumber, countryCode)
            // Verificamos si es un número válido para el país especificado
            phoneUtil.isValidNumber(number)
        } catch (e: NumberParseException) {
            e.printStackTrace()
            false
        }
    }








}