package tlt.th.co.toyotaleasing.model.entity.cache

interface Jsonable  {
    fun getJsonString() : String
    fun setJsonString(json : String)
}