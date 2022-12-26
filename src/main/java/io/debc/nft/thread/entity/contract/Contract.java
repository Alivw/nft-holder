package io.debc.nft.thread.entity.contract;

import io.debc.nft.thread.entity.entity.EsContract;
import io.debc.nft.thread.entity.thread.Pool;

import static io.debc.nft.config.ConfigurableConstants.CALL_CONTRACT_CORE_SIZE;
import static io.debc.nft.config.ConfigurableConstants.CALL_CONTRACT_QUEUE_SIZE;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-09 15:01
 **/
public interface Contract {
    Pool callContractPool = new Pool(CALL_CONTRACT_CORE_SIZE, CALL_CONTRACT_QUEUE_SIZE, "callContract");

    default String convert(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '0') {
                return ((i & 1) == 0) ? str.substring(0, i) : str.substring(0, i + 1);
            }
        }
        return "";
    }

    default String convertTooLongAddress(String address) {
        return address.length() == 66 ? "0x" + address.substring(26) : address;
    }


    EsContract callContractMethods(String contractAddresses);
}
