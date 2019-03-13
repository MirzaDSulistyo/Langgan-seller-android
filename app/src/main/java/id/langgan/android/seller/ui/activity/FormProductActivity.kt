package id.langgan.android.seller.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import id.langgan.android.seller.R
import timber.log.Timber

class FormProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_product)

        // Handle Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = getString(R.string.new_product)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.form, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_save -> {
                finish()
                return true
            }

            android.R.id.home -> {
                Timber.d("home")
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
