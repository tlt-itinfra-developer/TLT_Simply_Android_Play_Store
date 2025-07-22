package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.social.SocialProfile

class SocialRegisterRequest {

    @SerializedName("PSELECT")
    @Expose
    var TYPE = ""
    @SerializedName("PKEY1")
    @Expose
    var PKEY1 = ""
    @SerializedName("PKEY2")
    @Expose
    var PKEY2 = ""
    @SerializedName("PKEY3")
    @Expose
    var PKEY3 = ""
    @SerializedName("PKEY4")
    @Expose
    var PKEY4 = ""
    @SerializedName("PKEY5")
    @Expose
    var PKEY5 = ""
    @SerializedName("PKEY6")
    @Expose
    var PKEY6 = ""
    @SerializedName("PKEY7")
    @Expose
    var PKEY7 = ""
    @SerializedName("PKEY8")
    @Expose
    var PKEY8 = ""
    @SerializedName("PUSER")
    @Expose
    var PUSER = ""

    companion object {
        fun buildForEmail(): SocialRegisterRequest {
            return SocialRegisterRequest().apply {
                TYPE = SocialProfile.EMAIL
            }
        }

        fun buildForFacebook(facebookId: String,
                             email: String = "",
                             phonenumber: String = ""): SocialRegisterRequest {
            return SocialRegisterRequest().apply {
                TYPE = SocialProfile.FACEBOOK
                PKEY1 = facebookId
                PKEY2 = email
                PKEY3 = phonenumber
            }
        }

        fun buildForLine(lineId: String,
                         displayName: String,
                         imageProfileUrl: String,
                         status: String): SocialRegisterRequest {
            return SocialRegisterRequest().apply {
                TYPE = SocialProfile.LINE
                PKEY1 = lineId
                PKEY2 = displayName
                PKEY3 = imageProfileUrl
                PKEY4 = status
            }
        }

        fun buildForGoogle(googleId: String,
                           displayName: String,
                           givenName: String,
                           familyName: String,
                           email: String,
                           imageProfileUrl: String,
                           googleAccessToken: String): SocialRegisterRequest {
            return SocialRegisterRequest().apply {
                TYPE = SocialProfile.GOOGLE
                PKEY1 = googleId
                PKEY2 = displayName
                PKEY3 = givenName
                PKEY4 = familyName
                PKEY5 = email
                PKEY6 = imageProfileUrl
                PKEY7 = googleAccessToken
            }
        }

        fun buildBySocialProfile(socialProfile: SocialProfile): SocialRegisterRequest {
            return when (socialProfile.refSocial) {
                SocialProfile.FACEBOOK -> SocialRegisterRequest.buildForFacebook(
                        socialProfile.id,
                        socialProfile.email,
                        socialProfile.phonenumber
                )
                SocialProfile.GOOGLE -> SocialRegisterRequest.buildForGoogle(
                        socialProfile.id,
                        socialProfile.displayName,
                        socialProfile.givenName,
                        socialProfile.familyName,
                        socialProfile.email,
                        socialProfile.imageProfileUrl,
                        socialProfile.accessToken
                )
                SocialProfile.LINE -> SocialRegisterRequest.buildForLine(
                        socialProfile.id,
                        socialProfile.displayName,
                        socialProfile.imageProfileUrl,
                        socialProfile.statusMessage
                )
                else -> throw Exception()
            }
        }
    }
}