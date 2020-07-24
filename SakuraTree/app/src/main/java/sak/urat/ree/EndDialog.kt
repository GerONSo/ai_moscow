package sak.urat.ree

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class EndDialog(val openMainMenu: () -> Unit, val score: Int) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder
            .setMessage("You are failed, try again\n\nYou caught $score flowers")
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
                openMainMenu.invoke()
            }
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}
