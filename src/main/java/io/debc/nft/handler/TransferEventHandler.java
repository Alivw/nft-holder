package io.debc.nft.handler;

import io.debc.nft.annotation.Event;
import io.debc.nft.entity.NFTBalance;
import org.web3j.protocol.core.methods.response.Log;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 11:06
 **/
@Event
public class TransferEventHandler implements EventHandler {
    public static final String ID = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";

    @Override
    public List<NFTBalance> handle(List<Log> logs) {
        Set<String> contractAddress = logs.stream().map(Log::getAddress).collect(Collectors.toSet());

        return null;
    }

    @Override
    public String getEventId() {
        return ID;
    }
}
