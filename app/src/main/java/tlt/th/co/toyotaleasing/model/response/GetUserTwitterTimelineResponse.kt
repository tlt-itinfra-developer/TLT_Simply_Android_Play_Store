package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

/*data class GetUserTwitterTimelineResponse(
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("id") var id: Long?,
        @SerializedName("id_str") var idStr: String?,
        @SerializedName("text") var text: String?,
        @SerializedName("truncated") var truncated: Boolean?,
        @SerializedName("entities") var entities: Entities?,
        @SerializedName("source") var source: String?,
        @SerializedName("in_reply_to_status_id") var inReplyToStatusId: Any?,
        @SerializedName("in_reply_to_status_id_str") var inReplyToStatusIdStr: Any?,
        @SerializedName("in_reply_to_user_id") var inReplyToUserId: Any?,
        @SerializedName("in_reply_to_user_id_str") var inReplyToUserIdStr: Any?,
        @SerializedName("in_reply_to_screen_name") var inReplyToScreenName: Any?,
        @SerializedName("user") var user: User?,
        @SerializedName("geo") var geo: Any?,
        @SerializedName("coordinates") var coordinates: Any?,
        @SerializedName("place") var place: Any?,
        @SerializedName("contributors") var contributors: Any?,
        @SerializedName("is_quote_status") var isQuoteStatus: Boolean?,
        @SerializedName("retweet_count") var retweetCount: Int?,
        @SerializedName("favorite_count") var favoriteCount: Int?,
        @SerializedName("favorited") var favorited: Boolean?,
        @SerializedName("retweeted") var retweeted: Boolean?,
        @SerializedName("possibly_sensitive") var possiblySensitive: Boolean?,
        @SerializedName("lang") var lang: String?,
        @SerializedName("extended_entities") var extendedEntities: ExtendedEntities?,
        @SerializedName("retweeted_status") var retweetedStatus: RetweetedStatus?
) {

    data class Entities(
            @SerializedName("hashtags") var hashtags: List<Hashtags?>?,
            @SerializedName("symbols") var symbols: List<Any?>?,
            @SerializedName("user_mentions") var userMentions: List<UserMentions?>?,
            @SerializedName("urls") var urls: List<Url?>?,
            @SerializedName("media") var media: List<Media?>?
    ) {

        data class Url(
                @SerializedName("url") var url: String?,
                @SerializedName("expanded_url") var expandedUrl: String?,
                @SerializedName("display_url") var displayUrl: String?,
                @SerializedName("indices") var indices: List<Int?>?
        )

        data class Media(
                @SerializedName("id") var id: Long?,
                @SerializedName("id_str") var idStr: String?,
                @SerializedName("indices") var indices: List<Int?>?,
                @SerializedName("media_url") var mediaUrl: String?,
                @SerializedName("media_url_https") var mediaUrlHttps: String?,
                @SerializedName("url") var url: String?,
                @SerializedName("display_url") var displayUrl: String?,
                @SerializedName("expanded_url") var expandedUrl: String?,
                @SerializedName("type") var type: String?,
                @SerializedName("sizes") var sizes: Sizes?
        ) {

            data class Sizes(
                    @SerializedName("thumb") var thumb: Thumb?,
                    @SerializedName("large") var large: Large?,
                    @SerializedName("small") var small: Small?,
                    @SerializedName("medium") var medium: Medium?
            ) {

                data class Small(
                        @SerializedName("w") var w: Int?,
                        @SerializedName("h") var h: Int?,
                        @SerializedName("resize") var resize: String?
                )

                data class Thumb(
                        @SerializedName("w") var w: Int?,
                        @SerializedName("h") var h: Int?,
                        @SerializedName("resize") var resize: String?
                )

                data class Large(
                        @SerializedName("w") var w: Int?,
                        @SerializedName("h") var h: Int?,
                        @SerializedName("resize") var resize: String?
                )

                data class Medium(
                        @SerializedName("w") var w: Int?,
                        @SerializedName("h") var h: Int?,
                        @SerializedName("resize") var resize: String?
                )
            }
        }

        data class Hashtags(
                @SerializedName("text") var text: String = "",
                @SerializedName("indices") var indices: List<Int>? = listOf()
        )

        data class UserMentions(
                @SerializedName("screen_name") var screenName: String = "",
                @SerializedName("name") var name: String = "",
                @SerializedName("id") var id: Int = 0,
                @SerializedName("id_str") var idStr: String = "",
                @SerializedName("indices") var indices: List<Int>? = listOf()
        )
    }


    data class User(
            @SerializedName("id") var id: Int?,
            @SerializedName("id_str") var idStr: String?,
            @SerializedName("name") var name: String?,
            @SerializedName("screen_name") var screenName: String?,
            @SerializedName("location") var location: String?,
            @SerializedName("description") var description: String?,
            @SerializedName("url") var url: String?,
            @SerializedName("entities") var entities: Entities?,
            @SerializedName("protected") var protected: Boolean?,
            @SerializedName("followers_count") var followersCount: Int?,
            @SerializedName("friends_count") var friendsCount: Int?,
            @SerializedName("listed_count") var listedCount: Int?,
            @SerializedName("created_at") var createdAt: String?,
            @SerializedName("favourites_count") var favouritesCount: Int?,
            @SerializedName("utc_offset") var utcOffset: Any?,
            @SerializedName("time_zone") var timeZone: Any?,
            @SerializedName("geo_enabled") var geoEnabled: Boolean?,
            @SerializedName("verified") var verified: Boolean?,
            @SerializedName("statuses_count") var statusesCount: Int?,
            @SerializedName("lang") var lang: String?,
            @SerializedName("contributors_enabled") var contributorsEnabled: Boolean?,
            @SerializedName("is_translator") var isTranslator: Boolean?,
            @SerializedName("is_translation_enabled") var isTranslationEnabled: Boolean?,
            @SerializedName("profile_background_color") var profileBackgroundColor: String?,
            @SerializedName("profile_background_image_url") var profileBackgroundImageUrl: String?,
            @SerializedName("profile_background_image_url_https") var profileBackgroundImageUrlHttps: String?,
            @SerializedName("profile_background_tile") var profileBackgroundTile: Boolean?,
            @SerializedName("profile_image_url") var profileImageUrl: String?,
            @SerializedName("profile_image_url_https") var profileImageUrlHttps: String?,
            @SerializedName("profile_banner_url") var profileBannerUrl: String?,
            @SerializedName("profile_link_color") var profileLinkColor: String?,
            @SerializedName("profile_sidebar_border_color") var profileSidebarBorderColor: String?,
            @SerializedName("profile_sidebar_fill_color") var profileSidebarFillColor: String?,
            @SerializedName("profile_text_color") var profileTextColor: String?,
            @SerializedName("profile_use_background_image") var profileUseBackgroundImage: Boolean?,
            @SerializedName("has_extended_profile") var hasExtendedProfile: Boolean?,
            @SerializedName("default_profile") var defaultProfile: Boolean?,
            @SerializedName("default_profile_image") var defaultProfileImage: Boolean?,
            @SerializedName("following") var following: Any?,
            @SerializedName("follow_request_sent") var followRequestSent: Any?,
            @SerializedName("notifications") var notifications: Any?,
            @SerializedName("translator_type") var translatorType: String?
    ) {

        data class Entities(
                @SerializedName("url") var url: Url?,
                @SerializedName("description") var description: Description?
        ) {

            data class Description(
                    @SerializedName("urls") var urls: List<Any?>?
            )


            data class Url(
                    @SerializedName("urls") var urls: List<Url?>?
            ) {

                data class Url(
                        @SerializedName("url") var url: String?,
                        @SerializedName("expanded_url") var expandedUrl: String?,
                        @SerializedName("display_url") var displayUrl: String?,
                        @SerializedName("indices") var indices: List<Int?>?
                )
            }
        }
    }


    data class ExtendedEntities(
            @SerializedName("media") var media: List<Media?>?
    ) {

        data class Media(
                @SerializedName("id") var id: Long?,
                @SerializedName("id_str") var idStr: String?,
                @SerializedName("indices") var indices: List<Int?>?,
                @SerializedName("media_url") var mediaUrl: String?,
                @SerializedName("media_url_https") var mediaUrlHttps: String?,
                @SerializedName("url") var url: String?,
                @SerializedName("display_url") var displayUrl: String?,
                @SerializedName("expanded_url") var expandedUrl: String?,
                @SerializedName("type") var type: String?,
                @SerializedName("sizes") var sizes: Sizes?
        ) {

            data class Sizes(
                    @SerializedName("thumb") var thumb: Thumb?,
                    @SerializedName("large") var large: Large?,
                    @SerializedName("small") var small: Small?,
                    @SerializedName("medium") var medium: Medium?
            ) {

                data class Small(
                        @SerializedName("w") var w: Int?,
                        @SerializedName("h") var h: Int?,
                        @SerializedName("resize") var resize: String?
                )


                data class Thumb(
                        @SerializedName("w") var w: Int?,
                        @SerializedName("h") var h: Int?,
                        @SerializedName("resize") var resize: String?
                )


                data class Large(
                        @SerializedName("w") var w: Int?,
                        @SerializedName("h") var h: Int?,
                        @SerializedName("resize") var resize: String?
                )


                data class Medium(
                        @SerializedName("w") var w: Int?,
                        @SerializedName("h") var h: Int?,
                        @SerializedName("resize") var resize: String?
                )
            }
        }
    }

    data class RetweetedStatus(
            @SerializedName("created_at") var createdAt: String?,
            @SerializedName("id") var id: Long?,
            @SerializedName("id_str") var idStr: String?,
            @SerializedName("text") var text: String?,
            @SerializedName("truncated") var truncated: Boolean?,
            @SerializedName("entities") var entities: Entities?,
            @SerializedName("source") var source: String?,
            @SerializedName("in_reply_to_status_id") var inReplyToStatusId: Any?,
            @SerializedName("in_reply_to_status_id_str") var inReplyToStatusIdStr: Any?,
            @SerializedName("in_reply_to_user_id") var inReplyToUserId: Int?,
            @SerializedName("in_reply_to_user_id_str") var inReplyToUserIdStr: String?,
            @SerializedName("in_reply_to_screen_name") var inReplyToScreenName: String?,
            @SerializedName("user") var user: User?,
            @SerializedName("geo") var geo: Any?,
            @SerializedName("coordinates") var coordinates: Any?,
            @SerializedName("place") var place: Any?,
            @SerializedName("contributors") var contributors: Any?,
            @SerializedName("is_quote_status") var isQuoteStatus: Boolean?,
            @SerializedName("retweet_count") var retweetCount: Int?,
            @SerializedName("favorite_count") var favoriteCount: Int?,
            @SerializedName("favorited") var favorited: Boolean?,
            @SerializedName("retweeted") var retweeted: Boolean?,
            @SerializedName("lang") var lang: String?
    ) {

        data class User(
                @SerializedName("id") var id: Int?,
                @SerializedName("id_str") var idStr: String?,
                @SerializedName("name") var name: String?,
                @SerializedName("screen_name") var screenName: String?,
                @SerializedName("location") var location: String?,
                @SerializedName("description") var description: String?,
                @SerializedName("url") var url: Any?,
                @SerializedName("entities") var entities: Entities?,
                @SerializedName("protected") var protected: Boolean?,
                @SerializedName("followers_count") var followersCount: Int?,
                @SerializedName("friends_count") var friendsCount: Int?,
                @SerializedName("listed_count") var listedCount: Int?,
                @SerializedName("created_at") var createdAt: String?,
                @SerializedName("favourites_count") var favouritesCount: Int?,
                @SerializedName("utc_offset") var utcOffset: Any?,
                @SerializedName("time_zone") var timeZone: Any?,
                @SerializedName("geo_enabled") var geoEnabled: Boolean?,
                @SerializedName("verified") var verified: Boolean?,
                @SerializedName("statuses_count") var statusesCount: Int?,
                @SerializedName("lang") var lang: String?,
                @SerializedName("contributors_enabled") var contributorsEnabled: Boolean?,
                @SerializedName("is_translator") var isTranslator: Boolean?,
                @SerializedName("is_translation_enabled") var isTranslationEnabled: Boolean?,
                @SerializedName("profile_background_color") var profileBackgroundColor: String?,
                @SerializedName("profile_background_image_url") var profileBackgroundImageUrl: String?,
                @SerializedName("profile_background_image_url_https") var profileBackgroundImageUrlHttps: String?,
                @SerializedName("profile_background_tile") var profileBackgroundTile: Boolean?,
                @SerializedName("profile_image_url") var profileImageUrl: String?,
                @SerializedName("profile_image_url_https") var profileImageUrlHttps: String?,
                @SerializedName("profile_banner_url") var profileBannerUrl: String?,
                @SerializedName("profile_link_color") var profileLinkColor: String?,
                @SerializedName("profile_sidebar_border_color") var profileSidebarBorderColor: String?,
                @SerializedName("profile_sidebar_fill_color") var profileSidebarFillColor: String?,
                @SerializedName("profile_text_color") var profileTextColor: String?,
                @SerializedName("profile_use_background_image") var profileUseBackgroundImage: Boolean?,
                @SerializedName("has_extended_profile") var hasExtendedProfile: Boolean?,
                @SerializedName("default_profile") var defaultProfile: Boolean?,
                @SerializedName("default_profile_image") var defaultProfileImage: Boolean?,
                @SerializedName("following") var following: Any?,
                @SerializedName("follow_request_sent") var followRequestSent: Any?,
                @SerializedName("notifications") var notifications: Any?,
                @SerializedName("translator_type") var translatorType: String?
        ) {

            data class Entities(
                    @SerializedName("description") var description: Description?
            ) {

                data class Description(
                        @SerializedName("urls") var urls: List<Any?>?
                )
            }
        }


        data class Entities(
                @SerializedName("hashtags") var hashtags: List<Any?>?,
                @SerializedName("symbols") var symbols: List<Any?>?,
                @SerializedName("user_mentions") var userMentions: List<UserMention?>?,
                @SerializedName("urls") var urls: List<Any?>?
        ) {

            data class UserMention(
                    @SerializedName("screen_name") var screenName: String?,
                    @SerializedName("name") var name: String?,
                    @SerializedName("id") var id: Int?,
                    @SerializedName("id_str") var idStr: String?,
                    @SerializedName("indices") var indices: List<Int?>?
            )
        }
    }
}*/

data class GetUserTwitterTimelineResponse(
        @SerializedName("created_at") var createdAt: String,
        @SerializedName("id") var id: Long = 0,
        @SerializedName("id_str") var idString: String = "",
        @SerializedName("text") var text: String = "",
        @SerializedName("truncated") var truncated: Boolean = false,
        @SerializedName("entities") var entities: Entities
) {
    data class Entities(
            @SerializedName("hashtags") var hasgtags: List<Hashtags>? = listOf(),
            @SerializedName("symbols") var symbols: List<Any> = listOf(),
            @SerializedName("user_mentions") var userMentions: List<UserMentions> = listOf(),
            @SerializedName("urls") var urls: List<Urls> = listOf()
    ) {
        data class Hashtags(
                @SerializedName("text") var text: String = "",
                @SerializedName("indices") var indices: List<Int>? = listOf()
        )

        data class UserMentions(
                @SerializedName("screen_name") var screenName: String = "",
                @SerializedName("name") var name: String = "",
                @SerializedName("id") var id: Long = 0,
                @SerializedName("id_str") var idStr: String = "",
                @SerializedName("indices") var indices: List<Int>? = listOf()
        )

        data class Urls(
                @SerializedName("url") var url: String = "",
                @SerializedName("expanded_url") var expandedUrl: String = "",
                @SerializedName("display_url") var displayUrl: String = "",
                @SerializedName("indices") var indices: List<Int>? = listOf()
        )

    }
}

