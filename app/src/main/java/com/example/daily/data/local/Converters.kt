package com.example.daily.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.daily.data.local.entity.TopicSpecDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

@ProvidedTypeConverter
class Converters(moshi: Moshi) {
    private val type = Types.newParameterizedType(List::class.java, TopicSpecDto::class.java)
    private val adapter: JsonAdapter<List<TopicSpecDto>> = moshi.adapter(type)

    @TypeConverter
    fun fromList(list: List<TopicSpecDto>?) : String {
        return adapter.toJson(list)
    }

    @TypeConverter
    fun toList(json: String?) : List<TopicSpecDto> {
        if(json.isNullOrEmpty()) return emptyList()
        return adapter.fromJson(json) ?: emptyList()
    }
}