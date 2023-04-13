package com.mobdeve.s11.group12.mco2

class Entry {
    var id: Long = 0
        private set
    var latitude: Double = 0.0
        private set
    var longitude: Double = 0.0
        private set
    var entryLocation: String
        private set
    var entryDate: String
        private set
    var entryNotes: String
        private set
    var imageUri: String
        private set

    constructor(latitude: Double, longitude: Double, entryLocation: String, entryDate: String, entryNotes: String, imageUri: String) {
        this.latitude = latitude
        this.longitude = longitude
        this.entryLocation = entryLocation
        this.entryDate = entryDate
        this.entryNotes = entryNotes
        this.imageUri = imageUri
    }
    constructor(latitude: Double, longitude: Double, entryLocation: String, entryDate: String, entryNotes: String, imageUri: String, id: Long) {
        this.latitude = latitude
        this.longitude = longitude
        this.entryLocation = entryLocation
        this.entryDate = entryDate
        this.entryNotes = entryNotes
        this.imageUri = imageUri
        this.id = id
    }

    override fun toString(): String {
        return "Contact{" +
                "id=" + id +
                ", location='" + entryLocation + '\'' +
                ", date='" + entryDate + '\'' +
                ", notes='" + entryNotes + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}'
    }
}