package io.debc.nft;

import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
import io.debc.nft.contract.Erc721Contract;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 13:25
 **/
public class TestMain {

    public static void main(String[] args) throws Exception {


        //System.out.println(new Erc20Contract().name("0xbb9bc244d798123fde783fcc1c72d3bb8c189413"));
        Erc721Contract erc721Contract = new Erc721Contract();
        String s = "379e55f82f96d79c595190b3c83e7877229778dc0000000000000200000000010000000000000000000000000000000000000000000000000000000000000001";
        TupleType tupleType = TupleType.parse("(uint256,uint256)");
        Tuple decode = tupleType.decode(FastHex.decode(s));

        System.out.println();
    }


}
