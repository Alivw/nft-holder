package io.debc.nft.contract;

import io.debc.nft.annotation.Contract;
import io.debc.nft.entity.EsContract;

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

