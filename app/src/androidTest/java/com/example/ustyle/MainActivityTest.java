package com.example.ustyle;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.example.ustyle.data.Global;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new  ActivityScenarioRule<>(MainActivity.class);
    ActivityScenario<MainActivity> scenario;

    Context context = getApplicationContext();

    @Before
    public void launchActivity() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
    }

    @Test
    public void testAccessToken() {
        assertNotEquals(Global.accessToken, "");
    }

    @Test
    public void testContext(){
        assertNotNull(Global.context);
    }

    @Test
    public void testUserInfo(){
        while(Global.User.checked == false);

        assertEquals(Global.User.loggedIn, true);
    }

    @Test
    public void testAndroidID(){
        while(Global.User.checked == false );
        String androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        assertEquals(androidID, Global.User.androidID);
    }


    @After
    public void tearDown() throws Exception {
        scenario.close();
    }
}