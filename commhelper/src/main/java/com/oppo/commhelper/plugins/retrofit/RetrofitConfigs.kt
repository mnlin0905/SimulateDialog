package com.oppo.commhelper.plugins.retrofit

import android.os.Build
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.oppo.commhelper.plugins.functionplus.kindAnyReturn
import com.orhanobut.logger.Logger
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

/**
 * Created on 2019/11/4  20:07
 * function :
 *
 * @author mnlin0905@gmail.com
 */
class RetrofitConfigs {
    fun generateHttpInterface(): HttpInterface {
        //网络请求的Host
        val baseUrl = "http://tbp.tencentcloudapi.com/"

        //生成JSON转换的库
        val gson = GsonBuilder()
            .serializeNulls()
            .registerTypeAdapterFactory(ListTypeAdapterFactory())//对空列表处理
            .setDateFormat("yyyy:MM:dd HH:mm:ss")
            .registerTypeAdapter(String::class.java,
                ZeroDeleteAdapter()
            )//0.00值处理
            .create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)

        //生成RxJava转换的adapter
        val rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()
        // RxJavaCallAdapterFactory rxJavaCallAdapterFactory=RxJavaCallAdapterFactory.create();

        //生成OkHttp网络传输的客户端
        val cookieStore = HashMap<String, List<Cookie>>()
        val okHttpClient = OkHttpClient.Builder()
            //cookie
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieStore[url.host()] = cookies
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookies = cookieStore[url.host()]
                    return cookies ?: ArrayList()
                }
            })
            //header
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("mining-channel", "liantongshuzi")
                    .addHeader("SDK", Build.VERSION.SDK_INT.toString() + "")
                    .build()
                chain.proceed(request)
            }
            //全局 map 参数
            .addInterceptor { chain ->
                //content_type类型
                val contentType = "application/x-www-form-urlencoded"
                chain.proceed(chain.request().run {
                    //如果为表单形式,则默认添加一个client字段
                    contentType.equals(body()?.contentType().toString(), ignoreCase = true)
                        .kindAnyReturn({ this }) {
                            FormBody.Builder()
                                .add("client", "1")
                                .build()
                                .let { bodyToString(it) + "&" + bodyToString(body()) }
                                .let {
                                    newBuilder().post(
                                        RequestBody.create(
                                            MediaType.parse("$contentType;charset=UTF-8"),
                                            it
                                        )
                                    ).build()
                                }
                        }
                })
            }
            .addInterceptor(HttpLoggingInterceptor { message ->
                Logger.i("HttpLog   $message")
            }.setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(12000, TimeUnit.MILLISECONDS)
            .writeTimeout(12000, TimeUnit.MILLISECONDS)
            .build()

        //设置最大的线程数量(默认是5)
        okHttpClient.dispatcher().maxRequestsPerHost = 3

        //最后组合成Retrofit对象
        val retrofit = Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()

        //将注解后的interface请求接口转换为真正可用的网络请求对象
        return retrofit.create(HttpInterface::class.java)
    }

    /**
     * 将body变成流
     */
    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }
    }
}

private class ZeroDeleteAdapter : TypeAdapter<String>() {
    private var instance: NumberFormat? = null

    init {
        instance = NumberFormat.getInstance()
        instance!!.maximumFractionDigits = 6
        instance!!.minimumFractionDigits = 0
    }

    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for value.
     *
     * @param out
     * @param value the Java object to write. May be null.
     */
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: String) {
        out.value(value)
    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null)
     * and converts it to a Java object. Returns the converted object.
     *
     * @param reader
     * @return the converted Java object. May be null.
     */
    @Throws(IOException::class)
    override fun read(reader: JsonReader): String? {
        val peek = reader.peek()
        if (peek == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        if (peek == JsonToken.BOOLEAN) {
            return java.lang.Boolean.toString(reader.nextBoolean())
        }
        val value = reader.nextString()

        //如果string为double格式,则将小数点后多余的0去掉
        if (value.matches("^[\\d]+\\.[\\d]*0$".toRegex())) {
            try {
                return instance!!.format(java.lang.Double.parseDouble(value)).replace(",", "")
            } catch (ignored: Exception) {

            }
        }
        return value
    }
}