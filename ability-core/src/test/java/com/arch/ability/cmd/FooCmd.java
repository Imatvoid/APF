package com.arch.ability.cmd;

public class FooCmd extends BaseCmd {
    // 订单标记位
    public String orderMark;

    public Reply reply = new Reply();

    public static class Reply extends BaseReply {
        public byte orderMark1;
        public byte orderMark3;

    }
}
