package io.debc.nft.handler;

import io.debc.nft.annotation.Event;
import io.debc.nft.contract.Erc1155Contract;
import io.debc.nft.entity.NFTBalance;
import io.debc.nft.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-28 11:10
 **/
@Event
@Slf4j
public class TransferBatchEventHandler extends Abstract1155TransferEventHandler {

    private Erc1155Contract erc1155Contract = new Erc1155Contract();
    public static final String ID = "0x4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb";

    @Override
    public List<NFTBalance> handle(List<Log> logs) {
        List<Log> logList = logs.stream().flatMap(t -> {
            BigInteger[] ids = SysUtils.decodeTransferBatchData(t.getData().substring(2));
            List<Log> ans = new ArrayList<>(ids.length);
            Log l;
            for (BigInteger id : ids) {
                l = new Log();
                l.setData(id.toString());
                l.setTopics(t.getTopics());
                l.setAddress(t.getAddress());
                ans.add(l);
            }
            return ans.stream();
        }).collect(Collectors.toList());
        return super.handle(logList);
    }

    @Override
    public String getEventId() {
        return ID;
    }

}
