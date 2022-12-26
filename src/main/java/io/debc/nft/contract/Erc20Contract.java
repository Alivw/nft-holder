package io.debc.nft.contract;

import com.esaulpaugh.headlong.abi.Address;
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.util.FastHex;
import io.debc.nft.entity.EsBalance;
import io.debc.nft.entity.EsContract;
import io.debc.nft.product.Web3;
import io.debc.nft.utils.Assert;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-09 10:24
 **/
@Slf4j
@io.debc.nft.annotation.Contract
public class Erc20Contract implements Contract {


    public static final String TRANSFER_ID = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
    private Web3j web3j = new Web3().get();

    public static final com.esaulpaugh.headlong.abi.Function decimalFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"constant\":true,\"inputs\":[],\"name\":\"decimals\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}");

    public final com.esaulpaugh.headlong.abi.Function balanceOfFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"constant\":true,\"inputs\":[{\"name\":\"who\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}");
    public final com.esaulpaugh.headlong.abi.Function symbolFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"constant\":true,\"inputs\":[],\"name\":\"symbol\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}");
    public final com.esaulpaugh.headlong.abi.Function totalSupplyFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"constant\":true,\"inputs\":[],\"name\":\"totalSupply\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}");
    public final com.esaulpaugh.headlong.abi.Function nameFunc = com.esaulpaugh.headlong.abi.Function.fromJson("{\"constant\":true,\"inputs\":[],\"name\":\"name\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}");


    public BigInteger balanceOf(String address, String ContractAddress) throws Exception {
        if (address.length() == 66) {
            address = "0x" + address.substring(26);
        }
        byte[] bytes = balanceOfFunc.encodeCallWithArgs(com.esaulpaugh.headlong.abi.Address.wrap(Address.toChecksumAddress(address))).array();
        String data = FastHex.encodeToString(bytes);
        Transaction transaction = Transaction.createEthCallTransaction(address, ContractAddress, data);
        Request<?, EthCall> request = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST);
        EthCall ethCall = request.send();
        Tuple tuple = balanceOfFunc.decodeReturn(FastHex.decode(ethCall.getResult().substring(2)));
        return tuple.get(0);
    }

    public Integer decimal(String contractAddress) throws Exception {
        if (contractAddress.length() == 66) {
            contractAddress = "0x" + contractAddress.substring(26);
        }
        byte[] bytes = decimalFunc.encodeCallWithArgs().array();
        String data = FastHex.encodeToString(bytes);
        Transaction transaction = Transaction.createEthCallTransaction(null, contractAddress, data);
        Request<?, EthCall> request = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST);
        EthCall ethCall = request.send();
        Tuple tuple = decimalFunc.decodeReturn(FastHex.decode(ethCall.getResult().substring(2)));
        return tuple.get(0);
    }

    public BigInteger totalSupply(String contractAddress) throws Exception {
        if (contractAddress.length() == 66) {
            contractAddress = "0x" + contractAddress.substring(26);
        }
        byte[] bytes = totalSupplyFunc.encodeCallWithArgs().array();
        String data = FastHex.encodeToString(bytes);
        Transaction transaction = Transaction.createEthCallTransaction(null, contractAddress, data);
        Request<?, EthCall> request = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST);
        EthCall ethCall = request.send();
        Tuple tuple = totalSupplyFunc.decodeReturn(FastHex.decode(ethCall.getResult().substring(2)));
        return tuple.get(0);
    }

    public String symbol(String contractAddress) throws Exception {
        if (contractAddress.length() == 66) {
            contractAddress = "0x" + contractAddress.substring(26);
        }
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
        if (contractAddress.length() == 66) {
            contractAddress = "0x" + contractAddress.substring(26);
        }
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


    @Override
    public EsContract callContractMethods(String tokenAddress) {
        try {
            Integer decimal = decimal(tokenAddress);
            CompletableFuture<String> symbolFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return symbol(tokenAddress);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, callContractPool);
            CompletableFuture<String> totalSupplyFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return totalSupply(tokenAddress).divide(BigInteger.TEN.pow(decimal)).toString();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, callContractPool);
            CompletableFuture<String> nameFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return name(tokenAddress);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, callContractPool);
            EsContract esContract = new EsContract();
            esContract.setAddress(tokenAddress);
            esContract.setDecimal(decimal);
            esContract.setSymbol(symbolFuture.get());
            esContract.setTotalSupply(totalSupplyFuture.get());
            esContract.setName(nameFuture.get());
            esContract.setStd("erc20");
            return esContract;
        } catch (Exception e) {
            log.debug("erc20 contract.callAllMethod throws Exception,contractAddress:{}", tokenAddress, e);
        }
        return null;
    }

    public List<EsBalance> calculateBalance(List<Log> logs) {
        List<EsBalance> ba = new ArrayList<>(logs.size());
        EsBalance b1, b2 = null;
        for (Log ethLog : logs) {
            List<String> topics = ethLog.getTopics();
            Assert.isTrue(topics.size() > 1);
            CompletableFuture<EsBalance> f1 = CompletableFuture.supplyAsync(() -> calculateBalance(convertTooLongAddress(ethLog.getTopics().get(1)), convertTooLongAddress(ethLog.getAddress())), callContractPool);
            try {
                b1 = f1.get();
                if (TRANSFER_ID.equalsIgnoreCase(topics.get(0)) && topics.size() > 2) {
                    CompletableFuture<EsBalance> f2 = CompletableFuture.supplyAsync(() -> calculateBalance(convertTooLongAddress(ethLog.getTopics().get(2)), convertTooLongAddress(ethLog.getAddress())), callContractPool);
                    b2 = f2.get();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            if (b1 != null) {
                ba.add(b1);
            }
            if (b2 != null) {
                ba.add(b2);
            }
        }
        return ba;
    }

    private EsBalance calculateBalance(String userAddress, String contractAddress) {

        EsBalance balance;
        balance = new EsBalance();

        balance.setAddress(userAddress);
        balance.setContract(contractAddress);
        try {
            balance.setBalance(balanceOf(userAddress, contractAddress).divide(BigInteger.TEN.pow(decimal(contractAddress))).toString());
        } catch (Exception e) {
            return null;
        }
        return balance;
    }
}
