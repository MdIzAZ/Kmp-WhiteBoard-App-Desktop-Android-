package org.example.whiteboard.di

import org.example.whiteboard.data.database.AppDataBase
import org.example.whiteboard.data.database.getRoomDatabase
import org.example.whiteboard.data.remote.AuthenticationAPI
import org.example.whiteboard.data.remote.createHttpClient
import org.example.whiteboard.data.repo.AuthRepoImp
import org.example.whiteboard.data.repo.PathRepoImp
import org.example.whiteboard.data.repo.RoomRepoImp
import org.example.whiteboard.data.repo.SettingsRepoImp
import org.example.whiteboard.data.repo.WhiteboardRepoImp
import org.example.whiteboard.domain.repo.AuthRepo
import org.example.whiteboard.domain.repo.PathRepo
import org.example.whiteboard.domain.repo.RoomRepo
import org.example.whiteboard.domain.repo.SettingsRepo
import org.example.whiteboard.domain.repo.WhiteboardRepo
import org.example.whiteboard.presentation.authentication.AuthViewModel
import org.example.whiteboard.presentation.dashboard.DashboardViewModel
import org.example.whiteboard.presentation.whiteboard.WhiteBoardViewModel
import org.example.whiteboard.presentation.setting_screen.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {

    //database
    single {
        getRoomDatabase( get() )
    }

    //ktor
    single {
        createHttpClient(get())
    }

    single {
        AuthenticationAPI(get())
    }

    //dao
    single { get<AppDataBase>().pathDao() }

    single { get<AppDataBase>().whiteboardDao() }
    //viewmodel
    viewModelOf(::WhiteBoardViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::AuthViewModel)

    //repository
    singleOf(::PathRepoImp).bind<PathRepo>()
    singleOf(::SettingsRepoImp).bind<SettingsRepo>()
    singleOf(::WhiteboardRepoImp).bind<WhiteboardRepo>()
    singleOf(::RoomRepoImp).bind<RoomRepo>()
    singleOf(::AuthRepoImp).bind<AuthRepo>()

}