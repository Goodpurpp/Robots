package robots.gui.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class GsonHelper<T> {
    fun loadFromJson(path: Path, type: Type): T? {
        val file = path.toFile()

        return if (!file.exists()) {
            return null
        } else {
            file.bufferedReader(Charsets.UTF_8).use {
                GSON.fromJson(it, type)
            }
        }
    }

    fun loadToJson(path: Path, saveObject: T?) {
        saveObject?.let {
            val jsonString = GSON.toJson(saveObject)
            Files.newBufferedWriter(path, Charsets.UTF_8, StandardOpenOption.CREATE).use {
                it.write(jsonString)
                it.flush()
            }
        }
    }

    companion object {
        private val GSON: Gson = GsonBuilder()
            .setPrettyPrinting()
            .create()
    }
}
