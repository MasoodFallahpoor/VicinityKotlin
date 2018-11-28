package ir.fallahpoor.vicinity.common

import android.content.Context
import ir.fallahpoor.vicinity.data.R
import java.io.IOException
import javax.inject.Inject

class ExceptionHandler @Inject
constructor(private val context: Context) {

    fun parseException(throwable: Throwable): Error {

        val resources = context.resources

        return if (throwable is IOException) {
            Error(resources.getString(R.string.server_unreachable))
        } else {
            Error(resources.getString(R.string.error_unknown))
        }

    }

}