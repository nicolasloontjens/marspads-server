package be.howest.ti.mars.logic.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {
    @Test
    void testRead(){
        assertEquals("test",Config.getInstance().readSetting("test"));
    }

    @Test
    void storeSettingsToFileTest(){
        Config.getInstance().writeSetting("test","test");
    }
}