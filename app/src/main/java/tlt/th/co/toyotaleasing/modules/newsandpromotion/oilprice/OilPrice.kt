package tlt.th.co.toyotaleasing.modules.newsandpromotion.oilprice

data class OilPrice(
        var type: OilType = OilType.GASOHOL_95,
        var price: String = "",
        var image: Int = 0
)


