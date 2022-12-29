package io.debc.nft;

import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
import io.debc.nft.contract.Erc1155Contract;
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
        Erc1155Contract contract = new Erc1155Contract();
        System.out.println(contract.balanceOf("0x463def03f98b328a75051ee5ebe9a6235de4ac59", "0xd07dc4262BCDbf85190C01c996b4C06a461d2430", BigInteger.valueOf(29377)));
        System.out.println();
    }


}
