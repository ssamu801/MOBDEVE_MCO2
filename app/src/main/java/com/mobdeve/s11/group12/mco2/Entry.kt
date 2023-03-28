package com.mobdeve.s11.group12.mco2

class Entry {
    var id: Long = 0
        private set
    var entryLocation: String
        private set
    var entryDate: String
        private set
    var entryContent: String
        private set
    var imageUri: String
        private set

    constructor(entryLocation: String, entryDate: String, entryContent: String, imageUri: String, id: Long) {
        this.entryLocation = entryLocation
        this.entryDate = entryDate
        this.entryContent = entryContent
        this.imageUri = imageUri
        this.id = id
    }
}