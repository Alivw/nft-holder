package io.debc.nft.contract;

import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.util.FastHex;
import io.debc.nft.product.Web3;
import io.debc.nft.utils.Assert;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.utils.Numeric;

import java.util.concurrent.CompletableFuture;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 14:28
 **/
@Slf4j
public abstract class NftContract  {
    protected Web3j web3j = new Web3().get();

    public static final com.esaulpaugh.headlong.abi.Function supportInterfaceFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"inputs\":[{\"internalType\":\"bytes4\",\"name\":\"interfaceId\",\"type\":\"bytes4\"}],\"name\":\"supportsInterface\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"}");


    public abstract String getSupportsInterfaceId();



    public boolean supportInterface(String contractAddress) throws Exception {
        byte[] bytes1 = Numeric.hexStringToByteArray(getSupportsInterfaceId());
        byte[] bytes = supportInterfaceFunc.encodeCallWithArgs(bytes1).array();
        String data = FastHex.encodeToString(bytes);
        Transaction transaction = Transaction.createEthCallTransaction(null, contractAddress, data);
        Request<?, EthCall> request = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST);
        EthCall ethCall = request.send();
        if (ethCall.getResult() == null || ethCall.getResult().equals("0x")) {
            return false;
        }
        Tuple tuple = supportInterfaceFunc.decodeReturn(FastHex.decode(ethCall.getResult().substring(2)));
        return tuple.get(0);
    }

}
