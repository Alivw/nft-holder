package io.debc.nft.thread.entity.thread.contract;

import io.debc.nft.thread.entity.thread.annotation.Contract;
import io.debc.nft.thread.entity.thread.entity.EsContract;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 14:30
 **/
@Contract
public class Erc1155Contract extends NftContract {

    /**
     * see  https://eips.ethereum.org/EIPS/eip-1155
     */
    public static final String interfaceId = "0xd9b67a26";

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

