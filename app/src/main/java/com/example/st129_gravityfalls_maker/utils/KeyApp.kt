package com.example.st129_gravityfalls_maker.utils

object KeyApp {
    const val DOWNLOAD_ALBUM = "GravityFalls Maker"
    const val AVATAR = "avatar"

    object KeyIntent {
        const val INTENT_KEY = "INTENT_KEY"
        const val INTENT_KEY_LANG = "INTENT_KEY_LANG"
        const val FROM_SAVE = "from_suc"
        const val PATH_KEY = "PATH_KEY"
        const val STATUS_KEY = "STATUS_KEY"
        const val FROM_SETTINGS = "FROM_SETTINGS"
    }

    object KeySharePreference {
        const val FIRST_LANG = "first_lang"
        const val FIRST_LANG_KEY = "first_access_lang"

        const val FIRST_PERMISSION = "first_permission"
        const val FIRST_PERMISSION_KEY = "first_access_permission"

        const val KEY_LANGUAGE = "KEY_LANGUAGE"

        const val RATE = "rate"
        const val RATE_KEY = "rate_5_star"

        const val COUNT_BACK = "count_back"
        const val COUNT_BACK_KEY = "back_count"

    }

    object KeyPermission {
        const val IS_STORAGE = "IS_STORAGE"
        const val STORAGE_KEY = "STORAGE_KEY"
        const val IS_NOTIFICATION = "IS_NOTIFICATION"
        const val NOTIFICATION_KEY = "NOTIFICATION_KEY"
    }

    object RequestCode {
        const val STORAGE_PERMISSION_CODE = 999
        const val NOTIFICATION_PERMISSION_CODE = 997
        const val PICK_IMAGE_REQUEST_CODE = 103
    }

    object KeyAssets {
        const val ASSET_MANAGER = "file:///android_asset"
        const val DATA_ASSET = "file:///android_asset/data/"
        const val AVATAR_ASSET = "avatar"

        const val DATA = "data"

        const val FIRST_PNG = "1.png"
        const val FIRST_JPG = "1.jpg"
        const val FIRST_WEBP = "1.webp"
    }

    object ValueApp {
        const val VIEW = 0
        const val SUCCESSFUL = 1
    }

    object DomainAPI {
        const val BASE_URL = "https://lvtglobal.tech"
        const val BASE_URL_PREVENTIVE = "https://lvtglobal.site"
        const val SUB_DOMAIN = "/public/app/MechanoidMaker"
    }

    object NameFolderLayer {
        const val BODY = "body"
        const val HEAD = "head"
        const val EYE = "eye"
        const val EYELEFT = "eyeleft"
        const val EYERIGHT = "eyeright"
        const val EYELASH = "eyelash"
        const val IRISLEFT = "irisleft"
        const val IRISRIGHT = "irisright"
        const val MOUTH = "mouth"
        const val NOSE = "nose"
        const val BEARD = "beard"
        const val EYEBROW = "eyebrow"
        const val EARLEFT1 = "earleft1"
        const val EARLEFT2 = "earleft2"
        const val EAR = "ear"
        const val MASK = "mask"
        const val LEG = "leg"
        const val FRONTHAIR = "fronthair"
        const val SIDEHAIR = "sidehair"
        const val BACKHAIR = "backhair"
        const val ENDHAIR = "endhair"
        const val ENDHAIRRIGHT = "endhairright"
        const val ENDHAIRLEFT = "endhairleft"
        const val SHOULDERRIGHT = "shoulderright"
        const val SHOULDERLEFT = "shoulderleft"
        const val HANDRIGHT = "handright"
        const val HANDLEFT = "handleft"
        const val HAND = "hand"
        const val SLEEVE = "sleeve"
        const val CHIN = "chin"
        const val TROUSER = "trouser"
        const val SHIRT = "shirt"
        const val JACKET = "jacket"
        const val SHOES = "shoes"
        const val BODYPAINT = "bodypaint"
        const val FACEPAINT = "facepaint"
        const val EARRIGHT1 = "earright1"
        const val EARRIGHT2 = "earright2"
        const val LAPELPIN = "lapelpin"
        const val PIN = "pin"
        const val NECKLACE = "necklace"
        const val COLLAR = "collar"
        const val HAT = "hat"
        const val HORN = "horn"
        const val ACCESSORIES = "accessories"
        const val GLASS = "glass"
        const val OTHER = "other"
        const val EFFECT = "effect"
        const val BG = "background"
        const val WING = "wing"
    }

    object PositionLayer {
        const val QUANTITY_LAYER = 50

        const val NAV_BODY = 0
        const val NAV_HEAD = 1
        const val NAV_EYE = 2
        const val NAV_EYELEFT = 3
        const val NAV_EYERIGHT = 4
        const val NAV_EYELASH = 5
        const val NAV_IRISLEFT = 6
        const val NAV_IRISRIGHT = 7
        const val NAV_MOUTH = 8
        const val NAV_NOSE = 9
        const val NAV_BEARD = 10
        const val NAV_EYEBROW = 11
        const val NAV_EARLEFT1 = 12
        const val NAV_EARLEFT2 = 13
        const val NAV_EAR = 14
        const val NAV_MASK = 15
        const val NAV_LEG = 16
        const val NAV_FRONTHAIR = 17
        const val NAV_SIDEHAIR = 18
        const val NAV_BACKHAIR = 19
        const val NAV_ENDHAIR = 20
        const val NAV_ENDHAIRRIGHT = 21
        const val NAV_ENDHAIRLEFT = 22
        const val NAV_SHOULDERRIGHT = 23
        const val NAV_SHOULDERLEFT = 24
        const val NAV_HANDRIGHT = 25
        const val NAV_HANDLEFT = 26
        const val NAV_HAND = 27
        const val NAV_SLEEVE = 28
        const val NAV_CHIN = 29
        const val NAV_TROUSER = 30
        const val NAV_SHIRT = 31
        const val NAV_JACKET = 32
        const val NAV_SHOES = 33
        const val NAV_BODYPAINT = 34
        const val NAV_FACEPAINT = 35
        const val NAV_EARRIGHT1 = 36
        const val NAV_EARRIGHT2 = 37
        const val NAV_LAPELPIN = 38
        const val NAV_PIN = 39
        const val NAV_NECKLACE = 40
        const val NAV_COLLAR = 41
        const val NAV_HAT = 42
        const val NAV_HORN = 43
        const val NAV_ACCESSORIES = 44
        const val NAV_GLASS = 45
        const val NAV_OTHER = 46
        const val NAV_EFFECT = 47
        const val NAV_BG = 48
        const val NAV_WING = 49


        const val CUSTOM_BG = 0
        const val CUSTOM_EFFECT = 1
        const val CUSTOM_WING = 2
        const val CUSTOM_ENDHAIRLEFT = 3
        const val CUSTOM_ENDHAIRRIGHT = 4
        const val CUSTOM_ENDHAIR = 5
        const val CUSTOM_BACKHAIR = 6
        const val CUSTOM_EARLEFT1 = 7
        const val CUSTOM_EARLEFT2 = 8
        const val CUSTOM_EAR = 9
        const val CUSTOM_HANDLEFT = 10
        const val CUSTOM_SHOULDERLEFT = 11
        const val CUSTOM_BODY = 12
        const val CUSTOM_HEAD = 13
        const val CUSTOM_BODYPAINT = 14
        const val CUSTOM_FACEPAINT = 15
        const val CUSTOM_NECKLACE = 16
        const val CUSTOM_BEARD = 17
        const val CUSTOM_EYEBROW = 18
        const val CUSTOM_CHIN = 19
        const val CUSTOM_LEG = 20
        const val CUSTOM_SHOES = 21
        const val CUSTOM_HANDRIGHT = 22
        const val CUSTOM_SHOULDERRIGHT = 23
        const val CUSTOM_NOSE = 24
        const val CUSTOM_EYE = 25
        const val CUSTOM_EYELEFT = 26
        const val CUSTOM_EYERIGHT = 27
        const val CUSTOM_EYELASH = 28
        const val CUSTOM_IRISLEFT = 29
        const val CUSTOM_IRISRIGHT = 30
        const val CUSTOM_MOUTH = 31
        const val CUSTOM_MASK = 32
        const val CUSTOM_GLASS = 33
        const val CUSTOM_EARRIGHT1 = 34
        const val CUSTOM_EARRIGHT2 = 35
        const val CUSTOM_SIDEHAIR = 36
        const val CUSTOM_FRONTHAIR = 37
        const val CUSTOM_SLEEVE = 38
        const val CUSTOM_TROUSER = 39
        const val CUSTOM_SHIRT = 40
        const val CUSTOM_JACKET = 41
        const val CUSTOM_LAPELPIN = 42
        const val CUSTOM_PIN = 43
        const val CUSTOM_COLLAR = 44
        const val CUSTOM_HAT = 45
        const val CUSTOM_HORN = 46
        const val CUSTOM_ACCESSORIES = 47
        const val CUSTOM_OTHER = 48
        const val CUSTOM_HAND = 49

    }
}