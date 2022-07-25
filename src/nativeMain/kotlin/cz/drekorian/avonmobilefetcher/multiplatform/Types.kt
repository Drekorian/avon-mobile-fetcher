package cz.drekorian.avonmobilefetcher.multiplatform

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CPointer
import platform.posix.FILE

internal typealias CCharArray = CArrayPointer<ByteVar>
internal typealias CFile = CPointer<FILE>
