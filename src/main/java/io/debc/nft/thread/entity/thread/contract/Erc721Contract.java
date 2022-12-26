package io.debc.nft.thread.entity.thread.contract;

import io.debc.nft.thread.entity.thread.annotation.Contract;
import io.debc.nft.thread.entity.thread.entity.EsContract;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 10:48
 **/
@Slf4j
@Contract
public class Erc721Contract extends NftContract {

    /**
     * see https://eips.ethereum.org/EIPS/eip-721
     */
    public static final String interfaceId = "0x80ac58cd";

    @Override
    public String getSupportsInterfaceId() {
        return interfaceId;
    }

    @Override
    public EsContract callContractMethods(String contractAddress) {
        EsContract esContract = super.callContractMethods(contractAddress);
        if (esContract != null) {
            esContract.setStd("721");
        }
        return esContract;
    }

}
