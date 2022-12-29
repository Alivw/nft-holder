package io.debc.nft;

import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
import io.debc.nft.contract.Erc1155Contract;
import io.debc.nft.contract.Erc721Contract;
import io.debc.nft.utils.SysUtils;

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
        BigInteger[] bigIntegers = SysUtils.decodeTransferBatchData("000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000000000028000000000000000000000000000000500000000000000000000000000000003800000000000000000000000000000040000000000000000000000000000037f000000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000001");
        System.out.println();


        System.out.println();
    }


}
