package com.example.myapplication;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.Date;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LizardTest {

    @Test
    public void getIdReturnsId(){

        final int EXPECTED_ID = 69;
        Lizard bart = new Lizard(EXPECTED_ID, "Bart", "Large", new Date());
        int outputId = bart.getId();

        assertEquals(EXPECTED_ID, outputId);
    }

    @Test
    public void getNameReturnsName(){

        final String EXPECTED_NAME = "Bart";
        Lizard bart = new Lizard(0, EXPECTED_NAME, "eh", new Date());
        String outputName = bart.getName();

        assertEquals(EXPECTED_NAME, outputName);
    }

    @Test
    public void getDateOfBirthReturnsDateOfBirth(){

        final Date EXPECTED_DOB = new Date();
        Lizard bart = new Lizard(0, "Bart", "4.5inches", EXPECTED_DOB);
        Date outputDob = bart.getDateOfBirth();

        assertEquals(EXPECTED_DOB, outputDob);
    }
}