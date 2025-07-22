package tlt.th.co.toyotaleasing.modules.newsandpromotion.oilprice

import androidx.lifecycle.MutableLiveData
import android.text.Html
import android.util.Log
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import okhttp3.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.model.response.OilPriceSoapResponse
import tlt.th.co.toyotaleasing.util.CalendarUtils
import java.io.IOException
import java.net.URLEncoder


class OilPriceViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    fun requestPTTSoapData() {
        val client = OkHttpClient()
        val mediaType = MediaType.parse("text/xml")
        val body = RequestBody.create(
                mediaType,
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<soapenv:Envelope   \n\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  \n\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"  \n\txmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n    <soapenv:Body>\n        <CurrentOilPrice xmlns=\"http://www.pttplc.com/ptt_webservice/\">\n            <Language>EN</Language>\n        </CurrentOilPrice>\n    </soapenv:Body>\n</soapenv:Envelope>"
        )
        val request = Request.Builder()
                .url("http://www.pttplc.com/webservice/pttinfo.asmx")
                .post(body)
                .addHeader("Content-Type", "text/xml")
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Host", "www.pttplc.com")
                .addHeader("content-length", "418")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                var res = ""
            }

            override fun onResponse(call: Call, response: Response) {
                getXmlContentFromSoap(response)
            }
        })
    }

    fun getXmlContentFromSoap(soapResponse: Response) {
        try{
        val res: String? = soapResponse.body()?.string()
        val startString = "<CurrentOilPriceResult>"
        val sIndex: Int = res!!.indexOf(startString) + startString.length
        val eIndex: Int = res.indexOf("</CurrentOilPriceResult>")
        val finalStr = res.substring(sIndex, eIndex)
        val xmlContent: String = Html.fromHtml(finalStr).toString()

        val xmlToJson = XmlToJson.Builder(xmlContent)
                .build()

        val oilJson = JsonMapperManager.getInstance()
                .gson.fromJson(xmlToJson.toString(), OilPriceSoapResponse::class.java)

        val model = Model(
                date = CalendarUtils.getCurrentDate().toDatetime(),
                oilList = convertToOil(oilJson),
                gasList = convertToGas(oilJson)
        )
        whenDataLoaded.postValue(model)
        }catch (e : Exception) {
            whenDataLoaded.postValue(null)
        }
    }

    private fun convertToOil(oil: OilPriceSoapResponse): List<OilPrice> {
        return oil.pttDs?.results
                ?.filter { it?.productName != NGV }
                ?.map {
                    OilPrice(price = it?.price
                            ?: "", image = when {
                        it?.productName == BLUE_GASOLINE_95 -> R.drawable.gasoline_95
                        it?.productName == BLUE_DIESEL -> R.drawable.diesel
                        it?.productName == BLUE_GASOHOL_91 -> R.drawable.gasohol_91
                        it?.productName == BLUE_GASOHOL_E20 -> R.drawable.gasohol_e20
                        it?.productName == BLUE_GASOHOL_95 -> R.drawable.gasohol_95
                        it?.productName == BLUE_GASOHOL_E85 -> R.drawable.gasohol_e85
                        it?.productName == HYFORCE_PREICE_DIESEL -> R.drawable.premium_diesel
                        else -> 0
                    })
                }
                ?.filter {
                    it.price != ""
                } ?: listOf()
    }

    private fun convertToGas(oil: OilPriceSoapResponse): List<OilPrice> {
        return oil.pttDs?.results
                ?.filter { it?.productName == NGV }
                ?.map {
                    OilPrice(price = it?.price ?: "", image = R.drawable.ngv)
                } ?: listOf()
    }

    data class Model(
            val date: String = "",
            val oilList: List<OilPrice> = listOf(),
            val gasList: List<OilPrice> = listOf()
    )

    companion object {
        const val BLUE_GASOLINE_95 = "Blue Gasoline 95"
        const val BLUE_DIESEL = "Blue Diesel"
        const val BLUE_GASOHOL_91 = "Blue Gasohol 91"
        const val BLUE_GASOHOL_E20 = "Blue Gasohol E20"
        const val BLUE_GASOHOL_95 = "Blue Gasohol 95"
        const val BLUE_GASOHOL_E85 = "Blue Gasohol E85"
        const val HYFORCE_PREICE_DIESEL = "HyForce Premium Diesel"

        const val NGV = "NGV"
    }
}