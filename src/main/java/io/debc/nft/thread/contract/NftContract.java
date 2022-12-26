package io.debc.nft.thread.contract;

import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.util.FastHex;
import io.debc.nft.thread.entity.EsContract;
import io.debc.nft.thread.product.Web3;
import io.debc.nft.thread.utils.Assert;
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
public abstract class NftContract implements Contract {
    protected Web3j web3j = new Web3().get();

    public static final com.esaulpaugh.headlong.abi.Function supportInterfaceFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"inputs\":[{\"internalType\":\"bytes4\",\"name\":\"interfaceId\",\"type\":\"bytes4\"}],\"name\":\"supportsInterface\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"}");
    public static final com.esaulpaugh.headlong.abi.Function nameFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"inputs\":[],\"name\":\"name\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"}");
    public static final com.esaulpaugh.headlong.abi.Function symbolFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"inputs\":[],\"name\":\"symbol\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"}");


    public abstract String getSupportsInterfaceId();

    public String symbol(String contractAddress) throws Exception {
        byte[] bytes = symbolFunc.encodeCallWithArgs().array();
        String data = FastHex.encodeToString(bytes);
        Transaction transaction = Transaction.createEthCallTransaction(null, contractAddress, data);
        Request<?, EthCall> request = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST);
        EthCall ethCall = request.send();
        try {
            return symbolFunc.decodeReturn(FastHex.decode(ethCall.getResult().substring(2))).get(0);
        } catch (Exception e) {
            return new String(FastHex.decode(convert(ethCall.getResult().substring(2))));
        }
    }


    public String name(String contractAddress) throws Exception {
        byte[] bytes = nameFunc.encodeCallWithArgs().array();
        String data = FastHex.encodeToString(bytes);
        Transaction transaction = Transaction.createEthCallTransaction(null, contractAddress, data);
        Request<?, EthCall> request = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST);
        EthCall ethCall = request.send();
        try {
            return nameFunc.decodeReturn(FastHex.decode(ethCall.getResult().substring(2))).get(0);
        } catch (Exception e) {
            return new String(FastHex.decode(convert(ethCall.getResult().substring(2))));
        }
    }


    public boolean supportInterface(String contractAddress) throws Exception {
        byte[] bytes1 = Numeric.hexStringToByteArray(getSupportsInterfaceId());
        byte[] bytes = supportInterfaceFunc.encodeCallWithArgs(bytes1).array();
        String data = FastHex.encodeToString(bytes);
        Transaction transaction = Transaction.createEthCallTransaction(null, contractAddress, data);
        Request<?, EthCall> request = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST);
        EthCall ethCall = request.send();
        Tuple tuple = supportInterfaceFunc.decodeReturn(FastHex.decode(ethCall.getResult().substring(2)));
        return tuple.get(0);
    }

    @Override
    public EsContract callContractMethods(String contractAddress) {
        EsContract esContract = null;
        try {
            Assert.notBlank(contractAddress);
            if (supportInterface(contractAddress)) {
                esContract = new EsContract();
                CompletableFuture<String> nameFuture = CompletableFuture.supplyAsync(() -> {
                    try {
                        return name(contractAddress);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, callContractPool);
                CompletableFuture<String> symbolFuture = CompletableFuture.supplyAsync(() -> {
                    try {
                        return symbol(contractAddress);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, callContractPool);
                esContract.setName(nameFuture.get());
                esContract.setSymbol(symbolFuture.get());
                esContract.setAddress(contractAddress);
            }
        } catch (Exception ignored) {
        }
        return esContract;
    }
}
