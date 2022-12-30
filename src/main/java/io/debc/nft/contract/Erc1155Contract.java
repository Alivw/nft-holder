package io.debc.nft.contract;

import com.esaulpaugh.headlong.abi.Address;
import com.esaulpaugh.headlong.util.FastHex;
import io.debc.nft.annotation.Contract;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.io.IOException;
import java.math.BigInteger;

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

    public static final com.esaulpaugh.headlong.abi.Function balanceOfFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"inputs\":[{\"internalType\":\"address\",\"name\":\"account\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"balanceOf\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"}");

    @Override
    public String getSupportsInterfaceId() {
        return interfaceId;
    }


    public BigInteger balanceOf(String userId, String contractAddress, BigInteger tokenId) throws IOException {
        byte[] bytes = balanceOfFunc.encodeCallWithArgs(Address.wrap(Address.toChecksumAddress(userId)), tokenId).array();
        String data = FastHex.encodeToString(bytes);
        Transaction transaction = Transaction.createEthCallTransaction(null, contractAddress, data);
        Request<?, EthCall> request = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST);
        EthCall ethCall = request.send();
        if (ethCall.getResult() == null) {
            return null;
        }
        return balanceOfFunc.decodeReturn(FastHex.decode(ethCall.getResult().substring(2))).get(0);
    }
}

