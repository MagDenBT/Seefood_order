package ch.magdenbt.seefoodjyordertakingapp.customviews

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import ch.magdenbt.seefoodjyordertakingapp.R
import ch.magdenbt.seefoodjyordertakingapp.databinding.GeoAndDateViewBinding

typealias OnGeoAndDateListener = () -> Unit

class GeoAndDateView(
    context: Context,
    attrs: AttributeSet? ,
    defStyleAttr: Int ,
    defStyleRes: Int ,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: GeoAndDateViewBinding
    private var cityText: String
        set(value) {
            binding.city.text = value
        }
        get() = binding.city.text.toString()

    private var dateText: String
        set(value) {

            binding.date.text = value
        }
        get() = binding.date.text.toString()

    private var listener: OnGeoAndDateListener? = null

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : this(context, attrs, defStyleAttr, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?,

    ) : this(context, attrs, R.attr.geoAndDateStyle, 0)

    constructor(
        context: Context,

    ) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.geo_and_date_view, this, true)
        binding = GeoAndDateViewBinding.bind(this)
        initializeAttributes(attrs, defStyleAttr, defStyleRes)
        initListeners()
    }

    private fun initializeAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.GeoAndDateView,
            defStyleAttr,
            defStyleRes,
        )

        cityText = typedArray.getString(R.styleable.GeoAndDateView_cityText) ?: ""
        dateText = typedArray.getString(R.styleable.GeoAndDateView_dateText) ?: ""

        val drawableResId = typedArray.getResourceId(R.styleable.GeoAndDateView_geoIcon, -1)
        AppCompatResources.getDrawable(context, drawableResId)?.let {
            binding.gpsIcon.setImageDrawable(it)
        }

        typedArray.recycle()
    }

    private fun initListeners() {
        binding.date.setOnClickListener { this.listener?.invoke() }
        binding.gpsIcon.setOnClickListener { this.listener?.invoke() }
        binding.city.setOnClickListener { this.listener?.invoke() }
    }

    fun setListener(listener: OnGeoAndDateListener) {
        this.listener = listener
    }

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            it.cityText = cityText
            it.dateText = dateText
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(state.superState)
        cityText = savedState.cityText ?: ""
        dateText = savedState.dateText ?: ""
    }

    class SavedState : BaseSavedState {

        var dateText: String? = null
        var cityText: String? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(parcel: Parcel) : super(parcel) {
            dateText = parcel.readString()
            cityText = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(dateText)
            out.writeString(cityText)
        }
    }
}
