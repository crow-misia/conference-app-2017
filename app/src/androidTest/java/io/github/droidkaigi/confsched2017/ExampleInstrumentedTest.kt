package io.github.droidkaigi.confsched2017

import org.junit.Test
import org.junit.runner.RunWith

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Assert.assertEquals

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        val expectedPackageName = StringBuilder("io.github.droidkaigi.confsched2017")
        if (BuildConfig.FLAVOR == "develop") {
            expectedPackageName.append(".develop")
        }
        if (BuildConfig.DEBUG) {
            expectedPackageName.append(".debug")
        }

        assertEquals(expectedPackageName.toString(), appContext.packageName)
    }
}
