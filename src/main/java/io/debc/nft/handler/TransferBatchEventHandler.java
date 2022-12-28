package io.debc.nft.handler;

import io.debc.nft.annotation.Event;
import io.debc.nft.entity.NFTBalance;
import org.web3j.protocol.core.methods.response.Log;

import java.util.List;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 11:10
 **/
@Event
public class TransferBatchEventHandler implements EventHandler {

    public static final String ID = "0x4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb";

    @Override
    public List<NFTBalance> handle(List<Log> logs) {
        return null;
    }

    @Override
    public String getEventId() {
        return ID;
    }
}
