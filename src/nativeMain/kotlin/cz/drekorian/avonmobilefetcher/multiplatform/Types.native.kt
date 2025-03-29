package cz.drekorian.avonmobilefetcher.multiplatform

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
internal typealias CCharArray = CArrayPointer<ByteVar>
