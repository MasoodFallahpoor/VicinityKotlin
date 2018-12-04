package ir.fallahpoor.vicinity.presentation.common

import android.content.Context
import ir.fallahpoor.vicinity.data.R
import java.io.IOException
import javax.inject.Inject

class ExceptionParser @Inject
constructor(private val context: Context) {

    fun parseException(throwable: Throwable): String =
        when (throwable) {
            is IOException -> context.resources.getString(R.string.server_unreachable)
            else -> context.resources.getString(R.string.error_unknown)
        }

}