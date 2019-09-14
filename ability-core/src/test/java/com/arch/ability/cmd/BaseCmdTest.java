package com.arch.ability.cmd;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseCmdTest {

    private static class FooCmd extends BaseCmd {

        public Integer i;

    }

    @Test
    public void attachment() {
        FooCmd foo = new FooCmd();
        foo.setAttachment("a", "b");
        assertEquals("b", foo.getAttachment("a", String.class));
        // 使用错误的type调用getAttachment，不会抛出ClassCastException，而是返回null
        try {
            foo.getAttachment("a", Long.class);
            fail("throw ex");
        } catch (ClassCastException expected) {

        }

        foo.setAttachment("a", (byte) 1);
        assertEquals(new Byte("1"), foo.getAttachment("a", Byte.class));
        try {
            foo.getAttachment("a", byte.class);
            fail();
        } catch (ClassCastException expected) {

        }

        assertNull(foo.getAttachment("invalidKey"));
        assertNull(foo.getAttachment("invalidKey", String.class));
    }

}