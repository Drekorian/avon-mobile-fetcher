package cz.drekorian.avonmobilefetcher.multiplatform

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CArrayPointer

internal typealias CCharArray = CArrayPointer<ByteVar>
