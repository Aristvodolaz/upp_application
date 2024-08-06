package com.example.timetrekerforandroid.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.DialogFragment
import com.example.timetrekerforandroid.R

class ApproveSrokGodnostiDialog(private val percent: String,
                                private val onSendApproveOnSrokGodnosti: OnSendApproveOnSrokGodnosti? = null)
    : DialogFragment() {


    interface OnSendApproveOnSrokGodnosti {
        fun sendApprove(approve: Boolean)
    }

    companion object {
        const val TAG = "ApproveSrokGodnostiDialog"

        fun newInstance(percent: String, callback: OnSendApproveOnSrokGodnosti): ApproveSrokGodnostiDialog {
            return ApproveSrokGodnostiDialog(percent, callback)
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        builder.setTitle("Срок годности")
            .setMessage("Срок годности продукта составляет: $percent \n Продолжить выкладку?")
            .setNegativeButton(Html.fromHtml("<font color='#000000'>Нет</font>")) { dialog, _ ->
                onSendApproveOnSrokGodnosti?.sendApprove(false)
                dialog.cancel()
            }
            .setPositiveButton(Html.fromHtml("<font color='#F31616'>Да</font>")) { dialog, _ ->
                onSendApproveOnSrokGodnosti?.sendApprove(true)
                dialog.cancel()
            }

        return builder.create()
    }
}
