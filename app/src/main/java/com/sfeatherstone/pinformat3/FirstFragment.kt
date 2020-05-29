package com.sfeatherstone.pinformat3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.sfeatherstone.pinformat3.pinConversion.Iso3Pin
import kotlinx.android.synthetic.main.fragment_first.*
import org.koin.androidx.viewmodel.ext.android.viewModel

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val viewModel by viewModel<PinViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    private fun showResult(result: Iso3Pin.Result) {
        when(result) {
            is Iso3Pin.Result.Success -> {
                for (b in result.pinBlock) {
                    val st = String.format("%02X", b)
                    print(st)
                }
                pin3text.text = result.pinBlock.toHexString()
                resultStatusText.text = "Success"
            }
            is Iso3Pin.Result.Fail -> {
                pin3text.text = ""
                resultStatusText.text = "Fail: ${result.reason.name}"
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.iso3PinState.observe(viewLifecycleOwner, Observer { showResult(it) } )

        pinEntry.doAfterTextChanged{
            viewModel.updatePin(it.toString(), "1111222233334444")
        }

        pinEntry.requestFocus()
    }
}