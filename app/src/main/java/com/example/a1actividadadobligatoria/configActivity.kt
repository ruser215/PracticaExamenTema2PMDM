package com.example.a1actividadadobligatoria

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a1actividadadobligatoria.databinding.ActivityConfigBinding
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Calendar

class configActivity : AppCompatActivity() {

    private lateinit var confBinding : ActivityConfigBinding

    private lateinit var sharedFich : SharedPreferences

    private lateinit var nameSharedPhone : String

    private lateinit var nameSharedNombre : String

    private lateinit var nameSharedBirthdate : String


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
        this.nameSharedBirthdate = "user_birthdate"

        //abrir el fichero de preferencias compartidad
        this.sharedFich = getSharedPreferences(nameSharedFich, MODE_PRIVATE)

        radios()
        setupBirthdatePicker()
    }

    private fun start(){
        /// buscamos el telefono
        val sharedPhone : String? = sharedFich.getString(nameSharedPhone,  null)
        val sharedName : String? = sharedFich.getString(nameSharedNombre, null)
        val sharedBirthdate : String? = sharedFich.getString(nameSharedBirthdate, null)


        sharedPhone?.let{ phone ->
            sharedName?.let{ name ->
                sharedBirthdate?.let { birthdate ->
                    startMainActivity(phone, name, birthdate)
                }
            }
        }
        //boton de confirmacion de datos
        confBinding.btnAceptar.setOnClickListener {
            val numberPhone = confBinding.editPhone.text.toString()
            val nameUser = confBinding.editNameUser.text.toString()
            val birthdate = confBinding.editBirthdate.text.toString()

            if(numberPhone.isEmpty() && nameUser.isEmpty()){
                Toast.makeText(this, R.string.msg_empty_namephoner, Toast.LENGTH_LONG).show()
            }else if (numberPhone.isEmpty()){
                Toast.makeText(this, R.string.name_shared_phone, Toast.LENGTH_LONG).show()
            }else if(nameUser.isEmpty()){
                Toast.makeText(this, R.string.name_shered_name, Toast.LENGTH_LONG).show()
            }else if (birthdate.isEmpty()) {
                Toast.makeText(this, "Por favor, selecciona tu fecha de nacimiento", Toast.LENGTH_LONG).show()
            }else{
                if(!isValidPhoneNumber(numberPhone, "ES")){
                    Toast.makeText(this, R.string.msg_not_valid_number, Toast.LENGTH_LONG).show()
                }else{
                    //registramos el telefono verificado
                    val edit = sharedFich.edit()
                    edit.putString(nameSharedPhone, numberPhone)
                    edit.putString(nameSharedNombre, nameUser)
                    edit.putString(nameSharedBirthdate, birthdate)
                    edit.apply()
                    startMainActivity(numberPhone, nameUser, birthdate)
                }
            }


        }
    }

    //metodo para lanzar el activity principal pasandole los datos necesarios
    private fun startMainActivity(phone: String, name: String, birthdate: String){
        val intent = Intent(this@configActivity, pantallaprincipal::class.java)

        intent.apply{
            putExtra(getString(R.string.string_phone), phone) //pasamos informacion
            putExtra(getString(R.string.name_shered_name), name)
            putExtra("user_birthdate", birthdate)
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

    private fun setupBirthdatePicker() {
        confBinding.editBirthdate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, {
                _, selectedYear, selectedMonth, selectedDay ->
                val birthdate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                confBinding.editBirthdate.setText(birthdate)
            }, year, month, day)
            datePickerDialog.show()
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
