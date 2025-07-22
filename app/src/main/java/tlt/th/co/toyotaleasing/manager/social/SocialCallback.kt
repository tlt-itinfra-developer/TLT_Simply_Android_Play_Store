package tlt.th.co.toyotaleasing.manager.social

interface SocialCallback {
    fun onSuccess(socialProfile: SocialProfile?)
    fun onFailure(message: String)
}