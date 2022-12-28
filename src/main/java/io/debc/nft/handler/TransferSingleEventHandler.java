package io.debc.nft.handler;

import io.debc.nft.annotation.Event;
import io.debc.nft.entity.NFTBalance;
import org.web3j.protocol.core.methods.response.Log;

import java.util.List;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 11:07
 **/
@Event
public class TransferSingleEventHandler implements EventHandler {
    public static final String ID = "0xc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62";

    @Override
    public String getEventId() {
        return ID;
    }

    @Override
    public List<NFTBalance> handle(List<Log> logs) {

        return null;
    }
}
