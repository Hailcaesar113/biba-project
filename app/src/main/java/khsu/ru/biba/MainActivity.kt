package khsu.ru.biba

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import net.glxn.qrgen.android.QRCode
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.util.Random

class MainActivity : AppCompatActivity() {
    private var mEditCode: EditText? = null
    private var mButtonCreate: Button? = null
    private var mButtonScreen: Button? = null
    private var mImagePreview: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEditCode = findViewById(R.id.editCode) as EditText
        mButtonCreate = findViewById(R.id.buttonCreate) as Button
        mButtonScreen = findViewById(R.id.buttonScreen) as Button
        mImagePreview = findViewById(R.id.imagePreview) as ImageView

        (mButtonCreate as Button).setOnClickListener {
            val text = (mEditCode as EditText).text.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, getString(R.string.hint_enter_text_to_create_barcode),
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bitmap = QRCode.from(text).withSize(1000, 1000).bitmap()
            (mImagePreview as ImageView).setImageBitmap(bitmap)
            hideKeyboard()
        }

        (mButtonScreen as Button).setOnClickListener {
            val bitmap = loadBitmapFromView(findViewById(R.id.imagePreview), 1000, 1000)
            saveImage(bitmap)
        }
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun

            loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(0, 0, v.layoutParams.width, v.layoutParams.height)
        v.draw(c)
        return b
    }

    fun saveImage(bitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File(root + "/req_images")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        if (file.exists())
            file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
