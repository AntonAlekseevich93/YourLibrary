plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("app.cash.sqldelight")
}
//
kotlin{
    jvm("desktop")
    androidTarget()
//    ios() //todo чтобы подключить ios нужно переделать на новый вид где разные архитектуры ios

    //можем добавить сюда все зависимости которые должны быть во всех проектах к которым мы подключим этот плагин
}

sqldelight {
    databases.create("AppDatabase") {
        packageName.set("sqldelight.com.yourlibrary.database")
    }
    linkSqlite.set(true)
}