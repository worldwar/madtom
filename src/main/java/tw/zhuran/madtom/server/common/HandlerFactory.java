package tw.zhuran.madtom.server.common;

import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class HandlerFactory {
    public abstract ByteToMessageDecoder newHandler();
    public abstract ByteToMessageDecoder newDecoder();
}
