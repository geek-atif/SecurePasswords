package com.atif.qamar.securepassword.util


/**
 * Created by Atif Qamar on 26-05-2020.
 */
class Constant {
    companion object {
        const val PASSWORD_DATA_TABLE_NAME = "tbl_pwd_data"
        const val PASSWORD_LOCK_SCREEN_TABLE_NAME = "tbl_lock_screen"
        var SOCIAL = "Social"
        var SHOPPING = "Shopping"
        var TRAVEL = "Travel"
        var WORK = "Work"
        var PERSONAL = "Personal"
        var OTHER = "Other"

        val LIST_OF_PASSWORD_TYPE_ = listOf<String>(
            SOCIAL, SHOPPING,
            TRAVEL, WORK, PERSONAL, OTHER
        )

        val LIST_OF_SOCIAL_ =
            listOf<String>("Linkedin", "Facebook", "Instagram", "Twitter", "Whatsapp")

        val LIST_OF_SHOPPING_ = listOf<String>("Flipkart", "Amazon", "Ebay", "Myntra")
        val LIST_OF_TRAVEL_ = listOf<String>("Makemytrip", "Trivago", "Cleartrip", "easemytrip")
        val LIST_OF_WORK_ = listOf<String>("Gmail", "Outlook", "Zoho")

        const val PASSWORD_HASH = "password_hash"
    }
}