package com.arch.ability.cmd;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseReplyTest {

    private static class Reply extends BaseReply {

    }
    @Test
    public void basic() {
        Reply reply = new Reply();
        // 默认是成功的
        assertTrue(reply.isOk());
        assertNull(reply.getCause());

        // 通过fail(a, b)转换为失败状态
        reply.fail("201", "标记位", "为以下之一: ", "0、2");
        assertFalse(reply.isOk());

        assertEquals("201", reply.getResultErrorCode());
        assertEquals(3, reply.getResultErrorMsg().length);
        assertEquals("为以下之一: ", reply.getResultErrorMsg()[1]);

        // 通过数组形式，与变参形式结果一样
        reply.fail("201", new String[]{"标记位", "为以下之一: ", "0、2"});
        assertFalse(reply.isOk());
        assertEquals("201", reply.getResultErrorCode());
        assertEquals(3, reply.getResultErrorMsg().length);
        assertEquals("为以下之一: ", reply.getResultErrorMsg()[1]);
        assertEquals("0、2", reply.getResultErrorMsg()[2]);
    }

    @Test
    public void throwable() {
        Reply reply = new Reply();
        reply.fail(new Exception("bar"), "201", "标记位", "为以下之一: ", "0、2");
        assertFalse(reply.isOk());
        assertTrue(reply.getCause() instanceof Exception);
        assertEquals("bar", reply.getCause().getMessage());
        assertEquals("201", reply.getResultErrorCode());
        assertEquals(3, reply.getResultErrorMsg().length);
    }

}