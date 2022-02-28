package com.mauricio.marvel.characters.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CharactersResult (
    val code: Long,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: Data
)

data class Data (
    val offset: Long,
    val limit: Long,
    val total: Long,
    val count: Long,
    val results: List<Character>
)

@Parcelize
data class Character (
    val id: Long?,
    val name: String?,
    val description: String?,
    val modified: String?,
    val thumbnail: Thumbnail?,
    val resourceURI: String?,
    val comics: Comics?,
    val series: Comics?,
    val stories: Stories?,
    val events: Comics?,
    val urls: List<URL>?,
    val title: String?,
    val start: String?,
    val end: String?,
    val creators: Comics?,
    val characters: Comics?,
    val next: ComicsItem?,
    val previous: ComicsItem?

): Parcelable {

    val imageUrl get() = "${thumbnail?.path}.${thumbnail?.extension}".replace("http://", "https://")
    val getNameFormatted get() = if (!name.isNullOrEmpty()) name else title ?: ""
}

data class Comics (
    val available: Long?,
    val collectionURI: String?,
    val items: List<ComicsItem>?,
    val returned: Long?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.createTypedArrayList(ComicsItem),
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(available)
        parcel.writeString(collectionURI)
        parcel.writeTypedList(items)
        parcel.writeValue(returned)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comics> {
        override fun createFromParcel(parcel: Parcel): Comics {
            return Comics(parcel)
        }

        override fun newArray(size: Int): Array<Comics?> {
            return arrayOfNulls(size)
        }
    }
}

data class ComicsItem (
    val resourceURI: String?,
    val name: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(resourceURI)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComicsItem> {
        override fun createFromParcel(parcel: Parcel): ComicsItem {
            return ComicsItem(parcel)
        }

        override fun newArray(size: Int): Array<ComicsItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class Stories (
    val available: Long,
    val collectionURI: String?,
    val items: List<StoriesItem>?,
    val returned: Long
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.createTypedArrayList(StoriesItem),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(available)
        parcel.writeString(collectionURI)
        parcel.writeTypedList(items)
        parcel.writeLong(returned)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Stories> {
        override fun createFromParcel(parcel: Parcel): Stories {
            return Stories(parcel)
        }

        override fun newArray(size: Int): Array<Stories?> {
            return arrayOfNulls(size)
        }
    }
}

data class StoriesItem (
    val resourceURI: String?,
    val name: String?,
    val type: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(resourceURI)
        parcel.writeString(name)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StoriesItem> {
        override fun createFromParcel(parcel: Parcel): StoriesItem {
            return StoriesItem(parcel)
        }

        override fun newArray(size: Int): Array<StoriesItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class Thumbnail (
    val path: String?,
    val extension: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(extension)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Thumbnail> {
        override fun createFromParcel(parcel: Parcel): Thumbnail {
            return Thumbnail(parcel)
        }

        override fun newArray(size: Int): Array<Thumbnail?> {
            return arrayOfNulls(size)
        }
    }
}

data class URL (
    val type: String?,
    val url: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<URL> {
        override fun createFromParcel(parcel: Parcel): URL {
            return URL(parcel)
        }

        override fun newArray(size: Int): Array<URL?> {
            return arrayOfNulls(size)
        }
    }
}

const val EXTRA_CHARACTER = "9c415f49bc0dcf8b3fe1e3a9ab1d7a1ae1a25739"

