package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.common


data class ExpenseData(
        var appointment: String = "",
        var dueDate: String = "",
        var showroomName: String = "" ,
        var flagCurrent: Boolean ,
        var Curr_rEALADDRESS: String = "" ,
        var Curr_lat: String = "",
        var Curr_lng: String = "" ,
        var Regis_rEALADDRESS: String = "" ,
        var Regis_lat: String = "",
        var Regis_lng: String = "" ,
        var Mailing_rEALADDRESS: String = "" ,
        var Mailing_lat: String = "",
        var Mailing_lng: String = ""

)

