package tlt.th.co.toyotaleasing.modules.newsandpromotion.news

data class News(
        var id: String = "",
        var image: String = "",
        var title: String = "",
        var period: String = "",
        var isBooked: Boolean = false
)