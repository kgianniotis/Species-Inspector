package com.example.speciesinspector_v2

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testSuccessfulLogin() {

        onView(withHint("Email"))
            .perform(typeText("test@example.com"), closeSoftKeyboard())


        onView(withHint("Password"))
            .perform(typeText("Correct Password"), closeSoftKeyboard())


        onView(withText("Log In"))
            .perform(click())


        Intents.intended(hasComponent(MainMenu::class.java.name))


        assert(auth.currentUser != null)
    }

    @Test
    fun testFailedLogin() {

        onView(withHint("Email"))
            .perform(typeText("invalid@example.com"), closeSoftKeyboard())


        onView(withHint("Password"))
            .perform(typeText("Password was incorrect."), closeSoftKeyboard())


        onView(withText("Log In"))
            .perform(click())


        onView(withText("Authentication failed"))
            .check(matches(isDisplayed()))


        assert(auth.currentUser == null)
    }
}
