package tw.zhuran.madtom.server.common;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

public abstract class HandlerFactory {
    public abstract MessageToMessageDecoder newHandler();
    public abstract ByteToMessageDecoder newDecoder();
}
