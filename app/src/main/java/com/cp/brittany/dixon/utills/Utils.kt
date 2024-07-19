package com.cp.brittany.dixon.utills

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import com.cp.brittany.dixon.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Arrays
import java.util.Date
import java.util.Locale


object Utils {
    private var photoUri: Uri? = null
    val fileName = "Snap_" + System.currentTimeMillis() / 1000 + ".jpg"


    val oldPermissionsRequired = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    val newPermissionsRequired = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_MEDIA_IMAGES,
    )

    fun createImageFolder(context: Context, path: String): String? {
        val folderPath = context.getExternalFilesDir("")
            ?.absolutePath + "/" + context.getString(R.string.app_name)
        try {
            val file = File("$folderPath/$path")
            if (!file.exists()) file.mkdirs()
            return file.absolutePath
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return folderPath
    }

    fun isNougat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    fun providerPath(context: Context): String {
        return context.packageName + ".fileprovider"
    }


    fun getFilePath(context: Context, contentUri: Uri): String? {
        try {

            val filePathColumn = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
            )

            val returnCursor =
                contentUri.let {
                    context.contentResolver.query(
                        it,
                        filePathColumn,
                        null,
                        null,
                        null
                    )
                }

            if (returnCursor != null) {

                returnCursor.moveToFirst()
                val nameIndex = returnCursor.getColumnIndexOrThrow(
                    OpenableColumns.DISPLAY_NAME
                )
                val name = returnCursor.getString(nameIndex)
                val file = File(context.cacheDir, name)
                val inputStream = context.contentResolver.openInputStream(contentUri)
                val outputStream = FileOutputStream(file)
                var read: Int
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable = inputStream!!.available()

                val bufferSize = Integer.min(
                    bytesAvailable,
                    maxBufferSize
                )
                val buffers = ByteArray(bufferSize)

                while (inputStream.read(buffers).also { read = it } != -1) {
                    outputStream.write(buffers, 0, read)
                }

                inputStream.close()
                outputStream.close()
                return file.absolutePath
            } else {
                Log.d("", "returnCursor is null")
                return null
            }
        } catch (e: Exception) {
            Log.d("", "exception caught at getFilePath(): $e")
            return null
        }
    }

    fun parseDateString(dateString: String): LocalDate {
        // Define the format of your date string
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Parse the date string into a LocalDate object
        return LocalDate.parse(dateString, formatter)
    }

    fun isCurrentMonth(selectedMonth: String): Boolean {
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val currentMonth = formatter.format(currentDate)

        return selectedMonth == currentMonth
    }


    fun convertStringToArrayList(commaSeparatedString: String): ArrayList<String>? {
        // Split the string using the comma as a delimiter
        val itemsArray =
            commaSeparatedString.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()

        // Convert the array to an ArrayList
        return ArrayList(Arrays.asList(*itemsArray))
    }


    fun isDateBeforeCurrent(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()

        try {
            val date = LocalDate.parse(dateString, formatter)
            return date.isBefore(currentDate)
        } catch (e: Exception) {
            // Handle parsing exception
            e.printStackTrace()
        }

        return false
    }

    fun getMonthStartEndDate(currentMonth: String): Pair<String, String> {
        // Parse the input currentMonth string to obtain the year and month
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val firstDayOfMonth = LocalDate.parse(currentMonth + "-01", dateFormatter)

        // Calculate the start and end dates of the month
        val startOfMonth = firstDayOfMonth.format(dateFormatter)
        val endOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1).format(dateFormatter)

        return Pair(startOfMonth, endOfMonth)
    }


    fun isNeedToCompressImage(filepath: String): Boolean {
        val file: File = File(filepath)
        val fileSizeInBytes = file.length()
        val fileSizeInKB: Float = fileSizeInBytes / 1024.0f
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        val fileSizeInMB = fileSizeInKB / 1024.0f
        Log.i("file_size->", fileSizeInMB.toString())
        return fileSizeInMB > 1
    }

    fun fileSize(file: File?): Boolean {
        val fileSizeInBytes = file?.length() ?: 0
        val fileSizeInKB: Float = fileSizeInBytes / 1024.0f
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        val fileSizeInMB = fileSizeInKB / 1024.0f
        Log.i("file_size->", fileSizeInMB.toString())
        return fileSizeInMB > 1
    }

    fun convertStringToDate(dateString: String?): LocalDate? {
        // Define the date pattern of the input string
        val formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Parse the string to a LocalDate object
        return LocalDate.parse(dateString, formatter)
    }

    fun formatTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        val hours = (milliseconds / (1000 * 60 * 60)) % 24

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun convertToMilliseconds(timeString: String): Long {
        val parts = timeString.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val seconds = parts[2].toInt()

        val totalMilliseconds = hours * 3600000L + minutes * 60000L + seconds * 1000L
        return totalMilliseconds
    }

    fun getFcmToken(): String {
        var fcmToken: String? = null
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("SignUp FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            fcmToken = task.result

            // Log and toast
            Log.d("SignUp FCM", fcmToken ?: "failed")
        })
        return fcmToken ?: ""
    }

    fun validatePassword(input: String) = when {
        input.isBlank() -> {
            "Password is Required Field"
        }

        input.length < 6 -> {
            "Lenght of password should be greater than six"
        }

        input.length > 15 -> {
            "Lenght of password should be less than 15"
        }

        else -> {
            "Valid"
        }
    }

    fun validate(username: String, email: String, password: String, age: Int): String? {
        if (username.isEmpty()) return "please enter username"
        if (username.length < 6) return "very short username"
        if (username.length > 20) return "long username"

        if (email.isEmpty()) return "please enter email"
        if (!email.contains("@")) return "please enter valid email"
        if (email.filter { it.isDigit() }.isEmpty()) return "email must contain at least one digit"

        if(password.isEmpty()) return "please enter password"
        if(password.length<6)return "please enter valid password"

        if(age==0) return "please enter valid age"
        if(age<18) return "not eligible"

        return null

    }


    fun add(a: Int, b: Int): Int {
        return a + b
    }

}

