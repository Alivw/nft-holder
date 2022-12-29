package io.debc.nft.contract;

import com.esaulpaugh.headlong.abi.Address;
import com.esaulpaugh.headlong.util.FastHex;
import io.debc.nft.annotation.Contract;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.math.BigInteger;

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

    public static final com.esaulpaugh.headlong.abi.Function ownerOfFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"tokenId\",\"type\":\"uint256\"}],\"name\":\"ownerOf\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"}");

    @Override
    public String getSupportsInterfaceId() {
        return interfaceId;
    }


    public String ownerOf(String contractAddress, String tokenId) throws Exception {
        byte[] bytes = ownerOfFunc.encodeCallWithArgs(new BigInteger(tokenId, 16)).array();
        String data = FastHex.encodeToString(bytes);
        Transaction transaction = Transaction.createEthCallTransaction(null, contractAddress, data);
        Request<?, EthCall> request = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST);
        EthCall ethCall = request.send();
        if (ethCall.getResult() == null) {
            return null;
        }
        return ((Address) ownerOfFunc.decodeReturn(FastHex.decode(ethCall.getResult().substring(2))).get(0)).toString();
    }

}
