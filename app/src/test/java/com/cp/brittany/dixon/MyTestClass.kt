package com.cp.brittany.dixon

import com.cp.brittany.dixon.utills.Utils
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MyTestClass {

    lateinit var utilsClass:Utils
    @Before
    fun setUp() {
        utilsClass=Utils
    }

    @After
    fun runAfter() {

    }

    @Test
    fun validatePassword_blankInput_expectedRequiredInput() {
        val sut = Utils
        val result = sut.validatePassword("password9A@")
        Assert.assertEquals("Valid", result)
    }


    @Test
    fun test_empty_user_name() {
        val assert = utilsClass.validate("daniyalKhan", "dk1601477@gmail.com", "", 27)

        // Difference between assert and Assert.assertEquals() is Assert.assertEquals() is more descriptive and provide us message like  what was expected and what was found and assert just show us a message.
        Assert.assertEquals("please enter password", assert)
//        assert(result=="please enter username")

    }




}