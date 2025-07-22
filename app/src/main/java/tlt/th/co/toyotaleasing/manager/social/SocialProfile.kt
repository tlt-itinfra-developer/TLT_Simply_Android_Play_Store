package tlt.th.co.toyotaleasing.manager.social

data class SocialProfile(val id: String = "",
                         val displayName: String = "",
                         val familyName : String = "",
                         val givenName : String = "",
                         val email: String = "",
                         val phonenumber: String = "",
                         val imageProfileUrl: String = "",
                         val accessToken: String = "",
                         val statusMessage : String = "",
                         val refSocial: String = "") {
    companion object {
        val EMAIL = "NATIVE"
        val FACEBOOK = "FACEBOOK"
        val LINE = "LINE"
        val GOOGLE = "GOOGLE"
    }
}