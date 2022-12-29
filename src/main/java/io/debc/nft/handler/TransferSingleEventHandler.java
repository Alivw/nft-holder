package io.debc.nft.handler;

import io.debc.nft.annotation.Event;
import io.debc.nft.contract.Erc1155Contract;
import io.debc.nft.entity.NFTBalance;
import io.debc.nft.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 11:07
 **/
@Event
@Slf4j
public class TransferSingleEventHandler extends Abstract1155TransferEventHandler {
    public static final String ID = "0xc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62";

    @Override
    public String getEventId() {
        return ID;
    }

    private Erc1155Contract erc1155Contract = new Erc1155Contract();

    @Override
    public List<NFTBalance> handle(List<Log> logs) {
        logs = logs.stream().peek(t -> t.setData(SysUtils.decodeTransferSingleData(t.getData().substring(2)))).collect(Collectors.toList());
        return super.handle(logs);
    }
}
