package org.example.whiteboard.di

import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    config: KoinAppDeclaration? = null
) {
    startKoin {

        config?.invoke(this)
        logger(PrintLogger(Level.ERROR))
        modules(
            platformModule,
            sharedModule
        )
    }
}