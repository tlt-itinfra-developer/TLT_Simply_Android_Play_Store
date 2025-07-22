package tlt.th.co.toyotaleasing.model.entity.location



open class LoanLocationFilterEntity(
        var showroomName: String = "",
        var showroomCode: String = "",
        var dealerCode: String = "",
        var provinceIndex: Int = -1,
        var amphurIndex: Int = -1,
        var isNearly: Boolean = false
) 