import android.os.Parcel
import android.os.Parcelable

data class Even(
    val titulo: String,
    val nombre: String,
    val ubicacionTexto: String,
    val descripcion: String,
    val imagenUrl: String?,
    val ubicacionLat: Double?,
    val ubicacionLng: Double?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(titulo)
        parcel.writeString(nombre)
        parcel.writeString(ubicacionTexto)
        parcel.writeString(descripcion)
        parcel.writeString(imagenUrl)
        parcel.writeValue(ubicacionLat)
        parcel.writeValue(ubicacionLng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Even> {
        override fun createFromParcel(parcel: Parcel): Even {
            return Even(parcel)
        }

        override fun newArray(size: Int): Array<Even?> {
            return arrayOfNulls(size)
        }
    }
}