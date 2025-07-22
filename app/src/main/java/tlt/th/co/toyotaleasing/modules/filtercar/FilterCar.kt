package tlt.th.co.toyotaleasing.modules.filtercar

data class FilterCar(
        val id: String = "",
        val license: String = "",
        val province: String = "",
        val contractNumber: String = "",
        val isSelected : Boolean = false
)